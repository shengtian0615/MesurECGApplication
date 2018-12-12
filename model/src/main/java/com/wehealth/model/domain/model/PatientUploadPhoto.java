/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Aug 28, 2015
 */

package com.wehealth.model.domain.model;

import java.util.Date;

public class PatientUploadPhoto extends AbstractPhoto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long patientId;
	private String description;
	private byte[] photo;

	public PatientUploadPhoto() {
	}

	public PatientUploadPhoto(Long id, Long patientId, Date createTime,
                              String description) {
		setId(id);
		this.patientId = patientId;
		setCreateTime(createTime);
		this.description = description;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

}
