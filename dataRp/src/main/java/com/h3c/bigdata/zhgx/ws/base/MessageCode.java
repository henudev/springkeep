package com.h3c.bigdata.zhgx.ws.base;

/**
 * @author fangzhiheng
 */
public interface MessageCode extends RequireClose {

    static String ofPattern(String pattern, Object... strings) {
        return String.format(pattern, strings);
    }

    String getCode();
}
