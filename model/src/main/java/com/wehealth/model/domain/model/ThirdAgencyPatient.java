/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Nov 7, 2016
 */

package com.wehealth.model.domain.model;

public class ThirdAgencyPatient extends Patient {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tuid;
	// idCardNo store tpid, realIdCard store real IdCardNo.
	private String realIdCard;
	// commercial provided patient id
	private String pid;

	public String getTuid() {
		return tuid;
	}

	public void setTuid(String tuid) {
		this.tuid = tuid;
	}

	public String getRealIdCard() {
		return realIdCard;
	}

	public void setRealIdCard(String realIdCard) {
		this.realIdCard = realIdCard;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

}
