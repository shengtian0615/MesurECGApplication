package com.wehealth.model.domain.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class Domain implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String displayName;
	private Set<String> functions=new HashSet<String>();
	private String description;
	private Date creationTime;

	public Domain() {
		super();
	}
	public Domain(Long id, String displayName, String description,
                  Date creationTime, Set<String> functions) {
		super();
		this.id = id;
		this.displayName = displayName;
		this.description = description;
		this.creationTime = creationTime;
		this.functions=functions;
	}
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof Domain))
			return false;
		if (this.displayName.equals(((Domain) o).getDisplayName()))
			return true;
		return false;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public String getDescription() {
		return description;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public Set<String> getFunctions() {
		return functions;
	}
	public Long getId() {
		return id;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public void setDisplayName(String name) {
		displayName = name;
	}

	public void setFunctions(Set<String> functions) {
		this.functions = functions;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
