package com.wehealth.model.domain.interfaceutil;

public interface UserAuditable {

	void setCreateUserId(String createUserId);
	
	String getCreateUserId();
	
	void setUpdateUserId(String updateUserId);
	
	String getUpdateUserId();
	
}
