package com.h3c.bigdata.zhgx.function.report.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TemplateItemExcelModel implements Serializable {

    //闫参数
    private Boolean showMinus;

    //字段
    private String itemName;

    //字段中文名
    private String itemDesc;

    //字段类型（1，字符类型；2，整数；3，小数；4，枚举;5 时间）
    private int type;

    //是否可为空值（0：false, 1: true）
    private Byte isNull;

    //枚举类型，用逗号间隔
    private String enums;

    //是否为联合唯一（0：false, 1: true）
    private Byte isUnionOnly;

    //是否可搜索（0：false, 1: true）
    private Byte isSearch=0;

    //增删标识（0：删, 1: 增）
    private String sign;

    //更新人
    private String update_user;

    //更新时间
    private String update_time;

    private String typeValue;

    private String isNullValue;

    //是否排序（0：是, 1: 否）
    private Byte isSort;

    private String isSortValue;


}