package com.wehealth.model.domain.model;

import com.wehealth.model.domain.interfaceutil.Entity;

import java.io.Serializable;

public class Hospital extends AbstractFullyAuditableEntity implements Entity, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String province;
	private String city;
	private String address;
	// 0： 未知等级或其他等级， 33三甲，32三乙，31三丙，30三级，
	private int level;
	private String description;

	// whether have hospital admin.
	private boolean hasAdmin;
	private String licenseNo;// 诊所执照号
	private long clinicCode; //天奇诊所编号
	private String serial;// 天奇诊所编号
	private String inviteCode;// 诊所邀请码
	
	public Hospital() {
	}

	public Hospital(Long id, String name, String province, String city,
                    int level) {
		this.id = id;
		this.name = name;
		this.province = province;
		this.city = city;
		this.level = level;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 0： 未知等级或其他等级， 33三甲，32三乙，31三丙，30三级，
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * 0： 未知等级或其他等级， 33三甲，32三乙，31三丙，30三级，
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isHasAdmin() {
		return hasAdmin;
	}

	public void setHasAdmin(boolean hasAdmin) {
		this.hasAdmin = hasAdmin;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public long getClinicCode() {
		return clinicCode;
	}

	public void setClinicCode(long clinicCode) {
		this.clinicCode = clinicCode;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
}
