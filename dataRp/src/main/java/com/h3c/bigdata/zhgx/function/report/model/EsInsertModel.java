package com.h3c.bigdata.zhgx.function.report.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author q16926
 * @Title: EsInsertModel
 * @ProjectName dcplum
 * @Description: TODO
 * @date 2019/1/1110:27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="ES插入数据")
public class EsInsertModel {
    
    @ApiModelProperty("用户部门id")
    String departmentId;

    @ApiModelProperty("模板中文名")
    String templateCnName;

    @ApiModelProperty("模板源名称")
    String templateSourceName;
    @ApiModelProperty("模板编号")
    String templateNumber;
    
    @ApiModelProperty("数据列表")
    List<Map<String, Object>> dataMapList;
    
    @ApiModelProperty("联合唯一")
    List<Map<String, String>> unikeys;
}
