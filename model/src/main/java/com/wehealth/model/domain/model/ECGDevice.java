package com.wehealth.model.domain.model;

/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Nov 6, 2014
 */

import com.wehealth.model.domain.enumutil.ECGDeviceStatus;
import com.wehealth.model.domain.interfaceutil.TimeAuditable;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ECGDevice implements TimeAuditable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String serialNo;
	private String model;
	private String simCardNo;
	private String wirelesserviceProvider;
	private String imei;
	private Date createTime;
	private Date updateTime;
	
	private ECGDeviceStatus status;
	private String seller;
	private String demander;
	private String district;
	private String note;
	private Set<ThirdAgency> agencySet = new HashSet<ThirdAgency>();
	private boolean sentToThird;
	private String macAddr;   //设备MAC地址
	
	public ECGDevice() {
		super();
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getSerialNo() {
		return serialNo;
	}
	
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	
	public String getModel() {
		return model;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public String getSimCardNo() {
		return simCardNo;
	}
	
	public void setSimCardNo(String simCardNo) {
		this.simCardNo = simCardNo;
	}
	
	public String getWirelesserviceProvider() {
		return wirelesserviceProvider;
	}
	
	public void setWirelesserviceProvider(String wirelesserviceProvider) {
		this.wirelesserviceProvider = wirelesserviceProvider;
	}
	
	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((imei == null) ? 0 : imei.hashCode());
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
		ECGDevice other = (ECGDevice) obj;
		if (imei == null)
		{
			if (other.imei != null)
				return false;
		}
		else if (!imei.equals(other.imei))
			return false;
		return true;
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

	public ECGDeviceStatus getStatus()
	{
		return status;
	}

	public void setStatus(ECGDeviceStatus status)
	{
		this.status = status;
	}

	public String getSeller()
	{
		return seller;
	}

	public void setSeller(String seller)
	{
		this.seller = seller;
	}

	public String getDistrict()
	{
		return district;
	}

	public void setDistrict(String district)
	{
		this.district = district;
	}

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public String getDemander()
	{
		return demander;
	}

	public void setDemander(String demander)
	{
		this.demander = demander;
	}

	public Set<ThirdAgency> getAgencySet()
	{
		return agencySet;
	}

	public void setAgencySet(Set<ThirdAgency> agencySet)
	{
		this.agencySet = agencySet;
	}

	public boolean isSentToThird()
	{
		return sentToThird;
	}

	public void setSentToThird(boolean sentToThird)
	{
		this.sentToThird = sentToThird;
	}

	public String getMacAddr() {
		return macAddr;
	}

	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}
	
}
