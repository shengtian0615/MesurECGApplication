/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Jan 19, 2015
 */

package com.wehealth.model.domain.model;

import com.wehealth.model.domain.enumutil.CommonStatus;

import java.util.Date;

public class PublicFundAccountTransaction implements Comparable<PublicFundAccountTransaction> {
	private Long id;
	private Date time;
	private String fromRegUser;
	private String fromBankName;
	private String fromBankAccountID;
	private String fromBankAccountName;
	private double amount;
	private int type;
	private double balance;
	private String note;
	private String toUserId;
	private String toBankName;
	private String toBankAccountID;
	private String toBankAccountName;
	private String orderId;
	private UserChargePath chargePath;
	
	/** Use: pending, completed, error */
	private CommonStatus status = CommonStatus.pending;
	
	public PublicFundAccountTransaction() {
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

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getFromRegUser() {
		return fromRegUser;
	}

	public void setFromRegUser(String fromRegUser) {
		this.fromRegUser = fromRegUser;
	}

	public String getFromBankName() {
		return fromBankName;
	}

	public void setFromBankName(String fromBankName) {
		this.fromBankName = fromBankName;
	}

	public String getFromBankAccountID() {
		return fromBankAccountID;
	}

	public void setFromBankAccountID(String fromBankAccountID) {
		this.fromBankAccountID = fromBankAccountID;
	}

	public String getFromBankAccountName() {
		return fromBankAccountName;
	}

	public void setFromBankAccountName(String fromBankAccountName) {
		this.fromBankAccountName = fromBankAccountName;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getToBankName() {
		return toBankName;
	}

	public void setToBankName(String toBankName) {
		this.toBankName = toBankName;
	}

	public String getToBankAccountID() {
		return toBankAccountID;
	}

	public void setToBankAccountID(String toBankAccountID) {
		this.toBankAccountID = toBankAccountID;
	}

	public String getToBankAccountName() {
		return toBankAccountName;
	}

	public void setToBankAccountName(String toBankAccountName) {
		this.toBankAccountName = toBankAccountName;
	}

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

	public UserChargePath getChargePath()
	{
		return chargePath;
	}

	public void setChargePath(UserChargePath chargePath)
	{
		this.chargePath = chargePath;
	}

	public CommonStatus getStatus()
	{
		return status;
	}

	public void setStatus(CommonStatus status)
	{
		this.status = status;
	}

	@Override
	public int compareTo(PublicFundAccountTransaction another) {
		return another.getTime().compareTo(this.getTime());
	}
}
