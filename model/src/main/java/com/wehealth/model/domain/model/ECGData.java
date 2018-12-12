/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Nov 6, 2014
 */

package com.wehealth.model.domain.model;

import java.io.Serializable;
import java.util.Date;

public class ECGData implements Comparable<ECGData>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String equipmentSerialNo;
	private Date time;
	private byte[] v1;
	private byte[] v2;
	private byte[] v3;
	private byte[] v4;
	private byte[] v5;
	private byte[] v6;
	private byte[] vI;
	private byte[] vII;
	private byte[] vIII;
	private byte[] aVr;
	private byte[] aVl;
	private byte[] aVf;

	private String autoDiagnosisResult;

	private String patientId;

	private String registeredUserId;

	private int requestedDiagnosisType;

	private String doctorId;

	private String manulDiagnosisResult;

	private double latitude;
	private double longitude;
	private String address;

	private int heartRate;

	private ECGFilter ecgFilter;

	private Date updateTime;

	private String doctorComment;
	// 症状，逗号分开
	private String symptoms;

	// FNotch: "OFF"
	private String fnotch;
	// FHP: 0.50Hz
	private String fhp;
	// FLP: 40Hz
	private String flp;
	// ms
	private int pr;
	// ms
	private int qrs;
	// QT/QTc: 358/412 ms
	private int qt;
	private int qtc;
	// P/PRS/T: 12/16/10 (degree)
	private int paxis;
	private int qrsaxis;
	private int taxis;
	// RV5/SV1: 1.189/-0.412 mV
	private double rv5;
	private double sv1;
	// RV5+SV1: 1.601 mV
	private double rv5sv1;

	// default 0, the new ecg is 1
	private int version = 0;
	// default null
	private Integer score;
	
	private String cellphone;
	
	private String agencyId;
	
	private String patiName;

	public ECGData() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getEquipmentSerialNo() {
		return equipmentSerialNo;
	}

	public void setEquipmentSerialNo(String equipmentSerialNo) {
		this.equipmentSerialNo = equipmentSerialNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ECGData other = (ECGData) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public byte[] getV1() {
		return v1;
	}

	public void setV1(byte[] v1) {
		this.v1 = v1;
	}

	public byte[] getV2() {
		return v2;
	}

	public void setV2(byte[] v2) {
		this.v2 = v2;
	}

	public byte[] getV3() {
		return v3;
	}

	public void setV3(byte[] v3) {
		this.v3 = v3;
	}

	public byte[] getV4() {
		return v4;
	}

	public void setV4(byte[] v4) {
		this.v4 = v4;
	}

	public byte[] getV5() {
		return v5;
	}

	public void setV5(byte[] v5) {
		this.v5 = v5;
	}

	public byte[] getV6() {
		return v6;
	}

	public void setV6(byte[] v6) {
		this.v6 = v6;
	}

	public byte[] getvI() {
		return vI;
	}

	public void setvI(byte[] vI) {
		this.vI = vI;
	}

	public byte[] getvII() {
		return vII;
	}

	public void setvII(byte[] vII) {
		this.vII = vII;
	}

	public byte[] getvIII() {
		return vIII;
	}

	public void setvIII(byte[] vIII) {
		this.vIII = vIII;
	}

	public byte[] getaVr() {
		return aVr;
	}

	public byte[] getaVl() {
		return aVl;
	}

	public void setaVr(byte[] aVr) {
		this.aVr = aVr;
	}

	public void setaVl(byte[] aVl) {
		this.aVl = aVl;
	}

	public byte[] getaVf() {
		return aVf;
	}

	public void setaVf(byte[] aVf) {
		this.aVf = aVf;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ECGData another) {
		return -this.time.compareTo(another.getTime());
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

	// public void afterUnmarshal(Unmarshaller u, Object parent) {
	// this.patient = (Patient) parent;
	// }

	public ECGFilter getEcgFilter() {
		return ecgFilter;
	}

	public void setEcgFilter(ECGFilter ecgFilter) {
		this.ecgFilter = ecgFilter;
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

	public String getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFnotch() {
		return fnotch;
	}

	public void setFnotch(String fnotch) {
		this.fnotch = fnotch;
	}

	public String getFhp() {
		return fhp;
	}

	public void setFhp(String fhp) {
		this.fhp = fhp;
	}

	public String getFlp() {
		return flp;
	}

	public void setFlp(String flp) {
		this.flp = flp;
	}

	public int getPr() {
		return pr;
	}

	public void setPr(int pr) {
		this.pr = pr;
	}

	public int getQrs() {
		return qrs;
	}

	public void setQrs(int qrs) {
		this.qrs = qrs;
	}

	public int getQt() {
		return qt;
	}

	public void setQt(int qt) {
		this.qt = qt;
	}

	public int getQtc() {
		return qtc;
	}

	public void setQtc(int qtc) {
		this.qtc = qtc;
	}

	public int getPaxis() {
		return paxis;
	}

	public void setPaxis(int paxis) {
		this.paxis = paxis;
	}

	public int getQrsaxis() {
		return qrsaxis;
	}

	public void setQrsaxis(int qrsaxis) {
		this.qrsaxis = qrsaxis;
	}

	public int getTaxis() {
		return taxis;
	}

	public void setTaxis(int taxis) {
		this.taxis = taxis;
	}

	public double getRv5() {
		return rv5;
	}

	public void setRv5(double rv5) {
		this.rv5 = rv5;
	}

	public double getSv1() {
		return sv1;
	}

	public void setSv1(double sv1) {
		this.sv1 = sv1;
	}

	public double getRv5sv1() {
		return rv5sv1;
	}

	public void setRv5sv1(double rv5sv1) {
		this.rv5sv1 = rv5sv1;
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

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getPatiName() {
		return patiName;
	}

	public void setPatiName(String patiName) {
		this.patiName = patiName;
	}

	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

}
