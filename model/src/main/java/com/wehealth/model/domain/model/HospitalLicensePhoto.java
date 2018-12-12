package com.wehealth.model.domain.model;

public class HospitalLicensePhoto extends AbstractPhoto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long hospitalId;
	private byte[] photo;
	
	public byte[] getPhoto() {
		return photo;
	}
	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	public Long getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}
}
