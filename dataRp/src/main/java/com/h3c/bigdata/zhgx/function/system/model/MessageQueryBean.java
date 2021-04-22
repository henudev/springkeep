package com.h3c.bigdata.zhgx.function.system.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 个人消息查询条件bean.
 *
 * @Author J16898
 * @Date 2018/8/8
 * @Version 1.0
 */
public class MessageQueryBean implements Serializable {

    /**
     * 消息类型：0告警消息、1通知消息.
     */
    private String messageType;

    /**
     * 消息状态:0未读、1已读.
     */
    private String messageStatus;

    /**
     * 消息详情.
     */
    private String messageDetail;

    /**
     * 消息推送开始时间.
     */
    private Date createStartTime;
    /**
     * 消息推送结束时间.
     */
    private Date createEndTime;
    /**
     * 消息接受者.
     */
    private String receiveUser;

    /**
     * 未读数据申请消息统计值3
     */
    private int applyCount ;
    /**
     * 未读接口订阅消息统计值4
     */
    private int subscribeCount ;
    /**
     * 未读资源类目审核消息统计值7
     */
    private int categoryCount ;
    /**
     * 未读资源目录审核消息统计值0
     */
    private int catalogCount ;
    /**
     * 未读资源目录取消发布消息统计值6
     */
    private int cancelCatalogCount ;
    /**
     * 未读资源目录回收消息统计值2
     */
    private int recoverCatalogCount ;
    /**
     * 消息类型：0站内消息、1目录系统消息.
     */
    private String messageFlag;


    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }


    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getMessageDetail() {
        return messageDetail;
    }

    public void setMessageDetail(String messageDetail) {
        this.messageDetail = messageDetail;
    }

    public Date getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }

    public int getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(int applyCount) {
        this.applyCount = applyCount;
    }

    public int getSubscribeCount() {
        return subscribeCount;
    }

    public void setSubscribeCount(int subscribeCount) {
        this.subscribeCount = subscribeCount;
    }

    public int getCategoryCount() {
        return categoryCount;
    }

    public void setCategoryCount(int categoryCount) {
        this.categoryCount = categoryCount;
    }

    public int getCatalogCount() {
        return catalogCount;
    }

    public void setCatalogCount(int catalogCount) {
        this.catalogCount = catalogCount;
    }

    public int getCancelCatalogCount() {
        return cancelCatalogCount;
    }

    public void setCancelCatalogCount(int cancelCatalogCount) {
        this.cancelCatalogCount = cancelCatalogCount;
    }

    public int getRecoverCatalogCount() {
        return recoverCatalogCount;
    }

    public void setRecoverCatalogCount(int recoverCatalogCount) {
        this.recoverCatalogCount = recoverCatalogCount;
    }

    public String getMessageFlag() {
        return messageFlag;
    }

    public void setMessageFlag(String messageFlag) {
        this.messageFlag = messageFlag;
    }
}
