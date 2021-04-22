package com.h3c.bigdata.zhgx.function.system.model;

import com.h3c.bigdata.zhgx.function.system.entity.MessageInfo;

import java.io.Serializable;

/**
 * 消息结果返回封装体.
 * @Author J16898
 * @Date 2018/8/8
 * @Version 1.0
 */
public class MessageResultBean extends MessageInfo implements Serializable {

    /**
     * 消息类型code.
     */
    private String messageTypeCode;

    public String getMessageStatusCode() {
        return messageStatusCode;
    }

    public void setMessageStatusCode(String messageStatusCode) {
        this.messageStatusCode = messageStatusCode;
    }

    /**

     * 消息状态code.
     */
    private String messageStatusCode;

    public String getMessageTypeCode() {
        return messageTypeCode;
    }

    public void setMessageTypeCode(String messageTypeCode) {
        this.messageTypeCode = messageTypeCode;
    }
}
