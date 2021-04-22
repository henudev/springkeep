package com.h3c.bigdata.zhgx.function.system.service;

import com.h3c.bigdata.zhgx.common.exception.ComBaseException;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserRoleEntity;
import com.h3c.bigdata.zhgx.function.system.model.UserWithRoleModel;
import com.h3c.bigdata.zhgx.function.system.model.UserWithUserRoleListModel;

import java.util.List;

/**
 * @program: zhgx
 * @description: 用户角色关联关系
 * @author: h17338
 * @create: 2018-07-31 15:37
 **/
public interface AuthUserRoleService {
    /**
     * 新增用户角色信息.
     * @param authUserRoleEntityList
     * @return .
     */
    ApiResult<?> addUserRoleInfo(List<AuthUserRoleEntity> authUserRoleEntityList);

    /**
     * 变更用户角色信息.
     * @param authUserRoleEntityList
     * @return .
     */
    ApiResult<?> updateUserRoleInfo(List<AuthUserRoleEntity> authUserRoleEntityList);

    /**
     * 查询用户角色信息.
     * @param authUserRoleEntity
     * @return .
     */
    ApiResult<?> queryUserRoleInfo(int page, int pageSize, String field, String dir, AuthUserRoleEntity authUserRoleEntity);

    /**
     * 新增用户时勾选角色列表.
     * @param userWithUserRoleListModel
     * @return .
     */
    ApiResult<?> addUseWithRoleList(UserWithUserRoleListModel userWithUserRoleListModel,String token)throws ComBaseException;

    /**
     * 编辑用户时勾选角色列表.
     * @param userWithUserRoleListModel
     * @return .
     */
    ApiResult<?> updateUseWithRoleList(UserWithUserRoleListModel userWithUserRoleListModel);

    /**
     * 查询用户时返回角色列表，用户列表.
     * @param userWithRoleModel
     * @return .
     */
    ApiResult<?> queryUseWithRoleList(int page, int pageSize, String field, String dir, UserWithRoleModel userWithRoleModel,String userId);

    /**
     * 查询用户时返回角色列表，个人中心.
     * @param userWithRoleModel
     * @return .
     */
    ApiResult<?> queryUseWithRoleListForPerson(int page, int pageSize, String field, String dir, UserWithRoleModel userWithRoleModel);

    /**
     * 用户调用方名称校验.
     * @param callerOrPhone
     * @return.
     */
    ApiResult<?> checkCallerNameOrPhoneIsUsed(String callerOrPhone,String type);


}
