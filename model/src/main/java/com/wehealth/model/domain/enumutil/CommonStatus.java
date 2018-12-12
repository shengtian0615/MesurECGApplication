/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Nov 26, 2016
 */

package com.wehealth.model.domain.enumutil;

import com.wehealth.model.domain.interfaceutil.NamedObject;

public enum CommonStatus implements NamedObject {
	pending("新建"),
	dealing("处理中"),
	error("发生错误"),
	completed("完成"),
	canceled("取消"),
	need_check("人工检查"),
	;
	
	
	private String text;

	private CommonStatus(String text)
	{
		this.text = text;
	}

	@Override
	public String getText()
	{
		return text;
	}
}
