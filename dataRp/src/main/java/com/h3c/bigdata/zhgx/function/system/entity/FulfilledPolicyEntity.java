package com.h3c.bigdata.zhgx.function.system.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "se_fulfilled_policy")
public class FulfilledPolicyEntity implements Serializable {

    @Id
    private String id;
    private String projectId;
    private String year;
    private String financialSupport;
    private String siteSupport;
    private String decorationSubsidy;
    private String talentApartment;
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

    public String getFinancialSupport() {
        return financialSupport;
    }

    public void setFinancialSupport(String financialSupport) {
        this.financialSupport = financialSupport;
    }

    public String getSiteSupport() {
        return siteSupport;
    }

    public void setSiteSupport(String siteSupport) {
        this.siteSupport = siteSupport;
    }

    public String getDecorationSubsidy() {
        return decorationSubsidy;
    }

    public void setDecorationSubsidy(String decorationSubsidy) {
        this.decorationSubsidy = decorationSubsidy;
    }

    public String getTalentApartment() {
        return talentApartment;
    }

    public void setTalentApartment(String talentApartment) {
        this.talentApartment = talentApartment;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
