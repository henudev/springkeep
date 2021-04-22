package com.h3c.bigdata.zhgx.function.system.entity;

import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;
import java.util.Date;
import javax.persistence.*;

@Table(name = "auth_password")
public class AuthPasswordEntity extends BaseEntity {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 用户id，登录账号
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 密码
     */
    private String passowrd;

    /**
     * 盐值
     */
    private String salt;

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
     * 是否已修改初始密码，1已修改，0未修改
     */
    @Column(name = "is_modified")
    private String isModified;

    /**
     * des密码
     */
    @Column(name = "des_password")
    private String desPassword;

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
     * 获取用户id，登录账号
     *
     * @return user_id - 用户id，登录账号
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户id，登录账号
     *
     * @param userId 用户id，登录账号
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 获取密码
     *
     * @return passowrd - 密码
     */
    public String getPassowrd() {
        return passowrd;
    }

    /**
     * 设置密码
     *
     * @param passowrd 密码
     */
    public void setPassowrd(String passowrd) {
        this.passowrd = passowrd == null ? null : passowrd.trim();
    }

    /**
     * 获取盐值
     *
     * @return salt - 盐值
     */
    public String getSalt() {
        return salt;
    }

    /**
     * 设置盐值
     *
     * @param salt 盐值
     */
    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
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
     * 获取是否已修改初始密码，1已修改，0未修改
     *
     * @return is_modified - 是否已修改初始密码，1已修改，0未修改
     */
    public String getIsModified() {
        return isModified;
    }

    /**
     * 设置是否已修改初始密码，1已修改，0未修改
     *
     * @param isModified 是否已修改初始密码，1已修改，0未修改
     */
    public void setIsModified(String isModified) {
        this.isModified = isModified == null ? null : isModified.trim();
    }

    public String getDesPassword() {
        return desPassword;
    }

    public void setDesPassword(String desPassword) {
        this.desPassword = desPassword;
    }
}