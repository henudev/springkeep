package com.h3c.bigdata.zhgx.function.system.entity;

import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 系统参数表
 * 
 * @date 2018/09/06
 */
@Table(name = "sys_paramater")
public class SysParamaterEntity extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** UUID主键 */
	@Id
	private String id;
	/** 参数名称*/
	private String paraName;
	/** 参数值*/
	private String paraValue;
	/** 参数类型（示例：dict_para_type） */
	private String paraType;
	/** 创建者 */
	private String createUser;
	/** 创建时间 */
	private Date createTime;
	/** 状态：0启用、1禁用 */
	private String status;
	/** 参数描述 */
	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getParaName() {
		return paraName;
	}

	public void setParaName(String paraName) {
		this.paraName = paraName;
	}

	public String getParaValue() {
		return paraValue;
	}

	public void setParaValue(String paraValue) {
		this.paraValue = paraValue;
	}

	public String getParaType() {
		return paraType;
	}

	public void setParaType(String paraType) {
		this.paraType = paraType;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
