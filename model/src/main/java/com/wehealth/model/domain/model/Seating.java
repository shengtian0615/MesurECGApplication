package com.wehealth.model.domain.model;

import java.io.Serializable;

public class Seating implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	//坐席名
	private String name;
	//环信ID
	private String easename;
	//环信密码
	private String easepwd;
	//最大管理数量
	private int maxSize;
	//当前管理数量
	private int currentSize;
	//备注
	private String note;
	//已上线，如果为true，医助不应该再使用此座席登录
	private boolean online;
	//使用此坐席的医助ID
	private String currentAssistant;
	//属于第三方机构时，保存机构ID
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
	public String getEasename() {
		return easename;
	}
	public void setEasename(String easename) {
		this.easename = easename;
	}
	public String getEasepwd() {
		return easepwd;
	}
	public void setEasepwd(String easepwd) {
		this.easepwd = easepwd;
	}
	public int getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	public int getCurrentSize() {
		return currentSize;
	}
	public void setCurrentSize(int currentSize) {
		this.currentSize = currentSize;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}
	public String getCurrentAssistant() {
		return currentAssistant;
	}
	public void setCurrentAssistant(String currentAssistant) {
		this.currentAssistant = currentAssistant;
	}
	public String getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}
}
