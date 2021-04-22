package com.h3c.bigdata.zhgx.function.system.model;

import java.io.Serializable;

/**
 * @Author J16898
 * @Date 2018/8/27
 * @Version 1.0
 */
public class MenuButtonBean implements Serializable {

    private String buttonName;

    private String url;

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
