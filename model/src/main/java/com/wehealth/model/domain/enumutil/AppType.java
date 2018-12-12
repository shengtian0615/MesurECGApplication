/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Jul 22, 2015
 */

package com.wehealth.model.domain.enumutil;

import com.wehealth.model.domain.interfaceutil.NamedObject;

public enum AppType implements NamedObject {
	doctorApp("安卓心电专家（医生）"), // android doctor app
	patientApp("安卓找心电专家（病人）"), // android patient app
	patientMgt("病人随访系统（医生）"), // patient management system
	patientMgtPst("病人随访系统（病人）"), // patient management system
	iosDoctorApp("IOS心电专家（医生"), // ios doctor app
	iosPatientApp("IOS找心电专家（病人）"), // ios patient
	deviceApp("安卓测心电（家用）"), // android ecg
	deviceAppCus("安卓测心电(商用）"), // android ecgcom
	iosDeviceApp("IOS测心电（家用）"), // ios ecg
	iosDeviceAppCus("IOS测心电(商用）"), // ios ecgcom
	hqxy("华清心仪"), // ecg plugin
	oss("运维系统"), // oss
	anHeartChatUser("安卓爱心管家(用户)"),
	iosHeartChatUser("IOS爱心管家(用户)"),
	anHeartChatDoc("安卓爱心医生(医生)"),
	iosHeartChatDoc("IOS爱心医生(医生)"),
	anHeartChatAssist("安卓爱心助手(医助)"),
	iosHeartChatAssist("IOS爱心助手(医助)"),
	anWehealthClinic("安卓五维心康（诊所）"),
	;

	private String text;

	private AppType(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}

	public static AppType getType(String type) {
		if (type == null || "".equals(type))
			return null;
		try {
			return AppType.valueOf(type);
		} catch (Exception ex) {
			return null;
		}
	}
}
