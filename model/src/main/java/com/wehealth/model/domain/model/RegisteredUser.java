package com.wehealth.model.domain.model;

import com.wehealth.model.domain.enumutil.BloodType;
import com.wehealth.model.domain.enumutil.Gender;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class RegisteredUser extends Operator{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idCardNo;
	private Gender gender;
	private Date dateOfBirth;
	private String cellPhone;
	private ContactPerson emergencyContact;
	private ContactPerson emergencyContact1;
	private String easemobUserName;
	private String easemobPassword;
	private Set<String> preferDocs = new HashSet<String>();
	private String email;
	private String address;
	private Set<Patient> patients = new HashSet<Patient>();
	private double balance;
	private int height;
	private int weight;
	private BloodType bloodType;
	private String medicalHistory;
	private ECGDevice device;
	/**
	 * default:0, real user, 1:commercial user.
	 */
	private int regType;
	
	public RegisteredUser() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
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

	public Set<String> getPreferDocs() {
		return preferDocs;
	}

	public void setPreferDocs(Set<String> preferDocs) {
		this.preferDocs = preferDocs;
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

	public Set<Patient> getPatients() {
		return patients;
	}

	public void setPatients(Set<Patient> patients) {
		this.patients = patients;
	}
	
	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
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

	public ECGDevice getDevice()
	{
		return device;
	}

	public void setDevice(ECGDevice device)
	{
		this.device = device;
	}

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof RegisteredUser))
			return false;

		final RegisteredUser cat = (RegisteredUser) other;

		if (!cat.getIdCardNo().equals(getIdCardNo()))
			return false;
		return true;
	}

	public int hashCode() {
		int result;
		result = getIdCardNo().hashCode();
		return result;
	}

	/**
	 * default:0, real user, 1:commercial user.
	 */
	public int getRegType()
	{
		return regType;
	}

	/**
	 * default:0, real user, 1:commercial user.
	 */
	public void setRegType(int regType)
	{
		this.regType = regType;
	}

}
