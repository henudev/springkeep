package com.h3c.bigdata.zhgx.function.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


/**
 * @author q16926
 * @Title: ReturnData
 * @ProjectName dcplum
 * @Description: TODO
 * @date 2019/1/1715:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnData {
    long total;
    private List dataList;
    private List itemList;
}
