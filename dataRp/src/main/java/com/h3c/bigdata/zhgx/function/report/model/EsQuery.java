package com.h3c.bigdata.zhgx.function.report.model;

import com.h3c.bigdata.zhgx.function.report.entity.TemplateEntity;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * @author q16926
 * @Title: EsQuery
 * @ProjectName dcplum
 * @Description: TODO
 * @date 2019/1/1515:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("查询参数bean")
public class EsQuery {
    Integer id;
    /**
     * 关键词.
     */
    String keywords;
    /**
     * 当前页.
     */
    int page;
    /**
     * 每页条数.
     */
    int pageSize;
    /**
     * 模板英文名称.
     */
    String templateNum;
    /**
     * 模板源名称.
     */
    String templateSourceName;
    /**
     * 模板源英文名称.
     */
    String templateSourceNameEn;
    /**
     * 更新时间.
     */
    String updateTime;

    /**
     * 查询类型，空查询全部，catalog查询目录，api查询接口资源，file查询文件资源，db查询数据集资源,
     * app:应用，law：政策解读，anno:公告
     */
    String type;
    /**
     * 排序字段
     */
    String orderField = "name";
    String dir = "desc";
    /**
     * 目录标签：主题/基础/部门
     */
    private String theme;
    private String base;
    private String department;
    /**
     * 部门所对应的类目id
     */
    private String categoryId;
    /**
     * 文件/数据集/接口/无资源的目录
     */
    private String resourceType;

    /**
     * 目录所属部门的级别
     */
    private String catalogDepartmentLevel;
    /**
     * 目录所属部队的父部门，level为1的部门，parentDepartment设置为其本身；
     * level为2的部门，parentDepartment设置为其父部们
     */
    private String parentDepartment;
    /**
     * 起始时间.
     */
    private String startTime;
    /**
     * 终止时间.
     */
    private String endTime;
    /**
     * 可检索的字段列表
     */
    private List<String > fieldList;

    public EsQuery(TemplateEntity entity, String dir, Integer pageSize, String updateTime) {
        this.id = entity.getId();
        this.templateNum = entity.getNumber();
        this.templateSourceNameEn = entity.getTemplateSourceName();
        this.updateTime = updateTime;
        this.dir = dir;
        this.page = 1;
        this.pageSize = pageSize;
        this.keywords = "";
    }
}
