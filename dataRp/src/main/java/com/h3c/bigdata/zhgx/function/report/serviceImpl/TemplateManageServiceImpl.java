package com.h3c.bigdata.zhgx.function.report.serviceImpl;


import com.baomidou.mybatisplus.plugins.Page;
import com.h3c.bigdata.zhgx.common.utils.FileUtil;
import com.h3c.bigdata.zhgx.common.utils.PageResult;
import com.h3c.bigdata.zhgx.common.utils.PageUtil;
import com.h3c.bigdata.zhgx.common.utils.ToMoveTest;
import com.h3c.bigdata.zhgx.function.report.base.BusinessException;
import com.h3c.bigdata.zhgx.function.report.base.PublicResultConstant;
import com.h3c.bigdata.zhgx.function.report.dao.*;
import com.h3c.bigdata.zhgx.function.report.entity.TableDescriptionEntity;
import com.h3c.bigdata.zhgx.function.report.entity.TempToDpt;
import com.h3c.bigdata.zhgx.function.report.entity.TemplateCollectEntity;
import com.h3c.bigdata.zhgx.function.report.entity.TemplateEntity;
import com.h3c.bigdata.zhgx.function.report.model.EsQuery;
import com.h3c.bigdata.zhgx.function.report.model.ReturnData;
import com.h3c.bigdata.zhgx.function.report.model.TemplateAddBean;
import com.h3c.bigdata.zhgx.function.report.model.TemplateItemBean;
import com.h3c.bigdata.zhgx.function.report.model.TemplateItemExcelModel;
import com.h3c.bigdata.zhgx.function.report.model.TemplateModel;
import com.h3c.bigdata.zhgx.function.report.service.DataProcessService;
import com.h3c.bigdata.zhgx.function.report.service.TempToDptService;
import com.h3c.bigdata.zhgx.function.report.service.TemplateManageService;
import com.h3c.bigdata.zhgx.function.report.util.EsUtil;
import com.h3c.bigdata.zhgx.function.report.util.ExcelUtil;
import com.h3c.bigdata.zhgx.function.system.dao.AuthDepartmentInfoEntityMapper;
import com.h3c.bigdata.zhgx.function.system.dao.AuthUserInfoEntityMapper;
import com.h3c.bigdata.zhgx.function.system.dao.DictDataMapper;
import com.h3c.bigdata.zhgx.function.system.entity.AuthDepartmentInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.DictData;
import com.h3c.bigdata.zhgx.function.system.service.AuthDptService;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xpath.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName TemplateManageServiceImpl
 * @Description TODO
 * @Author zzzzitai
 * @Date 2018/11/16 9:19
 * @Version 1.0
 **/
@Service

public class TemplateManageServiceImpl implements TemplateManageService {
    Log logger = LogFactory.getLog(TemplateManageServiceImpl.class);

    @Resource
    private TemplateEntityMapper templateEntityMapper;

    @Resource
    private CreateTableBeanMapper createTableBeanMapper;

    @Resource
    private ToMoveTest toMoveTest;
    @Resource
    private TemplateEntityMapperExt templateEntityMapperExt;

    @Autowired
    private DictDataMapper dictDataMapper;

    @Resource
    private TableDescriptionEntityMapperExt tableDescriptionEntityMapperExt;

    @Resource
    private TableDescriptionEntityMapper tableDescriptionEntityMapper;
    @Resource
    private TemplateCollectEntityMapper templateCollectEntityMapper;
    @Resource
    private TempToDptMapper tempToDptMapper;

    @Autowired
    private TempToDptService tempToDptService;

    @Autowired
    private AuthDptService authDptService;
    @Autowired
    private AuthDepartmentInfoEntityMapper authDepartmentInfoEntityMapper;
    @Autowired
    private AuthUserInfoEntityMapper authUserInfoEntityMapper;
    @Autowired
    private DataProcessService dataProcessService;
    @Autowired
    EsDataOperationServiceImpl esDataOperationService;
    @Resource
    private TableFillLogEntityMapper tableFillLogEntityMapper;


    @Value("${enumType}")
    int enumType;
    @Value("${dateType}")
    int dateType;

    /**
     * /**
     * 功能描述: 持久化新增模板<br>
     *
     * @param templateAddBean
     * @return:
     */
    @Override
    @Transactional
    public void addTemplate(TemplateAddBean templateAddBean, String userId) throws Exception {
        templateAddBean.setTemplateNum(templateAddBean.getTemplateNum().toLowerCase());
        List<String> tableList = new ArrayList<>();

        if (templateAddBean == null) {
            throw new BusinessException(PublicResultConstant.PARAM_ERROR);
        }
        if ("".equals(templateAddBean.getTemplateNum())) {
            throw new BusinessException("父" + PublicResultConstant.TEMPLATE_NUM_IS_NULL);
        }
        if (templateAddBean.getDepartmentId().size() == 0) {
            throw new BusinessException("父模板部门不能为空！");
        }

        if (templateAddBean.getItemBeans().size() == 0) {
            throw new BusinessException("父模板行属性不能为空！");
        }
        try {
            checkAndCreateTable(templateAddBean, "");
            tableList.add(templateAddBean.getTemplateSourceName() + "." + templateAddBean.getTemplateNum());
            //插入模板信息
            getIdAndCreateTable(templateAddBean, userId);
        } catch (Exception e) {
            logger.info(e.getMessage());
            for (String tableName : tableList) {
                createTableBeanMapper.deleteTable(tableName);
            }
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 校验并创建表
     *
     * @param templateAddBean
     * @param flag
     * @throws BusinessException
     */
    private void checkAndCreateTable(TemplateAddBean templateAddBean, String flag) throws Exception, BusinessException {
        TemplateEntity tmp = templateEntityMapperExt.selectByTemplateId(templateAddBean.getTemplateSourceName(),
                templateAddBean.getTemplateNum());
        TemplateEntity tmp2 = templateEntityMapperExt.selectByTemplateName(templateAddBean.getTemplateName());
        //
        if (tmp != null) {
            throw new BusinessException(flag + PublicResultConstant.TEMPLATE_NUM_REPETITION);
        }
        if (tmp2 != null) {
            throw new BusinessException(flag + PublicResultConstant.TEMPLATE_NAME_REPETITION);
        }
        try {
            Map<String, String> map = createTableMap(templateAddBean);
            String name = map.get("name");
            String fields = map.get("fields");
            createTableBeanMapper.createTable(name, fields);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof  BadSqlGrammarException){
                throw new BusinessException(templateAddBean.getTemplateNum() + "字符串字段个数超过84，请将部分字段设置为长文本类型！");
            }else {
                throw new BusinessException(templateAddBean.getTemplateNum() + "建表失败！");
            }

        }
    }

    private void checkAndCreateTable2(TemplateAddBean templateAddBean) throws Exception, BusinessException {
        try {
            Map<String, String> map = createTableMap(templateAddBean);
            String name = map.get("name");
            String fields = map.get("fields");
            createTableBeanMapper.createTable(name, fields);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof  BadSqlGrammarException){
                throw new BusinessException(templateAddBean.getTemplateNum() + "字符串字段个数超过84，请将部分字段设置为长文本类型！");
            }else {
                throw new BusinessException(templateAddBean.getTemplateNum() + "建表失败！");
            }

        }
    }


