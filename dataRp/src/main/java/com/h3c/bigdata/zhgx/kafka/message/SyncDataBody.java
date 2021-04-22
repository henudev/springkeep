package com.h3c.bigdata.zhgx.kafka.message;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Title: SyncDataBody
 * @ProjectName platform
 * @Description: 第三方同步消息体
 * @date 2019/3/14 10:56
 */
public class SyncDataBody implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 同步类型
     */
    private String syncType;

    /**
     * 同步消息体
     */
    private List<Map<String,Object>> data;


    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
