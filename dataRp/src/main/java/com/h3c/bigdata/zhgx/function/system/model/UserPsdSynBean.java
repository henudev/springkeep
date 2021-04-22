package com.h3c.bigdata.zhgx.function.system.model;

import java.io.Serializable;
import java.util.List;

/**
 * @program: zhgx
 * @description: 同步账号密码给研发数据库
 * @author: h17338
 * @create: 2018-08-07 13:36
 **/
public class UserPsdSynBean implements Serializable {

    private String username;
    private String defaultUser;
    private String userType;
    private String primaryGroup;
    private String password;
    private String confirmPassword;
    private List<String> groupList;
    private String organizationName;
    private List<String> roleList;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDefaultUser() {
        return defaultUser;
    }

    public void setDefaultUser(String defaultUser) {
        this.defaultUser = defaultUser;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPrimaryGroup() {
        return primaryGroup;
    }

    public void setPrimaryGroup(String primaryGroup) {
        this.primaryGroup = primaryGroup;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public List<String> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<String> groupList) {
        this.groupList = groupList;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }
}
