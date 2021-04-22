package com.h3c.bigdata.zhgx.common.exception;

/**
 * @Description 请求参数错误
 * @Author l17561
 * @Date 2019/4/16 16:15
 * @Version V1.0
 */
public class RequestParamFaultException extends GlobalException {

    public RequestParamFaultException() {
        this("请求参数错误！");
    }

    public RequestParamFaultException(String message) {
        super(message);
    }
}
