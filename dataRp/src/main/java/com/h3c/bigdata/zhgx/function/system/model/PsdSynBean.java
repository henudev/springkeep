package com.h3c.bigdata.zhgx.function.system.model;

import java.io.Serializable;

/**
 * @program: zhgx
 * @description: 同步密码修改信息给研发数据库
 * @author: h17338
 * @create: 2018-08-07 15:30
 **/
public class PsdSynBean implements Serializable {

    /**
     *用户名
     * 长度（3-20个字符）；当更新时用户名是不可以更改的
     */
    private String username;
    /**
     *用户老的密码
     * 若是要加密，须前端实现AESCBC方式的加密）；密码长度（8-64个字符），至少有四种字符类型；
     */
    private String oldPassword;
    /**
     *新密码
     * 若是要加密，须前端实现AESCBC方式的加密）；密码长度（8-64个字符），至少有四种字符类型；
     */
    private String newPassword;
    /**
     *确认密码
     * 和password相同
     */
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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
