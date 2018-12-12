package com.wehealth.model.domain.model;

import com.wehealth.model.domain.interfaceutil.NamedObject;

public enum UserChargePath implements NamedObject {
	chinapay("银联"), wepay("微信"), alipay("支付宝"),

	;
	private String text;

	private UserChargePath(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
