/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Jun 7, 2016
 */

package com.wehealth.model.domain.interfaceutil;

import java.util.Date;

public interface CreateTimeAuditable
{
	void setCreateTime(Date createTime);
	
	Date getCreateTime();
}
