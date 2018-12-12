/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Jun 2, 2015
 */

package com.wehealth.model.domain.model;

public class DoctorLicensePhoto extends AbstractPhoto
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String licenseNo;
	private byte[] photo;

	public DoctorLicensePhoto()
	{
		super();
	}

	public String getLicenseNo()
	{
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo)
	{
		this.licenseNo = licenseNo;
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
