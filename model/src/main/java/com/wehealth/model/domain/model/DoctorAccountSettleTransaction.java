/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Jan 19, 2015
 */

package com.wehealth.model.domain.model;

import com.wehealth.model.domain.enumutil.DoctorAccountSettleTransactionType;
import com.wehealth.model.domain.enumutil.DoctorAccountSettleType;

import java.io.Serializable;
import java.util.Date;

public class DoctorAccountSettleTransaction implements Serializable, Comparable<DoctorAccountSettleTransaction> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date creationTime;
	private double amount;
	private DoctorAccountSettleTransactionType type;
	private String toBankName;
	private String toAccountId;
	private String toAccountName;
	private String doctorIdNo;
	private String note;
	private double taxRate;
	private Date updateTime;
	private DoctorAccountSettleType settleType;  //monthly, weekly

	public DoctorAccountSettleTransaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public DoctorAccountSettleTransactionType getType() {
		return type;
	}

	public void setType(DoctorAccountSettleTransactionType type) {
		this.type = type;
	}

	public String getToAccountId() {
		return toAccountId;
	}

	public void setToAccountId(String toAccountId) {
		this.toAccountId = toAccountId;
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

	public double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}

	public String getToBankName() {
		return toBankName;
	}

	public void setToBankName(String toBankName) {
		this.toBankName = toBankName;
	}

	public String getToAccountName() {
		return toAccountName;
	}

	public void setToAccountName(String toAccountName) {
		this.toAccountName = toAccountName;
	}

	public DoctorAccountSettleType getSettleType() {
		return settleType;
	}

	public void setSettleType(DoctorAccountSettleType settleType) {
		this.settleType = settleType;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Override
	public int compareTo(DoctorAccountSettleTransaction another) {
		return another.getUpdateTime().compareTo(getUpdateTime());
	}
}
