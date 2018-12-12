package com.wehealth.model.domain.model;

import com.wehealth.model.domain.enumutil.AdvertisementInfoGroupStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AdvertisementInfoGroup implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date createTime;
	private Date updateTime;
	private Set<AdvertisementInfo> infos = new HashSet<AdvertisementInfo>();
	private AdvertisementInfoGroupStatus status;
	
	public AdvertisementInfoGroup() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Set<AdvertisementInfo> getInfos() {
		return infos;
	}

	public void setInfos(Set<AdvertisementInfo> infos) {
		this.infos = infos;
	}

	public AdvertisementInfoGroupStatus getStatus() {
		return status;
	}

	public void setStatus(AdvertisementInfoGroupStatus status) {
		this.status = status;
	}
}
