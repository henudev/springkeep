package com.h3c.bigdata.zhgx.function.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "template_collect")
public class TemplateCollectEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @ApiModelProperty("主键")
    @Id
    private Integer id;

    /**
     * 模板编号，可能作为表名
     */
    @ApiModelProperty("模板编号，可能作为表名")
    @Column(name ="number")
    private String number;

    @ApiModelProperty("模板id")
    @Column(name ="template_id")
    private Integer templateId;
    /**
     * 表数量
     */
    @ApiModelProperty("表数量")
    @Column(name = "number_sum")
    private Integer numberSum=0;

    /**
     * 填报次数
     */
    @ApiModelProperty("填报次数")
    @Column(name ="fill_count")
    private Integer fillCount=0;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name ="update_time")
    private Date updateTime;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    @Column(name = " update_user")
    private String  updateUser;
    /**
     * 指标数量
     */
    @ApiModelProperty("指标数量")
    @Column(name = " index_count")
    private Integer  indexCount=0;

}