package com.wehealth.model.domain.model;

import com.wehealth.model.domain.enumutil.DoctorPackageStatus;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class DoctorPackage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;//用户购买套餐时使用此id
	private String shortName;//e.g: 套餐A
	private String name;//e.g: 套餐A的全名
	private int price;//价格，单位分，先做免费的
	private String note;//套餐描述
	
	private String idCardNo;//医生ID，必须有
	
	/**
	 * 新建的套餐记录，parentId=id；
	 * 修改套餐后会生成新的套餐记录，parentId=旧套餐的ID
	 * 即根据parentId能还原每个套餐的历史记录。
	 */
	private Long parentId;
	
	private int duration;//持续时间，例如：1
	private TimeUnit unit;//持续时间的单位，例如：年
	
	//新属性：默认为edit
	private DoctorPackageStatus status = DoctorPackageStatus.edit;

	//表示机构专属套餐，只对机构用户可见，可买
	//其他机构、或不属于机构的用户不可见
	private String agencyId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
//	public boolean isHistory() {
//		return history;
//	}
//	public void setHistory(boolean history) {
//		this.history = history;
//	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public TimeUnit getUnit() {
		return unit;
	}
	public void setUnit(TimeUnit unit) {
		this.unit = unit;
	}
	public DoctorPackageStatus getStatus() {
		return status;
	}
	public void setStatus(DoctorPackageStatus status) {
		this.status = status;
	}
	public String getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}
}
