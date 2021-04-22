package com.h3c.bigdata.zhgx.function.system.entity;

import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "td_s_staff_right")
public class StaffRightEntity extends BaseEntity {
    /**
     * 员工工号编码
     */
    @Id
    @Column(name = "staff_id")
    private String staffId;

    /**
     * 权限类型:R角色权限、B订阅项目权限
     */
    @Column(name = "right_type")
    private String rightType;

    /**
     * 权限编码:角色权限编码、项目权限编码
     */
    @Column(name = "right_code")
    private String rightCode;

    /**
     * 赋权时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 赋权员工工号
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
     * 获取员工工号编码
     *
     * @return staff_id - 员工工号编码
     */
    public String getStaffId() {
        return staffId;
    }

    /**
     * 设置员工工号编码
     *
     * @param staffId 员工工号编码
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId == null ? null : staffId.trim();
    }

    /**
     * 获取权限类型:R角色权限、B订阅项目权限
     *
     * @return right_type - 权限类型:R角色权限、B订阅项目权限
     */
    public String getRightType() {
        return rightType;
    }

    /**
     * 设置权限类型:R角色权限、B订阅项目权限
     *
     * @param rightType 权限类型:R角色权限、B订阅项目权限
     */
    public void setRightType(String rightType) {
        this.rightType = rightType == null ? null : rightType.trim();
    }

    /**
     * 获取权限编码:角色权限编码、项目权限编码
     *
     * @return right_code - 权限编码:角色权限编码、项目权限编码
     */
    public String getRightCode() {
        return rightCode;
    }

    /**
     * 设置权限编码:角色权限编码、项目权限编码
     *
     * @param rightCode 权限编码:角色权限编码、项目权限编码
     */
    public void setRightCode(String rightCode) {
        this.rightCode = rightCode == null ? null : rightCode.trim();
    }

    /**
     * 获取赋权时间
     *
     * @return update_time - 赋权时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置赋权时间
     *
     * @param updateTime 赋权时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取赋权员工工号
     *
     * @return update_staff_id - 赋权员工工号
     */
    public String getUpdateStaffId() {
        return updateStaffId;
    }

    /**
     * 设置赋权员工工号
     *
     * @param updateStaffId 赋权员工工号
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