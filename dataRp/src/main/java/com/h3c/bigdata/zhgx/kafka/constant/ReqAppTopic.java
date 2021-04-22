package com.h3c.bigdata.zhgx.kafka.constant;

import java.io.Serializable;
import java.util.List;

/**
 * @Title: ReqAppTopic
 * @ProjectName platform
 * @Description:
 * @date 2019/2/28 10:30
 */
public class ReqAppTopic implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * userid
     */
    private Long id;

    /**
     * 应用id列表
     */
    private List<Long> appIds;

    /**
     * 操作类型  create update delete
     */
    private String syncType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getAppIds() {
        return appIds;
    }

    public void setAppIds(List<Long> appIds) {
        this.appIds = appIds;
    }

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }
}
