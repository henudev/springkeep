package com.h3c.bigdata.zhgx.function.system.model;

import java.io.Serializable;

/**
 * @program: zhgx
 * @description: 向DSG同步新增用户信息
 * @author: h17338
 * @create: 2018-08-08 17:25
 **/
public class UserPsdSynDSGBean implements Serializable {

    private String userName;

    private String userPasswd;

    private String groupId;

    private String groupName;

    private String userDescr;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPasswd() {
        return userPasswd;
    }

    public void setUserPasswd(String userPasswd) {
        this.userPasswd = userPasswd;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserDescr() {
        return userDescr;
    }

    public void setUserDescr(String userDescr) {
        this.userDescr = userDescr;
    }
}
