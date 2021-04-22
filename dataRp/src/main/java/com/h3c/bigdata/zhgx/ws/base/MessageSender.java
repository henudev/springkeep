package com.h3c.bigdata.zhgx.ws.base;

/**
 * @author fangzhiheng
 */
@FunctionalInterface
public interface MessageSender {

    void sendMessage(Message message);

}
