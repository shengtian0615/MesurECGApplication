/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Feb 14, 2017
 */
package com.wehealth.model.domain.enumutil;

import com.wehealth.model.domain.interfaceutil.NamedObject;

public enum UserPackageStatus implements NamedObject
{
	completed("已完成"), //如果是不含硬件，套餐购买成功直接是这个状态
	deviceWait("需要发硬件"),//如果含硬件，套餐购买成功是这个状态，提示客服，客服要修改
	deviceSent("硬件已发出"),//设备发出，但没确认收到，客服修改为这个状态
	shipNoSent("已通知单号"),//已经发送了运单信息的通知，客服修改这个状态
	other("其它情况"), //其他未知情况是这种状态，在note里写情况
	;
	
	private String text;
	
	private UserPackageStatus(String text)
	{
		this.text = text;
	}
	
	public String getText()
	{
		return text;
	}
}
