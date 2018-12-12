/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Aug 14, 2015
 */

package com.wehealth.model.domain.model;

import java.io.Serializable;

public class AuthToken implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String access_token;
	private long expires_in;
	private String refresh_token;

	public AuthToken() {
		
	}
	
	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

}
