package com.h3c.bigdata.zhgx.function.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 系统（包含个人通知消息或系统通知公告）表 sys_message_info
 * 
 * @author j16898
 * @date 2018-07-30
 */
@Table(name = "sys_message_info")
public class MessageInfo extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** UUID主键 */
	@Id
	private String id;
	/** 消息类型：0告警消息、1通知消息 */
	private String messageType;
	/** 消息状态:0未读、1已读 */
	private String messageStatus;
	/** 消息详情 */
	private String messageDetail;
	/** 创建人 */
	private String createUser;
	/** 创建时间 */
	private Date createTime;
	/** 查看时间 */
	private Date readTime;
	/** 接收人 */
	private String receiveUser;

	/**
	 * 是否是数据申请.（1：是；0：不是）
	 */
	private String isDataInterface;

	/**
	 * 接口订阅：服务名称.
	 */
	private String serviceName;

	/**
	 * 数据申请id
	 */
	private String dataApplyId;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getIsDataInterface() {
		return isDataInterface;
	}

	public void setIsDataInterface(String isDataInterface) {
		this.isDataInterface = isDataInterface;
	}

	/**
	 * 设置：UUID主键
	 */
	public void setId(String id) 
	{
		this.id = id;
	}
	
	/**
	 * 获取：UUID主键
	 */
	public String getId() 
	{
		return id;
	}
	
	/**
	 * 设置：消息类型：0告警消息、1通知消息
	 */
	public void setMessageType(String messageType) 
	{
		this.messageType = messageType;
	}
	
	/**
	 * 获取：消息类型：0告警消息、1通知消息
	 */
	public String getMessageType() 
	{
		return messageType;
	}
	
	/**
	 * 设置：消息状态:0未读、1已读
	 */
	public void setMessageStatus(String messageStatus) 
	{
		this.messageStatus = messageStatus;
	}
	
	/**
	 * 获取：消息状态:0未读、1已读
	 */
	public String getMessageStatus() 
	{
		return messageStatus;
	}
	
	/**
	 * 设置：消息详情
	 */
	public void setMessageDetail(String messageDetail) 
	{
		this.messageDetail = messageDetail;
	}
	
	/**
	 * 获取：消息详情
	 */
	public String getMessageDetail() 
	{
		return messageDetail;
	}
	
	/**
	 * 设置：创建人
	 */
	public void setCreateUser(String createUser) 
	{
		this.createUser = createUser;
	}
	
	/**
	 * 获取：创建人
	 */
	public String getCreateUser() 
	{
		return createUser;
	}
	
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}
	
	/**
	 * 获取：创建时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getCreateTime() 
	{
		return createTime;
	}
	
	/**
	 * 设置：查看时间
	 */
	public void setReadTime(Date readTime) 
	{
		this.readTime = readTime;
	}
	
	/**
	 * 获取：查看时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getReadTime() 
	{
		return readTime;
	}
	
	/**
	 * 设置：接收人
	 */
	public void setReceiveUser(String receiveUser) 
	{
		this.receiveUser = receiveUser;
	}
	
	/**
	 * 获取：接收人
	 */
	public String getReceiveUser() 
	{
		return receiveUser;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getDataApplyId() {
		return dataApplyId;
	}

	public void setDataApplyId(String dataApplyId) {
		this.dataApplyId = dataApplyId;
	}
}
