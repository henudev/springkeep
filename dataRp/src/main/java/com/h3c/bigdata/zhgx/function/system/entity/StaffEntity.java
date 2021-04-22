package com.h3c.bigdata.zhgx.function.system.entity;


import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "td_s_staff")
public class StaffEntity extends BaseEntity {
    /**
     * 员工工号
     */
    @Id
    @Column(name = "staff_id")
    private String staffId;

    /**
     * 员工姓名
     */
    @Column(name = "staff_name")
    private String staffName;

    /**
     * 员工拼音姓名.
     */
    @Column(name = "staff_name_pinyin")
    private String staffNamePinyin;

    /**
     * 员工部门
     */
    @Column(name = "depart_id")
    private String departId;

    /**
     * 职位名称
     */
    @Column(name = "job_name")
    private String jobName;

    /**
     * 联系电话
     */
    @Column(name = "contact_phone")
    private String contactPhone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 离职状态：0正常在职，1离职
     */
    @Column(name = "dismission_tag")
    private String dismissionTag;

    /**
     * 员工类型 0--领导 1--员工
     */
    @Column(name = "staff_type")
    private Integer staffType;

    @Column(name = "reserve_str2")
    private String reserveStr2;

    @Column(name = "reserve_str3")
    private String reserveStr3;

    /**
     * 获取员工工号
     *
     * @return staff_id - 员工工号
     */
    public String getStaffId() {
        return staffId;
    }

    /**
     * 设置员工工号
     *
     * @param staffId 员工工号
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId == null ? null : staffId.trim();
    }

    /**
     * 获取员工姓名
     *
     * @return staff_name - 员工姓名
     */
    public String getStaffName() {
        return staffName;
    }

    /**
     * 设置员工姓名
     *
     * @param staffName 员工姓名
     */
    public void setStaffName(String staffName) {
        this.staffName = staffName == null ? null : staffName.trim();
    }

    /**
     * @return staff_name_pinyin
     */
    public String getStaffNamePinyin() {
        return staffNamePinyin;
    }

    /**
     * @param staffNamePinyin
     */
    public void setStaffNamePinyin(String staffNamePinyin) {
        this.staffNamePinyin = staffNamePinyin == null ? null : staffNamePinyin.trim();
    }

    /**
     * 获取员工部门
     *
     * @return depart_id - 员工部门
     */
    public String getDepartId() {
        return departId;
    }

    /**
     * 设置员工部门
     *
     * @param departId 员工部门
     */
    public void setDepartId(String departId) {
        this.departId = departId == null ? null : departId.trim();
    }

    /**
     * 获取职位名称
     *
     * @return job_name - 职位名称
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * 设置职位名称
     *
     * @param jobName 职位名称
     */
    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    /**
     * 获取联系电话
     *
     * @return contact_phone - 联系电话
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * 设置联系电话
     *
     * @param contactPhone 联系电话
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 获取离职状态：0正常在职，1离职
     *
     * @return dismission_tag - 离职状态：0正常在职，1离职
     */
    public String getDismissionTag() {
        return dismissionTag;
    }

    /**
     * 设置离职状态：0正常在职，1离职
     *
     * @param dismissionTag 离职状态：0正常在职，1离职
     */
    public void setDismissionTag(String dismissionTag) {
        this.dismissionTag = dismissionTag == null ? null : dismissionTag.trim();
    }

    public Integer getStaffType() {
        return staffType;
    }

    public void setStaffType(Integer staffType) {
        this.staffType = staffType;
    }

    /**
     * @return reserve_str2
     */
    public String getReserveStr2() {
        return reserveStr2;
    }

    /**
     * @param reserveStr2
     */
    public void setReserveStr2(String reserveStr2) {
        this.reserveStr2 = reserveStr2 == null ? null : reserveStr2.trim();
    }

    /**
     * @return reserve_str3
     */
    public String getReserveStr3() {
        return reserveStr3;
    }

    /**
     * @param reserveStr3
     */
    public void setReserveStr3(String reserveStr3) {
        this.reserveStr3 = reserveStr3 == null ? null : reserveStr3.trim();
    }
}