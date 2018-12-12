/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Aug 30, 2016
 */

package com.wehealth.model.domain.model;


import com.wehealth.model.domain.interfaceutil.Photo;

import java.util.Date;

public abstract class AbstractPhoto implements Photo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date createTime;
	private Long version;

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public Date getCreateTime() {
		return createTime;
	}

	@Override
	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public Long getVersion() {
		return version;
	}

}
