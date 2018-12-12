/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun May 29, 2015
 */

package com.wehealth.model.domain.enumutil;

public enum DoctorTitle {
	senior("主任医师"),
	associate("副主任医师"),
	attending("主治医师");
	
	private String _text;
	
	private DoctorTitle(String text)
	{
		_text = text;
	}
	
	public String getText()
	{
		return _text;
	}
}
