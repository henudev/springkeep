package com.h3c.bigdata.zhgx.function.report.serviceImpl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.h3c.bigdata.zhgx.common.utils.DateUtil;
import com.h3c.bigdata.zhgx.common.utils.PageUtil;
import com.h3c.bigdata.zhgx.common.utils.StringUtil;
import com.h3c.bigdata.zhgx.function.report.base.BusinessException;
import com.h3c.bigdata.zhgx.function.report.dao.*;
import com.h3c.bigdata.zhgx.function.report.entity.TableDescriptionEntity;
import com.h3c.bigdata.zhgx.function.report.entity.TableFillLogEntity;
import com.h3c.bigdata.zhgx.function.report.entity.TemplateCollectEntity;
import com.h3c.bigdata.zhgx.function.report.entity.TemplateEntity;
import com.h3c.bigdata.zhgx.function.report.model.*;
import com.h3c.bigdata.zhgx.function.report.service.DataProcessService;
import com.h3c.bigdata.zhgx.function.report.util.EsUtil;
import com.h3c.bigdata.zhgx.function.report.util.ExcelUtil;
import com.h3c.bigdata.zhgx.function.report.util.UUIDUtil;
import com.h3c.bigdata.zhgx.function.system.dao.AuthDepartmentInfoEntityMapper;
import com.h3c.bigdata.zhgx.function.system.dao.AuthUserInfoEntityMapper;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserInfoEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * @ClassName DataProcessServiceImpl
 * @Description TODO
 * @Author zzzzitai
 * @Date 2018/11/21 16:10
 * @Version 1.0
 **/
