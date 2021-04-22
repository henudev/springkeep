package com.h3c.bigdata.zhgx.function.report.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "提交数据")
public class UpdateData {
    @ApiModelProperty(value = "模板编号")
    public String number;
    @ApiModelProperty(value = "数据")
    public Map<String,Object> data;
}
