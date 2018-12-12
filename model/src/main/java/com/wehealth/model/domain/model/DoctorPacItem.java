package com.wehealth.model.domain.model;

import java.io.Serializable;

public class DoctorPacItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long pacId;//套餐ID
	private String name;//项目名
	private int times;//套餐有效期内项目的总次数，-1表示不计次数，不能为0。
	private String note;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPacId() {
		return pacId;
	}
	public void setPacId(Long pacId) {
		this.pacId = pacId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
