package com.h3c.bigdata.zhgx.function.system.model;

import java.io.Serializable;

public class LoginMenuBean implements Serializable {

    /**
     * 权限code
     */
    private String menuCode;

    /**
     * 功能类型，如：ADD,DELETE,QUERY,UPDATE等
     */
    private String functionType;

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public String getFunctionType() {
        return functionType;
    }

    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }
}
