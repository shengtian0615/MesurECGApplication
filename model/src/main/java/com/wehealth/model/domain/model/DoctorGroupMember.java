/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Jan 9, 2017
 */

package com.wehealth.model.domain.model;

import com.wehealth.model.domain.interfaceutil.TimeAuditable;

import java.io.Serializable;
import java.util.Date;

public class DoctorGroupMember implements TimeAuditable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String easeId;
	private String groupId;

	// 0: owner, 1: member
	private int type = 1;

	private String nickname;

	private Date createTime;
	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEaseId() {
		return easeId;
	}

	public void setEaseId(String easeId) {
		this.easeId = easeId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "DoctorGroupMember [id=" + id + ", easeId=" + easeId
				+ ", groupId=" + groupId + ", type=" + type + ", nickname="
				+ nickname + "]";
	}

}
