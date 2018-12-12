/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Jan 19, 2015
 */

package com.wehealth.model.domain.enumutil;

import com.wehealth.model.domain.interfaceutil.NamedObject;

public enum PatientPayTransactionType implements NamedObject {
	autoDiagnosisPay("自动诊断扣费"),//自动诊断收费  
	manualDiagnosisPay("人工诊断扣费"),//人工复核收费
	doctorDiagnosisPay("医生咨询扣费"),//医生诊断收费
	refund("退款"),//退款
	smsCharge("物联网卡扣费"),//物联网卡扣费
	mannualCharge("手动扣费"),//手动扣费
	mannualRecharge("后台充值"),//手动充值
	testRecharge("测试款充值"),//测试款充值
	
	;
	
	private String text;
	
	private PatientPayTransactionType(String text)
	{
		this.text = text;
	}

	@Override
	public String getText()
	{
		return text;
	}
}