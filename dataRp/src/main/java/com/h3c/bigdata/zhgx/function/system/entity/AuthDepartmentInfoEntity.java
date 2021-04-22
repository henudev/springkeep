package com.h3c.bigdata.zhgx.function.system.entity;

import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;
import java.util.Date;
import javax.persistence.*;

@Table(name = "auth_department_info")
public class AuthDepartmentInfoEntity extends BaseEntity {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 部门名称
     */
    @Column(name = "department_name")
    private String departmentName;

    /**
     * 状态，0启用，1停用
     */
    private String status;

    /**
     * 部门code
     */
    @Column(name = "department_code")
    private String departmentCode;

    /**
     * 父级部门code
     */
    @Column(name = "parent_department_code")
    private String parentDepartmentCode;

    /**
     * 部门描述
     */
    @Column(name = "department_detail")
    private String departmentDetail;

    /**
     *部门经度
     */
    @Column(name = "lng")
    private String lng;

    /**
     * 部门纬度
     */
    @Column(name = "lat")
    private  String lat;

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
     * 部门类型（1:内设机构2:办事处3:专业化机构4:园区运营中心）
     */
    private String departmentType;

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
     * 获取部门名称
     *
     * @return department_name - 部门名称
     */
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * 设置部门名称
     *
     * @param departmentName 部门名称
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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
     * 获取部门code
     *
     * @return department_code - 部门code
     */
    public String getDepartmentCode() {
        return departmentCode;
    }

    /**
     * 设置部门code
     *
     * @param departmentCode 部门code
     */
    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode == null ? null : departmentCode.trim();
    }

    /**
     * 获取父级部门code
     *
     * @return parent_department_code - 父级部门code
     */
    public String getParentDepartmentCode() {
        return parentDepartmentCode;
    }

    /**
     * 设置父级部门code
     *
     * @param parentDepartmentCode 父级部门code
     */
    public void setParentDepartmentCode(String parentDepartmentCode) {
        this.parentDepartmentCode = parentDepartmentCode == null ? null : parentDepartmentCode.trim();
    }

    /**
     * 获取部门描述
     *
     * @return department_detail - 部门描述
     */
    public String getDepartmentDetail() {
        return departmentDetail;
    }

    /**
     * 设置部门描述
     *
     * @param departmentDetail 部门描述
     */
    public void setDepartmentDetail(String departmentDetail) {
        this.departmentDetail = departmentDetail == null ? null : departmentDetail.trim();
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
     * 获取部门经度
     *
     * @return
     */
    public String getLng() {
        return lng;
    }

    /**
     * 设置部门经度
     *
     * @param lng
     */
    public void setLng(String lng) {
        this.lng =lng;
    }

    /**
     * 获取部门纬度
     *
     * @return
     */
    public String getLat() {
        return lat;
    }

    /**
     * 设置部门纬度
     *
     * @param lat
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getDepartmentType() {
        return departmentType;
    }

    public void setDepartmentType(String departmentType) {
        this.departmentType = departmentType;
    }
}