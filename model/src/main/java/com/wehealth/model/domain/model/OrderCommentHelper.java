/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Jul 22, 2015
 */

package com.wehealth.model.domain.model;

import java.util.Date;


/**
 * Used to help pass Order Comments
 */
public class OrderCommentHelper
{
	private Date updateTime;
	private String patientName;
	private String evaluation;
	private boolean satisfy;
	
	public OrderCommentHelper()
	{
	}
	
	public OrderCommentHelper(String patientName, Date updateTime, String evaluation, boolean satisfy)
	{
		this.patientName = patientName;
		this.updateTime = updateTime;
		this.satisfy = satisfy;
		this.evaluation = evaluation;
	}
	
	public Date getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}
	public String getPatientName()
	{
		return patientName;
	}
	public void setPatientName(String patientName)
	{
		this.patientName = patientName;
	}
	public String getEvaluation()
	{
		return evaluation;
	}
	public void setEvaluation(String evaluation)
	{
		this.evaluation = evaluation;
	}
	public boolean isSatisfy()
	{
		return satisfy;
	}
	public void setSatisfy(boolean satisfy)
	{
		this.satisfy = satisfy;
	}
	
	public String toString()
	{
		return "OrderCommentHelper[Patient Name: " + patientName + ", updateTime: " + updateTime
				+ ", statisfy:" + satisfy + ", evaluation:" + evaluation + "]";
	}
}
