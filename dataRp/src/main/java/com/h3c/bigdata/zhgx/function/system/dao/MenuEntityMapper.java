package com.h3c.bigdata.zhgx.function.system.dao;

import com.h3c.bigdata.zhgx.common.persistence.BaseMapper;
import com.h3c.bigdata.zhgx.function.system.entity.MenuEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuEntityMapper extends BaseMapper<MenuEntity> {

    /**
     * 根据roleId查询该角色所对应的菜单权限
     * @param roleCode
     * @return
     */
    List<MenuEntity> queryMenuByRoleId(String roleCode);

    /**
     * 根据roleId查询该角色所对应的菜单权限
     * @param menuEntity 菜单实体信息.
     * @return 菜单查询结果.
     */
    List<MenuEntity> queryMenuInfoByRoleId(@Param("query") MenuEntity menuEntity);

}