package com.h3c.bigdata.zhgx.function.report.entity;

import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


/**
 * 模板跟部门的关系
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_temp_dpt")
@ApiModel("模板部门关系表")
public class TempToDpt extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    @Id
    private String id;

    /**
     * 模板id
     */
    @ApiModelProperty("模板主键")
    @Column(name ="template_id")
    private String templateId;

    /**
     * 部门id
     */
    @ApiModelProperty("部门主键")
    @Column(name ="department_id")
    private String departmentId;
}
