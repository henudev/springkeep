package com.h3c.bigdata.zhgx.function.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 系统操作日志记录表 sys_operate_log
 * 
 * @author j16898
 * @date 2018-07-30
 */
@Table(name = "sys_operate_log")
public class OperateLog extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 主键UUID */
	@Id
	private String id;
	/** 模块名称 */
	private String moduleName;
	/** 功能请求 */
	private String action;
	/** 方法名称 */
	private String method;
	/** 操作人员（关联用户表主键） */
	private String operateUserId;
	/** 部门名称 */
	private String deptName;
	/** 请求URL */
	private String operateUrl;
	/** 主机地址 */
	private String operateIp;
	/** 操作状态：0正常、1异常 */
	private String status;
	/** 错误消息 */
	private String errorMsg;
	/** 操作时间 */
	private Date operateTime;

	/**
	 * 设置：主键UUID
	 */
	public void setId(String id) 
	{
		this.id = id;
	}
	
	/**
	 * 获取：主键UUID
	 */
	public String getId() 
	{
		return id;
	}
	
	/**
	 * 设置：模块名称
	 */
	public void setModuleName(String moduleName) 
	{
		this.moduleName = moduleName;
	}
	
	/**
	 * 获取：模块名称
	 */
	public String getModuleName() 
	{
		return moduleName;
	}
	
	/**
	 * 设置：功能请求
	 */
	public void setAction(String action) 
	{
		this.action = action;
	}
	
	/**
	 * 获取：功能请求
	 */
	public String getAction() 
	{
		return action;
	}
	
	/**
	 * 设置：方法名称
	 */
	public void setMethod(String method) 
	{
		this.method = method;
	}
	
	/**
	 * 获取：方法名称
	 */
	public String getMethod() 
	{
		return method;
	}
	
	/**
	 * 设置：操作人员（关联用户表主键）
	 */
	public void setOperateUserId(String operateUserId) 
	{
		this.operateUserId = operateUserId;
	}
	
	/**
	 * 获取：操作人员（关联用户表主键）
	 */
	public String getOperateUserId() 
	{
		return operateUserId;
	}
	
	/**
	 * 设置：部门名称
	 */
	public void setDeptName(String deptName) 
	{
		this.deptName = deptName;
	}
	
	/**
	 * 获取：部门名称
	 */
	public String getDeptName() 
	{
		return deptName;
	}
	
	/**
	 * 设置：请求URL
	 */
	public void setOperateUrl(String operateUrl) 
	{
		this.operateUrl = operateUrl;
	}
	
	/**
	 * 获取：请求URL
	 */
	public String getOperateUrl() 
	{
		return operateUrl;
	}
	
	/**
	 * 设置：主机地址
	 */
	public void setOperateIp(String operateIp) 
	{
		this.operateIp = operateIp;
	}
	
	/**
	 * 获取：主机地址
	 */
	public String getOperateIp() 
	{
		return operateIp;
	}
	
	/**
	 * 设置：操作状态：0正常、1异常
	 */
	public void setStatus(String status) 
	{
		this.status = status;
	}
	
	/**
	 * 获取：操作状态：0正常、1异常
	 */
	public String getStatus() 
	{
		return status;
	}
	
	/**
	 * 设置：错误消息
	 */
	public void setErrorMsg(String errorMsg) 
	{
		this.errorMsg = errorMsg;
	}
	
	/**
	 * 获取：错误消息
	 */
	public String getErrorMsg() 
	{
		return errorMsg;
	}
	
	/**
	 * 设置：操作时间
	 */
	public void setOperateTime(Date operateTime) 
	{
		this.operateTime = operateTime;
	}
	
	/**
	 * 获取：操作时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getOperateTime() 
	{
		return operateTime;
	}
	
}
