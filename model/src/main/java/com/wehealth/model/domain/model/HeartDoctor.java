package com.wehealth.model.domain.model;

import java.io.Serializable;

public class HeartDoctor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long docId;//关联Doctor.id
	//是否有免费套餐
	private boolean hasFreePac;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDocId() {
		return docId;
	}
	public void setDocId(Long docId) {
		this.docId = docId;
	}
	public boolean isHasFreePac() {
		return hasFreePac;
	}
	public void setHasFreePac(boolean hasFreePac) {
		this.hasFreePac = hasFreePac;
	}
}
