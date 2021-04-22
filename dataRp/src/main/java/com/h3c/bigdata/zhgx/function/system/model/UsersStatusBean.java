package com.h3c.bigdata.zhgx.function.system.model;

import com.h3c.bigdata.zhgx.function.system.entity.AuthUserInfoEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @program: zhgx
 * @description: 批量停用启用用户账号
 * @author: h17338
 * @create: 2018-08-03 14:59
 **/
public class UsersStatusBean implements Serializable {

    private String status;

    List<AuthUserInfoEntity> users;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AuthUserInfoEntity> getUsers() {
        return users;
    }

    public void setUsers(List<AuthUserInfoEntity> users) {
        this.users = users;
    }
}
