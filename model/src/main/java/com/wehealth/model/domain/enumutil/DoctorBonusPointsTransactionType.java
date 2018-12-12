/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Jan 19, 2015
 */

package com.wehealth.model.domain.enumutil;

import com.wehealth.model.domain.interfaceutil.NamedObject;

public enum DoctorBonusPointsTransactionType implements NamedObject {
	bonus("奖励"),
	cashOut("兑换"),;

	private String text;
	
	private DoctorBonusPointsTransactionType(String text)
	{
		this.text = text;
	}
	
	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return text;
	}
	
	public static DoctorBonusPointsTransactionType getType(String type)
	{
		try
		{
			return DoctorBonusPointsTransactionType.valueOf(type);
		}
		catch(Exception ex)
		{
			return null;
		}
	}
	
};