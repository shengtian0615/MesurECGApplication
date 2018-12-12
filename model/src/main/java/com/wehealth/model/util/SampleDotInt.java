package com.wehealth.model.util;

import java.util.ArrayList;
import java.util.List;

public class SampleDotInt {
	private static final int DON_MAX = 1500;
	private static final int INVALUE = -1;
	private int SrcSampleDot;				 //需转化的源数据点数
	private int DesSampleDot;				 //转化的目标数据点数
	private int stepCnt;
	private int srcBufCnt;
	private int desBufCnt;
	private int  preStepDot[];
	private int srcBuf[];
	private int desBuf[];
	private int needSrcDot;
	private boolean endFlag;
	

	
	public SampleDotInt(int scrSample, int desSample){
		SrcSampleDot = scrSample;
		DesSampleDot = desSample;
		preStepDot = new int[DON_MAX];
		srcBuf = new int[DON_MAX];
		desBuf = new int[DON_MAX];
		CalStepAndDopt();
		stepCnt = 0;	
		srcBufCnt = 0;
		desBufCnt = 0;
		needSrcDot = INVALUE;
		endFlag = false;
	}
	
	
	private void  CalStepAndDopt(){
		int i;
		int diffSample=0;
		
		diffSample = DesSampleDot - SrcSampleDot;

        if(diffSample < 0) //抽点
		{
			for(i = 0; i  < DesSampleDot +1; i++)
			{
				preStepDot[i] = (SrcSampleDot*(i + 1) / DesSampleDot
					- SrcSampleDot*i / DesSampleDot  );
			}
		}
	}
	
	
	
	public int[] SnapshotSample(int src){
		//Todo 频率一样
			CalNeedSrcDot();
				
			if(srcBufCnt < needSrcDot){
				srcBuf[srcBufCnt++] = src;
			}
			
			if(srcBufCnt >= needSrcDot){
				SnapshotFromSrcBuf();
				ResetSnapshotParameter();
			}
	
		return ReturnTheDesDot();
	}
	
	public List<Integer> SnapshotSample(int src[]){
		int des[];
		List<Integer> list = new ArrayList<Integer>();

		for(int i = 0; i < src.length; i++){
			des = SnapshotSample(src[i]);
			for(int j = 0; j < des.length; j++){
				list.add(des[j]);
			}
		}
		
		return list;
	}
	
	private void CalNeedSrcDot(){
		
		if(needSrcDot == INVALUE){
			if((stepCnt + 1) < DesSampleDot)
				needSrcDot = preStepDot[stepCnt] + preStepDot[stepCnt + 1];
			else{
				needSrcDot = preStepDot[stepCnt];
				endFlag = true;
			}
		}
	}
	

	private void SnapshotFromSrcBuf(){
		int max = srcBuf[0];
		int min = srcBuf[0];
		int minPosition = 0;
		int maxPosition = 0;
			
		for(int i = 0; i < srcBufCnt; i++){
			if(max < srcBuf[i]){
				max = srcBuf[i];
				maxPosition = i;
			}	
			if(min > srcBuf[i]){
				min = srcBuf[i];
				minPosition = i;
			}
		}
		
		if(maxPosition < minPosition){
			SetDesBuf(max, min);
			
		}else{
			SetDesBuf(min, max);
		}
	
	}
	
	private void ResetSnapshotParameter(){
		srcBufCnt = 0;
		needSrcDot = INVALUE;
		stepCnt += 2;  /* 两组数据抽取两个点最大值和最小值*/
		if(stepCnt >= DesSampleDot){
			stepCnt = 0;
			endFlag = false;
		}
	}
	
    private int[] ReturnTheDesDot( ){
    	int data[];
		int i;
		
		data = new int[desBufCnt];
	
		for(i = 0; i < desBufCnt; i++){
			data[i] = desBuf[i];
		}
		
		desBufCnt = 0;
		return data;
    }
	
	
	private void SetDesBuf(int left, int right){
		if(endFlag){
			desBuf[desBufCnt++] = right;
		}else{
			
			desBuf[desBufCnt++] = left;
			desBuf[desBufCnt++] = right;
		}
	}
}
