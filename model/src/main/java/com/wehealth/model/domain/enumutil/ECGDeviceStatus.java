/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Apr 29, 2016
 */

package com.wehealth.model.domain.enumutil;

import com.wehealth.model.domain.interfaceutil.NamedObject;

public enum ECGDeviceStatus implements NamedObject
{
	inventory("库存"),
	output("中间商"),
	second_repo("二级库"),
	
	sold("卖出"),
	rented("服务"),
	specimen("样机"),
	
	unbind("解绑"),
	
	maintain("维修"),
	refund("退库"),
	
	invalid("作废"),
	;
	
	private String text;
	
	private ECGDeviceStatus(String text)
	{
		this.text = text;
	}

	@Override
	public String getText()
	{
		return text;
	}
	
	public static ECGDeviceStatus getStatus(String value)
	{
		ECGDeviceStatus status = null;
		try
		{
			status = ECGDeviceStatus.valueOf(value);
		}
		catch(Exception ex)
		{
		}
		return status;
	}
}
