package com.h3c.bigdata.zhgx.ws.base;

/**
 * @author fangzhiheng
 */
public interface MessageCreater<T extends Message> {

    T create(Object param);

}
