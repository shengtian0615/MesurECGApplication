package com.wehealth.model.domain.enumutil;

import com.wehealth.model.domain.interfaceutil.LocalizedNamedObject;
import com.wehealth.model.domain.interfaceutil.LocalizedObject;

public enum FrequencyUnit implements LocalizedObject
{
	Minute(CommonText.MINUTE),
	Hour(CommonText.HOUR),
	Day(CommonText.DAY),
	Week(CommonText.WEEK),
	Month(CommonText.MONTH),
	Year(CommonText.YEAR)
	;

	private LocalizedNamedObject obj;
	
	private FrequencyUnit(LocalizedNamedObject obj)
	{
		this.obj = obj;
	}
	
	@Override
	public LocalizedNamedObject getObject()
	{
		return obj;
	}
	
}
