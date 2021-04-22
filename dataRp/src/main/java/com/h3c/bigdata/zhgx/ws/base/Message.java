package com.h3c.bigdata.zhgx.ws.base;

/**
 * @author l19624
 * @create 2019/3/11
 */
public interface Message {

//    @JSONField(serializeUsing = MessageCodeSerializer.class)
//    MessageCode getCode();

    Object getContent();

}
