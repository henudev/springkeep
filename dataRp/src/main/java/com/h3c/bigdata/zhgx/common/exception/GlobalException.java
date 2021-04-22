package com.h3c.bigdata.zhgx.common.exception;

/**
 * @Description 系统全局异常，用以抛出固定异常
 * @Author l17561
 * @Date 2019/4/11 15:04
 * @Version V1.0
 */
public class GlobalException extends RuntimeException {

    public GlobalException(String message) {
        super(message);
    }
}
