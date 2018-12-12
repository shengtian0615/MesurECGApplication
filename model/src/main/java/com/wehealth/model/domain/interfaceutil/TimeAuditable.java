package com.wehealth.model.domain.interfaceutil;

import java.util.Date;

public interface TimeAuditable {

	void setCreateTime(Date createTime);
	
	Date getCreateTime();
	
	void setUpdateTime(Date updateTime);
	
	Date getUpdateTime();
}
