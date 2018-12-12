/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Nov 6, 2014
 */

package com.wehealth.model.domain.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CaseHistory {

	private Long id;
	private Date time;
	private String diseaseName;
	private Set<ECGData> ecgData = new HashSet<ECGData>();
	private String diagnosisResult;
	private String patientId;
	private String doctorId;
	
	public CaseHistory() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getDiseaseName() {
		return diseaseName;
	}

	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}

	public Set<ECGData> getEcgData() {
		return ecgData;
	}

	public void setEcgData(Set<ECGData> ecgData) {
		this.ecgData = ecgData;
	}

	public String getDiagnosisResult() {
		return diagnosisResult;
	}

	public void setDiagnosisResult(String diagnosisResult) {
		this.diagnosisResult = diagnosisResult;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	
}
