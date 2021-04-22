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

import java.io.Serializable;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("template")
public class TemplateEntity extends Model<TemplateEntity> {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @ApiModelProperty("主键")
    @TableId(value = "id")
    private Integer id;

    /**
     *模板编号，可能作为表名
     */
    @ApiModelProperty("模板编号，可能作为表名")
    @TableField(value = "number")
    private String number;

    @ApiModelProperty("标签")
    @TableField(value = "tag")
    private String tag;
    /**
     *模板名称
     */
    @ApiModelProperty("模板名称")
    @TableField(value = "name")
    private String name;

    /**
     *填写说明
     */
    @ApiModelProperty("填写说明")
    @TableField(value = "description")
    private String description;

    /**
     *更新人
     */
    @ApiModelProperty("更新人")
    @TableField(value = "update_user")
    private String updateUser;
    /**
     *更新时间
     */
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time")
    private Date updateTime;
    /**
     *填报周期
     */
    @ApiModelProperty("填报周期")
    @TableField(value = "fill_in_period")
    private String fillInPeriod;
    /**
     *模板源id
     */
    @ApiModelProperty("模板源名称")
    @TableField(value = "template_source_name")
    private String templateSourceName;
    /**
     *模板是否使用
     */
    @ApiModelProperty("模板源名称")
    @TableField(value = "is_used")
    private String isUsed;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}