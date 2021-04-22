package com.h3c.bigdata.zhgx.function.system.service;

import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserInfoEntity;
import com.h3c.bigdata.zhgx.function.system.model.UserPsdSynBean;
import com.h3c.bigdata.zhgx.function.system.model.UserPsdSynDSGBean;
import com.h3c.bigdata.zhgx.function.system.model.UserWithUserRoleListModel;
import com.h3c.bigdata.zhgx.function.system.model.UsersStatusBean;

public interface AuthUserService {

    /**
     * 新增用户信息.
     * @param authUserInfoEntity
     * @return .
     */
    ApiResult<?> addUserInfo(AuthUserInfoEntity authUserInfoEntity);

    /**
     * 更新用户信息.
     * @param authUserInfoEntity
     * @return .
     */
    ApiResult<?> updateUserInfo(AuthUserInfoEntity authUserInfoEntity);

    /**
     * 查询用户信息.
     * @param authUserInfoEntity
     * @return .
     */
    ApiResult<?> queryUserInfo(int page, int pageSize, String field, String dir, AuthUserInfoEntity authUserInfoEntity);

    ApiResult<?> deleteUser(UsersStatusBean usersStatusBean);

    /**
     * 删除及禁用用户的校验，如果用户所属角色在流程中被使用，且角色下仅有当前用户，则不允许删除
     * @param userId
     * @return
     */
    String deleteUserCheck(String userId);

    ApiResult<?> updateUsersStatus(UsersStatusBean usersStatusBean);

    ApiResult<?> queryUsersByRoleId(String roleId);

    /**
     * 校验登录用户基本信息
     * @param userId
     * @return .
     */
    ApiResult<?> queryLoginUser(String userId);


    /**
     * 查询某用户所在部门的所有人员
     * @param userId
     * @return .
     */
    ApiResult<?> getPersonsByUserId(String userId);

    /**
     * 查询某用户所在部门的所有人员
     * @param deptId
     * @return .
     */
    ApiResult<?> getPersonsByDeptId(String deptId);

    /**
     * 更新用户首次登陆标志位
     *
     * @param        userId
     * @return
    */
    ApiResult<?> updateUserLoginFlag (String userId);
}
