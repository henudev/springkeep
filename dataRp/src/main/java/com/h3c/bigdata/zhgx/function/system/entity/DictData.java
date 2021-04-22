package com.h3c.bigdata.zhgx.function.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 系统字典数据表 sys_dict_data
 * 
 * @author j16898
 * @date 2018-07-30
 */
@Table(name = "sys_dict_data")
public class DictData extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** UUID主键 */
	@Id
	private String id;
	/** 字典排序 */
	private Integer dictSort;
	/** 字典标签 */
	private String dictLabel;
	/** 字典键值 */
	private String dictValue;
	/** 字典类型 */
	private String dictType;
	/** 状态：0启用、1禁用 */
	private String status;
	/** 创建者 */
	private String createUser;
	/** 创建时间 */
	private Date createTime;
	/** 更新者 */
	private String updateUser;
	/** 更新时间 */
	private Date updateTime;
	/** 备注 */
	private String remark;

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
	 * 设置：字典排序
	 */
	public void setDictSort(Integer dictSort) 
	{
		this.dictSort = dictSort;
	}
	
	/**
	 * 获取：字典排序
	 */
	public Integer getDictSort() 
	{
		return dictSort;
	}
	
	/**
	 * 设置：字典标签
	 */
	public void setDictLabel(String dictLabel) 
	{
		this.dictLabel = dictLabel;
	}
	
	/**
	 * 获取：字典标签
	 */
	public String getDictLabel() 
	{
		return dictLabel;
	}
	
	/**
	 * 设置：字典键值
	 */
	public void setDictValue(String dictValue) 
	{
		this.dictValue = dictValue;
	}
	
	/**
	 * 获取：字典键值
	 */
	public String getDictValue() 
	{
		return dictValue;
	}
	
	/**
	 * 设置：字典类型
	 */
	public void setDictType(String dictType) 
	{
		this.dictType = dictType;
	}
	
	/**
	 * 获取：字典类型
	 */
	public String getDictType() 
	{
		return dictType;
	}
	
	/**
	 * 设置：状态：0启用、1禁用
	 */
	public void setStatus(String status) 
	{
		this.status = status;
	}
	
	/**
	 * 获取：状态：0启用、1禁用
	 */
	public String getStatus() 
	{
		return status;
	}
	
	/**
	 * 设置：创建者
	 */
	public void setCreateUser(String createUser) 
	{
		this.createUser = createUser;
	}
	
	/**
	 * 获取：创建者
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
	 * 设置：更新者
	 */
	public void setUpdateUser(String updateUser) 
	{
		this.updateUser = updateUser;
	}
	
	/**
	 * 获取：更新者
	 */
	public String getUpdateUser() 
	{
		return updateUser;
	}
	
	/**
	 * 设置：更新时间
	 */
	public void setUpdateTime(Date updateTime) 
	{
		this.updateTime = updateTime;
	}
	
	/**
	 * 获取：更新时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getUpdateTime() 
	{
		return updateTime;
	}
	
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) 
	{
		this.remark = remark;
	}
	
	/**
	 * 获取：备注
	 */
	public String getRemark() 
	{
		return remark;
	}
	
}
