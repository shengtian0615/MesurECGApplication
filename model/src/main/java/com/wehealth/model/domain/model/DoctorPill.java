package com.wehealth.model.domain.model;

import java.io.Serializable;
import java.util.Date;

public class DoctorPill implements Serializable, Comparable<DoctorPill> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String patientId;
	private String doctorId;
	
	//药名
	private String name;
	//数量
	private int amount;
	//金额：单位分
	private int charge;
	//下次用药提醒日期: 新建/更新这个时间需要大于当前时间
	private Date nextDate;
	//是否短信提醒
	private boolean sendSms;
	
	//记录时间
	private Date createTime;
	//更新记录时间
	private Date updateTime;
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getUpdateTime() {
		return updateTime;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getCharge() {
		return charge;
	}
	public void setCharge(int charge) {
		this.charge = charge;
	}
	public Date getNextDate() {
		return nextDate;
	}
	public void setNextDate(Date nextDate) {
		this.nextDate = nextDate;
	}
	public boolean isSendSms() {
		return sendSms;
	}
	public void setSendSms(boolean sendSms) {
		this.sendSms = sendSms;
	}
	@Override
	public int compareTo(DoctorPill another) {
		return another.getCreateTime().compareTo(getCreateTime());
	}
}
