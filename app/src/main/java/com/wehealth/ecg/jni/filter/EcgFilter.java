package com.wehealth.ecg.jni.filter;

public class EcgFilter{
	

	
    /* 滤波器 */
public native int initFilter(short baseFilterConfig, short mcFilterConfig, 
			short acFilterConfig, short lpFilterConfig);
public native int filter(int ecgData[],int len,int leadNum);

public native int GetFilterOffset();

public native int initBaseLineJudge();
public native int isBaseLineStable(int ecgData[], int len, int leadNum);


}
