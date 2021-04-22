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
@TableName("data_storage")
public class DataStorageEntity{
    private static final long serialVersionUID = 1L;

    /**
     *模板编号，可能作为表名
     */
    @ApiModelProperty("内存id")
    @TableField(value = "storage_id")
    private String storageId;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time")
    private Date createTime;

}