package com.h3c.bigdata.zhgx.function.system.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录日志查询Bean.
 *
 * @Author J16898
 * @Date 2018/8/9
 * @Version 1.0
 */
public class LogInLogQueryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 登录状态：0成功、1失败 */
    private String status;

    /** 登录账号
     */
    private String loginUserName;

    /** 登录用户Id */
    private String loginUserId;

    public String getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    /** 访问系统开始时间 */
    private Date loginStartTime;

    /** 访问系统结束时间 */
    private Date loginEndTime;

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }

    public String getStatus() {
        return status;

    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLoginStartTime() {
        return loginStartTime;
    }

    public void setLoginStartTime(Date loginStartTime) {
        this.loginStartTime = loginStartTime;
    }

    public Date getLoginEndTime() {
        return loginEndTime;
    }

    public void setLoginEndTime(Date loginEndTime) {
        this.loginEndTime = loginEndTime;
    }
}