    /**
     * 插入模板信息并返回模板id
     *
     * @param templateAddBean
     * @return
     * @throws Exception
     */
    private Integer getIdAndCreateTable(TemplateAddBean templateAddBean, String userId) throws Exception {

        String number = templateAddBean.getTemplateNum();
        String templateSourceName = templateAddBean.getTemplateSourceName();
        templateEntityMapperExt.insertTemplate(number, templateAddBean.getTemplateName(),
                templateAddBean.getDescription(), templateAddBean.getTempTagId(), userId,
                new Date(), templateAddBean.getFillInPeriodKey(), templateSourceName, "0");
        //获取模板id
        TemplateEntity templateEntity = templateEntityMapperExt.selectByTemplateId(templateSourceName, number);
        Integer templateId = templateEntity.getId();

        //新增模板和部门关系
        List<AuthDepartmentInfoEntity> dptList = templateAddBean.getDepartmentId();
        for (AuthDepartmentInfoEntity dpt : dptList) {
            tempToDptService.insert(templateId, dpt.getId());
        }
        List<TemplateItemBean> itemBeanList = templateAddBean.getItemBeans();
        for (TemplateItemBean templateItemBean : itemBeanList) {
            if ("".equals(templateItemBean.getItemName())) {
                throw new BusinessException(PublicResultConstant.TEMPLATE_NUM_IS_NULL);
            }
            if ("".equals(templateAddBean.getTemplateNum())) {
                throw new BusinessException(PublicResultConstant.TEMPLATE_NUM_IS_NULL);
            }
            TableDescriptionEntity tableDescriptionEntityTmp = new TableDescriptionEntity();
            if (templateItemBean.getType() == Integer.valueOf(enumType)) {
                tableDescriptionEntityTmp.setEnums(templateItemBean.getEnums());
            }
            tableDescriptionEntityMapper.insertTableDescription(number, templateItemBean.getItemName(),
                    templateItemBean.getItemDesc(), templateItemBean.getType(),
                    templateItemBean.getIsNull(), tableDescriptionEntityTmp.getEnums(),
                    templateItemBean.getIsUnionOnly(), templateItemBean.getIsSearch(),
                    templateId,templateItemBean.getIsSort());
        }
        //新增模板汇总统计表对应记录
        templateAddBean.setId(templateId);
        AddOrEditTemplateCollect("add", templateAddBean, userId);
        return templateId;
    }


    /**
     * 插入模板信息并返回模板id
     *
     * @param templateAddBean
     * @return
     * @throws Exception
     */
    private Integer getIdAndCreateTable2(TemplateAddBean templateAddBean, String userId) throws Exception {

        String number = templateAddBean.getTemplateNum();
        String templateSourceName = templateAddBean.getTemplateSourceName();
        templateEntityMapperExt.insertTemplate(number, templateAddBean.getTemplateName(),
                templateAddBean.getDescription(), templateAddBean.getTempTagId(), userId,
                new Date(), templateAddBean.getFillInPeriodKey(), templateSourceName, "0");
        //获取模板id
        TemplateEntity templateEntity = templateEntityMapperExt.selectByTemplateId(templateSourceName, number);
        Integer templateId = templateEntity.getId();

        //新增模板和部门关系
        List<AuthDepartmentInfoEntity> dptList = templateAddBean.getDepartmentId();
        for (AuthDepartmentInfoEntity dpt : dptList) {
            tempToDptService.insert(templateId, dpt.getId());
        }
        List<TemplateItemBean> itemBeanList = templateAddBean.getItemBeans();
        for (TemplateItemBean templateItemBean : itemBeanList) {
            if ("".equals(templateItemBean.getItemName())) {
                throw new BusinessException(PublicResultConstant.TEMPLATE_NUM_IS_NULL);
            }
            if ("".equals(templateAddBean.getTemplateNum())) {
                throw new BusinessException(PublicResultConstant.TEMPLATE_NUM_IS_NULL);
            }
            TableDescriptionEntity tableDescriptionEntityTmp = new TableDescriptionEntity();
            if (templateItemBean.getType() == Integer.valueOf(enumType)) {
                tableDescriptionEntityTmp.setEnums(templateItemBean.getEnums());
            }
            tableDescriptionEntityMapper.insertTableDescription(number, templateItemBean.getItemName(),
                    templateItemBean.getItemDesc(), templateItemBean.getType(),
                    templateItemBean.getIsNull(), tableDescriptionEntityTmp.getEnums(),
                    templateItemBean.getIsUnionOnly(), templateItemBean.getIsSearch(),
                    templateId,templateItemBean.getIsSort());
        }
        //新增模板汇总统计表对应记录
        templateAddBean.setId(templateId);
        AddOrEditTemplateCollect("add", templateAddBean, userId);
        return templateId;
    }



