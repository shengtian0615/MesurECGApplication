package com.wehealth.model.domain.model;

import java.io.Serializable;

public class ScannedUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String doctorId;// 医生ID

	private String name;
	private String idCardNo;// 被添加用户身份证
	private boolean gender;// 不需要，后台从身份证提取
	private Long birthday;// 不需要，后台从身份证提取
	private String phone;
	private int height;
	private int weight;
	private boolean hasAlergic;// 是否有过敏史
	private String alergicDetail;// 过敏史详细内容
	private String symtom;// 最近症状

	private String medicalHistory;// 既往病史：参考3.1.7
	private String chronicName;// 其它慢性病具体名称
	private String surgeryHistory;// 手术史：参考3.1.7
	private String otherSurgery;// 其它手术具体名称
	private String familyHistory;// 家族史：参考3.1.7
	private String otherFH;// 其它具体名称
	private int highBloodPressure;// 高压，舒张压
	private int lowBloodPressure;// 低压，收缩压
	private String bloodSugar;// 血糖
	public String getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
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
	public boolean isGender() {
		return gender;
	}
	public void setGender(boolean gender) {
		this.gender = gender;
	}
	public Long getBirthday() {
		return birthday;
	}
	public void setBirthday(Long birthday) {
		this.birthday = birthday;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public boolean isHasAlergic() {
		return hasAlergic;
	}
	public void setHasAlergic(boolean hasAlergic) {
		this.hasAlergic = hasAlergic;
	}
	public String getAlergicDetail() {
		return alergicDetail;
	}
	public void setAlergicDetail(String alergicDetail) {
		this.alergicDetail = alergicDetail;
	}
	public String getSymtom() {
		return symtom;
	}
	public void setSymtom(String symtom) {
		this.symtom = symtom;
	}
	public String getMedicalHistory() {
		return medicalHistory;
	}
	public void setMedicalHistory(String medicalHistory) {
		this.medicalHistory = medicalHistory;
	}
	public String getChronicName() {
		return chronicName;
	}
	public void setChronicName(String chronicName) {
		this.chronicName = chronicName;
	}
	public String getSurgeryHistory() {
		return surgeryHistory;
	}
	public void setSurgeryHistory(String surgeryHistory) {
		this.surgeryHistory = surgeryHistory;
	}
	public String getOtherSurgery() {
		return otherSurgery;
	}
	public void setOtherSurgery(String otherSurgery) {
		this.otherSurgery = otherSurgery;
	}
	public String getFamilyHistory() {
		return familyHistory;
	}
	public void setFamilyHistory(String familyHistory) {
		this.familyHistory = familyHistory;
	}
	public String getOtherFH() {
		return otherFH;
	}
	public void setOtherFH(String otherFH) {
		this.otherFH = otherFH;
	}
	public int getHighBloodPressure() {
		return highBloodPressure;
	}
	public void setHighBloodPressure(int highBloodPressure) {
		this.highBloodPressure = highBloodPressure;
	}
	public int getLowBloodPressure() {
		return lowBloodPressure;
	}
	public void setLowBloodPressure(int lowBloodPressure) {
		this.lowBloodPressure = lowBloodPressure;
	}
	public String getBloodSugar() {
		return bloodSugar;
	}
	public void setBloodSugar(String bloodSugar) {
		this.bloodSugar = bloodSugar;
	}
}
