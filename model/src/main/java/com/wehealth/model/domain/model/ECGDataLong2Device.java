package com.wehealth.model.domain.model;

import java.io.Serializable;

public class ECGDataLong2Device implements Serializable, Comparable<ECGDataLong2Device>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int ecgdata_id;
	private long time;
	private String autoDiagnosisResult;
	private String patientId;
	private String registeredUserId;
	private int heartRate;
	private int totalTime;
	private String V1_path;
	private String V2_path;
	private String V3_path;
	private String V4_path;
	private String V5_path;
	private String V6_path;
	private String I_path;
	private String II_path;
	private String III_path;
	private String AVR_path;
	private String AVL_path;
	private String AVF_path;
	private String h24_path;
	private int saveFileType;
	private int h24_leadName;

	public int getEcgdata_id() {
		return ecgdata_id;
	}
	public void setEcgdata_id(int ecgdata_id) {
		this.ecgdata_id = ecgdata_id;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getAutoDiagnosisResult() {
		return autoDiagnosisResult;
	}
	public void setAutoDiagnosisResult(String autoDiagnosisResult) {
		this.autoDiagnosisResult = autoDiagnosisResult;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getRegisteredUserId() {
		return registeredUserId;
	}
	public void setRegisteredUserId(String registeredUserId) {
		this.registeredUserId = registeredUserId;
	}
	public int getHeartRate() {
		return heartRate;
	}
	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}
	public int getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}
	public String getV1_path() {
		return V1_path;
	}
	public void setV1_path(String v1_path) {
		V1_path = v1_path;
	}
	public String getV2_path() {
		return V2_path;
	}
	public void setV2_path(String v2_path) {
		V2_path = v2_path;
	}
	public String getV3_path() {
		return V3_path;
	}
	public void setV3_path(String v3_path) {
		V3_path = v3_path;
	}
	public String getV4_path() {
		return V4_path;
	}
	public void setV4_path(String v4_path) {
		V4_path = v4_path;
	}
	public String getV5_path() {
		return V5_path;
	}
	public void setV5_path(String v5_path) {
		V5_path = v5_path;
	}
	public String getV6_path() {
		return V6_path;
	}
	public void setV6_path(String v6_path) {
		V6_path = v6_path;
	}
	public String getI_path() {
		return I_path;
	}
	public void setI_path(String i_path) {
		I_path = i_path;
	}
	public String getII_path() {
		return II_path;
	}
	public void setII_path(String iI_path) {
		II_path = iI_path;
	}
	public String getIII_path() {
		return III_path;
	}
	public void setIII_path(String iII_path) {
		III_path = iII_path;
	}
	public String getAVR_path() {
		return AVR_path;
	}
	public void setAVR_path(String aVR_path) {
		AVR_path = aVR_path;
	}
	public String getAVL_path() {
		return AVL_path;
	}
	public void setAVL_path(String aVL_path) {
		AVL_path = aVL_path;
	}
	public String getAVF_path() {
		return AVF_path;
	}
	public void setAVF_path(String aVF_path) {
		AVF_path = aVF_path;
	}
	public int getSaveFileType() {
		return saveFileType;
	}
	public void setSaveFileType(int saveFileType) {
		this.saveFileType = saveFileType;
	}
	public String getH24_path() {
		return h24_path;
	}
	public void setH24_path(String h24_path) {
		this.h24_path = h24_path;
	}
	public int getH24_leadName() {
		return h24_leadName;
	}
	public void setH24_leadName(int h24_leadName) {
		this.h24_leadName = h24_leadName;
	}
	@Override
	public int compareTo(ECGDataLong2Device another) {
		return -((Long)this.time).compareTo((Long)another.getTime());
	}
}
