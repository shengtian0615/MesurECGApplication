package com.wehealth.model.domain.interfaceutil;


public interface LocalizedNamedObject extends NamedObject
{

	String getBundleName();
	
	String getKey();
}
