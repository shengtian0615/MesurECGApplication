package com.wehealth.ecg.jni.analyse;

public class EcgAnalyse{
	
	 public int RR;
	 public int HR;
	 public int PR;
	 public int QRS;
	 public int QT;
	 public int QTc;
	 public int Axis[];
	 public int RV5;
	 public int SV1;
	 
	 public int ecgResult[];
	
          /* 心电分析 */
 
   
	  public native int initEcgAnalyseLib(float ad);
	  public native int analyseEcgData(EcgAnalyse ecganalyse, int data[], int dataLen,
	  																 int leadNum, int pace[], int paceLen, int filterOffset);
	 
	  
}