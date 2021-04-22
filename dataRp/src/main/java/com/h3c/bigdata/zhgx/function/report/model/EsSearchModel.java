package com.h3c.bigdata.zhgx.function.report.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @Description: es检索资源目录信息实体类
 * @Author: w15112
 * @Date: 2019/8/28
 */
@Data
@NoArgsConstructor
public class EsSearchModel {
    private String id;
    /**
     * 目录/资源名称
     */
    private String name;
    private String department = "";
    private String remark = "";
    /**
     * catalog:目录，api:接口资源，file:文件资源，DB:数据集资源,app:应用，law：政策解读，anno:公告
     */
    private String type;

    private String updateTime;

    /**
     * 应用类别
     */
    private String applicationType = "";
    /**
     * 文件名称
     */
    private String fileName = "";
    /**
     * 目录/应用访问量
     */
    private Integer visitCount = 0;
    /**
     * 应用图片路径
     */
    private String photo;
    /**
     * 应用评分
     */
    private BigDecimal score = BigDecimal.valueOf(0);




}
