package com.h3c.bigdata.zhgx.function.report.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Description: 模板所有相关信息实体类
 * @Author: w15112
 * @CreateDate: 2019/06/03 9:40
 * @UpdateUser: w15112
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateModel {

    /**
     * id
     */
    private Integer id;

    /**
     * 模板编号，可能作为表名
     */

    private String number;

    /**
     * 标签
     */
    private String tag;
    /**
     * 模板名称
     */
    private String name;

    /**
     * 填写说明
     */
    private String description;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 表数量
     */
    private int numberSum;

    /**
     * 填报次数
     */
    private int fillCount;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新人
     */
    private String updateUser;
    /**
     * 指标数量
     */
    private int indexCount;


    private String departmentId;
    /**
     * 填报周期
     */
    private String fillInPeriod;

    private String type ="";

    private List<String> depList;

    /**
     * 数据源名称
     */
    private String templateSourceName;
    /**
     * 数据源英文名称
     */
    private String templateSourceNameEn;

    /**
     * 模板数据填报状态：0：无数据量；1：有数据量；2：所有模板
     */
    private String templateType;
    /**
     * 模板是否使用 : 0：正在使用，1代表未使用
     */
    private String isUsed;

    /**
     * 模板是否及时更新 : 0：未及时更新，1已更新
     */
    private Integer isCompleted;
}