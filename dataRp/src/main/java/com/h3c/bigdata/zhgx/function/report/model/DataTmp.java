package com.h3c.bigdata.zhgx.function.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author q16926
 * @Title: DataTmp
 * @ProjectName dcplum
 * @Description: TODO
 * @date 2019/1/1416:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataTmp {
    public int type;
    public String value;
}
