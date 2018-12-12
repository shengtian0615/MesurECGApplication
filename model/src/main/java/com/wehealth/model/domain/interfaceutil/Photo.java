/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Aug 24, 2015
 */

package com.wehealth.model.domain.interfaceutil;

public interface Photo extends Entity, CreateTimeAuditable, UpdateVersion {
	byte[] getPhoto();

	void setPhoto(byte[] photo);
}
