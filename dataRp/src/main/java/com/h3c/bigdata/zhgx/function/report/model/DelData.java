package com.h3c.bigdata.zhgx.function.report.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "提交数据")
public class DelData {
    @ApiModelProperty(value = "模板源名称")
    public String templateSourceNameEn;
    @ApiModelProperty(value = "模板编号")
    public String number;
    @ApiModelProperty(value = "数据")
    public List<DataModel> ids;
    @ApiModelProperty(value = "模板id")
    public Integer templateId;
}
