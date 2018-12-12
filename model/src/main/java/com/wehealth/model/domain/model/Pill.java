package com.wehealth.model.domain.model;

import java.io.Serializable;
import java.util.Date;

public class Pill implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String patientId;
	//药名
	private String name;
	
	//times per day: 每天吃几次
	private int timesPD;

	//amount: 每次几片
	private String amt;
	
	//使用一个变量记录多个时间，可以采用逗号分隔的形式
	//e.g: “0800,1200,1830”
	private String times;//

	//添加记录的时间，客户端不要更改
	private Date createTime;
	
	//是否开启了闹钟提醒
	private boolean enable;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTimesPD() {
		return timesPD;
	}

	public void setTimesPD(int timesPD) {
		this.timesPD = timesPD;
	}

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
}