    /**
     * 功能描述: 删除模板<br>
     *
     * @param templateIdList
     * @return:
     */
    @Override
    public void deleteTemplate(List<Integer> templateIdList) throws Exception {
        //TODO 日志记录
        if (templateIdList == null) {
            throw new BusinessException(PublicResultConstant.PARAM_ERROR);
        }
        int sum = 0;
        for (int tid : templateIdList) {
            try {
                String templateId = String.valueOf(tid);
                //删除模板跟部门的关系
                tempToDptService.deleteTempToDptByTempId(templateId);

                TemplateEntity entity = templateEntityMapper.selectByPrimaryKey(tid);
                String templateSourceName = entity.getTemplateSourceName();
                String templateTableName = entity.getNumber();
                String tableName = templateSourceName + "." + templateTableName;
                //删除模板及对应的字段
                tableDescriptionEntityMapperExt.deleteByTemplateId(tid);
                templateEntityMapper.deleteByPrimaryKey(tid);
                //为防止数据库表被误删，首先判断表是否存在
                int tableCount = createTableBeanMapper.checkTableIfExist(templateSourceName, templateTableName);

                if (tableCount > 0) {
                    //查询表数据
                    int dataCount = createTableBeanMapper.selectDataCount(tableName);
                    if (dataCount > 0) {
                        String newTableName = "bak." + templateSourceName + "_" + templateTableName
                                + System.currentTimeMillis();
                        //先备份表数据
                        createTableBeanMapper.bakTableData(newTableName, tableName);
                        //再删除原表
                        createTableBeanMapper.deleteTable(tableName);

                    } else {
                        //若是空表直接删除
                        createTableBeanMapper.deleteTable(tableName);
                    }
                    EsUtil.deleteByTmpNum(templateTableName, templateSourceName);
                }

            } catch (Exception e) {
                //TODO 日志
                logger.error(e);
                throw new RuntimeException(PublicResultConstant.ERROR);
            }
            sum++;
        }
    }

    @Override
    public PageResult getTemplateList(Page<TemplateEntity> page, String keyword,
                                      String departmentId, String userId, String searchDepId, String isUsed) {
        //平台开发者/系统管理员，可以操作所有模板，部门管理员只可查看本部门模板
        List<String> deptList = authDptService.getDepListByRoleKey(departmentId, userId);
        List<TemplateModel> templateList = templateEntityMapper.selectAllByKeyword(keyword, deptList);
        //设置模板部门名称
        setTemplateDeptName(templateList);
        if (StringUtils.isNotBlank(searchDepId)) {
            // 获取指定部门ID下的所有子部门及本部门ID
            List<String> searchDepList = authDptService.getDepListById(searchDepId);
            templateList = templateList.stream().filter(item -> searchDepList.contains(item.getDepartmentId())).
                    collect(Collectors.toList());
        }
        if (StringUtils.isNotBlank(isUsed)) {
            templateList = templateList.stream().filter(item -> (isUsed).equals(item.getIsUsed())).
                    collect(Collectors.toList());
        }
        PageResult res = new PageResult();
        if (templateList.size() > 0) {
            PageUtil<TemplateModel> pag = new PageUtil<>(templateList, page.getSize());
            pag.setCurrent_page(page.getCurrent());
            res.setData(pag.getCurrentPageData());
            res.setTotal(pag.getTotal_sum());
        }
        return res;
    }


    /**
     * @param
     * @return
     * @Description: 设置模板部门名称
     * @Author: w15112
     * @Date: 2019/6/18 16:17
     */
    private void setTemplateDeptName(List<TemplateModel> templateList) {
        for (TemplateModel model : templateList) {
            List<String> list = tempToDptMapper.getDptNameByTemplateId(model.getId());
            model.setDepartmentName(StringUtils.join(list.toArray(), ","));
            setTemplateUserName(model);
        }
    }

    /**
     * 设置模板更新人的名称
     */
    @Override
    public void setTemplateUserName(TemplateModel modelTemp) {
        String userId = modelTemp.getUpdateUser();
        AuthUserInfoEntity entity = authUserInfoEntityMapper.selectUserByUserId(userId);
        if (null != entity) {
            modelTemp.setUpdateUser(entity.getUserName());
        }
    }

    @Override
    public List<TemplateEntity> getTemplateList(String tag, String name, String departmentCode) {
        List<String> list = new ArrayList<>();
        if (tag.contains(",")) {
            list = Arrays.asList(tag.split(","));
        } else if (!"".equals(tag)) {
            list.add(tag);
        }
        List<TemplateEntity> templateList = templateEntityMapper.getTemplateList(list, name, departmentCode);
        return templateList;
    }


    @Override
    public Boolean createTable(TemplateAddBean templateAddBean) {
        Map<String, String> map = createTableMap(templateAddBean);
        String name = map.get("name");
        String fields = map.get("fields");
        int result = createTableBeanMapper.createTable(name, fields);
        return result > 0;
    }

