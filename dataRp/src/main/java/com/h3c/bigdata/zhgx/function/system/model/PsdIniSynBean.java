package com.h3c.bigdata.zhgx.function.system.model;

import java.io.Serializable;

/**
 * @program: zhgx
 * @description: 同步重置密码给研发系统实体
 * @author: h17338
 * @create: 2018-08-08 10:59
 **/
public class PsdIniSynBean implements Serializable {

    private String username;

    private String adminUserName;

    private String newPassword;

    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
