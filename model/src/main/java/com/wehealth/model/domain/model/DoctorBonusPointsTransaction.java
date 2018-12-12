package com.wehealth.model.domain.model;

import com.wehealth.model.domain.enumutil.CommonStatus;
import com.wehealth.model.domain.enumutil.DoctorBonusPointsTransactionType;

import java.io.Serializable;
import java.util.Date;

public class DoctorBonusPointsTransaction implements Serializable, Comparable<DoctorBonusPointsTransaction> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date time;
	private int amount;//变化数量
	private int balance;//余额
	private DoctorBonusPointsTransactionType type;//记录类型
	private String doctorIdNo;
	private String doctorName;
	private String note;
	/** Use: pending, completed, error */
	private CommonStatus status = CommonStatus.pending;
	
	private String orderId;

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

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public DoctorBonusPointsTransactionType getType() {
		return type;
	}

	public void setType(DoctorBonusPointsTransactionType type) {
		this.type = type;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
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

	public CommonStatus getStatus() {
		return status;
	}

	public void setStatus(CommonStatus status) {
		this.status = status;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public int compareTo(DoctorBonusPointsTransaction another) {
		return -this.time.compareTo(another.getTime());
	}
}
