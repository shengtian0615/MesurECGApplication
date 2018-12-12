package com.wehealth.mesurecg.utils;

import android.util.Log;

public class SampleDot {
	
	private static final int DON_MAX = 1500;
	private int  iSrcDontCnt;				 //需转化的源数据点数
	private int  iDesDontCnt;				 //转化的目标数据点数
	private int  DotCntPerStep[];  //每步需要插或需跳步的点数,
	private int  PerStepLen[];	 //每步的步长,每隔多少点要插点或跳点,
	private int  StepSumTotal;			 //需要的总步长数
	private int  NeedDontTotal;			 //在StepSumTotal 内需要插入或抽的总点数
	private int Stepindex;			//已走的步长
	private int idoCnt;				//一个周期:StepSumTotal内已抽或插的点数
	private int iSrcCnt;				//一个周期:StepSumTotal内源数据个数据计算
	private int iSrcCntCurstep;		//当前步长内已满足点数
	private int outbuff[];		//抽或插入缓冲区
	private int Srcbuff[];	//源数据缓冲区,放传入的源数据

	private int preMin;				//前面点最小值
	private int preMax;				//前面点最大值  preMax设计时永远在preMin右边,所 以可能会 preMax< preMin
	private int thisMin;				//当前最小值
	private int thisMax;				//当前最大值

	private boolean blFirst;			//首次是否满足抽样条件
	
	public SampleDot(int scrDontCnt, int desDontCnt){
		iSrcDontCnt = scrDontCnt;
		iDesDontCnt = desDontCnt;
		DotCntPerStep = new int[DON_MAX];
		PerStepLen = new int[DON_MAX];
		outbuff = new int[DON_MAX];
		Srcbuff = new int[DON_MAX];
		CalStepAndDopt();
		iSrcCntCurstep = 0;
		preMin = -32768;
		thisMin = -32768;
		preMax = 32767;
		thisMax = 32767;
		
	}
	

	
	public void SetSampleDotValue(int scrDontCnt, int desDontCnt){
		iSrcDontCnt = scrDontCnt;
		iDesDontCnt = desDontCnt;
	}
	
	private void  CalStepAndDopt()
	{
		int i;
		int diffSample=0;
		
		diffSample = iDesDontCnt - iSrcDontCnt;

        if(diffSample < 0) //抽点
		{
			for(i=0;i < DON_MAX;i++)
			{
				DotCntPerStep[i] = 1;
			}
			for(i=1;i<iDesDontCnt +1;i++)
			{
				PerStepLen[i-1] = (iSrcDontCnt * i /iDesDontCnt
					- iSrcDontCnt *(i-1) /iDesDontCnt  );

				if(PerStepLen[i-1] ==0)
				{
					PerStepLen[i-1] = 1;
				}
			}
			
			StepSumTotal = iDesDontCnt;
		}

		NeedDontTotal = diffSample;
	
	}
	




	/**
	 * 在scrBuf中抽取两个点，最大点和最小点
	 * @param srcBuff  原数据
	 * @param isrcLen  数据长度
	 * @param blDotMaxRight  最大值的位置 在前还是在后
	 * @return
	 */

	private boolean _SnapshotSample_(int srcBuff[],int isrcLen,boolean blDotMaxRight)
	{

		

		int i = 0;
		int iWap = 0;
		int tempWaveData[] = srcBuff;
		int _thisMin = 0 , _thisMax = 0;

		_thisMin = _thisMax = tempWaveData[0];	
		boolean blfalgMax = false;


		if(null == srcBuff)
		{
			return false;
		}

		for(i = 0;i < isrcLen;i++)
		{
			if(_thisMin < tempWaveData[i])
			{
				_thisMin = tempWaveData[i];
				blfalgMax = false;
			}
			if(_thisMax > tempWaveData[i])
			{
				_thisMax = tempWaveData[i];
				blfalgMax = true;
			}
		}	
		if(_thisMax < _thisMin)
		{
			iWap=_thisMax;
			_thisMax = _thisMin;
			_thisMin = iWap;

			blfalgMax = !blfalgMax; 
		}	
		
		thisMin = _thisMin;
		thisMax = _thisMax;
		
		//System.out.println("Swap data" + thisMin + this
		
		return blfalgMax;

	}
	