    /**
     * 功能描述: 模板导出
     *
     * @return:
     */
    @Override
    public void exportToExcel(HttpServletRequest request, Integer templateId, HttpServletResponse response) {

        try {

            //获取下载模信息
            //根据模板id获取模板信息
            TemplateEntity template = templateEntityMapper.selectByPrimaryKey(templateId);
            List<TableDescriptionEntity> descriptionList = tableDescriptionEntityMapperExt.selectByTemplateId(templateId);
            //创建一个excel文件工作薄
            XSSFWorkbook  workbook = new XSSFWorkbook ();
            //创建一张sheet页
            Sheet sheet = workbook.createSheet("sheet1");
            //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
            sheet.setColumnWidth(1, 24 * 256);
            //设置为居中加粗
            CellStyle style = workbook.createCellStyle();
            Font fontStyle = workbook.createFont();
            fontStyle.setFontName("微软雅黑");
            fontStyle.setFontHeightInPoints((short) 12);
            fontStyle.setBold(Boolean.TRUE);
            style.setFont(fontStyle);
            style.setAlignment(HorizontalAlignment.LEFT);

            String fileName = template.getName() + ".xlsx";
            //生成sheet1
            Row row = sheet.createRow(0);
            //写标题
            TableDescriptionEntity description = null;

            //设置文本格式
            CellStyle textStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            textStyle.setDataFormat(format.getFormat("@"));
            for (int i = 0; i < descriptionList.size(); i++) {
                Cell cell = row.createCell(i);
                description = descriptionList.get(i);
                cell.setCellValue(description.getName());
                cell.setCellType(CellType.STRING);
                cell.setAsActiveCell();
             /*   if (dateType == description.getType()) {
                    CellStyle dateStyle = workbook.createCellStyle();
                    dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));
                    sheet.setDefaultColumnStyle(i, dateStyle);
                } else {*/
                    sheet.setDefaultColumnStyle(i, textStyle);
               // }
                cell.setCellStyle(style);

            }
            //自动设置列宽
            for (int i = 0; i < descriptionList.size(); i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 17 / 10);
            }
            for (int j = 0; j < descriptionList.size(); j++) {
                description = descriptionList.get(j);
                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet)sheet);
                //枚举类型设置下拉列表
                if (description.getType() == enumType && null != description.getEnums()) {
                    XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) dvHelper
                            .createExplicitListConstraint(description.getEnums().split(",|，|/|/"));
                    CellRangeAddressList regions = new CellRangeAddressList(1, 65532, j, j);
                    XSSFDataValidation dataValidation = (XSSFDataValidation) dvHelper.createValidation(constraint, regions);
                    sheet.addValidationData(dataValidation);
                } /*else if (dateType == description.getType()) {
                    XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper
                            .createDateConstraint(DVConstraint.OperatorType.BETWEEN, "1900-01-01",
                                    "5000-01-01", "yyyy-mm-dd");
                    CellRangeAddressList addressList = new CellRangeAddressList(1, 65532, j, j);
                    XSSFDataValidation dataValidation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
                    dataValidation.setSuppressDropDownArrow(false);
                    dataValidation.createPromptBox("", "输入yyyy-mm-dd格式日期");
                    // 设置输入错误提示信息
                    dataValidation.createErrorBox("输入日期格式错误", "您输入的日期格式不符合'yyyy-mm-dd'格式规范，请重新输入！");
                    dataValidation.setShowPromptBox(true);
                    sheet.addValidationData(dataValidation);
                }*/
            }
            buildExcelDocument2(request, fileName, workbook, response);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * @param templateDesName
     * @param templateName
     * @param templateId
     * @return
     */
    @Override
    public void modifyTemplate(String templateDesName, String templateName, int templateId) throws Exception {
        TemplateEntity uptmp = new TemplateEntity();
        uptmp = templateEntityMapper.selectByPrimaryKey(templateId);
        uptmp.setDescription(templateDesName);
        uptmp.setName(templateName);
        try {

            templateEntityMapper.updateByPrimaryKey(uptmp);
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException(PublicResultConstant.ERROR);
        }
    }

    public static Map<String, String> createTableMap(TemplateAddBean templateAddBean) {
        try {
            List<String> fieldList = new ArrayList<>();
            List<TemplateItemBean> templateItemBeanList = templateAddBean.getItemBeans();
            Map<String, String> map = new HashMap<>();
            String sql = "( id varchar(64)  NOT NULL ,";
            for (int i = 0; i < templateItemBeanList.size(); i++) {
                String field = templateItemBeanList.get(i).getItemName();
                //校验表格中是否含有id及重复的标识
                if ("id".equals(field.toUpperCase())) {
                    throw new BusinessException("表格中的标识不能用id或ID或iD或Id！");
                }
                for (String str : fieldList) {
                    if (field.equals(str)) {
                        throw new BusinessException("表格中的名称或标识有重复！");
                    }
                }
                fieldList.add(field);
                String type = setType(templateItemBeanList.get(i).getType());
                sql += field + " " + type + " " + "comment '" + templateItemBeanList.get(i).getItemDesc() + "',";
            }
            sql = sql + "update_user varchar(150) comment '数据上传用户ID', " +
                    "update_time timestamp NULL default CURRENT_TIMESTAMP comment '数据上传时间'," +
                    "num_id bigint(20) PRIMARY KEY NOT NULL AUTO_INCREMENT comment '数据排序ID') comment '"
                    + templateAddBean.getTemplateName() + "'";
            map.put("name", templateAddBean.getTemplateSourceName() + "." + templateAddBean.getTemplateNum());
            map.put("fields", sql);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String setType(int type) throws Exception {
        if (type == 0) {
            return "varchar(255)";
        } else if (type == 1) {
            return "varchar(32)";
        } else if (type == 2) {
            return "varchar(64)";
        } else if (type == 3) {
            return "varchar(20)";
        } else if (type == 4) {
            return "varchar(255)";
        } else if (type == 5) {
            return "text";
        }else if (type == 6) {
            return "varchar(500)";
        }
        return null;
    }

    //浏览器下载excel
    private void buildExcelDocument(HttpServletRequest request, String filename, HSSFWorkbook workbook, HttpServletResponse response)
            throws Exception {
        FileUtil.setHeader(request, response, filename, "");
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }


    //浏览器下载excel2
    private void buildExcelDocument2(HttpServletRequest request, String filename, Workbook
            workbook, HttpServletResponse response)
            throws Exception {
        FileUtil.setHeader(request, response, filename, "");
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 根据模板number查询模板及下字段信息
     */
    @Override
    public TemplateAddBean queryTempByNumber(String tempNumber, String templateSourceName) {
        List<AuthDepartmentInfoEntity> departmentInfoEntityList = new ArrayList<>();
        TemplateAddBean templateAddBean = templateEntityMapper.queryTempByNumber(tempNumber, templateSourceName);
        if (null != templateAddBean) {
            List<TempToDpt> tempToDptList = tempToDptService.selectList(templateAddBean.getId());
            for (TempToDpt tempToDpt : tempToDptList) {
                String departmentId = tempToDpt.getDepartmentId();
                AuthDepartmentInfoEntity dpt = authDepartmentInfoEntityMapper.selectDepartmentById(departmentId);
                if (null != dpt) {
                    dpt.setId(departmentId);
                }
                departmentInfoEntityList.add(dpt);
            }
            templateAddBean.setDepartmentId(departmentInfoEntityList);
        }
        templateAddBean.setFillInPeriodKey(templateAddBean.getFillInPeriod());
        DictData data = dictDataMapper.getDictDataByTypeValue("FILLIN_PWRIOD", templateAddBean.getFillInPeriod());
        if (null != data) {
            templateAddBean.setFillInPeriod(data.getDictLabel());
        }
        String tagId = templateAddBean.getTempTag();
        templateAddBean.setTempTagId(tagId);
        String tagName = getTagName(tagId);
        templateAddBean.setTempTag(tagName);
        return templateAddBean;
    }

    @Override
    public String getTagName(String tag) {
        if (StringUtils.isNotBlank(tag)) {
            List<String> tagList = Arrays.asList(tag.split(","));
            StringBuffer newTag = new StringBuffer();
            for (String tagId : tagList) {
                DictData dictData = dictDataMapper.selectDictDataById(tagId);
                if (null != dictData) {
                    newTag.append(dictData.getDictLabel() + ",");
                }
            }
            tag = newTag.delete(newTag.lastIndexOf(","), newTag.length()).toString();
        }
        return tag;
    }

    @Override
    public List<TableDescriptionEntity> queryItemsByNumber(Integer templateId) {
        List<TableDescriptionEntity> list = tableDescriptionEntityMapperExt.selectByTemplateId(templateId);
        return list;
    }

    @Override
    @Transactional
    public void updateTempAndDescription(TemplateAddBean template, String userId) throws Exception {
        template.setTemplateSourceName(template.getTemplateSourceNameEn());
        TemplateEntity tmp2 = templateEntityMapperExt.selectByTemplateNameAndId(template.getTemplateName(), template.getTemplateNum());
        if (tmp2 != null) {
            throw new BusinessException(PublicResultConstant.TEMPLATE_NAME_REPETITION);
        }
        template.setUpdateUser(userId);
        template.setUpdateTime(new Date());
        template.setTempTag(template.getTempTagId());
        template.setFillInPeriod(template.getFillInPeriodKey());
        //1根据模板number修改模板名称和模板说明、模板是否使用
        templateEntityMapper.updateTempByTempNu(template);
        //2修改模板对应的表真实表的注释
        templateEntityMapper.updateTempCommentByNu(template);
        //3修改模板字段的字段名称根据模板numer和item，先删除原有的字段记录，然后重新插入
        tableDescriptionEntityMapper.deleteByTemplateId(template.getId());
        List<TemplateItemBean> itemBeans = template.getItemBeans();
        for (TemplateItemBean itemBean : itemBeans) {
            if (!"0".equals(itemBean.getSign())) {
                itemBean.setTemplateId(template.getId());
                itemBean.setNumber(template.getTemplateNum());
            }
        }
        tableDescriptionEntityMapper.insertTableDescriptionEntity(itemBeans);
        //编辑模板汇总统计表对应记录
        AddOrEditTemplateCollect("edit", template, userId);
        //4修改模板对应的表的真实表结构字段注释及更新es中的数据
        List<String> updateDescCommentSql = getUpdateDescCommentSql(template);
        if (updateDescCommentSql.size()>0){
            try {
                tableDescriptionEntityMapper.updateDescCommentByTmpNu(updateDescCommentSql);
            } catch (Exception e) {
                e.printStackTrace();
                e.printStackTrace();
                if (e instanceof  BadSqlGrammarException){
                    throw new BusinessException(template.getTemplateNum() + "字符串字段个数超过84，请将部分字段设置为长文本类型！");
                }else {
                    throw new BusinessException(template.getTemplateNum() + "编辑表失败！");
                }
            }
            dataProcessService.InsertSingleDataToEs(template.getId());
        }
        //5更改模板部门关系先删除
        tempToDptService.deleteTempToDptByTempId(String.valueOf(template.getId()));
        //再插入
        List<AuthDepartmentInfoEntity> dptList = template.getDepartmentId();
        for (AuthDepartmentInfoEntity dpt : dptList) {
            tempToDptService.insert(template.getId(), dpt.getId());
        }


    }

    @Override
    public Map checkItemExist(String templateSourceName, String tableName, String item) {
        Boolean flag = false;
        item = tableDescriptionEntityMapper.getTableItem(templateSourceName, tableName, item);
        Map<String, Boolean> map = new HashMap<>(1);
        if (StringUtils.isNotBlank(item)) {
            flag = true;
        }
        map.put("flag", flag);
        return map;
    }


    /**
     * @param
     * @return
     * @Description: 新增/编辑模板汇总表记录
     * @Author: w15112
     * @Date: 2019/6/5 15:58
     */
    private void AddOrEditTemplateCollect(String type, TemplateAddBean template, String userId) {
        Integer templateId = template.getId();
        TemplateCollectEntity entity = new TemplateCollectEntity();
        entity.setTemplateId(templateId);
        entity.setIndexCount(template.getItemBeans().size());
        entity.setUpdateUser(userId);
        entity.setUpdateTime(new Date());
        if ("edit".equals(type)) {
            TemplateCollectEntity templateCollectEntity = templateCollectEntityMapper.
                    getTemplateCollectById(templateId);
            entity.setId(templateCollectEntity.getId());
            entity.setNumberSum(templateCollectEntity.getNumberSum());
            entity.setFillCount(templateCollectEntity.getFillCount());
            entity.setUpdateUser(templateCollectEntity.getUpdateUser());
            entity.setUpdateTime(templateCollectEntity.getUpdateTime());
            List<TemplateItemBean> items = template.getItemBeans();
            Integer addCount = Math.toIntExact(items.stream().filter(item -> "1".equals(item.getSign())).count());
            Integer delCount = Math.toIntExact(items.stream().filter(item -> "0".equals(item.getSign())).count());
            Integer  indexCount= templateCollectEntity.getIndexCount();
            entity.setIndexCount(indexCount+addCount-delCount);

            if (delCount == indexCount){
                //原模板字段全部删除，则删除数据库中原表中的数据
                createTableBeanMapper.deleteData(template.getTemplateSourceNameEn()+"."+template.getTemplateNum());
                entity.setFillCount(0);
                entity.setNumberSum(0);
                tableFillLogEntityMapper.deleteByTemplateId(template.getId());
            }
            templateCollectEntityMapper.updateByPrimaryKey(entity);
        } else {
            templateCollectEntityMapper.insert(entity);
        }
    }

    public List<String> getUpdateDescCommentSql(TemplateAddBean template) throws Exception {
        ArrayList<String> sqls = new ArrayList<>();
        ArrayList<String> ibs = new ArrayList<>();
        List<TemplateItemBean> itemBeans = template.getItemBeans();
        String tableName = template.getTemplateSourceName() + "." + template.getTemplateNum();
        String delSql = "alter table " + tableName + " drop ";
        String editSql = "alter table " + tableName + " change column ";
        String addSql = "alter table " + tableName + " add ";

        for (int i = 0; i < itemBeans.size(); i++) {
            StringBuilder editStr = new StringBuilder(editSql);
            StringBuilder addStr = new StringBuilder(addSql);
            TemplateItemBean item = itemBeans.get(i);
            String type = setType(item.getType());
            String sign = item.getSign();
            String itemName = item.getItemName();
            if ("1".equals(sign)) {
                //增加列,第一个字段和之后的字段按序分别处理
                if (0 == i) {
                    addStr.append(itemName + " " + type + " comment '" + item.getItemDesc() + "'");
                } else {
                    TemplateItemBean frontItem = itemBeans.get(i - 1);
                    addStr.append(itemName + " " + type + " comment '" + item.getItemDesc() + "' after " +
                            frontItem.getItemName());
                }

                sqls.add(addStr.toString());
            } else if ("2".equals(sign)) {
                editStr.append(item.getItemOldName() + " " + itemName + " " + type + " comment '" + item.getItemDesc() + "'");
                sqls.add(editStr.toString());
            } else if ("0".equals(sign)) {
                StringBuilder delStr = new StringBuilder(delSql);
                delStr.append(itemName);
                sqls.add(delStr.toString());
                EsUtil.deleteField(template.getTemplateNum(), template.getTemplateSourceName(), itemName);
            }
            ibs.add(item.getItemName());
        }
        return sqls;
    }

    @Override
    public Map checkTemOrTabExist(String temOrTabName, String item) {
        Boolean flag = false;
        if (temOrTabName.equals("number")) {
            item = item.toLowerCase();
        }
        item = templateEntityMapper.getTemOrTabNameItem(temOrTabName, item);
        Map<String, Boolean> map = new HashMap<>(1);
        if (StringUtils.isNotBlank(item)) {
            flag = true;
        }
        map.put("flag", flag);
        return map;
    }

    /**
     * @Description: 批量导入模板字段
     * @Param:
     * @Author: l17503
     * @Date: 2019/9/2 14:14
     * @Updater: l17503
     * @UpdateTime: 2019/09/05 19:53
     * @UpdateContent: “接口参数”为空的判断；“字符类型”-->“字符串”
     */
    @Override
    public List<TemplateItemExcelModel> batchImportField(MultipartFile file) throws Exception {

        //1、模板验证
        checkTemplate(file);

        //2、读取excel文件
        Map<String, String> map = new HashMap<>();
        map.put("字段名", "itemName");
        map.put("中文标识", "itemDesc");
        map.put("是否必填", "isNullValue");
       // map.put("是否排序", "isSortValue");
        map.put("类型", "typeValue");
        map.put("枚举值", "enums");

        InputStream inputStream = file.getInputStream();
        List<TemplateItemExcelModel> list = ExcelUtil.readExcel(file.getOriginalFilename(), inputStream, TemplateItemExcelModel.class, map);
        inputStream.close();

        //3、转换“必填”和“类型”值
        list.forEach(item -> {
            String isNullValue = item.getIsNullValue();
            String typeValue = item.getTypeValue();
            String isSortValue=item.getIsSortValue();

            if ("是".equals(isNullValue) || "1".equals(isNullValue)) {
                item.setIsNull((byte) 0);
            } else if ("否".equals(isNullValue) || "0".equals(isNullValue)) {
                item.setIsNull((byte) 1);
            } else {
                item.setIsNull((byte) 1);
            }

            if ("否".equals(isSortValue) || "0".equals(isSortValue)) {
                item.setIsSort((byte) 1);
            } else if ("是".equals(isSortValue) || "1".equals(isSortValue)) {
                item.setIsSort((byte) 0);
            } else {
                item.setIsSort((byte) 0);
            }

            if ("字符串".equals(typeValue) || "0".equals(typeValue)) {
                item.setType(0);
            } else if ("整数".equals(typeValue) || "1".equals(typeValue)) {
                item.setType(1);
            } else if ("小数".equals(typeValue) || "2".equals(typeValue)) {
                item.setType(2);
            } else if ("日期".equals(typeValue) || "3".equals(typeValue)) {
                item.setType(3);
            } else if ("枚举".equals(typeValue) || "4".equals(typeValue)) {
                item.setType(4);
            } else if ("文本".equals(typeValue) || "5".equals(typeValue)) {
                item.setType(5);
            } else {
                item.setType(0);
            }

            if (StringUtils.isEmpty(item.getEnums())) {
                item.setEnums(null);
            }
        });

        return list;
    }

    /**
     * 批量停用/启用表格是否使用状态
     */
    @Override
    public Map updateTemplateStatus(TemplateAddBean templateAddBean) {
        Map<String, String> map = new HashMap<>();
        String isUsed = templateAddBean.getIsUsed();
        List<Integer> templateIds = templateAddBean.getTemplateIds();
        try {
            for (Integer templateId : templateIds) {
                templateEntityMapper.updateTempById(templateId, isUsed);
            }
            if ("0".equals(isUsed)) {
                map.put("message", "表格启用成功！");
            } else {
                map.put("message", "表格禁用成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if ("0".equals(isUsed)) {
                map.put("message", "表格启用失败！");
            } else {
                map.put("message", "表格禁用失败！");
            }
        }
        return map;
    }

    /**
     * @Description: 模板检查
     * @Param:
     * @Author: l17503
     * @Date: 2019/9/3 16:58
     */
    public void checkTemplate(MultipartFile file) throws Exception{
        String filename = file.getOriginalFilename();
        String suffixName = filename.substring(filename.lastIndexOf(".") + 1);

        List itemList = new ArrayList();
        if (!StringUtils.isEmpty(suffixName)) {
            InputStream inputStream = file.getInputStream();
            if ("xls".equals(suffixName)) {
                HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
                HSSFSheet sheetAt0 = hssfWorkbook.getSheetAt(0);
                HSSFRow row = sheetAt0.getRow(0);
                itemList.add(row.getCell(0).getStringCellValue());
                itemList.add(row.getCell(1).getStringCellValue());
                itemList.add(row.getCell(2).getStringCellValue());
                itemList.add(row.getCell(3).getStringCellValue());
                itemList.add(row.getCell(4).getStringCellValue());
                //itemList.add(row.getCell(5).getStringCellValue());

            } else if ("xlsx".equals(suffixName)) {
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);

                XSSFSheet sheetAt0 = xssfWorkbook.getSheetAt(0);
                XSSFRow row = sheetAt0.getRow(0);
                if(null == row){
                    throw new BusinessException("上传excel文件为空文件！");
                }
                if(row.getLastCellNum()!=5){
                    throw new BusinessException("模板不匹配，请按照模板“字段名|中文标识|是否必填|类型|枚举值”导入数据");
                }
                itemList.add(row.getCell(0).getStringCellValue());
                itemList.add(row.getCell(1).getStringCellValue());
                itemList.add(row.getCell(2).getStringCellValue());
                itemList.add(row.getCell(3).getStringCellValue());
                itemList.add(row.getCell(4).getStringCellValue());
                //itemList.add(row.getCell(5).getStringCellValue());
            } else {
                inputStream.close();
                throw new BusinessException("请上传excel文件");
            }
            inputStream.close();
        } else {
            throw new BusinessException("请上传excel文件");
        }

        if (!itemList.contains("字段名") || !itemList.contains("中文标识") || !itemList.contains("是否必填")
                || !itemList.contains("类型") || !itemList.contains("枚举值")) {
            throw new BusinessException("模板不匹配，请按照模板“字段名|中文标识|是否必填|类型|枚举值”导入数据");
        }
    }

    /**
     * 功能描述: 检查模板中是否存在数据
     *
     * @param
     * @return:
     */
    @Override
    public Map checkTemplateDataExist(List<Integer> templateIdList) throws Exception {

        if (templateIdList == null) {
            throw new BusinessException(PublicResultConstant.PARAM_ERROR);
        }
        Map<String, String> map = new HashMap<>();
        for (int tid : templateIdList) {
            try {
                TemplateCollectEntity templateCollectEntity = templateCollectEntityMapper.getTemplateCollectById(tid);
                if (null != templateCollectEntity){
                    int numberSum = templateCollectEntity.getNumberSum();
                    if (numberSum > 0) {
                        TemplateEntity entity = templateEntityMapper.selectByPrimaryKey(tid);
                        String templateName = entity.getName();
                        map.put("warn", "【" + templateName + "】表中存在数据");
                    } else {
                        map.put("warn", "");
                    }
                }

            } catch (Exception e) {
                //TODO 日志
                logger.error(e);
                throw new RuntimeException(PublicResultConstant.ERROR);
            }

        }
        return map;
    }
    /*    @Override
        public void exportData(Integer templateId, String updateTime,
                               String dir, HttpServletRequest request, HttpServletResponse response) throws Exception{
            ExecutorService pool = newFixedThreadPool(15, 15);
            BlockThreadPool p = new BlockThreadPool(2);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        exportData1(templateId,updateTime,dir,request,response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            p.execute(runnable);


        }*/
    @Override
    public void exportData(Integer templateId, String updateTime,
                           String dir, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //根据模板id获取模板信息
        TemplateEntity templateEntity = templateEntityMapper.selectByPrimaryKey(templateId);
        TemplateCollectEntity collectEntity = templateCollectEntityMapper.getTemplateCollectById(templateId);
        EsQuery esQuery = new EsQuery(templateEntity, dir, collectEntity.getNumberSum(),updateTime);
        esQuery.setOrderField("num_id");
        ReturnData returnData = esDataOperationService.searchByKeywords(esQuery);
        List<Map<String, Object>> dataList = returnData.getDataList();
        List<TemplateItemBean> itemList = returnData.getItemList();
       /* List<TableDescriptionEntity> itemList1 = tableDescriptionEntityMapper.selectByTemplateId(templateId);
        String sql = "select * from "+templateEntity.getTemplateSourceName()+"."+templateEntity.getNumber()+
                "where update_time = "+updateTime+" order by num_id"+dir;
        List<Map<String, Object>> dataList1 = templateCollectEntityMapper.getMapsBySql(sql);*/

        String fileName = templateEntity.getName() + ".xlsx";
        //提交要执行的任务

        SXSSFWorkbook sxssfWorkbook = null;
        // 获取SXSSFWorkbook,内存中保留100条数据，其余写入硬盘临时文件
        sxssfWorkbook = new SXSSFWorkbook(100);
        Sheet sheet = sxssfWorkbook.createSheet("Sheet1");
        // 冻结第一行
        sheet.createFreezePane(0, 1);
        //创建第一行，作为header表头
        Row header = sheet.createRow(0);
        //移除itemList中更新时间字段
        if(itemList.size()>0){
            itemList.remove(itemList.size()-1);
        }
        //循环创建header单元格
        for (int cellNum = 0; cellNum < itemList.size(); cellNum++) {
            Cell cell = header.createCell(cellNum);
            cell.setCellValue(itemList.get(cellNum).getItemDesc());
            //设置每列固定宽度
            sheet.setColumnWidth(cellNum, 20 * 256);
        }
        //遍历创建行，导出数据
        for (int rowNum = 1; rowNum <= dataList.size(); rowNum++) {
            Row row = sheet.createRow(rowNum);
            Map<String, Object> map = dataList.get(rowNum-1);

            //循环创建单元格
            for (int cellNum = 0; cellNum < itemList.size(); cellNum++) {
                Cell cell = row.createCell(cellNum);

                Object value = map.get(itemList.get(cellNum).getItemName());
                if (value != null) {
                    cell.setCellValue(String.valueOf(value));
                } else {
                    cell.setCellValue("null");
                }
            }
        }
        buildExcelDocument2(request, fileName, sxssfWorkbook, response);
        if (sxssfWorkbook != null) {
            //处理SXSSFWorkbook导出excel时，产生的临时文件
            sxssfWorkbook.dispose();
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

    /**
     * 是否含有附件类型
     * @param templateId templateId
     * @return 含有个数（0,1,2）
     */
    @Override
    public boolean countAnnex(Integer templateId) throws Exception {
        int count = tableDescriptionEntityMapperExt.countAnnex(templateId);
        return count > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 根据模板名称获取附件字段列表，用于大屏展示附件
     * @param templateName
     * @return
     * @throws Exception
     */
    @Override
    public List<Map> getAnnexWordByTemplateName(String templateName) throws Exception {

        return null;
    }

    /**
     * 数据插入
     * @throws Exception
     */
    @Override
    public void move2() throws Exception {
        //1 获取模板主要数据
        List<Map<String, String>> templateWords = toMoveTest.getYfTemplateWords();
        //2 获取模板具体字段
        List<Map<String, String>> templateDes = toMoveTest.getYfTemplateDes();
        //3 插入模板信息

        for (int i = 0 ; i < templateWords.size(); i++) {
            Map<String, String> template = templateWords.get(i);

//            TemplateAddBean addBean = queryTempByNumber(template.get("number"), "report");
//            addBean.setTemplateSourceName("report");
//            checkAndCreateTable2(addBean);

            //4根据模板number抽取数据
            List<String> items = templateEntityMapper.getItermwords(String.valueOf(template.get("id")));
            StringBuilder sb = new StringBuilder();
            for (Object s:items) {
                sb.append(s).append(" ,");
            }
            String s = sb.toString();
            String s1=s.substring(0, sb.length() -1);
            int re = templateEntityMapper.mvInsert("id, " + s1, template.get("number"));
            System.out.println("hell0" + re);
        }
        System.out.println("hell0");
        return;
    }


    /**
     * 模板插入
     * @throws Exception
     */
    @Override
    public void move() throws Exception {
        //1 获取模板主要数据
        List<Map<String, String>> templateWords = toMoveTest.getYfTemplateWords();
        //2 获取模板具体字段
        List<Map<String, String>> templateDes = toMoveTest.getYfTemplateDes();
        //3 插入模板信息

        for (int i = 0 ; i < templateWords.size(); i++) {
            Map<String, String> template = templateWords.get(i);

            TemplateAddBean addBean = queryTempByNumber(template.get("number"), "report");
            addBean.setTemplateSourceName("report");
            checkAndCreateTable2(addBean);
        }
        System.out.println("hell0");
        return;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void move3() throws Exception {
        List<String> templateList = templateEntityMapper.selectTemplateList();
        for (String number : templateList){
            logger.info("正在更新：" + "report."+number + "表");
            int result = templateEntityMapper.updateCollectCount(number);
            logger.info("更新结束：" + "report."+number + "表" );
        }
    }
}
