package com.h3c.bigdata.zhgx.function.system.entity;


import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "td_s_role")
public class RoleEntity extends BaseEntity {
    /**
     * 角色编码
     */
    @Id
    @Column(name = "role_code")
    private String roleCode;

    /**
     * 角色名字
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 是否内置
     */
    @Column(name = "is_default")
    private String isDefault;

    /**
     * 角色类型：M-菜单角色;P-项目角色;H-高级角色（根据行业和片区进行限制的角色）
     */
    @Column(name = "role_type")
    private String roleType;

    /**
     * 行业限制：限制某一行业的项目，为空则不限制
     */
    @Column(name = "industry_limit")
    private String industryLimit;

    /**
     * 片区限制：限制某一片区的项目，为空则不限制
     */
    @Column(name = "area_limit")
    private String areaLimit;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 更新操作者工号
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
     * 获取角色名字
     *
     * @return role_name - 角色名字
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 设置角色名字
     *
     * @param roleName 角色名字
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    /**
     * 获取是否内置
     *
     * @return is_default - 是否内置
     */
    public String getIsDefault() {
        return isDefault;
    }

    /**
     * 设置是否内置
     *
     * @param isDefault 是否内置
     */
    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault == null ? null : isDefault.trim();
    }

    /**
     * 获取角色类型：M-菜单角色;P-项目角色;H-高级角色（根据行业和片区进行限制的角色）
     *
     * @return role_type - 角色类型：M-菜单角色;P-项目角色;H-高级角色（根据行业和片区进行限制的角色）
     */
    public String getRoleType() {
        return roleType;
    }

    /**
     * 设置角色类型：M-菜单角色;P-项目角色;H-高级角色（根据行业和片区进行限制的角色）
     *
     * @param roleType 角色类型：M-菜单角色;P-项目角色;H-高级角色（根据行业和片区进行限制的角色）
     */
    public void setRoleType(String roleType) {
        this.roleType = roleType == null ? null : roleType.trim();
    }

    /**
     * 获取行业限制：限制某一行业的项目，为空则不限制
     *
     * @return industry_limit - 行业限制：限制某一行业的项目，为空则不限制
     */
    public String getIndustryLimit() {
        return industryLimit;
    }

    /**
     * 设置行业限制：限制某一行业的项目，为空则不限制
     *
     * @param industryLimit 行业限制：限制某一行业的项目，为空则不限制
     */
    public void setIndustryLimit(String industryLimit) {
        this.industryLimit = industryLimit == null ? null : industryLimit.trim();
    }

    /**
     * 获取片区限制：限制某一片区的项目，为空则不限制
     *
     * @return area_limit - 片区限制：限制某一片区的项目，为空则不限制
     */
    public String getAreaLimit() {
        return areaLimit;
    }

    /**
     * 设置片区限制：限制某一片区的项目，为空则不限制
     *
     * @param areaLimit 片区限制：限制某一片区的项目，为空则不限制
     */
    public void setAreaLimit(String areaLimit) {
        this.areaLimit = areaLimit == null ? null : areaLimit.trim();
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取更新操作者工号
     *
     * @return update_staff_id - 更新操作者工号
     */
    public String getUpdateStaffId() {
        return updateStaffId;
    }

    /**
     * 设置更新操作者工号
     *
     * @param updateStaffId 更新操作者工号
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