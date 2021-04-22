package com.h3c.bigdata.zhgx.function.report.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;
import org.assertj.core.internal.bytebuddy.implementation.bind.annotation.Default;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "提交数据")
public class QueryData {
    @ApiModelProperty(value = "模板编号")
    public String number;
    @ApiModelProperty(value = "数据")
    public Map<String,Object>data;
    @ApiModelProperty(value = "页数")
    public int page;
    @ApiModelProperty(value = "条数")
    public int size;
}
