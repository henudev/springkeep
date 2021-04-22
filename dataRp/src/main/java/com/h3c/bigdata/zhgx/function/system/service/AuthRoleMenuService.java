package com.h3c.bigdata.zhgx.function.system.service;

import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.function.system.entity.AuthRoleMenuEntity;
import com.h3c.bigdata.zhgx.function.system.model.RoleWithRoleMenuBean;
import com.h3c.bigdata.zhgx.function.system.model.RoleWithRoleMenuListModel;

import java.util.List;

/**
 * @program: zhgx
 * @description: 角色权限关联关系
 * @author: h17338
 * @create: 2018-07-31 15:50
 **/
public interface AuthRoleMenuService {

    /**
     * 新增角色权限信息.
     * @param authRoleMenuEntityList
     * @return .
     */
    ApiResult<?> addRoleMenuInfo(List<AuthRoleMenuEntity> authRoleMenuEntityList);

    /**
     * 变更角色权限信息.
     * @param authRoleMenuEntityList
     * @return .
     */
    ApiResult<?> updateRoleMenuInfo(List<AuthRoleMenuEntity> authRoleMenuEntityList);

    /**
     * 查询角色权限信息.
     * @param authRoleMenuEntity
     * @return .
     */
    ApiResult<?> queryRoleMenuInfo(int page, int pageSize, String field, String dir, AuthRoleMenuEntity authRoleMenuEntity);

   /**
   *@Description: 获取角色类型列表方法.
   *@Author:      w15112
   *@CreateDate:  2018/11/27 14:06
   *@param
   *@return
   *@exception
   */
    ApiResult<?> getRoleTypeList(String userId);
    /**
     * 新增角色权限信息（角色、角色权限两张表）.
     * @param roleWithRoleMenuListModel
     * @return .
     */
    ApiResult<?> addRoleWithMenuList(RoleWithRoleMenuListModel roleWithRoleMenuListModel,String userId);

    /**
     * 变更角色权限信息（角色、角色权限两张表）.
     * @param roleWithRoleMenuListModel
     * @return .
     */
    ApiResult<?> updateRoleWithMenuList(RoleWithRoleMenuListModel roleWithRoleMenuListModel);

    /**
     * 查询角色返回角色已勾选权限列表
     * @param roleWithRoleMenuBean
     * @return .
     */
    ApiResult<?> queryRoleWithMenuList(int page, int pageSize, String field, String dir,
                                       RoleWithRoleMenuBean roleWithRoleMenuBean,String userId);

    /**
     * @Description: 为流程节点查询角色列表
     * @Param:
     * @UpdateContent: 流程节点中角色的待选列表只由部门决定
     * @Updater: l17503
     * @UpdateTime: 2020/1/17 11:21
     */
    ApiResult<?> queryRoleForApprove(RoleWithRoleMenuBean roleWithRoleMenuBean);

}
