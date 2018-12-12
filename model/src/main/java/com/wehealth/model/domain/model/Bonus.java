/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Sep 1, 2016
 */

package com.wehealth.model.domain.model;


import com.wehealth.model.domain.enumutil.BonusType;
import com.wehealth.model.domain.interfaceutil.Entity;

public class Bonus extends AbstractFullyAuditableEntity implements Entity, Comparable<Bonus> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	/**
	 * Default, reason's name
	 */
	private String name;
	private BonusType type;
	private String forUserId;
	// 剩余数量
	private Integer times;
	// 额度
	private Integer limit;

	/**
	 * BonusReason reasonId
	 */
	private String bonusReason;
	/**
	 * Default, reason't expireTime
	 */
	private Long expireTime;
	/**
	 * Default, reason's note
	 */
	private String note;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BonusType getType() {
		return type;
	}

	public void setType(BonusType type) {
		this.type = type;
	}

	public String getForUserId() {
		return forUserId;
	}

	public void setForUserId(String forUserId) {
		this.forUserId = forUserId;
	}

	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

	public String getBonusReason() {
		return bonusReason;
	}

	public void setBonusReason(String bonusReason) {
		this.bonusReason = bonusReason;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int compareTo(Bonus another) {
		return this.expireTime.compareTo(another.expireTime);
	}

}
