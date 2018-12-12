/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Mar 25, 2016
 */

package com.wehealth.model.domain.model;

import com.wehealth.model.domain.interfaceutil.TimeAuditable;

import java.util.Date;


public class BloodPressure implements TimeAuditable
{
	private Long id;
	private Date createTime;
	private Date updateTime;
	//病人身份证号
	private String patientId;
	private double high;//高压，收缩压 //mmHg:毫米汞柱
	private double low;//低压，舒张压 //mmHg:毫米汞柱
	private int heartRate;//脉搏   bpm 次每分钟
	private String note;
	private Date testTime;
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getPatientId()
	{
		return patientId;
	}

	public void setPatientId(String patientId)
	{
		this.patientId = patientId;
	}

	public double getHigh()
	{
		return high;
	}

	public void setHigh(double high)
	{
		this.high = high;
	}

	public double getLow()
	{
		return low;
	}

	public void setLow(double low)
	{
		this.low = low;
	}

	@Override
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	@Override
	public Date getCreateTime()
	{
		return createTime;
	}

	@Override
	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	@Override
	public Date getUpdateTime()
	{
		return updateTime;
	}

	public int getHeartRate()
	{
		return heartRate;
	}

	public void setHeartRate(int heartRate)
	{
		this.heartRate = heartRate;
	}

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public Date getTestTime()
	{
		return testTime;
	}

	public void setTestTime(Date testTime)
	{
		this.testTime = testTime;
	}
}
