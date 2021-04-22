package com.h3c.bigdata.zhgx.function.system.model;

/**
 * de2.0交互返回值
 * @author w17193
 */
public class DEResponse {
   private String status;
   private Integer errorCode;
   private String errorInfo;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }
}
