package com.h3c.bigdata.zhgx.function.system.entity;

import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Description: 角色类型信息类
 * @Author: w15112
 * @CreateDate: 2018/11/27 13:56
 * @UpdateUser: w15112
 * @UpdateDate: 2018/11/27 13:56
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Table(name = "auth_role_type")
public class AuthRoleTypeEntity extends BaseEntity {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 角色类型key值
     */
    @Column(name = "role_key")
    private String roleKey;

    /**
     * 角色类型value名称
     */
    @Column(name = "role_value")
    private String roleValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public String getRoleValue() {
        return roleValue;
    }

    public void setRoleValue(String roleValue) {
        this.roleValue = roleValue;
    }
}