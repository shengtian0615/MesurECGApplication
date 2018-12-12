package com.wehealth.model.domain.enumutil;

import com.wehealth.model.domain.interfaceutil.NamedObject;

public enum SurgeryHistoryEnum implements NamedObject {
	pm("心脏起搏器术后"),//paceMaker
	ra("射频消融术后"),//radiofrequency ablation
	hs("心脏支架术后"),//heart stenting
	os("其他手术"),//other surgery
	bp("心脏搭桥术后"),//bypass surgery
	;

	private String text;

	private SurgeryHistoryEnum(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}
}
