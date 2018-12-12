/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 */

package com.wehealth.model.domain.model;

import java.io.Serializable;
import java.util.Date;

public class DoctorAccountTransaction implements Serializable,
        Comparable<DoctorAccountTransaction> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Date time;
	private String regName;
	private String fromRegUser;
	private String fromPatientId;
	private double amount;
	private double balance;
	private int type;
	private String doctorIdNo;
	private String note;
	private Long settleId;

	public DoctorAccountTransaction() {
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

	public String getRegName() {
		return regName;
	}

	public void setRegName(String regName) {
		this.regName = regName;
	}

	public String getFromRegUser() {
		return fromRegUser;
	}

	public void setFromRegUser(String fromRegUser) {
		this.fromRegUser = fromRegUser;
	}

	public String getFromPatientId() {
		return fromPatientId;
	}

	public void setFromPatientId(String fromPatientId) {
		this.fromPatientId = fromPatientId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDoctorIdNo() {
		return doctorIdNo;
	}

	public void setDoctorIdNo(String doctorIdNo) {
		this.doctorIdNo = doctorIdNo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getSettleId() {
		return settleId;
	}

	public void setSettleId(Long settleId) {
		this.settleId = settleId;
	}

	@Override
	public int compareTo(DoctorAccountTransaction another) {
		return another.getTime().compareTo(this.getTime());
	}

}
