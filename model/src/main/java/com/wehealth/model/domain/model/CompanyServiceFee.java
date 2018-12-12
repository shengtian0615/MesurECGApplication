/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Jan 22, 2015
 */

package com.wehealth.model.domain.model;

public class CompanyServiceFee {
	private double priceOfAutoDiagnosis;
	private double priceOfManulDiagnosis;
	private double priceDoctorDiagnosis;
	private double taxRate;

	public CompanyServiceFee() {
		super();
		// TODO Auto-generated constructor stub
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
	
}
