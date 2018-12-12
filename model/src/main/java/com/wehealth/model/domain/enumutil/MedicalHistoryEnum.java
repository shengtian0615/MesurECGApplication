package com.wehealth.model.domain.enumutil;

import com.wehealth.model.domain.interfaceutil.NamedObject;

public enum MedicalHistoryEnum implements NamedObject {
	hbp("高血压"),//high blood pressure
	ci("心功能不全"),//cardiacInsufficiency
	db("糖尿病"),//diabetes
	ifc("传染病"),//infection
	ch("冠心病"),//coronary heart disease
	cd("脑血管病"),//cerebrovascular disease
	mi("心肌梗塞"),//myocardial infarct
	the("恶性肿瘤"),//therioma
	hlm("高血脂"),//hyperlipemia
	ckd("慢性肾病"),//chronic kidney disease
	hou("高尿酸或痛风"),//hyperuricemia or urathritis
	crd("慢性呼吸系统疾病"),//Chronic respiratory disease
	ary("心律失常"),//arythmia
	ocd("其他慢性疾病"),//other chronic disease
	chd("先天性心脏病"),
	hbf("既往体健"),
	nmh("病史不详"),;

	private String text;

	private MedicalHistoryEnum(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}
}
