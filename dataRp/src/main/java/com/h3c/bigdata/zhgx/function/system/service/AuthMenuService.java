package com.h3c.bigdata.zhgx.function.system.service;

import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.function.system.entity.AuthMenuInfoEntity;
import com.h3c.bigdata.zhgx.function.system.model.AuthMenuBean;
import com.h3c.bigdata.zhgx.function.system.model.LoginMenuBean;

import java.util.List;

/**
 * @program: zhgx
 * @description: 权限维护
 * @author: h17338
 * @create: 2018-07-31 09:58
 **/
public interface AuthMenuService {

    /**
     * 新增权限信息.
     * @param authMenuBean  .
     * @return .
     */
    ApiResult<?> addMenuInfo(AuthMenuBean authMenuBean, String userId);

        /**
         * 更新部权限信息.
         * @param authMenuBean 查询实体 .
         * @return 部门信息结果信息.
         */
        ApiResult<?> updateMenuInfo(AuthMenuBean authMenuBean, String userId);

        /**
         * 查询权限信息.
         * @param authMenuInfoEntity
         * @return .
         */
        ApiResult<?> queryMenuInfo(int page, int pageSize, String field, String dir, AuthMenuInfoEntity authMenuInfoEntity);

    /**
     * 准确查询
     * @param page
     * @param pageSize
     * @param field
     * @param dir
     * @param authMenuInfoEntity
     * @return
     */
    ApiResult<?> queryMenuInfoPinpoint(int page, int pageSize, String field, String dir, AuthMenuInfoEntity authMenuInfoEntity);

    /**
     * 删除部权限信息.
     * @param authMenuInfoEntity .
     * @return
     */
    ApiResult<?> deleteMenuInfo(AuthMenuInfoEntity authMenuInfoEntity);

    ApiResult<?> deleteMenus(List<AuthMenuInfoEntity> menus);

    /**
     * 查询菜单树
     * @param authMenuInfoEntity
     * @return .菜单树
     */
    ApiResult<?> queryMenuTree(AuthMenuInfoEntity authMenuInfoEntity);
    /**
     * 获取父级菜单分类列表
     * @param
     * @return .菜单树
     */
    ApiResult<?> getMenuCategoryList();

    /**
     * 根据登录账号查询菜单树
     * @param userId
     * @return .菜单树
     */
    ApiResult<?> queryMenuTreeByUserId(String userId);

    /**
     * 根据登录账号返回公共资源引擎首页免密跳转地址
     * @param userId
     * @return .公共资源引擎首页免密跳转地址
     */
    ApiResult<?> queryPaasUrlByUserId(String userId) throws Exception;

    ApiResult<?> queryMenuUrlByUserId(String userId);

    /**
     * 根据菜单码获取菜单详情（菜单对应的按钮及URL信息）
     * @param id 菜单主键.
     * @return result.
     */
    AuthMenuBean queryMenuButtonByMenuCode(String id);

    /**
     * 登录成功时 获取 menuCode值 和 对应功能操作
     * @param userId
     * @return
     */
    List<LoginMenuBean> queryLoginMenuByUserId(String userId);

    List<LoginMenuBean> queryLoginMenuByAdmin();

    /**
     * 新增角色时获取菜单列表
     * @param userId
     * @return .
     */
    ApiResult<?> getMenuList(String userId);
}
