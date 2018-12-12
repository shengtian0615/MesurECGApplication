/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Jan 19, 2015
 */

package com.wehealth.model.domain.model;

public class Company {
	private Long id;
	private String companyName;
	private double balance;
	private String bankName;
	private String bankAccountId;
	private String bankAccountName;
	private double rateDoctorDiagnosis;
	private double rateManualDiagnosis;
	private double priceOfAutoDiagnosis;
	private double priceOfManulDiagnosis;
	private double priceDoctorDiagnosis;
	private double taxRate;
	private PublicFundAccount pubAccount;
	private int doctorIncomeSettleType;

	public Company() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(String bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public String getBankAccountName() {
		return bankAccountName;
	}

	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}

	public double getRateDoctorDiagnosis() {
		return rateDoctorDiagnosis;
	}

	public void setRateDoctorDiagnosis(double rateDoctorDiagnosis) {
		this.rateDoctorDiagnosis = rateDoctorDiagnosis;
	}

	public double getRateManualDiagnosis() {
		return rateManualDiagnosis;
	}

	public void setRateManualDiagnosis(double rateManualDiagnosis) {
		this.rateManualDiagnosis = rateManualDiagnosis;
	}

	public double getPriceOfAutoDiagnosis() {
		return priceOfAutoDiagnosis;
	}

	public void setPriceOfAutoDiagnosis(double priceOfAutoDiagnosis) {
		this.priceOfAutoDiagnosis = priceOfAutoDiagnosis;
	}

	public double getPriceOfManulDiagnosis() {
		return priceOfManulDiagnosis;
	}

	public void setPriceOfManulDiagnosis(double priceOfManulDiagnosis) {
		this.priceOfManulDiagnosis = priceOfManulDiagnosis;
	}

	public double getPriceDoctorDiagnosis() {
		return priceDoctorDiagnosis;
	}

	public void setPriceDoctorDiagnosis(double priceDoctorDiagnosis) {
		this.priceDoctorDiagnosis = priceDoctorDiagnosis;
	}

	public double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}

	public PublicFundAccount getPubAccount() {
		return pubAccount;
	}

	public void setPubAccount(PublicFundAccount pubAccount) {
		this.pubAccount = pubAccount;
	}

	public int getDoctorIncomeSettleType() {
		return doctorIncomeSettleType;
	}

	public void setDoctorIncomeSettleType(int doctorIncomeSettleType) {
		this.doctorIncomeSettleType = doctorIncomeSettleType;
	}

	
}
