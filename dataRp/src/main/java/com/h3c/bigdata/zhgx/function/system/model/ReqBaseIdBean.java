package com.h3c.bigdata.zhgx.function.system.model;

import java.io.Serializable;

/**
 *请求基础类 只包含id属性
 */
public class ReqBaseIdBean implements Serializable {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
