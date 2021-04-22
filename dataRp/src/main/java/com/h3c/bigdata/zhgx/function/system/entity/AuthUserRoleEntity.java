package com.h3c.bigdata.zhgx.function.system.entity;

import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;
import javax.persistence.*;

@Table(name = "auth_user_role")
public class AuthUserRoleEntity extends BaseEntity {
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
     * 用户id，登录账号
     */
    @Column(name = "user_id")
    private String userId;

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
     * 获取用户id，登录账号
     *
     * @return user_id - 用户id，登录账号
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户id，登录账号
     *
     * @param userId 用户id，登录账号
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }
}