package com.h3c.bigdata.zhgx.function.report.service;


import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.utils.PageResult;
import com.h3c.bigdata.zhgx.function.report.entity.TableDescriptionEntity;
import com.h3c.bigdata.zhgx.function.report.model.TemplateCollectModel;
import com.h3c.bigdata.zhgx.function.report.model.TemplateModel;

import java.util.List;
import java.util.Map;

/**
 * @author w15112
 * @title: 首页、模板总览、模板预览方法service类
 * @projectName new_zhgx
 * @description: TODO
 * @date 2019/6/311:02
 */
public interface TemplatePreviewService {
    /**
     * 功能描述: 获取模板预览统计值（模板总量、数据总量、模板总完成率 ）
     *
     * @return
     * @param userId
     */

    Map getPreviewInfo(String userId,String departmentId);

    /**
     * 功能描述: 获取部门名称、模板数量、完成率的列表
     *
     * @return
     */
    PageResult getDepartmentList(int pageNum, int pageSize, String userId, String departmentId,
                                 String field, String dir,String searchDepId);

    /**
     * 功能描述: 根据部门id获取模板列表信息
     *
     * @return
     */
    PageResult getTemplatesByDepId(int pageNum, int pageSize,String field,String dir,
                                   TemplateModel templateModel, String userId,String searchDepId );

    /**
     * @Description: 数据填报页面统计信息：模板总量、未填报模板数量、已填报模板数量
     * @param departmentId
     * @return
     * @Author: w15112
     * @Date: 2019/6/5 10:35
     */
    Map getTemplateCountInfo(String userId, String departmentId);


    /**
     * @Description: 根据数据库表名查询表字段信息
     * @param templateId
     * @return
     * @Author: w15112
     * @Date: 2019/6/6 11:11
     */
    List<TableDescriptionEntity> selectTableDescriptionById(Integer templateId);


    /**
     * @param templateId
     * @return
     * @Description: 根据表名查询填报记录列表
     * @Author: w15112
     * @Date: 2019/7/6 14:00
     */
    PageResult getTableFillLogsByTableName(Integer templateId, int pageNum, int pageSize);

    /**
     * 获取该部门及其子部门的数据填报信息
     *
     * @param
     * @return
     */
    TemplateCollectModel getParentDepartmentList(String departmentId);

    /**
     * 驾驶舱使用
     * 获取最新的十条数据填报详情
     *
     * @return
     * @author lvyacong
     */
    ApiResult getTemplateCollectInfo();
}
