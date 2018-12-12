package com.wehealth.model.domain.model;

import com.wehealth.model.domain.interfaceutil.CreateTimeAuditable;
import com.wehealth.model.domain.interfaceutil.Entity;

import java.util.Date;

public class RentDeviceTrace implements Entity, CreateTimeAuditable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long rentId;
	private String imei;
	private double latitude;
	private double longitude;
	private String address;
	private String note;
	private Date createTime;
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Long getRentId()
	{
		return rentId;
	}
	public void setRentId(Long rentId)
	{
		this.rentId = rentId;
	}
	public double getLatitude()
	{
		return latitude;
	}
	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}
	public double getLongitude()
	{
		return longitude;
	}
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getImei()
	{
		return imei;
	}
	public void setImei(String imei)
	{
		this.imei = imei;
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
	
}
