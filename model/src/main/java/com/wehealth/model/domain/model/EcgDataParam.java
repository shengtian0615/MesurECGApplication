package com.wehealth.model.domain.model;

import java.util.Date;

public class EcgDataParam {

	private String AnalysisState;
	private String PWidth;
	private String PExist;
	private String RRInterval;
	private String HeartRate;
	private String PRInterval;
	private String QRSDuration;
	private String QTD;
	private String QTC;
	private String PAxis;
	private String QRSAxis;
	private String TAxis;
	private String RV5SV1;
	private String RV5;
	private String SV1;
	private String FNotch;
	private String FHP;
	private String FLP;
	private String WaveQuality;
	private String autoDiagnosisResult;
	private String gender;
	private String Age;
	private String name;
	private Date tesTime;

	private short[] i;
	private short[] ii;
	private short[] iii;
	private short[] avR;
	private short[] avL;
	private short[] avF;
	private short[] v1;
	private short[] v2;
	private short[] v3;
	private short[] v4;
	private short[] v5;
	private short[] v6;
	
	public String getAnalysisState() {
		return AnalysisState;
	}
	public void setAnalysisState(String analysisState) {
		AnalysisState = analysisState;
	}
	public String getPWidth() {
		return PWidth;
	}
	public void setPWidth(String pWidth) {
		PWidth = pWidth;
	}
	public String getPExist() {
		return PExist;
	}
	public void setPExist(String pExist) {
		PExist = pExist;
	}
	public String getRRInterval() {
		return RRInterval;
	}
	public void setRRInterval(String rRInterval) {
		RRInterval = rRInterval;
	}
	public String getHeartRate() {
		return HeartRate;
	}
	public void setHeartRate(String heartRate) {
		HeartRate = heartRate;
	}
	public String getPRInterval() {
		return PRInterval;
	}
	public void setPRInterval(String pRInterval) {
		PRInterval = pRInterval;
	}
	public String getQRSDuration() {
		return QRSDuration;
	}
	public void setQRSDuration(String qRSDuration) {
		QRSDuration = qRSDuration;
	}
	public String getQTD() {
		return QTD;
	}
	public void setQTD(String qTD) {
		QTD = qTD;
	}
	public String getQTC() {
		return QTC;
	}
	public void setQTC(String qTC) {
		QTC = qTC;
	}
	public String getPAxis() {
		return PAxis;
	}
	public void setPAxis(String pAxis) {
		PAxis = pAxis;
	}
	public String getQRSAxis() {
		return QRSAxis;
	}
	public void setQRSAxis(String qRSAxis) {
		QRSAxis = qRSAxis;
	}
	public String getTAxis() {
		return TAxis;
	}
	public void setTAxis(String tAxis) {
		TAxis = tAxis;
	}
	public String getRV5SV1() {
		return RV5SV1;
	}
	public void setRV5SV1(String rV5SV1) {
		RV5SV1 = rV5SV1;
	}
	public String getRV5() {
		return RV5;
	}
	public void setRV5(String rV5) {
		RV5 = rV5;
	}
	public String getSV1() {
		return SV1;
	}
	public void setSV1(String sV1) {
		SV1 = sV1;
	}
	public String getFHP() {
		return FHP;
	}
	public void setFHP(String fHP) {
		FHP = fHP;
	}
	public String getFLP() {
		return FLP;
	}
	public void setFLP(String fLP) {
		FLP = fLP;
	}
	public String getWaveQuality() {
		return WaveQuality;
	}
	public void setWaveQuality(String waveQuality) {
		WaveQuality = waveQuality;
	}
	public String getAutoDiagnosisResult() {
		return autoDiagnosisResult;
	}
	public void setAutoDiagnosisResult(String autoDiagnosisResult) {
		this.autoDiagnosisResult = autoDiagnosisResult;
	}
	public Date getTesTime() {
		return tesTime;
	}
	public void setTesTime(Date tesTime) {
		this.tesTime = tesTime;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAge() {
		return Age;
	}
	public void setAge(String age) {
		Age = age;
	}
	public short[] getI() {
		return i;
	}
	public void setI(short[] i) {
		this.i = i;
	}
	public short[] getIi() {
		return ii;
	}
	public void setIi(short[] ii) {
		this.ii = ii;
	}
	public short[] getIii() {
		return iii;
	}
	public void setIii(short[] iii) {
		this.iii = iii;
	}
	public short[] getAvR() {
		return avR;
	}
	public void setAvR(short[] avR) {
		this.avR = avR;
	}
	public short[] getAvL() {
		return avL;
	}
	public void setAvL(short[] avL) {
		this.avL = avL;
	}
	public short[] getAvF() {
		return avF;
	}
	public void setAvF(short[] avF) {
		this.avF = avF;
	}
	public short[] getV1() {
		return v1;
	}
	public void setV1(short[] v1) {
		this.v1 = v1;
	}
	public short[] getV2() {
		return v2;
	}
	public void setV2(short[] v2) {
		this.v2 = v2;
	}
	public short[] getV3() {
		return v3;
	}
	public void setV3(short[] v3) {
		this.v3 = v3;
	}
	public short[] getV4() {
		return v4;
	}
	public void setV4(short[] v4) {
		this.v4 = v4;
	}
	public short[] getV5() {
		return v5;
	}
	public void setV5(short[] v5) {
		this.v5 = v5;
	}
	public short[] getV6() {
		return v6;
	}
	public void setV6(short[] v6) {
		this.v6 = v6;
	}
	public String getFNotch() {
		return FNotch;
	}
	public void setFNotch(String fNotch) {
		FNotch = fNotch;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