@Service
public class DataProcessServiceImpl extends ServiceImpl<TemplateEntityMapper, TemplateEntity>
        implements DataProcessService {
    Log logger = LogFactory.getLog(DataProcessServiceImpl.class);

    @Value("${lwxs}")
    String lwxs;

    @Value("${numReg}")
    String numReg;
    @Value("${indexMaxResultCount}")
    int indexMaxResultCount;

    @Autowired
    EsDataOperationServiceImpl esDataOperationService;

    @Resource
    TableDescriptionEntityMapper tableDescriptionEntityMapper;

    @Autowired
    FileProcessServiceImpl fileProcessService;

    @Resource
    TemplateEntityMapper templateEntityMapper;

    @Resource
    private TemplateCollectEntityMapper templateCollectEntityMapper;

    @Resource
    DataStorageEntityMapper dataStorageEntityMapper;

    @Resource
    private TableFillLogEntityMapper tableFillLogEntityMapper;

    @Autowired
    private AuthDepartmentInfoEntityMapper authDepartmentInfoEntityMapper;

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Autowired
    private AuthUserInfoEntityMapper authUserInfoEntityMapper;

    /**
     * 上传excel读取数据
     *
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String, Object>> uploadFile(MultipartFile file, Integer templateId) throws Exception {
        List<Map<String, Object>> restList = new ArrayList<Map<String, Object>>();
        InputStream in = file.getInputStream();
        // 获取excel文件
        Workbook hssfWorkbook = new XSSFWorkbook(in);
        Sheet sheet = hssfWorkbook.getSheetAt(0);
        // 取第一行
        Row row1 = sheet.getRow(0);
        // 取title列表
        List<String> nameList = new ArrayList<>();
        List<String> itermList = new ArrayList<>();
        for (int i = 0; i < row1.getLastCellNum(); i++) {
            nameList.add(row1.getCell(i).getStringCellValue());
        }
        //获取iterm列表
        List<TableDescriptionEntity> descriptionList = tableDescriptionEntityMapper.selectByTemplateId(templateId);
        Map<String, String> totalMap = new HashMap<>();
        for (TableDescriptionEntity tableDescriptionEntity : descriptionList) {
            totalMap.put(tableDescriptionEntity.getName(), tableDescriptionEntity.getItem());
        }
        // 校验模板属性正确性
        for (String str : nameList) {
            if (!totalMap.keySet().contains(str) || nameList.size() != nameList.size()) {
                throw new BusinessException("表格不匹配");
            }
        }
        restList = getExcelDataList(file.getInputStream(), nameList, totalMap);
        hssfWorkbook.close();
        in.close();
        return restList;
    }

    private List<Map<String, Object>> getExcelDataList(InputStream in, List<String> nameList, Map iterms) {
        List<Map<Integer, Object>> listMap = EasyExcel.read(in).sheet().doReadSync();
        List<Map<String, Object>> result = new ArrayList<>(listMap.size());

        listMap.stream().forEach(data -> {
            Map<String, Object> resultTemp = new HashMap<>();
            for (Integer i = 0; i < nameList.size(); i++) {
                String name = iterms.get(nameList.get(i)).toString();
                if (data.containsKey(i)) {
                    resultTemp.put(name, data.get(i));
                }
            }
            result.add(resultTemp);
        });
        return result;
    }


    @Override
    public Map<String, Object> saveData(ReportData reportData, String userId) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> dataList = reportData.getData();
        Integer templateId = reportData.getTemplateId();
        TemplateEntity templateEntity = templateEntityMapper.selectByPrimaryKey(templateId);
        if (null == templateEntity) {
            resultMap.put("flag", true);
            resultMap.put("totalNum", "填报表格不存在");
        }
        String templateSourceName = templateEntity.getTemplateSourceName();
        String number = templateEntity.getNumber();
        String templateName = templateEntity.getName();
        reportData.setTemplateSourceName(templateSourceName);
        reportData.setNumber(number);

        List<Map<String, Object>> insertList = new ArrayList<>();
        List<Map<String, Object>> updateList = new ArrayList<>();

        // 根据模板编号查询出来联合唯一字段
        List<String> unionOnlyKey = tableDescriptionEntityMapper.selectUnionOnly(templateId);
        // 如果没有联合唯一键那么就是说数据库中没有联合唯一的数据，所以直接初始化，避免空指针异常
        List<String> unionOnlyData = new ArrayList<>();
        if (!unionOnlyKey.isEmpty()) {
            // 根据模板ID和联合唯一键查询到联合唯一键的数据
            unionOnlyData = tableDescriptionEntityMapper.selectUnionOnlyData(unionOnlyKey, templateSourceName + "." + number);
        }
        // 存放Excel中的数据
        List<String> excelData = new ArrayList<>();
        // 存放excel中重复数据的行号
        List<String> excelRowNum = new ArrayList<>();
        List<String> dbRowNum = new ArrayList<>();
        boolean result = false;
        boolean excelFlag = false;
        List<TableDescriptionEntity> descriptionList = tableDescriptionEntityMapper.selectByTemplateId(templateId);

        AuthUserInfoEntity userInfoEntity = authUserInfoEntityMapper.selectUserByUserId(userId);
        String currentTime = DateUtil.getNowDate();
        for (int j = 0; j < dataList.size(); j++) {
            Map<String, Object> map = dataList.get(j);
            for (TableDescriptionEntity entity : descriptionList) {
                checkItemValue(map, entity);
            }

            map.put("id", UUIDUtil.getUUID());
            map.put("update_user", userInfoEntity.getUserName());

            map.put("update_time", currentTime);
            //数据重复校验
            if (unionOnlyKey != null && !unionOnlyKey.isEmpty()) {
                try {
                    //当前提交这条数据得联合唯一键字符串
                    StringBuffer sb = new StringBuffer();
                    // 按照查询字段的顺序拼接将要插入的数据联合唯一字段
                    for (int i = 0; i < unionOnlyKey.size(); i++) {
                        String value = map.get(unionOnlyKey.get(i)).toString();
                        if (i < unionOnlyKey.size() - 1) {
                            sb.append(value).append("-");
                        } else {
                            sb.append(value);
                        }
                    }
                    // 查看excel中自己本身的数据是否重复
                    boolean contains = excelData.contains(sb.toString());
                    if (contains) {
                        // 将重复数据的行号返回
                        excelFlag = true;
                        excelRowNum.add(String.valueOf(j + 1));
                    } else {
                        // 将数据存放在excel数据集合中
                        excelData.add(sb.toString());
                    }
                    if (!excelFlag) {
                        // 查看数据库中是否有该数据
                        boolean dbfalg = unionOnlyData.contains(sb.toString());
                        if (dbfalg) {
                            result = true;
                            updateList.add(map);
                            dbRowNum.add(String.valueOf(j + 1));

                        } else {
                            insertList.add(map);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e);
                }
            } else {
                insertList = dataList;
            }
        }
        // 写入数据库
        String departmentId = authDepartmentInfoEntityMapper.selectDeptByUserId(userId).getId();
        if (excelFlag) {
            resultMap.put("excelMessage", excelRowNum.toString());
        } else {
            if (!result) {
                //数据库没有重复数据，直接插入ES
                EsInsertModel esInsertModel = new EsInsertModel();
                List<Map<String, Object>> insertEsList = insertExcelData(insertList, reportData, userId, currentTime);
                if (insertEsList.size() > 0) {
                    insertEsList.stream().forEach(item -> {
                        if (null != item.get("update_time")) {
                            String updateTime = String.valueOf(item.get("update_time"));
                            updateTime = updateTime.substring(0, updateTime.lastIndexOf("."));
                            item.replace("update_time", updateTime);
                        }
                    });
                }
                esInsertModel.setDataMapList(insertEsList);
                esInsertModel.setTemplateCnName(templateName);
                esInsertModel.setDepartmentId(departmentId);
                esInsertModel.setTemplateSourceName(templateSourceName);
                esInsertModel.setTemplateNumber(number);
                logger.info("########### INSERT ES START#######");
                int insertEsNum = esDataOperationService.insertEsCommitData(esInsertModel);
                logger.info("*******插入ES数据条数:" + insertEsNum);
                logger.info("########### INSERT ES END#########");
                resultMap.put("totalNum", "成功添加数据" + insertEsNum + "条！");
            } else {
                // 将数据存储到内存中
                Map<String, Object> dataStorage = new HashMap<>();
                dataStorage.put("key", unionOnlyKey);
                dataStorage.put("templateSourceName", templateSourceName);
                dataStorage.put("number", number);
                dataStorage.put("insert", insertList);
                dataStorage.put("update", updateList);
                String uuid = UUIDUtil.getUUID();
                DataProcessStorage.dataProcessMap.put(uuid, dataStorage);
                dataStorageEntityMapper.insert(uuid);
                resultMap.put("key", uuid);
            }
            resultMap.put("dbFlag", result);
        }
        resultMap.put("excelFlag", excelFlag);
        resultMap.put("excelRowNum", excelRowNum);
        resultMap.put("dbRowNum", dbRowNum);
        return resultMap;
    }

    @Override
    public Object strategyData(Map<String, String> map) throws Exception {

        String message = "";
        // 获取策略 1,差异覆盖; 2,全覆盖
        String departmentId = map.get("departmentId");
        String userName = map.get("userName");
        String choose = map.get("choose");
        // 获取uuid
        String uuid = map.get("key");
        // 根据UUID从内存中取出数据
        Map<String, Object> data = DataProcessStorage.dataProcessMap.get(uuid);
        DataProcessStorage.dataProcessMap.remove(uuid);
        // 取出联合唯一键
        if (data.isEmpty()) {
            new Exception("内存暂存数据为空");
        }
        List<String> unionOnlyKey = (List<String>) data.get("key");
        // 取出模板tamplateNum
        String number = (String) data.get("number");

        String templateSourceName = (String) data.get("templateSourceName");
        // 取出新增的数据
        List<Map<String, Object>> insertData = (List<Map<String, Object>>) data.get("insert");
        // 取出重复的数据
        List<Map<String, Object>> updateData = (List<Map<String, Object>>) data.get("update");
        // 再次判断数据库中有没有新增的数据和insertData的数据重复-
        List<String> unionOnlyData = tableDescriptionEntityMapper.selectUnionOnlyData(unionOnlyKey, templateSourceName + "." + number);


        for (int j = 0; j < insertData.size(); j++) {
            Map<String, Object> rowMap = insertData.get(j);
            StringBuilder unionOnlyValue = new StringBuilder();
            for (int i = 0; i < unionOnlyKey.size(); i++) {
                Object dataTmp = rowMap.get(unionOnlyKey.get(i));
                String value = dataTmp.toString();
                if (i < unionOnlyKey.size() - 1) {
                    unionOnlyValue.append(value).append("-");
                } else {
                    unionOnlyValue.append(value);
                }
            }
            // 查看数据库中是否有该数据
            boolean falg = unionOnlyData.contains(unionOnlyValue.toString());
            if (falg) {
                updateData.add(rowMap);
                insertData.remove(j);
            }
        }
        // 如果策略是差异覆盖就保留数据库中重复的数据只添加新增的
        TemplateEntity templateEntity = templateEntityMapper.selectByNumber(number);
        ReportData reportData = new ReportData();
        reportData.setTemplateSourceName(templateSourceName);
        reportData.setNumber(number);
        reportData.setDepartmentId(departmentId);
        String currentTime = DateUtil.getNowDate();
        if ("1".equals(choose)) {
            // 当选择差异覆盖的时候切没有新增的数据时，不需要再调用新增方法。
            if (!insertData.isEmpty()) {

                EsInsertModel esInsertModel = new EsInsertModel();
                List<Map<String, Object>> insertEsList = insertExcelDataForUpdate(insertData, reportData, currentTime);

                if (insertEsList.size() > 0) {
                    insertEsList.stream().forEach(item -> {
                        if (null != item.get("update_time")) {
                            String updateTime = String.valueOf(item.get("update_time"));
                            updateTime = updateTime.substring(0, updateTime.lastIndexOf("."));
                            item.replace("update_time", updateTime);
                        }
                    });
                }
                esInsertModel.setDataMapList(insertEsList);
                esInsertModel.setTemplateCnName(templateEntity.getName());
                esInsertModel.setDepartmentId(reportData.getDepartmentId());
                esInsertModel.setTemplateSourceName(templateSourceName);
                esInsertModel.setTemplateNumber(number);
                logger.info("########### INSERT ES START#######");
                int insertEsNum = EsUtil.insertEsCommitData(esInsertModel);
                logger.info("*******插入ES数据条数:" + insertEsNum);
                logger.info("########### INSERT ES END#########");
            }
        } else {
            // 如果是全替换就删除数据库中重复的将数据全部添加。
            List<Map<String, String>> unionOnlyKeys = new ArrayList<>();
            for (Map<String, Object> map2 : updateData) {
                Map<String, String> key = new LinkedHashMap<>();
                for (String s : unionOnlyKey) {
                    Object value = map2.get(s);
                    key.put(s, value.toString());
                }
                if (!key.isEmpty()) {
                    unionOnlyKeys.add(key);
                }
            }
            long start = System.currentTimeMillis();

            // 根据联合唯一键删除数据库中重复的数据
            if (!unionOnlyKeys.isEmpty()) {
                List<String> deleteIds = tableDescriptionEntityMapper.selectIdsByUnionOnlyKeys(unionOnlyKeys, unionOnlyKey, templateSourceName + "." + number);

                //先删除ES数据
                EsUtil.delete(number, templateSourceName, deleteIds);
                tableDescriptionEntityMapper.deleteBatchByUnionOnlyKeys(unionOnlyKeys, unionOnlyKey, templateSourceName + "." + number);
            }
            EsInsertModel esInsertModel = new EsInsertModel();
            long delete = System.currentTimeMillis();
            logger.info("删除耗时：" + (delete - start));
            message = "成功添加数据" + insertData.size() + "条，" + "覆盖数据" + updateData.size() + "条！";
            insertData.addAll(updateData);

            List<Map<String, Object>> insertEsList = insertExcelDataForUpdate(insertData, reportData, currentTime);

            if (insertEsList.size() > 0) {
                insertEsList.stream().forEach(item -> {
                    if (null != item.get("update_time")) {
                        String updateTime = String.valueOf(item.get("update_time"));
                        updateTime = updateTime.substring(0, updateTime.lastIndexOf("."));
                        item.replace("update_time", updateTime);
                    }
                });
            }
            esInsertModel.setDataMapList(insertEsList);
            esInsertModel.setTemplateCnName(templateEntity.getName());
            esInsertModel.setDepartmentId(reportData.getDepartmentId());
            esInsertModel.setTemplateSourceName(templateSourceName);
            esInsertModel.setTemplateNumber(number);
            logger.info("########### INSERT ES START#######");
            int insertEsNum = EsUtil.insertEsCommitData(esInsertModel);
            logger.info("*******插入ES数据条数:" + insertEsNum);
            logger.info("########### INSERT ES END#########");
        }
        return message;
    }

    public List<Map<String, Object>> insertExcelDataForUpdate(List<Map<String, Object>> dataMap, ReportData reportData, String currentTime) throws Exception {


        String templateSourceName = reportData.getTemplateSourceName();
        String number = reportData.getNumber();
        String tableName = templateSourceName + "." + number;
        // 生成sql语句
        Set itemSet = dataMap.get(0).keySet();
        Object[] items = itemSet.toArray();
        int total = dataMap.size();
        //向数据库批量插入数据
        insertIntoData(dataMap, tableName, items);
        logger.info("插入数据库前的条数" + total);
        //更新填报次数，更新人，更新时间
//        templateCollectEntityMapper.updateCollect(templateId, total, currentTime, userId);
        //按照部门更新填报记录
//        tempToDptMapper.updateDptTemplateNum(userDptId,total,templateId.toString(),currentTime,userId);
        //增加填报次数记录表
//        insetTableFillLog(templateId, total, userDptId);
        logger.info("插入数据库后的条数" + total);
//        String sql = "select *  from " + tableName + " where update_time ='" + currentTime + "' order by num_id asc";
        String sql = "select *  from " + tableName + " order by update_time desc  limit " + total;
        return templateCollectEntityMapper.getMapsBySql(sql);
    }

    /**
     * @param
     * @return
     * @Description: 校验所有字段的类型
     * @Author: w15112
     * @Date: 2019/8/17 10:13
     */
    private void checkItemValue(Map<String, Object> map, TableDescriptionEntity tableDescriptionEntity)
            throws BusinessException {
        // cell 取值
        String item = tableDescriptionEntity.getItem();
        int type = tableDescriptionEntity.getType();
        String itemValue = String.valueOf(map.get(item));
        Byte isNull = tableDescriptionEntity.getIsNull();
        //对数据进行非空校验
        if (isNull == 0 && (StringUtils.isBlank(itemValue))) {
            throw new BusinessException("非空字段数据为空，请检查");
        }
        if (StringUtils.isNotBlank(itemValue)) {
            switch (type) {
                // 整数类型
                case 1: {
                    Pattern pattern = Pattern.compile(numReg);
                    Matcher matcher = pattern.matcher(itemValue);
                    if (!matcher.matches()) {
                        throw new BusinessException("整形数据格式错误");
                    }
                    break;
                }
                // 小数点两位
                case 2: {
                    Pattern pattern = Pattern.compile(lwxs);
                    Pattern patternInt = Pattern.compile(numReg);
                    Matcher matcher = pattern.matcher(itemValue);
                    Matcher matcherInt = patternInt.matcher(itemValue);
                    if (!matcher.matches() && !matcherInt.matches()) {
                        throw new BusinessException("保留两位小数格式错误");
                    }
                    break;
                }
                default:
            }
        }

    }

    @Override
    public int deleteData(DelData delData, String userId) throws Exception {
        List<DataModel> idList = delData.getIds();
        String templateSourceName = delData.getTemplateSourceNameEn();
        String number = delData.getNumber();
        Integer templateId = delData.getTemplateId();
        TemplateCollectEntity templateCollectEntity = templateCollectEntityMapper.getTemplateCollectById(templateId);
        if (null != templateCollectEntity && templateCollectEntity.getNumberSum() > 0) {
            List<String> idLists = new ArrayList<>();
            for (int i = 0; i < idList.size(); i++) {
                DataModel model = idList.get(i);
                String id = model.getId();
                String updateTime = model.getUpdateTime();

                //删除附件优先进行
                List<String> annexItems = getTableAnnex(templateId);
                if (annexItems != null && annexItems.size() > 0) {
                    //获取附件ids
                    List<String> annexIds = getAnnexIds(templateSourceName + "." + number, annexItems, id);
                    //删除附件
                    for (String annexids : annexIds) {
                        String deleInfo = fileProcessService.deleteAnnex(annexids);
                        logger.info(deleInfo);
                    }
                }

                tableDescriptionEntityMapper.delData(templateSourceName + "." + number, id);
                //根据更新时间，更新填报记录表数据
                tableFillLogEntityMapper.updateFillLogSum(templateId, updateTime);
                idLists.add(id);
            }

            esDataOperationService.delete(number, templateSourceName, idLists);
            String currentTime = DateUtil.getNowDate();
            templateCollectEntityMapper.updateCollectSum(templateId, idList.size(), currentTime, userId);
        }
        return 0;
    }

    /**
     * 清空此模板.
     *
     * @param templateId 模板ID
     * @throws Exception exception
     */
    @Override
    public int deleteAllDataByTemplateId(Integer templateId) throws Exception {
        TemplateEntity entity = templateEntityMapper.selectByPrimaryKey(templateId);
        if (entity == null) {
            return -1;
        }
        //删除ES
        EsUtil.deleteByTmpNum(entity.getNumber(), entity.getTemplateSourceName());
        //删除MYSQL
        StringBuilder sb = new StringBuilder();
        String tableName = sb.append(entity.getTemplateSourceName()).append(".").append(entity.getNumber()).toString();
        String sql = "delete from " + tableName;
        int count = tableDescriptionEntityMapper.execSql(sql);
        logger.info(sql + "::" + count);
        //删除填报记录
        String sql2 = "delete from table_fill_log where template_id=" + entity.getId();
        String sql3 = "update template_collect set number_sum=0, fill_count=0, index_count=0 where template_id=" + entity.getId();
        int count2 = tableDescriptionEntityMapper.execSql(sql2);
        logger.info(sql2 + "::" + count2);
        int count3 = tableDescriptionEntityMapper.execSql(sql3);
        logger.info(sql3 + "::" + count3);
        return 0;
    }

    /**
     * 根据模板id获取附件字段列表
     *
     * @param templateId
     * @return
     */
    private List<String> getTableAnnex(Integer templateId) {
        List<String> annexs = tableDescriptionEntityMapper.selectAnnex(templateId);
        return annexs;
    }

    private List<String> getAnnexIds(String table, List<String> annexList, String dataId) {
        List<String> annexIds = new ArrayList<>();

        for (int i = 0; i < annexList.size(); i++) {
            String annexids = tableDescriptionEntityMapper.selectAnnexIds(table, annexList.get(i), dataId);
            annexIds.add(annexids);
        }
        return annexIds;
    }

    @Override
    public Object updateData(ReportData reportData, String userId) throws Exception {
        Map<String, Object> data = reportData.getDataModel();
        Integer templateId = reportData.getTemplateId();
        TemplateEntity templateEntity = templateEntityMapper.selectByPrimaryKey(templateId);
        String templateSourceName = templateEntity.getTemplateSourceName();
        String number = templateEntity.getNumber();
        String tableName = templateSourceName + "." + number;
        // 根据模板ID查询到所有的字段及字段的情况
        List<TableDescriptionEntity> descriptionList = tableDescriptionEntityMapper.selectByTemplateId(templateId);
        String id = String.valueOf(data.get("id"));

        // 校验所有字段的类型
        for (TableDescriptionEntity tableDescriptionEntity : descriptionList) {
            checkItemValue(data, tableDescriptionEntity);
            // 根据模板编号查询出来联合唯一字段
            List<String> unionOnlyKey = tableDescriptionEntityMapper.selectUnionOnly(templateId);
            //如果有联合唯一键就根据联合唯一键来修改数据
            if (!unionOnlyKey.isEmpty()) {
                StringBuilder newValue = new StringBuilder();
                Map<String, String> keyDate = new HashMap<>();
                for (String key : unionOnlyKey) {
                    String n = (String) data.get(key);
                    if (StringUtils.isBlank(n)) {
                        throw new BusinessException("联合唯一键必须有值！");
                    }
                    newValue.append(n);
                    keyDate.put(key, n);
                }
                //新的数据和旧的联合唯一键是不相同的,避免修改后的数据在数据库中已经存在
                int count = tableDescriptionEntityMapper.selectByUnionOnlyKeys(keyDate, id, tableName);
                if (count != 0) {
                    throw new BusinessException("该数据已存在！");
                }
            }
        }
        try {

            EsUtil.updataData(number, templateSourceName, id, data);
            data.remove("id");
            int num = tableDescriptionEntityMapper.updateData(data, id, tableName);
            if (1 == num) {
                templateCollectEntityMapper.updateCollectData(templateId, 0, new Date(), userId);
                return "更新成功！";
            } else {
                return "请确认数据是否存在！";
            }
        } catch (Exception e) {
            logger.error("修改数据失败！", e);
            return "修改数据失败！";
        }


    }

    /**
     * 写入数据.
     */
    public List<Map<String, Object>> insertExcelData(List<Map<String, Object>> dataMap, ReportData reportData,

                                                     String userId, String currentTime) throws Exception {
        String templateSourceName = reportData.getTemplateSourceName();
        String number = reportData.getNumber();
        Integer templateId = reportData.getTemplateId();
        String tableName = templateSourceName + "." + number;
        // 生成sql语句
        Set itemSet = dataMap.get(0).keySet();
        Object[] items = itemSet.toArray();
        int total = dataMap.size();
        //向数据库批量插入数据
        insertIntoData(dataMap, tableName, items);
        logger.info("插入数据库前的条数" + total);
        //更新填报次数，更新人，更新时间
        templateCollectEntityMapper.updateCollect(templateId, total, currentTime, userId);
        //增加填报次数记录表
        insetTableFillLog(templateId, total);
        logger.info("插入数据库后的条数" + total);
        String sql = "select *  from " + tableName + " where update_time ='" + currentTime + "' order by num_id asc";
        return templateCollectEntityMapper.getMapsBySql(sql);
    }


    /**
     * @param
     * @return
     * @Description: TODO
     * @Author: w15112
     * @Date: 2019/8/26 17:23
     */
    @NotNull
    private String setValues(List<Map<String, Object>> dataMap, String numberId, Object[] items) {
        String value = "";
        String table = "";
        for (Object item : items) {
            table += item.toString() + ",";
        }
        table = table.substring(0, table.lastIndexOf(","));
        for (Map<String, Object> map : dataMap) {
            value += "(";
            for (Object item : items) {
                String itemData = String.valueOf(map.get(item));
                if (StringUtils.isBlank(itemData)) {
                    value += "null ,";

                } else {
                    value += "'" + itemData + "',";
                }

            }
            value = value.substring(0, value.lastIndexOf(","));
            value += "),";
        }
        value = value.substring(0, value.length() - 1);
        String sql = "insert into " + numberId + "(" + table + ") values" + value;
        return sql;
    }

    /**
     * @param
     * @return
     * @Description: 向数据库批量插入数据
     * @Author: w15112
     * @Date: 2019/8/26 17:23
     */
    private void insertIntoData(List<Map<String, Object>> templist, String numberId, Object[] items) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        System.out.println("数据不存在，开始执行插入数据。。。。。。");
        if (templist != null) {
            int groupSize = 500;
            int groupNo = templist.size() / groupSize;
            SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);

            if (templist.size() <= groupSize) {
                sql = setValues(templist, numberId, items);
                tableDescriptionEntityMapper.execSql(sql);
            } else {
                List<Map<String, Object>> subList = null;
                for (int i = 0; i < groupNo; i++) {
                    subList = templist.subList(0, groupSize);
                    sql = setValues(subList, numberId, items);
                    tableDescriptionEntityMapper.execSql(sql);
                    templist.subList(0, groupSize).clear();
                }
                if (templist.size() > 0) {
                    sql = setValues(templist, numberId, items);
                    tableDescriptionEntityMapper.execSql(sql);
                }
            }
            long endTime = System.currentTimeMillis();
            sqlSession.flushStatements();
            System.out.println("插入完成，耗时" + (endTime - startTime) + "ms");
        }
    }

    /**
     * @param templateId
     * @param total
     * @return
     * @Description: 增加填报次数记录表
     * @Author: w15112
     * @Date: 2019/7/6 11:15
     */
    private void insetTableFillLog(Integer templateId, int total) {
        TemplateCollectEntity entity = templateCollectEntityMapper.getTemplateCollectById(templateId);
        if (null != entity) {
            TableFillLogEntity tableFillLogEntity = new TableFillLogEntity();
            tableFillLogEntity.setId(UUIDUtil.getUUID());
            tableFillLogEntity.setTemplateId(templateId);
            tableFillLogEntity.setFillSum(total);
            tableFillLogEntity.setUpdateTime(entity.getUpdateTime());
            tableFillLogEntity.setUpdateUser(entity.getUpdateUser());
            tableFillLogEntity.setFillCount(entity.getFillCount());
            tableFillLogEntityMapper.insert(tableFillLogEntity);
        }
    }

    /**
     * 查询数据信息
     *
     * @param queryData
     * @return
     */
    @Override
    public Map<String, Object> queryData(QueryData queryData) {
        List<Map<String, Object>> data = tableDescriptionEntityMapper.queryData(queryData.getNumber(),
                queryData.getData(), (queryData.getPage() - 1) * queryData.getSize(), queryData.getSize());
        int dataCount = tableDescriptionEntityMapper.queryDataCount(queryData.getNumber(), queryData.getData());
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("count", dataCount);
        retMap.put("data", data);
        return retMap;
    }


    /**
     * @param
     * @return
     * @Description: 删除填报记录
     * @Author: w15112
     * @Date: 2019/8/20 14:28
     */
    @Override
    public void deleteFillLogByUpdateTime(Integer templateId, String updateTime, String userId) {
        TemplateEntity templateEntity = templateEntityMapper.selectByPrimaryKey(templateId);
        String templateSourceName = templateEntity.getTemplateSourceName();
        String number = templateEntity.getNumber();
        String tableName = templateSourceName + "." + number;
        //根据updateTime获取待删除的数据并删除
        String selectSql = "select id from " + tableName + " where update_time = '" + updateTime + "'";
        List<LinkedHashMap> mapList = templateCollectEntityMapper.getTableDataBySql(selectSql);
        List<String> idList = new ArrayList<>();
        for (LinkedHashMap map : mapList) {
            String id = String.valueOf(map.get("id"));
            // tableDescriptionEntityMapper.delData(tableName, id);
            idList.add(id);
        }

        tableDescriptionEntityMapper.delDataByTime(tableName, "'" + updateTime + "'");
        esDataOperationService.delete(number, templateSourceName, idList);
        //根据tableName和updateTime获取待更新的填报记录并更新
        List<TableFillLogEntity> list = tableFillLogEntityMapper.getLastFillLogsByUpdateTime(templateId, updateTime);
        for (TableFillLogEntity entity : list) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(entity.getUpdateTime());
            Integer fillCount = entity.getFillCount();
            if (time.equals(updateTime)) {
                tableFillLogEntityMapper.delete(entity);
                templateCollectEntityMapper.updateCollectByFillLog(templateId, mapList.size(), userId);
            }
            if (fillCount > 1) {
                //根据更新时间，更新填报记录表数据
                tableFillLogEntityMapper.updateFillLogCount(templateId, time);
            }
        }
    }

    /**
     * @param
     * @param pageNum
     * @return
     * @Description: 将现场表数据添加至es中
     * @Author: w15112
     * @Date: 2019/9/4 15:00
     */
    @Override
    public void InsertOldDataToEs(int pageNum) throws Exception {
        List<TemplateEntity> tableNameList = templateEntityMapper.getAllTemplates();
        //对list进行分页
        PageUtil<TemplateEntity> pag = new PageUtil<TemplateEntity>(tableNameList, 60);
        pag.setCurrent_page(pageNum);
        tableNameList = pag.getCurrentPageData();
        ExecutorService pool = newFixedThreadPool(15, 15);
        for (TemplateEntity entity : tableNameList) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        insertOldDataToEs(entity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            //提交要执行的任务
            pool.submit(runnable);
        }
        //启动之前提交的任务，有序的关闭
        pool.shutdown();
    }

    /**
     * 修改小数、整数字段的表结构
     */
    private void editFieldsById(TemplateEntity entity) {
        ArrayList<String> sqls = new ArrayList<>();
        String editSql = "alter table " + entity.getTemplateSourceName() + "." + entity.getNumber() + " modify ";

        List<TableDescriptionEntity> list = tableDescriptionEntityMapper.selectByTemplateId(entity.getId());
        list = list.stream().filter(item -> ((1 == item.getType()) || (2 == item.getType()))).collect(Collectors.toList());
        for (TableDescriptionEntity des : list) {
            StringBuilder editStr = new StringBuilder(editSql);
            editStr.append(des.getItem() + " varchar(32)  comment '" + des.getName() + "'");
            sqls.add(editStr.toString());
        }
        if (sqls.size() > 0) {
            tableDescriptionEntityMapper.updateDescCommentByTmpNu(sqls);
        }
    }

    /**
     * 将现场单条数据添加至es中
     */
    @Override
    public void InsertSingleDataToEs(Integer templateId) {
        ExecutorService pool = newFixedThreadPool(15, 15);
        //提交要执行的任务
        pool.submit(new Thread(() -> {
            TemplateEntity entity = templateEntityMapper.selectByPrimaryKey(templateId);
            insertOldDataToEs(entity);
        }));
        //启动之前提交的任务，有序的关闭
        pool.shutdown();
    }

    /**
     * 批量更新整数、小数字段数据
     */
    @Override
    public void editOldDataToEs() {
        List<TemplateEntity> tableNameList = templateEntityMapper.getAllNumcriTemplates();

        ExecutorService pool = newFixedThreadPool(15, 15);
        for (TemplateEntity entity : tableNameList) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        editFieldsById(entity);
                        insertOldDataToEs(entity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            //提交要执行的任务
            pool.submit(runnable);
        }
        //启动之前提交的任务，有序的关闭
        pool.shutdown();
    }

    /**
     * 将现场表一条模板数据添加至es中
     */
    private void insertOldDataToEs(TemplateEntity entity) {
        String templateSourceName = entity.getTemplateSourceName();
        Integer templateId = entity.getId();
        String tableName = entity.getNumber();
        //更新填报记录表和表字段信息表
        tableFillLogEntityMapper.updateFillLogTemplateId(templateId, tableName);
        tableDescriptionEntityMapper.updateDescriptionTemplateId(templateId, tableName);

        //删除原es数据
        if (EsUtil.indexExists(tableName)) {
            EsUtil.deleteByTmpNum(tableName, tableName);
        }
        String sql = "select * from " + templateSourceName + "." + tableName;
        List<Map<String, Object>> mapList = templateCollectEntityMapper.getMapsBySql(sql);
        if (mapList.size() > 0) {
            mapList.stream().forEach(item -> {
                if (null != item.get("update_time")) {
                    String updateTime = String.valueOf(item.get("update_time"));
                    updateTime = updateTime.substring(0, updateTime.lastIndexOf("."));
                    item.replace("update_time", updateTime);
                }
            });
            EsInsertModel esInsertModel = new EsInsertModel();
            esInsertModel.setDataMapList(mapList);

            esInsertModel.setTemplateSourceName(templateSourceName);
            esInsertModel.setTemplateNumber(tableName);
            int insertEsNum = 0;
            try {
                insertEsNum = esDataOperationService.insertEsCommitData(esInsertModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("*******插入ES数据条数:" + insertEsNum);
            logger.info("########### INSERT ES END#########");
            if (mapList.size() > 10000) {
                EsUtil.updateIndex(tableName, indexMaxResultCount);
            }
        }

    }

    /**
     * 创建线程池
     */
    private static ExecutorService newFixedThreadPool(int corePoolSize, int maximumPoolSize) {
        /**
         * corePoolSize - 线程池核心池的大小。
         * maximumPoolSize - 线程池的最大线程数。
         * keepAliveTime - 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间。
         * unit - keepAliveTime 的时间单位。
         * workQueue - 用来储存等待执行任务的队列。
         * threadFactory - 线程工厂。
         * handler - 拒绝策略。
         */
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), new ThreadPoolExecutor.DiscardOldestPolicy());
    }

}
