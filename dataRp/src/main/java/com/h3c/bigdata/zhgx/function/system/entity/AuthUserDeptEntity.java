package com.h3c.bigdata.zhgx.function.system.entity;

public class AuthUserDeptEntity extends AuthUserInfoEntity{
    AuthDepartmentInfoEntity departmentInfo;
    public AuthDepartmentInfoEntity getDepartmentInfo(){
        return departmentInfo;
    }
    public void setDepartmentInfo(AuthDepartmentInfoEntity departmentInfo){
        this.departmentInfo = departmentInfo;
    }
}
