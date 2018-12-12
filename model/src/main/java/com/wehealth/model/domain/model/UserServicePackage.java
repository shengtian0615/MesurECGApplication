package com.wehealth.model.domain.model;

import com.wehealth.model.domain.enumutil.UserPackageStatus;
import com.wehealth.model.domain.interfaceutil.Entity;
import com.wehealth.model.domain.interfaceutil.TimeAuditable;

import java.util.Date;

public class UserServicePackage implements Entity, TimeAuditable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String packageId;
	private String forUserId;
	
	private Date assignTime;
	private Date endTime;
	private String note;
	
	private Boolean deviceIncluded;
	private UserPackageStatus pacStatus;
	private Date createTime;
	private Date updateTime;
	private Long addressId;
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public String getPackageId()
	{
		return packageId;
	}
	public void setPackageId(String packageId)
	{
		this.packageId = packageId;
	}
	public String getForUserId()
	{
		return forUserId;
	}
	public void setForUserId(String forUserId)
	{
		this.forUserId = forUserId;
	}
	public Boolean isDeviceIncluded()
	{
		return deviceIncluded;
	}
	public void setDeviceIncluded(Boolean deviceIncluded)
	{
		this.deviceIncluded = deviceIncluded;
	}
	public Date getEndTime()
	{
		return endTime;
	}
	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}
	public String getNote()
	{
		return note;
	}
	public void setNote(String note)
	{
		this.note = note;
	}
	public Date getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
	public Date getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}
	public Date getAssignTime()
	{
		return assignTime;
	}
	public void setAssignTime(Date assignTime)
	{
		this.assignTime = assignTime;
	}
	public UserPackageStatus getPacStatus()
	{
		return pacStatus;
	}
	public void setPacStatus(UserPackageStatus pacStatus)
	{
		this.pacStatus = pacStatus;
	}
	public Boolean getDeviceIncluded()
	{
		return deviceIncluded;
	}
	public Long getAddressId()
	{
		return addressId;
	}
	public void setAddressId(Long addressId)
	{
		this.addressId = addressId;
	}
}
