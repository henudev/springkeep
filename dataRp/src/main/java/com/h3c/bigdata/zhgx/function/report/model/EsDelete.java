package com.h3c.bigdata.zhgx.function.report.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author q16926
 * @Title: EsDelete
 * @ProjectName dcplum
 * @Description: TODO
 * @date 2019/1/2119:41
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="待删除数据")
public class EsDelete {
    /**
     * 待删除数据.
     */
    Map<String, String> esData;
    /**
     * 模板id.
     */
    String templateNum;
}
