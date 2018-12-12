package com.wehealth.model.domain.model;

import java.io.Serializable;
import java.util.List;

public class DoctorPackagePassHelper implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DoctorPackage pac;
	private List<DoctorPacItem> items;
	
	public DoctorPackage getPac() {
		return pac;
	}
	public void setPac(DoctorPackage pac) {
		this.pac = pac;
	}
	public List<DoctorPacItem> getItems() {
		return items;
	}
	public void setItems(List<DoctorPacItem> items) {
		this.items = items;
	}
}
