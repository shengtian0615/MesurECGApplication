/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Jul 6, 2016
 */
package com.wehealth.model.domain.model;

import com.wehealth.model.domain.interfaceutil.LongTimeAuditable;

public class ThirdAgency implements LongTimeAuditable
{
	private long id;
	private String name;
	private Long createTime;
	private Long updateTime;
	private String clientId;
	/**
	 * Agency call us need this key
	 * timeStamp=yyyy-MM-dd
	 * token=md5(timeStamp+clientId+inKey)
	 */
	private String inKey;
	private String note;
	private boolean enable;
	
	// abnormal ecg result sms
	private boolean abNormalEcgSMS;
	// customer service, like moniter panel、telephone
	private boolean customerService;
	// money service, need recharge
	private boolean moneyService;
	// emergency ecg check
	private boolean emergencyECGCheck;
	
	
	
	public long getId()
	{
		return id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getInKey()
	{
		return inKey;
	}
	public void setInKey(String inKey)
	{
		this.inKey = inKey;
	}
	public String getNote()
	{
		return note;
	}
	public void setNote(String note)
	{
		this.note = note;
	}
	
	public boolean isEnable()
	{
		return enable;
	}
	public void setEnable(boolean enable)
	{
		this.enable = enable;
	}
	@Override
	public Long getCreateTime()
	{
		return createTime;
	}
	@Override
	public void setCreateTime(Long createTime)
	{
		this.createTime = createTime;
	}
	@Override
	public Long getUpdateTime()
	{
		return updateTime;
	}
	@Override
	public void setUpdateTime(Long updateTime)
	{
		this.updateTime = updateTime;
	}
	public String getClientId()
	{
		return clientId;
	}
	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ThirdAgency other = (ThirdAgency) obj;
		if (id != other.id)
			return false;
		return true;
	}
	public boolean isAbNormalEcgSMS()
	{
		return abNormalEcgSMS;
	}
	public void setAbNormalEcgSMS(boolean abNormalEcgSMS)
	{
		this.abNormalEcgSMS = abNormalEcgSMS;
	}
	public boolean isCustomerService()
	{
		return customerService;
	}
	public void setCustomerService(boolean customerService)
	{
		this.customerService = customerService;
	}
	public boolean isMoneyService()
	{
		return moneyService;
	}
	public void setMoneyService(boolean moneyService)
	{
		this.moneyService = moneyService;
	}
	public boolean isEmergencyECGCheck()
	{
		return emergencyECGCheck;
	}
	public void setEmergencyECGCheck(boolean emergencyECGCheck)
	{
		this.emergencyECGCheck = emergencyECGCheck;
	}
	
	
}
