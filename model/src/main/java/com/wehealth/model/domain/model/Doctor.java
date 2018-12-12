/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Nov 6, 2014
 */
package com.wehealth.model.domain.model;

import com.wehealth.model.domain.enumutil.DoctorTitle;
import com.wehealth.model.domain.enumutil.Gender;

import java.util.Date;

public class Doctor extends Operator{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idCardNo;
	private Gender gender;
	private Date dateOfBirth;
	private String department;
	private String hospital;
	private Long hospitalId;
	private DoctorTitle title;
	private String position;
	private String degree;
	private String biography;
	private String bankName;
	private String bankAccountId;
	private String bankAccountName;
	private String licenseNo;
	private String cellPhone;
	private String easemobUserName;
	private String easemobPassword;
	private String email;
	private String address;
	private int commentsCount;
	private int satisfiedCount;
	private int totalDiagnoseCount;
	private String fields;
	
	private boolean proved;
	private double balance;
	private String bindedDeviceSN; //绑定设备序列号
	private int bonusPoints; // 累计积分
	
	public Doctor() {
		super();
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public Gender getGender()
	{
		return gender;
	}

	public void setGender(Gender gender)
	{
		this.gender = gender;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public Long getHospitalId()
	{
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId)
	{
		this.hospitalId = hospitalId;
	}

	public DoctorTitle getTitle() {
		return title;
	}

	public void setTitle(DoctorTitle title) {
		this.title = title;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
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

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getEasemobUserName() {
		return easemobUserName;
	}

	public void setEasemobUserName(String easemobUserName) {
		this.easemobUserName = easemobUserName;
	}

	public String getEasemobPassword() {
		return easemobPassword;
	}

	public void setEasemobPassword(String easemobPassword) {
		this.easemobPassword = easemobPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean equals(Object other) {
        if (this == other) return true;
        if ( !(other instanceof Doctor) ) return false;

        final Doctor cat = (Doctor) other;

        if ( !cat.getIdCardNo().equals( getIdCardNo() ) ) return false;
        return true;
    }

    public int hashCode() {
        int result;
        result =getIdCardNo().hashCode();
        return result;
    }

	public int getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}

	public int getSatisfiedCount() {
		return satisfiedCount;
	}

	public void setSatisfiedCount(int satisfiedCount) {
		this.satisfiedCount = satisfiedCount;
	}

	public int getTotalDiagnoseCount() {
		return totalDiagnoseCount;
	}

	public void setTotalDiagnoseCount(int totalDiagnoseCount) {
		this.totalDiagnoseCount = totalDiagnoseCount;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public boolean isProved()
	{
		return proved;
	}

	public void setProved(boolean proved)
	{
		this.proved = proved;
	}

	public double getBalance()
	{
		return balance;
	}

	public void setBalance(double balance)
	{
		this.balance = balance;
	}

	public String getBindedDeviceSN() {
		return bindedDeviceSN;
	}

	public void setBindedDeviceSN(String bindedDeviceSN) {
		this.bindedDeviceSN = bindedDeviceSN;
	}

	public int getBonusPoints() {
		return bonusPoints;
	}

	public void setBonusPoints(int bonusPoints) {
		this.bonusPoints = bonusPoints;
	}
}
