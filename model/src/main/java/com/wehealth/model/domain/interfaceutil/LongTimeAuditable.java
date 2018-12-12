/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Mar 10, 2016
 */

package com.wehealth.model.domain.interfaceutil;

public interface LongTimeAuditable
{
	Long getCreateTime();
	
	void setCreateTime(Long createTime);
	
	Long getUpdateTime();
	
	void setUpdateTime(Long updateTime);
}
