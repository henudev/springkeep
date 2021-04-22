package com.h3c.bigdata.zhgx.function.system.entity;

import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "auth_department_location")
public class AuthDepartLocInfoEntity extends BaseEntity {
    /**
     * 主键
     */
    @Id
    private String id;

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

    /**
     * 获取部门id
     *
     * @param
     */
    public String getId() {
        return id;
    }

    /**
     * 设置部门id
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }
}