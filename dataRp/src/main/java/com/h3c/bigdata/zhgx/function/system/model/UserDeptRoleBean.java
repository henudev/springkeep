package com.h3c.bigdata.zhgx.function.system.model;

import java.io.Serializable;

/**
 *
 * @author w17193
 */
public class UserDeptRoleBean implements Serializable {
    /**
     * 部门id
     */
    private String deptId;


    /**
     * 角色类型 0--超级 1--用户超级 2--普通用户
     */
    private String roleKey;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }
}
