package com.h3c.bigdata.zhgx.function.system.service;

import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.function.system.entity.AuthRoleInfoEntity;

import java.util.List;

public interface AuthRoleService {

    /**
     * 添加角色信息.
     * @param authRoleInfoEntity
     * @return .
     */
    ApiResult<?> addRoleInfo(AuthRoleInfoEntity authRoleInfoEntity,String userId);

    /**
     * 查询角色信息.
     * @param authRoleInfoEntity
     * @return .
     */
    ApiResult<?> updateRoleInfo(AuthRoleInfoEntity authRoleInfoEntity);

    /**
     * 查询角色信息.
     * @param authRoleInfoEntity
     * @return .
     */
    ApiResult<?> queryRoleInfo(int page, int pageSize, String field, String dir, AuthRoleInfoEntity authRoleInfoEntity);

    /**
     * 删除角色信息.
     * @param authRoleInfoEntity
     * @return .
     */
    ApiResult<?> deleteRoleInfo(AuthRoleInfoEntity authRoleInfoEntity);

    ApiResult<?> deleteRoles(List<AuthRoleInfoEntity> roles);
}
