/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Dec 17, 2014
 */

package com.wehealth.model.domain.model;

import com.wehealth.model.domain.enumutil.NotificationMesageStatus;
import com.wehealth.model.domain.enumutil.NotifyDoctorAskStatus;

import java.io.Serializable;

public class AppNotificationMessage implements Comparable<AppNotificationMessage>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//时间
	private long time;
	//添加理由
	private String message;
	//测试时间
	private long testTime;
	
	private int id;
	
	private String msgId;
	
	private NotificationMesageStatus status;
	
	private String subject;
	
	private NotifyDoctorAskStatus askStatus;
	
	private String msgDoctorIdCardNo;
	
	private String msgRegisterIdCardNo;
	
	private String msgPatientIdCardNo;
	
	private String msgEaseMobString;
	
	private String msgLevel;
	
	private String msgOther;
	
	private String msgHospital;
	
	private String msgComment;
	
	private String doctorName;
	
	public AppNotificationMessage() {
		super();
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getTestTime() {
		return testTime;
	}

	public void setTestTime(long testTime) {
		this.testTime = testTime;
	}

	public NotificationMesageStatus getStatus() {
		return status;
	}

	public void setStatus(NotificationMesageStatus status) {
		this.status = status;
	}

	public NotifyDoctorAskStatus getAskStatus() {
		return askStatus;
	}

	public void setAskStatus(NotifyDoctorAskStatus askStatus) {
		this.askStatus = askStatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMsgDoctorIdCardNo() {
		return msgDoctorIdCardNo;
	}

	public void setMsgDoctorIdCardNo(String msgDoctorIdCardNo) {
		this.msgDoctorIdCardNo = msgDoctorIdCardNo;
	}

	public String getMsgRegisterIdCardNo() {
		return msgRegisterIdCardNo;
	}

	public void setMsgRegisterIdCardNo(String msgRegisterIdCardNo) {
		this.msgRegisterIdCardNo = msgRegisterIdCardNo;
	}

	public String getMsgPatientIdCardNo() {
		return msgPatientIdCardNo;
	}

	public void setMsgPatientIdCardNo(String msgPatientIdCardNo) {
		this.msgPatientIdCardNo = msgPatientIdCardNo;
	}
	
	public String getMsgEaseMobString() {
		return msgEaseMobString;
	}

	public void setMsgEaseMobString(String msgEaseMobString) {
		this.msgEaseMobString = msgEaseMobString;
	}

	public String getMsgLevel() {
		return msgLevel;
	}

	public void setMsgLevel(String msgLevel) {
		this.msgLevel = msgLevel;
	}

	public String getMsgOther() {
		return msgOther;
	}

	public void setMsgOther(String msgOther) {
		this.msgOther = msgOther;
	}
	
	@Override
	public int compareTo(AppNotificationMessage another) {
		return ((Long)another.getTime()).compareTo(((Long)getTime()));
	}

	public String getMsgHospital() {
		return msgHospital;
	}

	public void setMsgHospital(String msgHospital) {
		this.msgHospital = msgHospital;
	}

	public String getMsgComment() {
		return msgComment;
	}

	public void setMsgComment(String msgComment) {
		this.msgComment = msgComment;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
}