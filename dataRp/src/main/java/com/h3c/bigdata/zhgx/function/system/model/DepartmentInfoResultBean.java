package com.h3c.bigdata.zhgx.function.system.model;

import com.h3c.bigdata.zhgx.function.system.entity.AuthDepartmentInfoEntity;

public class DepartmentInfoResultBean extends AuthDepartmentInfoEntity {
    private String parentDepartmentName;

    public void setParentDepartmentName(String parentDepartmentName) {
        this.parentDepartmentName = parentDepartmentName;
    }

    public String getParentDepartmentName() {
        return parentDepartmentName;
    }
}
