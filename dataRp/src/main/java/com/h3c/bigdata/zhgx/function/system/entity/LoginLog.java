package com.h3c.bigdata.zhgx.function.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 系统访问日志记录表 sys_login_log
 * 
 * @author j16898
 * @date 2018-07-30
 */
@Table(name = "sys_login_log")
public class LoginLog extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 主键UUID */
	@Id
	private String id;
	/** 登录账号 */
	private String loginName;
	/** 登录IP地址 */
	private String ipAddress;
	/** 登录地点 */
	private String loginLocation;
	/** 浏览器类型 */
	private String browser;
	/** 操作系统 */
	private String os;
	/** 登录状态：0成功、1失败 */
	private String status;
	/** 登录提示消息 */
	private String msg;
	/** 访问系统时间 */
	private Date loginTime;

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
	 * 设置：登录账号
	 */
	public void setLoginName(String loginName) 
	{
		this.loginName = loginName;
	}
	
	/**
	 * 获取：登录账号
	 */
	public String getLoginName() 
	{
		return loginName;
	}
	
	/**
	 * 设置：登录IP地址
	 */
	public void setIpAddress(String ipAddress) 
	{
		this.ipAddress = ipAddress;
	}
	
	/**
	 * 获取：登录IP地址
	 */
	public String getIpAddress() 
	{
		return ipAddress;
	}
	
	/**
	 * 设置：登录地点
	 */
	public void setLoginLocation(String loginLocation) 
	{
		this.loginLocation = loginLocation;
	}
	
	/**
	 * 获取：登录地点
	 */
	public String getLoginLocation() 
	{
		return loginLocation;
	}
	
	/**
	 * 设置：浏览器类型
	 */
	public void setBrowser(String browser) 
	{
		this.browser = browser;
	}
	
	/**
	 * 获取：浏览器类型
	 */
	public String getBrowser() 
	{
		return browser;
	}
	
	/**
	 * 设置：操作系统
	 */
	public void setOs(String os) 
	{
		this.os = os;
	}
	
	/**
	 * 获取：操作系统
	 */
	public String getOs() 
	{
		return os;
	}
	
	/**
	 * 设置：登录状态：0成功、1失败
	 */
	public void setStatus(String status) 
	{
		this.status = status;
	}
	
	/**
	 * 获取：登录状态：0成功、1失败
	 */
	public String getStatus() 
	{
		return status;
	}
	
	/**
	 * 设置：登录提示消息
	 */
	public void setMsg(String msg) 
	{
		this.msg = msg;
	}
	
	/**
	 * 获取：登录提示消息
	 */
	public String getMsg() 
	{
		return msg;
	}
	
	/**
	 * 设置：访问系统时间
	 */
	public void setLoginTime(Date loginTime) 
	{
		this.loginTime = loginTime;
	}
	
	/**
	 * 获取：访问系统时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getLoginTime() 
	{
		return loginTime;
	}
	
}
