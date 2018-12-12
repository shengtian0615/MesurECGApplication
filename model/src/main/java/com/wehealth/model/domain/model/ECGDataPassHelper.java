/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Sep 24, 2015
 */

package com.wehealth.model.domain.model;

import java.io.Serializable;
import java.util.Date;

public class ECGDataPassHelper implements Serializable, Comparable<ECGDataPassHelper> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String equipmentSerialNo;
	private int requestedDiagnosisType;
	private String manulDiagnosisResult;
	private double latitude;
	private double longitude;
	private int heartRate;
	
	private Date time;
	private Date updateTime;
	
	private String autoDiagnosisResult;
	private String symptoms;
	private int version;
	private Integer score;
	
	private String patientId;
	private String patientName;
	
	private String registeredUserId;
	
	private String doctorId;
	private String doctorName;
	private String doctorComment;

	public ECGDataPassHelper() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEquipmentSerialNo() {
		return equipmentSerialNo;
	}

	public void setEquipmentSerialNo(String equipmentSerialNo) {
		this.equipmentSerialNo = equipmentSerialNo;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
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

	public int getRequestedDiagnosisType() {
		return requestedDiagnosisType;
	}

	public void setRequestedDiagnosisType(int requestedDiagnosisType) {
		this.requestedDiagnosisType = requestedDiagnosisType;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getManulDiagnosisResult() {
		return manulDiagnosisResult;
	}

	public void setManulDiagnosisResult(String manulDiagnosisResult) {
		this.manulDiagnosisResult = manulDiagnosisResult;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getDoctorComment() {
		return doctorComment;
	}

	public void setDoctorComment(String doctorComment) {
		this.doctorComment = doctorComment;
	}

	@Override
	public int compareTo(ECGDataPassHelper another) {
		return another.getTime().compareTo(getTime());
	}

	public String getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

}