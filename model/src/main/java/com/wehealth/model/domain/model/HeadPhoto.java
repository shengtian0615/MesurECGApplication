/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Jan 11, 2017
 */

package com.wehealth.model.domain.model;

public class HeadPhoto extends AbstractPhoto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String photoId;
	private byte[] photo;

	@Override
	public byte[] getPhoto() {
		return photo;
	}

	@Override
	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getPhotoId() {
		return photoId;
	}

	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}

}
