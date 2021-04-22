package com.h3c.bigdata.zhgx.function.system.entity;

import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "se_resource_score")
public class ResourceScoreEntity extends BaseEntity {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 资源类型：0应用中心
     */
    @Column(name = "resource_type")
    private String resourceType;

    /**
     * 评分对象id
     */
    @Column(name = "resource_id")
    private String resourceId;

    /**
     * 评分分数，分数为1-5分
     */
    private BigDecimal score;

    /**
     * 评论时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 评分人账号
     */
    @Column(name = "create_person")
    private String createPerson;

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
     * 获取资源类型：0应用中心
     *
     * @return resource_type - 资源类型：0应用中心
     */
    public String getResourceType() {
        return resourceType;
    }

    /**
     * 设置资源类型：0应用中心
     *
     * @param resourceType 资源类型：0应用中心
     */
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType == null ? null : resourceType.trim();
    }

    /**
     * 获取评分对象id
     *
     * @return resource_id - 评分对象id
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * 设置评分对象id
     *
     * @param resourceId 评分对象id
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId == null ? null : resourceId.trim();
    }

    /**
     * 获取评分分数，分数为1-5分
     *
     * @return score - 评分分数，分数为1-5分
     */
    public BigDecimal getScore() {
        return score;
    }

    /**
     * 设置评分分数，分数为1-5分
     *
     * @param score 评分分数，分数为1-5分
     */
    public void setScore(BigDecimal score) {
        this.score = score;
    }

    /**
     * 获取评论时间
     *
     * @return create_time - 评论时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置评论时间
     *
     * @param createTime 评论时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取评分人账号
     *
     * @return create_person - 评分人账号
     */
    public String getCreatePerson() {
        return createPerson;
    }

    /**
     * 设置评分人账号
     *
     * @param createPerson 评分人账号
     */
    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson == null ? null : createPerson.trim();
    }
}