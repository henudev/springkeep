package com.h3c.bigdata.zhgx.function.system.entity;

import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;
import javax.persistence.*;

@Table(name = "auth_role_menu")
public class AuthRoleMenuEntity extends BaseEntity {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private String roleId;

    /**
     * 权限id
     */
    @Column(name = "menu_id")
    private String menuId;

    /**
     * 功能类型
     */
    @Column(name = "function_type")
    private String functionType;

    @Column(name = "menu_code")
    private String menuCode;
    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取角色id
     *
     * @return role_id - 角色id
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * 设置角色id
     *
     * @param roleId 角色id
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    /**
     * 获取权限id
     *
     * @return menu_id - 权限id
     */
    public String getMenuId() {
        return menuId;
    }

    /**
     * 设置权限id
     *
     * @param menuId 权限id
     */
    public void setMenuId(String menuId) {
        this.menuId = menuId == null ? null : menuId.trim();
    }

    public String getFunctionType() {
        return functionType;
    }

    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }
}