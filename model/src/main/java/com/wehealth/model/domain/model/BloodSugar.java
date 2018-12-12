/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Mar 25, 2016
 */

package com.wehealth.model.domain.model;


import com.wehealth.model.domain.enumutil.BloodSugarType;
import com.wehealth.model.domain.interfaceutil.TimeAuditable;

import java.util.Date;

public class BloodSugar implements TimeAuditable
{
	private Long id;
	private Date createTime;
	private Date updateTime;
	//身份证号
	private String patientId;
	private double number;// mmol/L叫毫摩尔每升
	private BloodSugarType type;
	private String note;
	private Date testTime;
	
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

	public double getNumber()
	{
		return number;
	}

	public void setNumber(double number)
	{
		this.number = number;
	}
	public BloodSugarType getType()
	{
		return type;
	}

	public void setType(BloodSugarType type)
	{
		this.type = type;
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
