package com.h3c.bigdata.zhgx.function.system.model;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

/**
 * @program: zhgx
 * @description: 用户、用户角色、角色三表关联查询实体
 * @author: h17338
 * @create: 2018-08-01 13:53
 **/
public class UserWithRoleModel {

    /**
     * 主键
     */
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
    private String userNamePinyin;

    /**
     * 用户关联的部门id
     */
    private String departmentId;

    private List<String> childDeptId;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 邮箱地址
     */
    private String emailAddress;

    /**
     * 年龄
     */
    private String age;

    /**
     * 身份证号
     */
    private String identityNumber;

    /**
     * 状态，0启用，1停用
     */
    private String status;

    /**
     * 调用方名称
     */
    private String callerName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 变更时间
     */
    private Date updateTime;

    /**
     * 性别，0男，1女
     */
    private String sex;

    /**
     * 用户头像.

     */
    private String avatar;

    private String RoleId;

    private String RoleName;
    @Column(name = "department_name")
    private String departmentName;

    /**
     * 用户角色类型key值.

     */
    private String roleKey;

    /**
     * 用户角色类型.

     */
    private String roleType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserNamePinyin() {
        return userNamePinyin;
    }

    public void setUserNamePinyin(String userNamePinyin) {
        this.userNamePinyin = userNamePinyin;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRoleId() {
        return RoleId;
    }

    public void setRoleId(String roleId) {
        RoleId = roleId;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public List<String> getChildDeptId() {
        return childDeptId;
    }

    public void setChildDeptId(List<String> childDeptId) {
        this.childDeptId = childDeptId;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    //    AuthUserInfoEntity authUserInfoEntity;
//
//    List<AuthRoleInfoEntity> authRoleInfoEntityList;
//
//    public AuthUserInfoEntity getAuthUserInfoEntity() {
//        return authUserInfoEntity;
//    }
//
//    public void setAuthUserInfoEntity(AuthUserInfoEntity authUserInfoEntity) {
//        this.authUserInfoEntity = authUserInfoEntity;
//    }
//
//    public List<AuthRoleInfoEntity> getAuthRoleInfoEntityList() {
//        return authRoleInfoEntityList;
//    }
//
//    public void setAuthRoleInfoEntityList(List<AuthRoleInfoEntity> authRoleInfoEntityList) {
//        this.authRoleInfoEntityList = authRoleInfoEntityList;
//    }
}
