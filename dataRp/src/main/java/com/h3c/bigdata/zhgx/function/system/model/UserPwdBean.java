package com.h3c.bigdata.zhgx.function.system.model;

import java.io.Serializable;

/**
 * @program: zhgx
 * @description: 用于用户个人修改密码
 * @author: h17338
 * @create: 2018-08-02 10:45
 **/
public class UserPwdBean implements Serializable {

    /**
     * 用户账号
     */
    private String userId;

    /**
     * 当前密码
     */
    private String curPassword;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 确认新密码
     */
    private String newPasswordAgain;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCurPassword() {
        return curPassword;
    }

    public void setCurPassword(String curPassword) {
        this.curPassword = curPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordAgain() {
        return newPasswordAgain;
    }

    public void setNewPasswordAgain(String newPasswordAgain) {
        this.newPasswordAgain = newPasswordAgain;
    }
}
