package com.h3c.bigdata.zhgx.function.system.entity;


import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "td_s_role_right")
public class RoleRightEntity extends BaseEntity {
    /**
     * 角色编码
     */
    @Id
    @Column(name = "role_code")
    private String roleCode;

    /**
     * 权限类型：M-菜单权限,P项目权限（项目权限可细至子流程的权限）
     */
    @Column(name = "right_type")
    private String rightType;

    /**
     * 具体项目权限编码或菜单编码
     */
    @Column(name = "right_code")
    private String rightCode;

    /**
     * 项目权限的阶段编码
     */
    @Column(name = "flow_id")
    private String flowId;

    /**
     * 更新赋权时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 更新赋权操作员工id
     */
    @Column(name = "update_staff_id")
    private String updateStaffId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 备用字段01
     */
    @Column(name = "reserve_str1")
    private String reserveStr1;

    /**
     * 备用字段02
     */
    @Column(name = "reserve_str2")
    private String reserveStr2;

    /**
     * 备用字段03
     */
    @Column(name = "reserve_str3")
    private String reserveStr3;

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
     * 获取权限类型：M-菜单权限,P项目权限（项目权限可细至子流程的权限）
     *
     * @return right_type - 权限类型：M-菜单权限,P项目权限（项目权限可细至子流程的权限）
     */
    public String getRightType() {
        return rightType;
    }

    /**
     * 设置权限类型：M-菜单权限,P项目权限（项目权限可细至子流程的权限）
     *
     * @param rightType 权限类型：M-菜单权限,P项目权限（项目权限可细至子流程的权限）
     */
    public void setRightType(String rightType) {
        this.rightType = rightType == null ? null : rightType.trim();
    }

    /**
     * 获取具体项目权限编码或菜单编码
     *
     * @return right_code - 具体项目权限编码或菜单编码
     */
    public String getRightCode() {
        return rightCode;
    }

    /**
     * 设置具体项目权限编码或菜单编码
     *
     * @param rightCode 具体项目权限编码或菜单编码
     */
    public void setRightCode(String rightCode) {
        this.rightCode = rightCode == null ? null : rightCode.trim();
    }

    /**
     * 获取项目权限的阶段编码
     *
     * @return flow_id - 项目权限的阶段编码
     */
    public String getFlowId() {
        return flowId;
    }

    /**
     * 设置项目权限的阶段编码
     *
     * @param flowId 项目权限的阶段编码
     */
    public void setFlowId(String flowId) {
        this.flowId = flowId == null ? null : flowId.trim();
    }

    /**
     * 获取更新赋权时间
     *
     * @return update_time - 更新赋权时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新赋权时间
     *
     * @param updateTime 更新赋权时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取更新赋权操作员工id
     *
     * @return update_staff_id - 更新赋权操作员工id
     */
    public String getUpdateStaffId() {
        return updateStaffId;
    }

    /**
     * 设置更新赋权操作员工id
     *
     * @param updateStaffId 更新赋权操作员工id
     */
    public void setUpdateStaffId(String updateStaffId) {
        this.updateStaffId = updateStaffId == null ? null : updateStaffId.trim();
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 获取备用字段01
     *
     * @return reserve_str1 - 备用字段01
     */
    public String getReserveStr1() {
        return reserveStr1;
    }

    /**
     * 设置备用字段01
     *
     * @param reserveStr1 备用字段01
     */
    public void setReserveStr1(String reserveStr1) {
        this.reserveStr1 = reserveStr1 == null ? null : reserveStr1.trim();
    }

    /**
     * 获取备用字段02
     *
     * @return reserve_str2 - 备用字段02
     */
    public String getReserveStr2() {
        return reserveStr2;
    }

    /**
     * 设置备用字段02
     *
     * @param reserveStr2 备用字段02
     */
    public void setReserveStr2(String reserveStr2) {
        this.reserveStr2 = reserveStr2 == null ? null : reserveStr2.trim();
    }

    /**
     * 获取备用字段03
     *
     * @return reserve_str3 - 备用字段03
     */
    public String getReserveStr3() {
        return reserveStr3;
    }

    /**
     * 设置备用字段03
     *
     * @param reserveStr3 备用字段03
     */
    public void setReserveStr3(String reserveStr3) {
        this.reserveStr3 = reserveStr3 == null ? null : reserveStr3.trim();
    }
}