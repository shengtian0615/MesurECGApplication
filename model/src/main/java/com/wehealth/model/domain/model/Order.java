/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 */

package com.wehealth.model.domain.model;

import java.io.Serializable;
import java.util.Date;

public class Order implements Comparable<Order>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date creationTime;
	private String registeredUserId;
	private String patientId;
	private String doctorId;
	private double price;
	private int status;
	private String userName;//patient name
	private String address;//patient 地址
	private String note;
	private String cellPhone;//reg user cellphone
	private String doctorName;
	private Date updateTime;
	private String evaluation;
	private boolean satisfy;
	
	private String doctorEasename;
	private String patientEasename;//reg user account
	private Long bonusId;
	
	public Order() {
		super();
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	
	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getRegisteredUserId() {
		return registeredUserId;
	}
	
	public void setRegisteredUserId(String registeredUserId) {
		this.registeredUserId = registeredUserId;
	}
	
	public String getPatientId() {
		return patientId;
	}
	
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	
	public String getDoctorId() {
		return doctorId;
	}
	
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public boolean equals(Object other) {
        if (this == other) return true;
        if ( !(other instanceof Order) ) return false;

        final Order cat = (Order) other;

        if ( !cat.getRegisteredUserId().equals( getRegisteredUserId() ) || 
        	 !cat.getDoctorId().equals( getDoctorId() ) ||
        	 (cat.getCreationTime().getTime() !=getCreationTime().getTime()))
        		 return false;
        return true;
    }

    @SuppressWarnings("deprecation")
	public int hashCode() {
        int result;
        result =getRegisteredUserId().hashCode() + getCreationTime().getDate() + getCreationTime().getHours() + getCreationTime().getMinutes();
        return result;
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

 	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public boolean isSatisfy() {
		return satisfy;
	}

	public void setSatisfy(boolean satisfy) {
		this.satisfy = satisfy;
	}

	public String getDoctorEasename()
	{
		return doctorEasename;
	}

	public void setDoctorEasename(String doctorEasename)
	{
		this.doctorEasename = doctorEasename;
	}

	public String getPatientEasename()
	{
		return patientEasename;
	}

	public void setPatientEasename(String patientEasename)
	{
		this.patientEasename = patientEasename;
	}

	public Long getBonusId()
	{
		return bonusId;
	}

	public void setBonusId(Long bonusId)
	{
		this.bonusId = bonusId;
	}
	
	@Override
	public int compareTo(Order another) {
		return another.getCreationTime().compareTo(this.getCreationTime());
	}

}
