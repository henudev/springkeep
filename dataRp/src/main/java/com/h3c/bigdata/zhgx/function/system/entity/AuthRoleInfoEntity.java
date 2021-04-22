package com.h3c.bigdata.zhgx.function.system.entity;

import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "auth_role_info")
public class AuthRoleInfoEntity extends BaseEntity {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 角色编码
     */
    @Column(name = "role_code")
    private String roleCode;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 状态，0启用，1停用
     */
    private String status;

    /**
     * 角色详情
     */
    @Column(name = "role_detail")
    private String roleDetail;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 变更时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 角色类型key值
     */
    @Column(name = "role_key")
    private String roleKey;

    /**
     * 角色类型key值
     */
    @Column(name = "department_id")
    private String departmentId;

    /**
     * 安全等级
     */
    @Column(name = "level")
    private String roleLevel="4";

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
     * 获取角色编码
     *
     * @return role_code - 角色编码
     */
    public String getRoleCode() {
        return roleCode;
    }

    /**
     * 设置角色编码
     *
     * @param roleCode 角色编码
     */
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode == null ? null : roleCode.trim();
    }

    /**
     * 获取角色名称
     *
     * @return role_name - 角色名称
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 设置角色名称
     *
     * @param roleName 角色名称
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * 获取状态，0启用，1停用
     *
     * @return status - 状态，0启用，1停用
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态，0启用，1停用
     *
     * @param status 状态，0启用，1停用
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * 获取角色详情
     *
     * @return role_detail - 角色详情
     */
    public String getRoleDetail() {
        return roleDetail;
    }

    /**
     * 设置角色详情
     *
     * @param roleDetail 角色详情
     */
    public void setRoleDetail(String roleDetail) {
        this.roleDetail = roleDetail == null ? null : roleDetail.trim();
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取变更时间
     *
     * @return update_time - 变更时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置变更时间
     *
     * @param updateTime 变更时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
    public String getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(String roleLevel) {
        this.roleLevel = roleLevel;
    }
}