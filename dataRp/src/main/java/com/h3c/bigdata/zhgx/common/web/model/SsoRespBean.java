package com.h3c.bigdata.zhgx.common.web.model;

import com.h3c.bigdata.zhgx.common.persistence.PageBaseModel;

import java.io.Serializable;

public class SsoRespBean implements Serializable {
    private String loginUrl;

    private String logoutUrl;

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }
}
