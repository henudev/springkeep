package com.h3c.bigdata.zhgx.function.report.dao;

import com.h3c.bigdata.zhgx.common.persistence.BaseMapper;
import com.h3c.bigdata.zhgx.function.report.entity.TemplateCollectEntity;
import com.h3c.bigdata.zhgx.function.report.model.TemplateCollectModel;
import com.h3c.bigdata.zhgx.function.report.model.TemplateModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface TemplateCollectEntityMapper extends BaseMapper<TemplateCollectEntity> {
    /**
     * 功能描述: 获取模板预览统计值（模板总量、数据总量、模板总完成率 ）
     *
     * @return
     */
    TemplateCollectModel getPreviewInfo(@Param("list") List list);
    /**
     * 功能描述: 获取管理员模板预览统计值（模板总量、数据总量、模板总完成率 ）
     *
     * @return
     */
    TemplateCollectModel getAdminPreviewInfo();
    /**
     * 功能描述: 获取首页的模板总量、未完成模板数量、已完成模板数量
     *
     * @return
     */
    TemplateCollectModel getDataFillIndexInfo(@Param("list") List list);

    /**
     * 功能描述: 获取部门名称、模板数量、完成率的列表
     *
     * @return
     */
    List<TemplateCollectModel> getDepartmentList(@Param("list") List list);

    /**
     * 功能描述: 根据部门id获取模板列表信息
     *
     * @return
     */
    List<TemplateModel> getTemplatesByDepId(@Param("list") List list);


    /**
     * @Description: 动态sql查询
     * @param
     * @return
     * @Author: w15112
     * @Date: 2019/6/5 17:04
     */
    List<LinkedHashMap> getTableDataBySql(@Param("sql") String sql);
    List<Map<String, Object>> getMapsBySql(@Param("sql") String sql);
    List<String> getTableNamesBySql(@Param("sql") String sql);
    /**
     * @Description: 根据表名获取该模板的汇总信息
     * @param  templateId
     * @return
     * @Author: w15112
     * @Date: 2019/6/5 17:04
     */
    TemplateCollectEntity getTemplateCollectById(@RequestParam("templateId") Integer templateId);

    /**
     * 更新数据统计信息
     * @param templateId
     * @param count
     * @param updateTime
     * @param updateUser
     * @return
     */
    int updateCollect(@Param("templateId") Integer templateId,@Param("count") Integer count,
                      @Param("updateTime") String updateTime,@Param("updateUser")String updateUser);
    int updateCollectSum(@Param("templateId") Integer templateId,@Param("count") Integer count,
                      @Param("updateTime") String updateTime,@Param("updateUser")String updateUser);
    int updateCollectData(@Param("templateId") Integer templateId,@Param("count") Integer count,
                      @Param("updateTime") Date updateTime,@Param("updateUser")String updateUser);
    int updateCollectByFillLog(@Param("templateId") Integer templateId,@Param("sumCount") Integer sumCount,
                          @Param("updateUser")String updateUser);
    /**
     * @param
     * @return
     * @Description: 根据部门id或者模板名称、标签、数据库名获取模板列表信息
     * @Author: w15112
     * @Date: 2019/7/10 12:15
     */
    List<TemplateModel> getTemplates(@Param("query")TemplateModel templateModel,
                                     @Param("field")String field,@Param("dir")String dir);

    /**
     * 功能描述: 获取系统管理员首页的模板总量、未完成模板数量、已完成模板数量
     *
     * @return
     */
    TemplateCollectModel getAdminDataFillIndexInfo();
    /**
     * 功能描述: 获取创建了模板的所有部门ID
     *
     * @return
     */
    List<String>  getDepartmentIdByTemplate();

    /**
     * 功能描述: 获取部门ID的父级部门Code
     *
     * @return
     */
    String  getParentCode(@Param("departmentId")String departmentId);

    /**
     * 功能描述: 根据部门Code获取部门ID
     *
     * @return
     */
    String  getDepartmentIdByParentCode(@Param("parentCode")String parentCode);

    /**
     * 驾驶舱使用 获取最新的十条数据填报详情
     *
     * @return
     * @author lvyacong
     */
    List<TemplateCollectModel> getTemplateCollectInfo();


    /**
     * 分部门统计填报及时更新完成率
     */
    List<Map<String, Object>> selectCompleteRate();
}