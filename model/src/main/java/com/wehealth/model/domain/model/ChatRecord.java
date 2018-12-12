package com.wehealth.model.domain.model;

import java.io.Serializable;
import java.util.Date;

public class ChatRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String patientId;
	private String doctorId;
	
	private Date date;
	private String mainStatement;
	private String nowHistory;//现病史？--!
	private String advice;
	
	private Date createTime;
	private Date updateTime;
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getMainStatement() {
		return mainStatement;
	}
	public void setMainStatement(String mainStatement) {
		this.mainStatement = mainStatement;
	}
	public String getNowHistory() {
		return nowHistory;
	}
	public void setNowHistory(String nowHistory) {
		this.nowHistory = nowHistory;
	}
	public String getAdvice() {
		return advice;
	}
	public void setAdvice(String advice) {
		this.advice = advice;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