	public int SnapshotSample( int srcBuff[],int isrcLen,int Outbuff[],int Len)
	{
		int idotCnt = 0;
		int iStepindex = 0;
		int iStepLen0 = 0,iStepLen1 = 0;
		int iStepNeedsum = 0;  //本次抽点需要的点数
		
		int pOutbuff[];
		int _StepSumTotal = 0;
	
		pOutbuff = Outbuff;
		
		if(isrcLen<=0)
		{
			return 0;
		}
	

		
		
		if(iSrcDontCnt == iDesDontCnt)
		{
			for(int i = 0; i < isrcLen; i++){
				pOutbuff[i] = srcBuff[i];
			}
			
			return isrcLen;
		}
		
		_StepSumTotal = StepSumTotal;

		iStepindex = Stepindex;	

		iStepLen0 = PerStepLen[iStepindex];
		if(iStepindex+1 < _StepSumTotal)
		{
			iStepLen1 = PerStepLen[iStepindex+1];
		}

		if( _StepSumTotal <= 1 && !blFirst)
		{
			//抽点只少要等2 个步长的数据来之后才可抽点
			iStepLen1 = iStepLen0;
		}
		iStepNeedsum = iStepLen0 + iStepLen1;

			if(iSrcCntCurstep + isrcLen < iStepNeedsum)
			{
				for(int i = 0; i < isrcLen; i++)
				{
					Srcbuff[iSrcCntCurstep + i] = srcBuff[i];
				}
				iSrcCntCurstep += isrcLen;
				iSrcCnt += isrcLen;				
			}
			else		
			{	

		   		boolean blfalgMax = false;   // Max 是否在Min 值右边,TRUE:是

				int idiff = iSrcCntCurstep + isrcLen - iStepNeedsum;
				
				int idifflen = iStepNeedsum - iSrcCntCurstep;
				
				if( idiff <=0)
				{
					idiff = 0;
				}
				if(idifflen <= 0)
				{
					idifflen = 0;
				}
				if(idifflen >0)
				{
					for(int i = 0; i < idifflen; i++){
						Srcbuff[iSrcCntCurstep] = srcBuff[i];
					}
				
					iSrcCntCurstep += idifflen;
					iSrcCnt += idifflen;			



					
			blfalgMax = _SnapshotSample_(Srcbuff,iStepNeedsum, blfalgMax);

					if(!blFirst)
					{
						preMin = thisMin;
						preMax = thisMax;

						if(blfalgMax)
						{
							pOutbuff[idotCnt++] = thisMin;
							pOutbuff[idotCnt++] = thisMax;
							preMax = thisMax;
							preMin = thisMin;
						
						}
						else 
						{
							pOutbuff[idotCnt++] = thisMax;
							pOutbuff[idotCnt++] = thisMin;	
							preMax = thisMin;	
							preMin = thisMax;
						}

						blfalgMax = false;
					}
					else
					{
						if(iStepindex+1 >= _StepSumTotal)
						{
							if(preMax > preMin)
							{//前个点上升趋势
								if(!blfalgMax)
								{//这次采样的点是下降趋势
									if(thisMax > preMax)
									{//用本次的下降趋势最大值更上次点的最大值
										int _index = idotCnt-1;
										if(_index >= 0)
										{
											pOutbuff[idotCnt-1] = thisMax;
											preMax = thisMax;
										}
										
									}
									pOutbuff[idotCnt++] = thisMin;

									preMin = preMax;
									preMax = thisMin;
								}
								else
								{//这次采样的点是上升趋势
									pOutbuff[idotCnt++] = thisMax;

									preMin = preMax;
									preMax = thisMax;	
								}

							}
							else
							{//前个点下降趋势
								if(!blfalgMax)
								{//这次采样的点是下降趋势
									
									pOutbuff[idotCnt++] = thisMin;

									preMin = preMax;
									preMax = thisMin;
								}
								else
								{//这次采样的点是上升趋势
									if(thisMin < preMax)
									{
										int _index = idotCnt-1;
										if(_index < 0) 
										{
											_index = 0;
										}
										pOutbuff[idotCnt-1] = thisMin;	
										preMax = thisMin;	

									}
									
									pOutbuff[idotCnt++] = thisMax;
									
									preMin = preMax;
									preMax = thisMax;	
								}
							}
							
			
							blfalgMax = false;

						}
						else
						{
							if(blfalgMax)
							{
								pOutbuff[idotCnt++] = thisMin;
								pOutbuff[idotCnt++] = thisMax;
								preMax = thisMax;

								preMin = thisMin;
							}
							else 
							{
								pOutbuff[idotCnt++] = thisMax;
								pOutbuff[idotCnt++] = thisMin;	
								preMax = thisMin;	

								preMin = thisMax;
							}
							//pSnapshot->preMin = pSnapshot->thisMin;	
							//pSnapshot->preMax = pSnapshot->thisMax;
								
							blfalgMax = false;
						}	
					}					
					

					iSrcCntCurstep = 0;

					
					if(iStepindex+1 >= _StepSumTotal)
					{
						Stepindex = 0;
					}
					else
					{
						Stepindex +=2;
						if(Stepindex >= _StepSumTotal)
						{
							Stepindex = 0;
						}	
					}		
					
				}
				
				if(idiff > 0)
				{
					int _diffpos = idifflen;

					while(idiff > 0)
					{
			
						iStepLen0 = iStepLen1 = 0; 

						iStepindex = Stepindex;				
						iStepLen0 = PerStepLen[iStepindex];

						if(iStepindex+1 < _StepSumTotal)
						{
							iStepLen1 = PerStepLen[iStepindex+1];
						}
						iStepNeedsum = iStepLen0 + iStepLen1;
						
						if(idiff < iStepNeedsum )
						{				
							for(int i = 0; i < idiff; i++)
								Srcbuff[i] = srcBuff[idifflen + i];
							iSrcCntCurstep += idiff;
							break;
						}
						else
						{
							
							
								
						 for(int i = 0; i < iStepNeedsum; i++){
							 Srcbuff[i] = srcBuff[_diffpos + i];
						 }
						 blfalgMax = _SnapshotSample_(Srcbuff,iStepNeedsum, blfalgMax);
							
					
							if(iStepindex+1 >= _StepSumTotal)
							{
							
								if(preMax > preMin)
								{//前个点上升趋势
									if(!blfalgMax)
									{//这次采样的点是下降趋势
										if(thisMax > preMax)
										{
											int _index = idotCnt-1;
											if(_index >= 0)
											{
												pOutbuff[idotCnt-1] = thisMax;
												preMax = thisMax;
											}
											
										}
										pOutbuff[idotCnt++] = thisMin;

										preMin = preMax;
										preMax = thisMin;
									}
									else
									{//这次采样的点是上升趋势
										pOutbuff[idotCnt++] = thisMax;

										preMin = preMax;
										preMax = thisMax;	
									}

								}
								else
								{//前个点下降趋势
									if(!blfalgMax)
									{//这次采样的点是下降趋势
										
										Log.e("idotCnt is ", String.valueOf(idotCnt));
										pOutbuff[idotCnt++] = thisMin;

										preMin = preMax;
										preMax = thisMin;
									}
									else
									{//这次采样的点是上升趋势
										if(thisMin < preMax)
										{
											int _index = idotCnt-1;
											if(_index < 0) 
											{
												_index = 0;
											}
											pOutbuff[idotCnt-1] = thisMin;	
										    preMax = thisMin;	

										}
										
										pOutbuff[idotCnt++] = thisMax;
										
										preMin = preMax;
										preMax = thisMax;	
									}
								}
								
						

								blfalgMax = false;

							}
							else
							{
								if(blfalgMax)
								{
									pOutbuff[idotCnt++] = thisMin;
									pOutbuff[idotCnt++] = thisMax;
									preMax = thisMax;
									preMin = thisMin;
								}
								else 
								{
									pOutbuff[idotCnt++] = thisMax;
									pOutbuff[idotCnt++] = thisMin;	
									preMax = thisMin;	
									preMin = thisMax;
								}
								//pSnapshot->preMin = pSnapshot->thisMin;	
								//pSnapshot->preMax = pSnapshot->thisMax;
									
								blfalgMax = false;
							}						

							iSrcCnt += iStepNeedsum;

							iSrcCntCurstep = 0;

							_diffpos += iStepNeedsum;
							idiff -= iStepNeedsum;

							if(iStepindex+1 >= _StepSumTotal)
							{
								Stepindex = 0;
							}
							else
							{
								Stepindex +=2;
								if(Stepindex >= _StepSumTotal)
								{
									Stepindex = 0;
								}	
							}					

						}					
						
					}
					
					
				}
			
				blFirst = true;	

			}
			
		return idotCnt;

	}

	
	
}

