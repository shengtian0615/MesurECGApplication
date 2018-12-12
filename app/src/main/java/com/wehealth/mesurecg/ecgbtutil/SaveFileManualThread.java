package com.wehealth.mesurecg.ecgbtutil;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;

import com.wehealth.ecg.jni.analyse.EcgAnalyse;
import com.wehealth.mesurecg.MeasurECGApplication;
import com.wehealth.mesurecg.utils.ByteFileUtil;
import com.wehealth.mesurecg.utils.PDFUtils2Device;
import com.wehealth.model.domain.model.RegisteredUser;
import com.wehealth.model.util.Constant;
import com.wehealth.model.util.StringUtil;

@SuppressLint("SdCardPath")
public class SaveFileManualThread extends Thread {

	private final int SAVE_PDFXML_FILE = 897;
	public boolean saveFileThreadFLAG;
	private EcgAnalyse ecgAnalyse;
	private Handler handler;
	private Context context;
	public LinkedList<Map<String, Object>> queue = new LinkedList<Map<String, Object>>();
	PDFUtils2Device pdfUtils;
	private boolean saveFileState = false;
	
	public SaveFileManualThread(Context mContext, Handler mHandler){
		ecgAnalyse = new EcgAnalyse();
		ecgAnalyse.Axis = new int[3];
    	ecgAnalyse.ecgResult = new int[12];
    	context = mContext;
    	handler = mHandler;
    	saveFileThreadFLAG = true;
    	pdfUtils = new PDFUtils2Device();
	}
	
	public void addToQueue(Map<String, Object> map){
		synchronized (queue) {
			queue.add(map);
		}
	}

