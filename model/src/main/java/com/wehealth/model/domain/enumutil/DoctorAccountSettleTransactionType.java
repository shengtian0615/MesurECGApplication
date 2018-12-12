/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Jan 19, 2015
 */

package com.wehealth.model.domain.enumutil;

import com.wehealth.model.domain.interfaceutil.NamedObject;

public enum DoctorAccountSettleTransactionType implements NamedObject {
	pending("未结算"),  // 新建： in a settle period
	settling("结算中"), // 正在结算: under settling, waiting for one Accountant's operation
	dealing("处理中"), //已经发往银联处理
	settled("已结算"), // 已结算: money has been sent
	error("结算错误"),
	cancel("取消结算"),
	;

	private String text;
	
	private DoctorAccountSettleTransactionType(String text)
	{
		this.text = text;
	}
	
	@Override
	public String getText()
	{
		return text;
	} // settled error, may be account error, name error ...
};