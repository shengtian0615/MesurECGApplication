package com.wehealth.ecg.jni.heartrate;

public class EcgHRDetect
{	
	  public native int  initHr(float ad);
	  public native int  hrDetect(int ecgData[], int len, int leadNum);
}