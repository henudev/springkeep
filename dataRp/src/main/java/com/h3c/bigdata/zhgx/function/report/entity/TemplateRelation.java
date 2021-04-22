package com.h3c.bigdata.zhgx.function.report.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("template_relation")
public class TemplateRelation extends Model<TemplateRelation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty("父模板id")
    @TableField(value = "parent_id")
    private String parentId;

    @ApiModelProperty("子模板id")
    @TableField(value = "children_id")
    private String childrenId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
