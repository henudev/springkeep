package com.h3c.bigdata.zhgx.function.report.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("table_description")
public class TableDescriptionEntity extends Model<TableDescriptionEntity> {
    private static final long serialVersionUID = 1L;
    /**
     *id
     */
    @TableId("id")
    private Integer id;

    /**
     *模板编号，外键
     */
    @TableField("number")
    private String number;

    /**
     *字段英文，可能作为字段名
     */
    @TableField("item")
    private String item;

    /**
     *字段名称
     */
    @TableField("name")
    private String name;

    /**
     *字段类型（0，字符类型；1，整数；2，小数；3，日期;4 ，枚举；5，文本）
     */
    @TableField("type")
    private Integer type;

    /**
     *是否可为空值
     */
    @TableField("is_null")
    private Byte isNull;

    /**
     *枚举类型，用逗号间隔
     */
    @TableField("enums")
    private String enums;

    @ApiModelProperty(value = "是否为联合唯一（0：false, 1: true）")
    @TableField("is_union_only")
    private Byte isUnionOnly;
    
    @ApiModelProperty(value = "是否可搜索（0：false, 1: true）")
    @TableField("is_search")
    private Byte isSearch;

    @ApiModelProperty("模板id")
    @Column(name ="template_id")
    private Integer templateId;

    @ApiModelProperty(value = "是否排序（1：不排序，0：排序）")
    @TableField("is_sort")
    private Byte isSort;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}