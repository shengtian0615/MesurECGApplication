package com.wehealth.model.domain.enumutil;

import com.wehealth.model.domain.interfaceutil.NamedObject;

public enum FamilyMedicalHistoryEnum implements NamedObject {
	hbp("高血压"),//high blood pressure
	cd("脑血管病"),//cerebrovascular disease
	db("糖尿病"),//diabetes
	mi("心肌梗塞"),//myocardial infarct
	ch("冠心病"),//coronary heart disease
	the("恶性肿瘤"),//therioma
	hlm("高血脂"),//hyperlipemia
	other("其他"),
	ary("心律失常"),//arythmia
	;

	private String text;

	private FamilyMedicalHistoryEnum(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}
}
