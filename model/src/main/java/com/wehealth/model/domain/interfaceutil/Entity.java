/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Jul 1, 2015
 */

package com.wehealth.model.domain.interfaceutil;

import java.io.Serializable;

public interface Entity extends Serializable {
	void setId(Long id);

	Long getId();
}
