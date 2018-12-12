/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 */
package com.wehealth.model.domain.enumutil;

import com.wehealth.model.domain.interfaceutil.NamedObject;

public enum OrderStatus  implements NamedObject {
	placed("排队中"),  // user placed an order
	accepted("咨询中"), // doctor accepted
	rejected("拒绝"), // doctor rejected
	completed("完成"), 
	canceled("取消"),// patient canceled
	mannualCancel("系统取消"),//oss system operatored cancel
	;

	private String text;
	
	private OrderStatus(String text)
	{
		this.text = text;
	}
	
	@Override
	public String getText()
	{
		return text;
	} 
	
};