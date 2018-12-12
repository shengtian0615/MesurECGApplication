package com.wehealth.model.domain.model;

import com.wehealth.model.domain.enumutil.Gender;

import java.io.Serializable;
import java.util.Date;

public class DoctorAssistant implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	//required
	private String name;
	//required, unchange
	private String idCardNo;
	//required, unchange
	private String cellPhone;
	
	private String username;
	//required, 前端传来的md5一次，后台会添加seed再md5一次
	private String password;
	//从IDCard抽取
	private Date birthday;
	//从IDCard抽取
	private Gender gender;
	
	//公司/工作单位
	private String company;
	//擅长、领域
	private String fields;
	//备注、介绍 ...
	private String note;
	//第三方机构ID，如果不为空，获取坐席时需要传值
	private String agencyId;
	
	
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getFields() {
		return fields;
	}
	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}
}
