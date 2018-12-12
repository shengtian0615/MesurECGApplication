package com.wehealth.model.domain.model;

import com.wehealth.model.domain.interfaceutil.FullyAuditable;

import java.util.Date;

public abstract class AbstractFullyAuditableEntity  implements FullyAuditable
{
	private Date createTime;
	private Date updateTime;
	private String createUserId;
	private String updateUserId;
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
}
