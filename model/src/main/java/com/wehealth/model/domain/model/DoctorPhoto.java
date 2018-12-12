/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Nov 6, 2014
 */
package com.wehealth.model.domain.model;

public class DoctorPhoto extends AbstractPhoto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idCardNo;
	private byte[] photo;

	public DoctorPhoto() {
		super();
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

}
