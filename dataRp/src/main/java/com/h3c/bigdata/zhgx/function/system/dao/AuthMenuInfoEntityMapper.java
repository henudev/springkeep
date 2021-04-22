package com.h3c.bigdata.zhgx.function.system.dao;

import com.h3c.bigdata.zhgx.common.persistence.BaseMapper;
import com.h3c.bigdata.zhgx.function.system.entity.AuthMenuInfoEntity;
import com.h3c.bigdata.zhgx.function.system.model.LoginMenuBean;
import com.h3c.bigdata.zhgx.function.system.model.MenuButtonBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AuthMenuInfoEntityMapper extends BaseMapper<AuthMenuInfoEntity> {

    /**
     * 查询某一节点的最大code子节点
     */
    List<AuthMenuInfoEntity> queryMaxMenuCodeByParentCode(Map<String, Object> map);

    /**
     * 菜单列表查询
     */
    List<AuthMenuInfoEntity> queryMenuInfoList(AuthMenuInfoEntity authMenuInfoEntity);

    void deleteMenusByParentMenuId(@Param("parentMenuCode") String parentMenuCode);

    void deleteMenusByMenuIds(@Param("list") List<AuthMenuInfoEntity> list);

    List<AuthMenuInfoEntity> queryChildrenMenusByParentId(@Param("list") List<AuthMenuInfoEntity> list);

    /**
     * 菜单树查询
     */
    List<AuthMenuInfoEntity> queryMenuTree(AuthMenuInfoEntity authMenuInfoEntity);

    /**
     * 获取父级菜单分类列表
     */
    List<AuthMenuInfoEntity> getMenuCategoryList();


    /**
     * 根据账号查询菜单列表
     */
    List<AuthMenuInfoEntity> queryMenuListByUserId(@Param("userId") String userId);

    /**
     * 查询根节点
     */
    AuthMenuInfoEntity queryRootMenu();

    /**
     * 根据账号查询菜单列表
     */
    List<String> queryMenuUrlByUserId(String userId);

    /**
     * 根据账号查询用戶有權限URL
     */
    List<AuthMenuInfoEntity> queryUrlByUserId(String userId);


    /**
     * 删除菜单下按钮信息.
     *
     * @param menuCode 菜单id.
     */
    void deleteMenuByParentMenuAndType(String menuCode);

    /**
     * 根据菜单码查询对应菜单下按钮URL信息.
     *
     * @param menuCode 菜单码.
     * @return result.
     */
    List<AuthMenuInfoEntity> queryMenuButtonByMenuCode(String menuCode);

    /**
     * 登录成功时 获取 menuCode值 和 对应功能操作
     *
     * @param userId
     * @return
     */
    List<LoginMenuBean> queryLoginMenuByUserId(String userId);

    List<LoginMenuBean> queryLoginMenuByAdmin();

    List<AuthMenuInfoEntity> queryMenuListByRoleId(@Param("roleId") String roleId);

    /**
     * 获取菜单及子菜单列表
     *
     * @param        id
     * @param menuCode
     * @return
    */
    List<AuthMenuInfoEntity> queryMeunAndChilden (@Param("id") String id,@Param("menuCode") String menuCode);
}