	@Override
	public void run() {
		super.run();
		while (saveFileThreadFLAG) {
			try {
				if (queue.isEmpty() && !saveFileState) {
					sleep(1000);
				}else if(queue.size()>0){
					synchronized (queue) {
						long start = System.currentTimeMillis();
						StringUtil.writException2File("/sdcard/manual.txt", "\n\n循环开始 \nThread 中的开始运行   queue的数量="+queue.size()+"  saveFileState ="+saveFileState+" 时间："+new Date(start).toString());
						Map<String, Object> map = queue.removeFirst();
						RegisteredUser patient = MeasurECGApplication.getInstance().getRegisterUser();
						
						long ecg2DeviceData_time = (Long) map.get("ecg2DeviceData_time");
						int saveFileManualCount = (Integer) map.get("saveFileManualCount");
						int timeSecondCount = (Integer) map.get("timeSecondCount");
						
						StringUtil.writException2File("/sdcard/manual.txt", "\n Thread 中的开始保存   保存时间分钟："+saveFileManualCount+"  秒数："+timeSecondCount);
						Map<Integer, Integer[]> datas = StringUtil.praseIntegerData(map);
						ByteFileUtil.saveIntegers(ecg2DeviceData_time, datas);
						if (saveFileManualCount==0 && timeSecondCount<20) {
							int[] paceBuffer = (int[]) map.get("paceBuffer");
							String Gain = (String) map.get("Gain");
							String Speed = (String) map.get("Speed");
							String FilterBase = (String) map.get("FilterBase");
							String FilterMC = (String) map.get("FilterMC");
							String FilterAC = (String) map.get("FilterAC");
							int waveGain = (Integer) map.get("waveGain");
							int waveSpeed = (Integer) map.get("waveSpeed");
							int waveSingleDisplay_Switch = (Integer) map.get("waveSingleDisplay_Switch");
							Map<String, Map<String, String>> listMaps = new HashMap<String, Map<String,String>>();
							Map<String, String> pinfos = new HashMap<String, String>();
							Map<String, String> analyses = new HashMap<String, String>();
							int[] ecgAnas = StringUtil.getMapEcgDataINTs(map);
							int datalen = datas.get(0).length;
							//自动分析结论
							ecgAnalyse.initEcgAnalyseLib(3495.2533333f * 3);
							ecgAnalyse.analyseEcgData(ecgAnalyse, ecgAnas, datalen, 12, paceBuffer, 0, 0);
							
							StringUtil.writException2File("/sdcard/manual.txt", "\n Thread 中jni分析完成");
							
							int[] axis = ecgAnalyse.Axis;
							int[] resultCode = ecgAnalyse.ecgResult;
							StringUtil.writException2File("/sdcard/manual.txt", "\n Thread 获取心电结论结果    resultCode.length = "+resultCode.length);
							Set<String> ECGResult = ECGData_Analyse.getAnalyseResult(resultCode);
							StringUtil.writException2File("/sdcard/manual.txt", "\n Thread 心电结论完成+++  ECGResult.size="+ECGResult.size());
							int hr = ecgAnalyse.HR;
							int pr = ecgAnalyse.PR;
							int qrs = ecgAnalyse.QRS;
							int qt = ecgAnalyse.QT;
							int qtc = ecgAnalyse.QTc;
							int rr = ecgAnalyse.RR;
							int rv5 = ecgAnalyse.RV5;
							int sv1 = ecgAnalyse.SV1;
							analyses.put("HeartRate", String.valueOf(hr));
							analyses.put("PRInterval", String.valueOf(pr));
							analyses.put("RRInterval", String.valueOf(rr));
							analyses.put("QRSDuration", String.valueOf(qrs));
							analyses.put("QTD", String.valueOf(qt));
							analyses.put("QTC", String.valueOf(qtc));
							analyses.put("RV5", String.valueOf(rv5));
							analyses.put("SV1", String.valueOf(sv1));
							analyses.put("RV5SV1", String.valueOf((Math.abs(rv5)+Math.abs(sv1))));
							analyses.put("PAxis", String.valueOf(axis[0]));
							analyses.put("QRSAxis", String.valueOf(axis[1]));
							analyses.put("TAxis", String.valueOf(axis[2]));
//							String Auto_Result = "";
//							for (String str : ECGResult) {
//								Auto_Result=Auto_Result+" "+str;
//							}
//							analyses.put("Auto_Result", Auto_Result);
							analyses.put("Gain", Gain);
							analyses.put("Speed", Speed);
							analyses.put("FilterBase", FilterBase);
							analyses.put("FilterMC", FilterMC);
							analyses.put("FilterAC", FilterAC);
							
							pinfos.put("Name", patient.getName());
							pinfos.put("ID", patient.getIdCardNo());
							pinfos.put("Gender", StringUtil.getIntGender(patient.getIdCardNo())+"");
							pinfos.put("AGE", StringUtil.getAgeByBirthDay(patient.getIdCardNo())+"");
							pinfos.put("ecg_checktime", String.valueOf(System.currentTimeMillis()));
							
							listMaps.put(Constant.ECG_PATIENT_INFO, pinfos);
							listMaps.put(Constant.ECG_ANALYSE_PARAM, analyses);
							StringUtil.writException2File("/sdcard/manual.txt", "\n Thread 中pdf文件开始保存");
							
							pdfUtils.savePDFIntegers(context, listMaps, datas, waveGain, waveSpeed, waveSingleDisplay_Switch);

							StringUtil.writException2File("/sdcard/manual.txt", "\n Thread 中pdf文件保存结束");
						}
						
						long end = System.currentTimeMillis();
						StringUtil.writException2File("/sdcard/manual.txt", "\n Thread 中的保存完成  用时："+(end-start)+"\n一个循环结束\n \n ");
						handler.sendEmptyMessage(SAVE_PDFXML_FILE);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				StringUtil.writeException(e, "");
			}
			if (saveFileState) {
				saveFileThreadFLAG = false;
			}
		}
	}
	
	/**
	 * 设置为false，表示线程不再sleep
	 * @param b
	 */
	public void setThreadSleepState(boolean b){
		saveFileState = b;
	}
	
	/***
	 * 设置为false，线程循环标识为false
	 * @param flag
	 */
	public void setThreadFlag(boolean flag){
		saveFileThreadFLAG = flag;
	}
	
}
