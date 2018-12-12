/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun May 26, 2015
 */

package com.wehealth.model.domain.model;

public class PatientPhoto extends AbstractPhoto
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idCardNo;
	private byte[] photo;

	public PatientPhoto()
	{
		super();
	}

	public String getIdCardNo()
	{
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo)
	{
		this.idCardNo = idCardNo;
	}

	public byte[] getPhoto()
	{
		return photo;
	}

	public void setPhoto(byte[] photo)
	{
		this.photo = photo;
	}

}
