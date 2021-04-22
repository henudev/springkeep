package com.h3c.bigdata.zhgx.function.system.model;

import java.io.Serializable;
import java.util.List;

/**
 * 删除消息所用bean
 */
public class DeleteMsgBean implements Serializable {

    /**
     * 消息类型：0告警消息、1通知消息.
     */
    private String messageType;

    /**
     * msg集合
     */
    private List<String> ids;
    /**
     * 消息类型：0站内消息、1目录系统消息.
     */
    private String messageFlag;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getMessageFlag() {
        return messageFlag;
    }

    public void setMessageFlag(String messageFlag) {
        this.messageFlag = messageFlag;
    }
}
