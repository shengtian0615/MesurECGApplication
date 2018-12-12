/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Jul 29, 2015
 */

package com.wehealth.model.domain.model;

import com.wehealth.model.domain.enumutil.AppType;
import com.wehealth.model.domain.interfaceutil.TimeAuditable;

import java.util.Date;


public class AppVersion implements TimeAuditable
{
	private Long id;
	private AppType appType;
	private String versionName;
	private int versionCode;
	private String link;
	private String note;
	private Date createTime;
	private Date updateTime;
	
	public AppVersion()
	{
	}
	
	public AppVersion(AppType appType, String versionName, String link, String note)
	{
		this.appType = appType;
		this.versionName = versionName;
		this.link = link;
		this.note = note;
	}
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public AppType getAppType()
	{
		return appType;
	}
	public void setAppType(AppType appType)
	{
		this.appType = appType;
	}
	public String getVersionName()
	{
		return versionName;
	}
	public void setVersionName(String versionName)
	{
		this.versionName = versionName;
	}
	public int getVersionCode()
	{
		return versionCode;
	}
	public void setVersionCode(int versionCode)
	{
		this.versionCode = versionCode;
	}
	public String getLink()
	{
		return link;
	}
	public void setLink(String link)
	{
		this.link = link;
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
	
	public String toString()
	{
		return "AppVersion[appType:"+appType.getText()+", versionName:"+versionName+", versionCode:"+versionCode+"]";
	}
}
