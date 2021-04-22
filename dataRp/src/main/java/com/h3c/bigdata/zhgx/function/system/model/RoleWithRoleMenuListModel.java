package com.h3c.bigdata.zhgx.function.system.model;

import com.h3c.bigdata.zhgx.function.system.entity.AuthRoleInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthRoleMenuEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @program: zhgx
 * @description: 角色实体与权限实体列表
 * @author: h17338
 * @create: 2018-08-01 09:30
 **/
public class RoleWithRoleMenuListModel implements Serializable {

    AuthRoleInfoEntity authRoleInfoEntity;

    List<AuthRoleMenuEntity> authRoleMenuEntityList;

    public AuthRoleInfoEntity getAuthRoleInfoEntity() {
        return authRoleInfoEntity;
    }

    public void setAuthRoleInfoEntity(AuthRoleInfoEntity authRoleInfoEntity) {
        this.authRoleInfoEntity = authRoleInfoEntity;
    }

    public List<AuthRoleMenuEntity> getAuthRoleMenuEntityList() {
        return authRoleMenuEntityList;
    }

    public void setAuthRoleMenuEntityList(List<AuthRoleMenuEntity> authRoleMenuEntityList) {
        this.authRoleMenuEntityList = authRoleMenuEntityList;
    }
}
