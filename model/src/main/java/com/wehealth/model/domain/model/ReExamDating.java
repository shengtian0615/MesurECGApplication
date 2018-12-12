package com.wehealth.model.domain.model;

import java.io.Serializable;
import java.util.Date;

public class ReExamDating implements Serializable,Comparable<ReExamDating> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String patientId;
	private String doctorId;

	// 约定日期+时间
	private Date dateTime;

	// 记录时间
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public int compareTo(ReExamDating another) {
		return this.getDateTime().compareTo(another.getDateTime());
	}
}
