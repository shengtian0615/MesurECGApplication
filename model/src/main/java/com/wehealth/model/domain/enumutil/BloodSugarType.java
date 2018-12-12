/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Mar 28, 2016
 */

package com.wehealth.model.domain.enumutil;

import com.wehealth.model.domain.interfaceutil.NamedObject;

public enum BloodSugarType implements NamedObject
{
	beforeBreakfast("早餐前"),
	afterBreakfastOneHour("早餐后一小时"),
	afterBreakfastTwoHour("早餐后两小时"),
	beforeLunch("午餐前"),
	afterLunchOneHour("午餐后一小时"),
	afterLunchTwoHour("午餐后两小时"),
	beforeSupper("晚餐前"),
	afterSupperOneHour("晚餐后一小时"),
	afterSupperTwoHour("晚餐后两小时"),
	beforeSleep("睡前"),
	other("其它")
	;

	private String text;
	
	private BloodSugarType(String text)
	{
		this.text = text;
	}
	
	
	@Override
	public String getText()
	{
		return text;
	}
	
}
