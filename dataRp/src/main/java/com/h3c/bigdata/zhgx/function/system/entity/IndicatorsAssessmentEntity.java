package com.h3c.bigdata.zhgx.function.system.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 考核指标履约情况
 */
@Table(name = "se_indicators_assessment")
public class IndicatorsAssessmentEntity implements Serializable {

    @Id
    private String id;
    private String projectId;
    private String year;
    private String operationReceipt;
    private String revenue;
    private String teamIntroduce;
    private String patentIndicator;
    private String platform;
    private String other;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getOperationReceipt() {
        return operationReceipt;
    }

    public void setOperationReceipt(String operationReceipt) {
        this.operationReceipt = operationReceipt;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public String getTeamIntroduce() {
        return teamIntroduce;
    }

    public void setTeamIntroduce(String teamIntroduce) {
        this.teamIntroduce = teamIntroduce;
    }

    public String getPatentIndicator() {
        return patentIndicator;
    }

    public void setPatentIndicator(String patentIndicator) {
        this.patentIndicator = patentIndicator;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
