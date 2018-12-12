package com.wehealth.model.domain.model;

import com.wehealth.model.domain.interfaceutil.TimeAuditable;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class Operator implements TimeAuditable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String description;
	private Date createTime;
	private Date updateTime;
	private String username;
	private String password;
	private boolean enabled = true;
	private boolean deleted = false;
	private boolean searchAble = true;
	private Set<OperatorGroup> operatorGroups;
	
	
	public Operator() {
		super();
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public boolean isDeleted()
	{
		return deleted;
	}

	public void setDeleted(boolean deleted)
	{
		this.deleted = deleted;
	}

	public boolean isSearchAble()
	{
		return searchAble;
	}

	public void setSearchAble(boolean searchAble)
	{
		this.searchAble = searchAble;
	}

	public Set<OperatorGroup> getOperatorGroups()
	{
		return operatorGroups;
	}

	public void setOperatorGroups(Set<OperatorGroup> operatorGroups)
	{
		this.operatorGroups = operatorGroups;
	}
}
