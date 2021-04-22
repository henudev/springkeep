package com.h3c.bigdata.zhgx.function.system.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 轻资产项目履约及政策兑现情况表
 */
@Table(name = "se_light_asset_project")
public class LightAssetProjectEntity implements Serializable {

    @Id
    private String id;
    private String projectName;
    private Date signTime;
    private String park;
    private String registerPlace;
    private Date registerTime;
    private String workingConditions;
    private String formalOperation;
    private Date serviceTime;
    private String overallEvaluation;
    private String reason;

    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public String getPark() {
        return park;
    }

    public void setPark(String park) {
        this.park = park;
    }

    public String getRegisterPlace() {
        return registerPlace;
    }

    public void setRegisterPlace(String registerPlace) {
        this.registerPlace = registerPlace;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getWorkingConditions() {
        return workingConditions;
    }

    public void setWorkingConditions(String workingConditions) {
        this.workingConditions = workingConditions;
    }

    public Date getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Date serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getOverallEvaluation() {
        return overallEvaluation;
    }

    public void setOverallEvaluation(String overallEvaluation) {
        this.overallEvaluation = overallEvaluation;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getFormalOperation() {
        return formalOperation;
    }

    public void setFormalOperation(String formalOperation) {
        this.formalOperation = formalOperation;
    }
}
