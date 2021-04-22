package com.h3c.bigdata.zhgx.function.report.serviceImpl;

import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.utils.PageResult;
import com.h3c.bigdata.zhgx.common.utils.PageUtil;
import com.h3c.bigdata.zhgx.function.report.dao.TableDescriptionEntityMapper;
import com.h3c.bigdata.zhgx.function.report.dao.TableFillLogEntityMapper;
import com.h3c.bigdata.zhgx.function.report.dao.TemplateCollectEntityMapper;
import com.h3c.bigdata.zhgx.function.report.entity.TableDescriptionEntity;
import com.h3c.bigdata.zhgx.function.report.entity.TableFillLogEntity;
import com.h3c.bigdata.zhgx.function.report.model.TemplateCollectModel;
import com.h3c.bigdata.zhgx.function.report.model.TemplateModel;
import com.h3c.bigdata.zhgx.function.report.service.TemplateManageService;
import com.h3c.bigdata.zhgx.function.report.service.TemplatePreviewService;
import com.h3c.bigdata.zhgx.function.system.dao.AuthDepartmentInfoEntityMapper;
import com.h3c.bigdata.zhgx.function.system.dao.AuthUserInfoEntityMapper;
import com.h3c.bigdata.zhgx.function.system.dao.DictDataMapper;
import com.h3c.bigdata.zhgx.function.system.entity.AuthDepartmentInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthRoleInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.DictData;
import com.h3c.bigdata.zhgx.function.system.service.AuthDptService;
import com.h3c.bigdata.zhgx.function.system.serviceImpl.AuthRoleMenuServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author w15112
 * @title: TemplatePreviewServiceImpl
 * @projectName new_zhgx
 * @description: TODO
 * @date 2019/6/311:12
 */


@Service
@Slf4j
public class TemplatePreviewServiceImpl implements TemplatePreviewService {

    @Resource
    private TemplateCollectEntityMapper templateCollectEntityMapper;
    @Resource
    private TableDescriptionEntityMapper tableDescriptionEntityMapper;
    @Resource
    private TableFillLogEntityMapper tableFillLogEntityMapper;
    @Autowired
    private AuthUserInfoEntityMapper authUserInfoEntityMapper;
    @Autowired
    private AuthRoleMenuServiceImpl authRoleMenuService;
    @Autowired
    private DictDataMapper dictDataMapper;
    @Autowired
    private AuthDptService authDptService;
    @Autowired
    private TemplateManageService templateManageService;
    @Autowired
    private AuthDepartmentInfoEntityMapper authDepartmentInfoEntityMapper;


    /**
     * 功能描述: 获取模板预览统计值（模板总量、数据总量、模板总完成率 ）
     *
     * @param userId
     * @return
     */
    @Override
    public Map getPreviewInfo(String userId,String departmentId) {
        Map resultMap = new HashMap();
        TemplateCollectModel model = new TemplateCollectModel();
        //平台开发者/系统管理员，可以操作所有模板，部门管理员只可查看本部门模板
        AuthRoleInfoEntity entity = authRoleMenuService.getMinRoleKey(userId);
        List<String> deptList = new ArrayList<>();
        if (StringUtils.isNotBlank(departmentId)){
            deptList = authDptService.getDepListById(departmentId);
            model = templateCollectEntityMapper.getPreviewInfo(deptList);
        }else {
            if (null != entity) {
                if (Integer.valueOf(entity.getRoleKey()) < 2) {
                    model = templateCollectEntityMapper.getAdminPreviewInfo();
                } else {
                    AuthDepartmentInfoEntity departmentInfoEntity = authDepartmentInfoEntityMapper.
                            selectDeptByUserId(userId);
                    deptList = authDptService.getDepListById( departmentInfoEntity.getId());
                    model = templateCollectEntityMapper.getPreviewInfo(deptList);

                }
            }
        }
        int templateTotal = 0;
        int dataSum = 0;
        BigDecimal completionRate=new BigDecimal("0.00");
        if (null != model) {
            templateTotal = model.getTemplateTotal();
            dataSum = model.getDataSum();
            completionRate = model.getCompletionRate();
        }
        resultMap.put("templateTotal", templateTotal);
        resultMap.put("dataSum", dataSum);
        resultMap.put("completionRate", completionRate);
        return resultMap;
    }

    /**
     * 功能描述: 获取部门名称、模板数量、完成率的列表
     *
     * @return
     */
    @Override
    public PageResult getDepartmentList(int pageNum, int pageSize,String userId,String departmentId,
                                        String field,String dir,String searchDepId) {
        PageResult res = new PageResult();
        List<TemplateCollectModel> list = new ArrayList<>();
        //平台开发者/系统管理员，可以操作所有模板，部门管理员只可查看本部门模板
        List<String> deptList = getTemplateDepListByRoleKey(departmentId, userId);
        for (String depId:deptList){
            if (!"2".equals(depId)){
                list.add(getParentDepartmentList(depId));
            }
        }
        //部门筛选
        if (StringUtils.isNotBlank(searchDepId)){
            list = list.stream().filter(item -> searchDepId.equals(item.getDepartmentId())).collect(toList());
        }
        //排序
        list = sortTemplateModelList(list,field,dir);
        if (list.size()>0){
            res = setPageList(pageNum, pageSize, list, res);
        }else{
            res.setData(new ArrayList());
        }
        return res;
    }

