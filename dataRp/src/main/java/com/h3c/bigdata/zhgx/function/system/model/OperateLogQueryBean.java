package com.h3c.bigdata.zhgx.function.system.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志查询bean.
 *
 * @Author J16898
 * @Date 2018/8/9
 * @Version 1.0
 */
public class OperateLogQueryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 操作状态：0正常、1异常 */
    private String status;

    /** 模块名称 */
    private String moduleName;

    /** 功能请求 */
    private String action;

    /** 访问系统开始时间 */
    private Date operateStartTime;

    /** 访问系统结束时间 */
    private Date operateEndTime;

    /** 登录用户Id */
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getOperateStartTime() {
        return operateStartTime;
    }

    public void setOperateStartTime(Date operateStartTime) {
        this.operateStartTime = operateStartTime;
    }

    public Date getOperateEndTime() {
        return operateEndTime;
    }

    public void setOperateEndTime(Date operateEndTime) {
        this.operateEndTime = operateEndTime;
    }
}
