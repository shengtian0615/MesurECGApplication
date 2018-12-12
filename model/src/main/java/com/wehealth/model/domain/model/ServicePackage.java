/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Dec 29, 2016
 */

package com.wehealth.model.domain.model;

import com.wehealth.model.domain.enumutil.FrequencyUnit;
import com.wehealth.model.domain.interfaceutil.Entity;
import com.wehealth.model.domain.interfaceutil.TimeAuditable;

import java.util.Date;


public class ServicePackage implements Entity, TimeAuditable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;
	private String packageId;

	// 1 year, 6 months ... the number is period
	private int period;
	// day, week, month
	private FrequencyUnit unit;

	private double cost;
	private double markedPrice;
	private double renewPrice;
	private double exceptDevicePrice;

	private int level = -1;// 0 template,

	private Date createTime;
	private Date updateTime;

	private String note;

	private boolean enable;

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

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public FrequencyUnit getUnit() {
		return unit;
	}

	public void setUnit(FrequencyUnit unit) {
		this.unit = unit;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getMarkedPrice() {
		return markedPrice;
	}

	public void setMarkedPrice(double markedPrice) {
		this.markedPrice = markedPrice;
	}

	public double getRenewPrice() {
		return renewPrice;
	}

	public void setRenewPrice(double renewPrice) {
		this.renewPrice = renewPrice;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public double getExceptDevicePrice() {
		return exceptDevicePrice;
	}

	public void setExceptDevicePrice(double exceptDevicePrice) {
		this.exceptDevicePrice = exceptDevicePrice;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