    /**
     * 按表格数量/数据总量排序
     */
    private List<TemplateCollectModel> sortTemplateModelList(List<TemplateCollectModel> list,
                                                             String field, String dir) {
        Comparator<TemplateCollectModel> comparator = null;
        if ("dataSum".equals(field)) {
            comparator = Comparator.comparing(TemplateCollectModel::getDataSum);
        } else {
            comparator = Comparator.comparing(TemplateCollectModel::getTemplateTotal);
        }
        if ("desc".equals(dir)) {
            comparator = comparator.reversed();
        }
        list = list.stream().sorted(comparator).collect(toList());
        return list;
    }

    public List<String> getTemplateDepListByRoleKey(String departmentId, String userId) {
        AuthRoleInfoEntity entity = authRoleMenuService.getMinRoleKey(userId);
        List<String> depList = new ArrayList<>();
        if (StringUtils.isNotBlank(departmentId)){
            //本部門下的子部門
            depList = authDptService.getDepListById(departmentId);
            depList.remove(departmentId);
        }else {
            if (null != entity && Integer.valueOf(entity.getRoleKey()) < 2) {
                //  獲取所有模板的部門
                List<String> depList1 = templateCollectEntityMapper.getDepartmentIdByTemplate();
                //对二级部门ID修改为一级部门ID,并放入ID列表
                for (String dep : depList1) {
                    String parentCode = null;
                    parentCode = templateCollectEntityMapper.getParentCode(dep);
                    if ("1".equals(parentCode) && !depList.contains(dep)) {
                        depList.add(dep);
                    } else {
                        dep = templateCollectEntityMapper.getDepartmentIdByParentCode(parentCode);
                        if (StringUtils.isNotEmpty(dep) && !depList.contains(dep)) {
                            depList.add(dep);
                        }
                    }
                }
            } else {
                //普通用戶、部門管理員
                AuthDepartmentInfoEntity department = authDepartmentInfoEntityMapper.selectDeptByUserId(userId);
                depList.add(department.getId());
            }
        }

        return depList;
    }

    /**
     * 功能描述: 根据部门id获取模板列表信息
     *
     * @param templateModel
     * @param userId
     * @return
     */
    @Override
    public PageResult getTemplatesByDepId(int pageNum, int pageSize,String field,String dir,
                                          TemplateModel templateModel, String userId,String searchDepId) {
        List<String> deptList = new ArrayList<>();

        if (StringUtils.isBlank(templateModel.getType())) {
            //平台开发者/系统管理员，可以操作所有模板，部门管理员只可查看本部门模板
            AuthRoleInfoEntity entity = authRoleMenuService.getMinRoleKey(userId);
            if (null != entity && Integer.valueOf(entity.getRoleKey()) < 2) {
                deptList =null;
            }else {
                //type为1，在系统设置-模板总览下使用，查看该部门下的模板列表
                deptList = authDptService.getDepListById(templateModel.getDepartmentId());
            }
        }else {
            deptList.add(templateModel.getDepartmentId());
        }
        templateModel.setDepList(deptList);
        PageResult res = new PageResult();
        List<TemplateModel> list = templateCollectEntityMapper.getTemplates(templateModel,field,dir);
        for (TemplateModel model : list) {
            setTemplateModel(model);
        }
        if (StringUtils.isNotBlank(searchDepId)){
            // 获取指定部门ID下的所有子部门及本部门ID
            List<String> searchDepList = authDptService.getDepListById(searchDepId);
            list = list.stream().filter(item -> searchDepList.contains(item.getDepartmentId())).collect(toList());
        }

        if (list.size() > 0) {
            res = setPageList(pageNum, pageSize, list, res);
        }else{
            res.setData(list);
        }
        return res;
    }

    /**
     * @param departmentId
     * @return
     * @Description: 数据填报页面统计信息：模板总量、未填报模板数量、已填报模板数量
     * @Author: w15112
     * @Date: 2019/6/5 10:35
     */
    @Override
    public Map getTemplateCountInfo(String userId, String departmentId) {
        Map resultMap = new HashMap();
        Map map = new HashMap();
        TemplateCollectModel model = new TemplateCollectModel();
        List<String> deptList = new ArrayList<>();
        //平台开发者/系统管理员，可以操作所有模板，部门管理员只可查看本部门模板
        AuthRoleInfoEntity entity = authRoleMenuService.getMinRoleKey(userId);
        if (null != entity) {
            if (Integer.valueOf(entity.getRoleKey()) > 1) {
                deptList = authDptService.getDepListById(departmentId);
                model = templateCollectEntityMapper.getDataFillIndexInfo(deptList);
            } else {
                deptList = null;
                model = templateCollectEntityMapper.getAdminDataFillIndexInfo();
            }
        }
        if (null != model) {
            int incompletedTotal = model.getTemplateTotal() - model.getDataSum();
            map.put("templateTotal", model.getTemplateTotal());
            map.put("incompletedTotal", incompletedTotal);
            map.put("completedTotal", model.getDataSum());
        }
        resultMap.put("totalInfo", map);

        return resultMap;
    }


