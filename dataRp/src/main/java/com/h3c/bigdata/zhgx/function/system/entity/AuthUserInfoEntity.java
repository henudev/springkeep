package com.h3c.bigdata.zhgx.function.system.entity;

import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "auth_user_info")
public class AuthUserInfoEntity extends BaseEntity {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 用户id，登录账号
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 用户姓名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 用户姓名拼音
     */
    @Column(name = "user_name_pinyin")
    private String userNamePinyin;

    /**
     * 用户关联的部门id
     */
    @Column(name = "department_id")
    private String departmentId;

    /**
     * 手机号
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * 邮箱地址
     */
    @Column(name = "email_address")
    private String emailAddress;

    /**
     * 年龄
     */
    private String age;

    /**
     * 身份证号
     */
    @Column(name = "identity_number")
    private String identityNumber;

    /**
     * 状态，0启用，1停用
     */
    private String status;

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
     * 性别，0男，1女
     */
    private String sex;

    /**
     * 用户头像:存储的二进制数据.
     */
    private String avatar;

    /**
     * 是否首次登陆，0:首次登陆，1:不是首次登陆
     */
    @Column(name = "first_login_flag")
    private String firstLoginFlag;

    /**
     * 调用方名称
     */
    @Column(name = "caller_name")
    private String callerName;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

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

    /**
     * 获取用户姓名
     *
     * @return user_name - 用户姓名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户姓名
     *
     * @param userName 用户姓名
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 获取用户姓名拼音
     *
     * @return user_name_pinyin - 用户姓名拼音
     */
    public String getUserNamePinyin() {
        return userNamePinyin;
    }

    /**
     * 设置用户姓名拼音
     *
     * @param userNamePinyin 用户姓名拼音
     */
    public void setUserNamePinyin(String userNamePinyin) {
        this.userNamePinyin = userNamePinyin == null ? null : userNamePinyin.trim();
    }

    /**
     * 获取用户关联的部门id
     *
     * @return department_id - 用户关联的部门id
     */
    public String getDepartmentId() {
        return departmentId;
    }

    /**
     * 设置用户关联的部门id
     *
     * @param departmentId 用户关联的部门id
     */
    public void setDepartmentId(String departmentId) {
        //this.departmentId = departmentId == null ? null : departmentId.trim();
        this.departmentId = departmentId;
    }

    /**
     * 获取手机号
     *
     * @return phone_number - 手机号
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * 设置手机号
     *
     * @param phoneNumber 手机号
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    /**
     * 获取邮箱地址
     *
     * @return email_address - 邮箱地址
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * 设置邮箱地址
     *
     * @param emailAddress 邮箱地址
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress == null ? null : emailAddress.trim();
    }

    /**
     * 获取年龄
     *
     * @return age - 年龄
     */
    public String getAge() {
        return age;
    }

    /**
     * 设置年龄
     *
     * @param age 年龄
     */
    public void setAge(String age) {
        this.age = age == null ? null : age.trim();
    }

    /**
     * 获取身份证号
     *
     * @return identity_number - 身份证号
     */
    public String getIdentityNumber() {
        return identityNumber;
    }

    /**
     * 设置身份证号
     *
     * @param identityNumber 身份证号
     */
    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber == null ? null : identityNumber.trim();
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

    /**
     * 获取性别，0男，1女
     *
     * @return sex - 性别，0男，1女
     */
    public String getSex() {
        return sex;
    }

    /**
     * 设置性别，0男，1女
     *
     * @param sex 性别，0男，1女
     */
    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getFirstLoginFlag() {
        return firstLoginFlag;
    }

    public void setFirstLoginFlag(String firstLoginFlag) {
        this.firstLoginFlag = firstLoginFlag;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }
}