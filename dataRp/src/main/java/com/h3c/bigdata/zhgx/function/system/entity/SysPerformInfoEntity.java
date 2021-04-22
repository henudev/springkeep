package com.h3c.bigdata.zhgx.function.system.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * sys_perform_info表对应实体类
 * @author w17193
 */
@Table(name="sys_perform_info")
public class SysPerformInfoEntity implements Serializable {

    @Id
    private String id;

    /**
     * 内存占用率
     */
    @Column(name="memory_rate")
    private Double memoryRate;

    /**
     * cpu使用率
     */
    @Column(name="cpu_rate")
    private Double cpuRate;

    /**
     * 磁盘使用率
     */
    @Column(name="disk_rate")
    private Double diskRate;

    /**
     * 上行速度
     */
    @Column(name="up_speed")
    private Double upSpeed;

    /**
     * 下行速度
     */
    @Column(name="down_speed")
    private Double downSpeed;

    /**
     * 线程数量
     */
    @Column(name="thread_count")
    private Integer threadCount;

    /**
     * 创建时间
     */
    @Column(name="create_time")
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getMemoryRate() {
        return memoryRate;
    }

    public void setMemoryRate(Double memoryRate) {
        this.memoryRate = memoryRate;
    }

    public Double getCpuRate() {
        return cpuRate;
    }

    public void setCpuRate(Double cpuRate) {
        this.cpuRate = cpuRate;
    }

    public Double getDiskRate() {
        return diskRate;
    }

    public void setDiskRate(Double diskRate) {
        this.diskRate = diskRate;
    }

    public Double getUpSpeed() {
        return upSpeed;
    }

    public void setUpSpeed(Double upSpeed) {
        this.upSpeed = upSpeed;
    }

    public Double getDownSpeed() {
        return downSpeed;
    }

    public void setDownSpeed(Double downSpeed) {
        this.downSpeed = downSpeed;
    }

    public Integer getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
