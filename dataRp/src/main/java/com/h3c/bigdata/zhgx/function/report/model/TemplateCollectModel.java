package com.h3c.bigdata.zhgx.function.report.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author w15112
 * @title: TemplateCollectMoedl
 * @projectName new_zhgx
 * @description: 模板统计汇总类
 * @date 2019/6/311:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "模板统计汇总类")
public class TemplateCollectModel {

    /**
     * 模板总量
     */
    private int templateTotal;
    /**
     * 数据总量
     */
    private int dataSum;
    /**
     * 模板完成率
     */
    private BigDecimal completionRate=new BigDecimal("0");
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 部门id
     */
    private String departmentId;
    /**
     * 更新人
     */
    private String  updateUser;
    /**
     * 指标数量
     */
    private int  indexCount;
    /**
     * 填报次数
     */
    private int fillCount;
    /**
     * 已完成模板数
     */
    private int completeTemplateTotal;
    /**
     * 未完成模板数
     */
    private int incompleteTemplateTotal;
    /**
     * 模板未完成率
     */
    private BigDecimal incompletionRate=new BigDecimal("100.00");
    /**
     * 判断该部门是否存在子部门，0表示无子部门；1表示有子部门
     */
    private int flag =0;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd ")
    private Date updateTime;
    /**
     * 模板名称
     */
    private String name;
}
