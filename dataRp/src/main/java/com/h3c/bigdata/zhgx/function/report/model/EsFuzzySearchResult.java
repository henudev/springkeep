package com.h3c.bigdata.zhgx.function.report.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author q16926
 * @Title: EsFuzzySearchResult
 * @ProjectName dcplum
 * @Description: TODO
 * @date 2019/1/2114:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value="数据检索模板列表")
public class EsFuzzySearchResult {
    private long total;
    private List<Map<String, Object>> templateList;
}
