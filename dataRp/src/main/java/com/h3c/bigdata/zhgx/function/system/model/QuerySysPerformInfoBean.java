package com.h3c.bigdata.zhgx.function.system.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 前端查询系统平台状态 传递参数
 * @author w17193
 */
public class QuerySysPerformInfoBean implements Serializable {
    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
