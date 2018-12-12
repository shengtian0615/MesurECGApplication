/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Aug 21, 2015
 */

package com.wehealth.model.domain.model;

import java.util.Date;

public class ECGPhoto extends AbstractPhoto
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long patientId;
	
	private Date diagnoseTime;

	private byte[] photo;
	
	private String description;
	
	/**0:pciReport, 1: ecgPhotoSearchUI */
	private int createPlace;
	
	public ECGPhoto()
	{
	}
	
	public ECGPhoto(Long id, Long patientId, Date diagnoseTime, Date createTime,
                    String description, int createPlace, Long version)
	{
		setId(id);
		setCreateTime(createTime);
		setVersion(version);
		this.patientId = patientId;
		this.diagnoseTime = diagnoseTime;
		this.description = description;
		this.createPlace = createPlace;
	}
	
	public Long getPatientId()
	{
		return patientId;
	}

	public void setPatientId(Long patientId)
	{
		this.patientId = patientId;
	}

	public Date getDiagnoseTime()
	{
		return diagnoseTime;
	}

	public void setDiagnoseTime(Date diagnoseTime)
	{
		this.diagnoseTime = diagnoseTime;
	}

	public byte[] getPhoto()
	{
		return photo;
	}

	public void setPhoto(byte[] photo)
	{
		this.photo = photo;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	/**0:pciReport, 1: ecgPhotoSearchUI */
	public int getCreatePlace()
	{
		return createPlace;
	}
	/**0:pciReport, 1: ecgPhotoSearchUI */
	public void setCreatePlace(int createPlace)
	{
		this.createPlace = createPlace;
	}
	
}
