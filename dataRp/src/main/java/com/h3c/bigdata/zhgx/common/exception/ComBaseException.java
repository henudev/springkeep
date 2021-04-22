package com.h3c.bigdata.zhgx.common.exception;

import java.text.MessageFormat;

/**
 * 异常基类，用来封装异常信息及初始化异常信息
 * @author w17193
 */
public class ComBaseException extends Exception{

    private String errorCode;

    private String errorMsg;

    private String msgDetail;

    /**
     * 支持异常信息占位符
     * @param comErrorCode
     * @param fileStr
     */
    public ComBaseException(ComErrorCode comErrorCode, Object...fileStr){
    this.errorCode = comErrorCode.getErrorCode();
    this.errorMsg = MessageFormat.format(comErrorCode.getErrorMsg(),fileStr);
    this.msgDetail = comErrorCode.getMsgDetail();
}

    @Override
    public String toString() {
        return "错误码:[ " + this.getErrorCode() + " ] ,错误信息:[ " + this.getErrorMsg() + " ] ," + "解决办法:[ "
                + this.getMsgDetail() + " ]";
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getMsgDetail() {
        return msgDetail;
    }

    public void setMsgDetail(String msgDetail) {
        this.msgDetail = msgDetail;
    }

    /**
     * 不加序列化id MessageFormat.format异常
     */
    private static final long serialVersionUID = 1L;
}
