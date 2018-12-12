/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Nov 6, 2014
 */

package com.wehealth.model.domain.model;

import com.wehealth.model.domain.enumutil.BloodType;
import com.wehealth.model.domain.enumutil.Gender;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Patient extends Operator implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idCardNo;
	private String nickName;
	private Gender gender = Gender.male;
	private Date dateOfBirth;
	private String cellPhone;
	private ECGData initialECGData;
	private int height;
	private int weight;
	private int bloodPresssureHigh;
	private int bloodPresssureLow;
	private int heartBeatRate;
	private String registeredUserIdCardNo;
	private String ethnicity;
	private BloodType bloodType;
	private String medicalHistory;
	private String address;
	private String career;
	private ContactPerson emergencyContact;
	private ContactPerson emergencyContact1;

	private Set<String> secHospital = new HashSet<String>();
	
	public Patient() {
		super();
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public ECGData getInitialECGData() {
		return initialECGData;
	}

	public void setInitialECGData(ECGData initialECGData) {
		this.initialECGData = initialECGData;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getBloodPresssureHigh() {
		return bloodPresssureHigh;
	}

	public void setBloodPresssureHigh(int bloodPresssureHigh) {
		this.bloodPresssureHigh = bloodPresssureHigh;
	}

	public int getBloodPresssureLow() {
		return bloodPresssureLow;
	}

	public void setBloodPresssureLow(int bloodPresssureLow) {
		this.bloodPresssureLow = bloodPresssureLow;
	}

	public int getHeartBeatRate() {
		return heartBeatRate;
	}

	public void setHeartBeatRate(int heartBeatRate) {
		this.heartBeatRate = heartBeatRate;
	}

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof Patient))
			return false;

		final Patient cat = (Patient) other;

		if (!cat.getIdCardNo().equals(getIdCardNo()))
			return false;
		return true;
	}

	public int hashCode() {
		int result;
		result = getIdCardNo().hashCode();
		return result;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRegisteredUserIdCardNo() {
		return registeredUserIdCardNo;
	}

	public void setRegisteredUserIdCardNo(String registeredUserIdCardNo) {
		this.registeredUserIdCardNo = registeredUserIdCardNo;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	public BloodType getBloodType() {
		return bloodType;
	}

	public void setBloodType(BloodType bloodType) {
		this.bloodType = bloodType;
	}

	public String getMedicalHistory() {
		return medicalHistory;
	}

	public void setMedicalHistory(String medicalHistory) {
		this.medicalHistory = medicalHistory;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCareer() {
		return career;
	}

	public void setCareer(String career) {
		this.career = career;
	}

	public ContactPerson getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(ContactPerson emergencyContact) {
		this.emergencyContact = emergencyContact;
	}

	public ContactPerson getEmergencyContact1()
	{
		return emergencyContact1;
	}

	public void setEmergencyContact1(ContactPerson emergencyContact1)
	{
		this.emergencyContact1 = emergencyContact1;
	}

	public Set<String> getSecHospital()
	{
		return secHospital;
	}

	public void setSecHospital(Set<String> secHospital)
	{
		this.secHospital = secHospital;
	}
}
