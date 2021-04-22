package com.h3c.bigdata.zhgx.common.utils;

import java.util.List;

/**
 * @author Mingchao.Ji
 * @version 1.0
 * @date 2018/4/23
 */
public class PageResult<T> {

    /**
     * 数据的总个数.
     */
    private long total;

    /**
     * 当前页的数据结果集.
     */
    private List<T> data;

    /**
     * @return the total.
     */
    public long getTotal() {
        return total;
    }

    /**
     * @param total the total to set.
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * @return the data.
     */
    public List<T> getData() {
        return data;
    }

    /**
     * @param data the data to set.
     */
    public void setData(List<T> data) {
        this.data = data;
    }
}
