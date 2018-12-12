/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Jan 19, 2015
 */

package com.wehealth.model.domain.model;

import java.io.Serializable;
import java.util.Date;

public class PatientPayTransaction implements Serializable, Comparable<PatientPayTransaction> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date time;
	private String regUserName;
	private String regUserIDCard;
	private String patientName;
	private String patientIDCard;
	private Long orderId;
	private double amount;
	private int type;
	private double balance;
	private String note;
	private String paidTo;   // system Id 1.auto diagnosis
	                         // doctorId  2. manual diagnosis
	                         // doctorId  3. diagnosis
	                         // regUserIdCard 4. refund
	private String receiverName;

	public PatientPayTransaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRegUserIDCard() {
		return regUserIDCard;
	}

	public void setRegUserIDCard(String regUserIDCard) {
		this.regUserIDCard = regUserIDCard;
	}

	public String getPatientIDCard() {
		return patientIDCard;
	}

	public void setPatientIDCard(String patientIDCard) {
		this.patientIDCard = patientIDCard;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPaidTo() {
		return paidTo;
	}

	public void setPaidTo(String paidTo) {
		this.paidTo = paidTo;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getReceiverName()
	{
		return receiverName;
	}

	public void setReceiverName(String receiverName)
	{
		this.receiverName = receiverName;
	}

	public String getRegUserName()
	{
		return regUserName;
	}

	public void setRegUserName(String regUserName)
	{
		this.regUserName = regUserName;
	}

	public String getPatientName()
	{
		return patientName;
	}

	public void setPatientName(String patientName)
	{
		this.patientName = patientName;
	}
	
	@Override
	public int compareTo(PatientPayTransaction another) {
		
		return another.getTime().compareTo(this.getTime());
	}
}
