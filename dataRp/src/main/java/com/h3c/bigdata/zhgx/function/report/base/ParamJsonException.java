package com.h3c.bigdata.zhgx.function.report.base;

/**
 * 参数异常
 * @author sunleilei
 * @since 2018-05-06
 */
public class ParamJsonException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public ParamJsonException() {}

    public ParamJsonException(String message) {
        super(message);
        this.message = message;
    }


}
