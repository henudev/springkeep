package com.h3c.bigdata.zhgx.function.report.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author q16926
 * @Title: EsUpdate
 * @ProjectName dcplum
 * @Description: TODO
 * @date 2019/1/2210:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="待更新数据")
public class EsUpdate {
    String templateNum;
    Map<String, String> oldData;
    Map<String, String> newData;
}
