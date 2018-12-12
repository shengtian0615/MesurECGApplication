package com.wehealth.model.domain.model;

import java.io.Serializable;

public class HeartDoctorPassHelper implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HeartDoctor heart;
	private Doctor doctor;
	
	public HeartDoctor getHeart() {
		return heart;
	}
	public void setHeart(HeartDoctor heart) {
		this.heart = heart;
	}
	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
}
