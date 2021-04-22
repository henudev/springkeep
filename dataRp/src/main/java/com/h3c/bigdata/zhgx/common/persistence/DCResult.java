package com.h3c.bigdata.zhgx.common.persistence;

import java.io.Serializable;

/**
 * @program: zhgx
 * @description: 研发DC系统返回信息实体
 * @author: h17338
 * @create: 2018-08-07 15:03
 **/
public class DCResult implements Serializable {

    /**
     * 响应码
     * 如0表示成功，其他失败
     */
    private int errorCode;
    /**
     * 结果描述（成功时则为空）
     */
    private String errorInfo;
    /**
     * success:处理成功；error:处理失败
     */
    private String status;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
