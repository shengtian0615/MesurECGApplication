package com.wehealth.model.domain.model;

import java.io.Serializable;
import java.util.Date;

public class HeartChatGroup implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String groupName;//患者咨询室
	private String groupId;//环信群组id，如果为空表示用户还未选医生
	private String userCellPhone;  //注册用户CellPhone
	
	private String userId;//注册用户身份证
	private Long seatingId;//坐席ID
	private String doctorId;//医生身份证

	private String userEase;//注册用户环信ID
	private String seatingEase;//坐席环信ID
	private String docEase;//医生环信ID
	
	private Long pacId;//医生套餐ID，DoctorPackage.id
	private Date startTime;//套餐生效时间
	private Date endTime;//套餐失效时间
	
	//用户套餐余量，使用Json格式字符串保存用户套餐项目的剩余次数
	//{itemId1:-1, itemId2:5, itemId3:0 ...}
	private String pacItems;

	private String groupDesc;//组描述“用户名+医生名” 改成json字符串
	private String note;//备注
	private String docNote;//医生备注
	
	private Long sorTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Long getSeatingId() {
		return seatingId;
	}
	public void setSeatingId(Long seatingId) {
		this.seatingId = seatingId;
	}
	public String getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	public String getUserEase() {
		return userEase;
	}
	public void setUserEase(String userEase) {
		this.userEase = userEase;
	}
	public String getSeatingEase() {
		return seatingEase;
	}
	public void setSeatingEase(String seatingEase) {
		this.seatingEase = seatingEase;
	}
	public String getDocEase() {
		return docEase;
	}
	public void setDocEase(String docEase) {
		this.docEase = docEase;
	}
	public Long getPacId() {
		return pacId;
	}
	public void setPacId(Long pacId) {
		this.pacId = pacId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getPacItems() {
		return pacItems;
	}
	public void setPacItems(String pacItems) {
		this.pacItems = pacItems;
	}
	public String getGroupDesc() {
		return groupDesc;
	}
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getDocNote() {
		return docNote;
	}
	public void setDocNote(String docNote) {
		this.docNote = docNote;
	}
	public String getUserCellPhone() {
		return userCellPhone;
	}
	public void setUserCellPhone(String userCellPhone) {
		this.userCellPhone = userCellPhone;
	}
	public Long getSorTime() {
		return sorTime;
	}
	public void setSorTime(Long sorTime) {
		this.sorTime = sorTime;
	}
}