    /**
     * 设置更新周期和标签
     */
    private void setTemplateModel(TemplateModel modelTemp) {
        templateManageService.setTemplateUserName(modelTemp);
        DictData data = dictDataMapper.getDictDataByTypeValue("FILLIN_PWRIOD", modelTemp.getFillInPeriod());
        if (null != data) {
            modelTemp.setFillInPeriod(data.getDictLabel());
        }
        String tagId = modelTemp.getTag();
        String tagName = templateManageService.getTagName(tagId);
        modelTemp.setTag(tagName);
    }





    /**
     * @param templateId
     * @return
     * @Description: 根据数据库表名查询表字段信息
     * @Author: w15112
     * @Date: 2019/6/6 11:13
     */
    @Override
    public List<TableDescriptionEntity> selectTableDescriptionById(Integer templateId) {
        return tableDescriptionEntityMapper.selectByTemplateId(templateId);
    }


    /**
     * @param templateId
     * @return
     * @Description: 根据表名查询填报记录列表
     * @Author: w15112
     * @Date: 2019/7/6 14:03
     */
    @Override
    public PageResult getTableFillLogsByTableName(Integer templateId, int pageNum, int pageSize) {
        List<TableFillLogEntity> list = tableFillLogEntityMapper.getTableFillLogsByTableName(templateId);
       for (TableFillLogEntity tableFillLogEntity :list){
           String userId = tableFillLogEntity.getUpdateUser();
           AuthUserInfoEntity entity = authUserInfoEntityMapper.selectUserByUserId(userId);
           if (null != entity) {
               tableFillLogEntity.setUpdateUser(entity.getUserName());
           }
       }
        PageResult res = new PageResult();
        //数据分页
        if (list.size() > 0) {
            res = setPageList(pageNum, pageSize, list, res);
        }
        return res;
    }
    /**
     * 获取该部门及其子部门的数据填报信息
     *
     * @param
     * @return
     */
    @Override
    public TemplateCollectModel getParentDepartmentList(String departmentId)
    {
        //获取该部门的部门信息
        AuthDepartmentInfoEntity departmentInfo = authDepartmentInfoEntityMapper.selectDepartmentById(departmentId);
        //遍历ID列表,对每个ID获取其部门及子部门id
        List<String> depList2 = authDptService.getDepListById(departmentId);
        //统计该部门下所有的数据填报信息
        List<TemplateCollectModel> list = templateCollectEntityMapper.getDepartmentList(depList2);
        TemplateCollectModel temp=new TemplateCollectModel();
        //判断该部门下是否存在子部门
        if (depList2.size()>1){
            temp.setFlag(1);
        }

        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                TemplateCollectModel collectModel = list.get(i);
                temp.setDataSum(temp.getDataSum()+collectModel.getDataSum());
                temp.setFillCount(temp.getFillCount() +collectModel.getFillCount());
                temp.setTemplateTotal(temp.getTemplateTotal() +collectModel.getTemplateTotal());
//                temp.setIncompleteTemplateTotal(temp.getIncompleteTemplateTotal()+collectModel.getIncompleteTemplateTotal());
                temp.setCompleteTemplateTotal(temp.getCompleteTemplateTotal()+collectModel.getCompleteTemplateTotal());
            }
            temp.setCompletionRate(new BigDecimal(100*(((float)temp.getCompleteTemplateTotal())
                    /((float)temp.getTemplateTotal()))).setScale(1, BigDecimal.ROUND_HALF_UP));
        }
        temp.setDepartmentId(departmentId);
        temp.setDepartmentName(departmentInfo.getDepartmentName());
        temp.setIncompleteTemplateTotal(temp.getTemplateTotal()-temp.getCompleteTemplateTotal());
        temp.setIncompletionRate(new BigDecimal("100").subtract(temp.getCompletionRate()));
        return temp;
    }

    /**
     * 填报详情Top10查询
     *
     * @return
     */
    @Override
    public ApiResult<?> getTemplateCollectInfo() {
        List<TemplateCollectModel> list = new ArrayList<>();
        try {
            list = templateCollectEntityMapper.getTemplateCollectInfo();
            log.info("填报详情查询成功");
        } catch (Exception e) {
            log.error("填报详情查询失败", e.getMessage());
            return ApiResult.fail("填报详情查询失败", e.getMessage());
        }
        return ApiResult.fail("填报详情查询成功", list);
    }

    /**
     * @param
     * @return
     * @Description: 设置分页
     * @Author: w15112
     * @Date: 2019/7/6 14:21
     */
    public PageResult setPageList(int pageNum, int pageSize, List list, PageResult res) {

        PageUtil<T> pag = new PageUtil<>(list, pageSize);
        pag.setCurrent_page(pageNum);
        res.setData(pag.getCurrentPageData());
        res.setTotal(pag.getTotal_sum());
        return res;
    }
}
