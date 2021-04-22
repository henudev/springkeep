package com.h3c.bigdata.zhgx.function.report.entity;

import com.baomidou.mybatisplus.annotations.TableField;
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

@Table(name = "table_fill_log")
public class TableFillLogEntity extends BaseEntity {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 模板编号，外键
     */
    @Column(name = "number")
    private String number;

    /**
     * 填报数量
     */
    @TableField("fill_sum")
    private Integer fillSum=0;
    /**
     * 填报次数
     */
    @Column(name = "fill_count")
    private Integer fillCount=0;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 更新人
     */
    @Column(name = "update_user")
    private String updateUser;

    @ApiModelProperty("模板id")
    @Column(name ="template_id")
    private Integer templateId;
}