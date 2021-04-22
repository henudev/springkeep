package com.h3c.bigdata.zhgx.function.system.model;

import com.h3c.bigdata.zhgx.function.system.entity.AuthUserInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserRoleEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @program: zhgx
 * @description: 用户实体与用户角色实体列表
 * @author: h17338
 * @create: 2018-08-01 09:29
 **/
public class UserWithUserRoleListModel implements Serializable {

    AuthUserInfoEntity authUserInfoEntity;

    List<AuthUserRoleEntity> authUserRoleEntityList;

    public AuthUserInfoEntity getAuthUserInfoEntity() {
        return authUserInfoEntity;
    }

    public void setAuthUserInfoEntity(AuthUserInfoEntity authUserInfoEntity) {
        this.authUserInfoEntity = authUserInfoEntity;
    }

    public List<AuthUserRoleEntity> getAuthUserRoleEntityList() {
        return authUserRoleEntityList;
    }

    public void setAuthUserRoleEntityList(List<AuthUserRoleEntity> authUserRoleEntityList) {
        this.authUserRoleEntityList = authUserRoleEntityList;
    }
}
