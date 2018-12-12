package com.wehealth.model.domain.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class OperatorGroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String description;
	private Date creationTime;
	//private Set<Operator> Operators;
	private Set<Domain> domains;

	public OperatorGroup() {
		super();
	}

	public OperatorGroup(Long id, String name, String description,
                         Date creationTime, Set<Operator> operators, Set<Domain> domains) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.creationTime = creationTime;
		//Operators = operators;
		this.domains = domains;
	}

/*	public Set<Operator> getOperators() {
		return Operators;
	}

	public void setOperators(Set<Operator> operators) {
		Operators = operators;
	}*/

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Set<Domain> getDomains() {
		return domains;
	}

	public void setDomains(Set<Domain> domains) {
		this.domains = domains;
	}


}
