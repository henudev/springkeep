package com.h3c.bigdata.zhgx.function.report.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("template_source")
public class TemplateSourceEntity {
    @ApiModelProperty("主键")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty("数据库中文名")
    @TableField(value = "source_name_cn")
    private String sourceNameCN;

    @ApiModelProperty("数据库英文名")
    @TableField(value = "source_name_en")
    private String sourceNameEN;

    @ApiModelProperty("更新用户")
    @TableField(value = "update_user")
    private String updateUser;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_time")
    private Date updateTime;


}
