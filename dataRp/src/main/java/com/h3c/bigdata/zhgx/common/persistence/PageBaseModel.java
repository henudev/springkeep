package com.h3c.bigdata.zhgx.common.persistence;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class PageBaseModel implements Serializable {

    //pageNum 当前记录起始索引
    private Integer pageNum;

    //pageSize 每页显示记录数
    private Integer pageSize;

    //orderBy 排序列
    private String orderBy;

    //排序的方向 "desc" 或者 "asc"
    private String dir;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}

