package com.wehealth.model.domain.model;

import com.wehealth.model.domain.enumutil.DoctorTitle;

import java.io.Serializable;

public class HeartDoctorHelper implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		//医生必填信息
		private String name;
		private String idCardNo;
		private String cellPhone;

		//医生选填信息
		private String wechat;//微信，应该是选填
		private DoctorTitle title;
		private String position;// 职位
		private String department;// 部门
		private String fields;// 领域
		private String biography;// 简历

		//诊所信息
		private String serial;// 诊所编号，必填
		private String hospital;// 诊所名，必填
		private String address;// 必填：所有级别组合而成
		private String province;// 一级(省、执辖市、自治区、特别行政区)
		private String city;// 二级（市，区）
		
		private String inviteCode;// 诊所邀请码，这个版本不需要。
		private Long hospitalId;//Hospital.id
		private String hospitalLicense;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getIdCardNo() {
			return idCardNo;
		}
		public void setIdCardNo(String idCardNo) {
			this.idCardNo = idCardNo;
		}
		public String getCellPhone() {
			return cellPhone;
		}
		public void setCellPhone(String cellPhone) {
			this.cellPhone = cellPhone;
		}
		public String getWechat() {
			return wechat;
		}
		public void setWechat(String wechat) {
			this.wechat = wechat;
		}
		public DoctorTitle getTitle() {
			return title;
		}
		public void setTitle(DoctorTitle title) {
			this.title = title;
		}
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
		public String getDepartment() {
			return department;
		}
		public void setDepartment(String department) {
			this.department = department;
		}
		public String getFields() {
			return fields;
		}
		public void setFields(String fields) {
			this.fields = fields;
		}
		public String getBiography() {
			return biography;
		}
		public void setBiography(String biography) {
			this.biography = biography;
		}
		public String getSerial() {
			return serial;
		}
		public void setSerial(String serial) {
			this.serial = serial;
		}
		public String getHospital() {
			return hospital;
		}
		public void setHospital(String hospital) {
			this.hospital = hospital;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
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
		public String getInviteCode() {
			return inviteCode;
		}
		public void setInviteCode(String inviteCode) {
			this.inviteCode = inviteCode;
		}
		public Long getHospitalId() {
			return hospitalId;
		}
		public void setHospitalId(Long hospitalId) {
			this.hospitalId = hospitalId;
		}
		public String getHospitalLicense() {
			return hospitalLicense;
		}
		public void setHospitalLicense(String hospitalLicense) {
			this.hospitalLicense = hospitalLicense;
		}
}
