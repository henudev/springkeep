package com.h3c.bigdata.zhgx.function.system.model;

import com.h3c.bigdata.zhgx.function.system.entity.LoginLog;

import java.io.Serializable;

/**
 *登录日志信息封装Bean.
 *
 * @Author J16898
 * @Date 2018/8/9
 * @Version 1.0
 */
public class LoginLogResultBean extends LoginLog implements Serializable {

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
