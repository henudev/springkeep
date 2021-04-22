package com.h3c.bigdata.zhgx.function.system.model;

import com.h3c.bigdata.zhgx.function.system.entity.OperateLog;

import java.io.Serializable;

/**
 * 操作日志返回结果封装Bean.
 *
 * @Author J16898
 * @Date 2018/8/9
 * @Version 1.0
 */
public class OperateLogResultBean extends OperateLog implements Serializable {

    private String deptName;

    private String userName;

    private String actionName;

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    @Override
    public String getDeptName() {
        return deptName;
    }

    @Override
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
