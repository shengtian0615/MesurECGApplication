package com.wehealth.mesurecg.ecgbtutil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Message;

import com.wehealth.ecg.jni.analyse.EcgAnalyse;
import com.wehealth.mesurecg.MeasurECGApplication;
import com.wehealth.mesurecg.dao.ECGDao;
import com.wehealth.mesurecg.utils.ByteFileUtil;
import com.wehealth.mesurecg.utils.PDFUtils2Device;
import com.wehealth.model.domain.model.RegisteredUser;
import com.wehealth.model.util.Constant;
import com.wehealth.model.util.FileUtil;
import com.wehealth.model.util.StringUtil;

public class ExecutorThreadUtils {

	private final int SAVE_PDFXML_FILE = 897;
	private final int SHOW_PROGRESS_DIALOG = 800;
	private EcgAnalyse ecgAnalyse;
	
	private Handler handler;
	private String xmlFilePath;
	private PDFUtils2Device pdfUtils;
    public ExecutorThreadUtils instance;
    private ExecutorService executorService;

    public ExecutorThreadUtils(Handler handle){
        executorService = Executors.newFixedThreadPool(5);
        handler = handle;
        ecgAnalyse = new EcgAnalyse();
		ecgAnalyse.Axis = new int[3];
    	ecgAnalyse.ecgResult = new int[12];
    	pdfUtils = new PDFUtils2Device();
    }

    public void startAutoTask(Map<String, Object> map){
        executorService.submit(new AutoTask(map));
    }
    
    public void startManulTask(Map<String, Object> map){
    	executorService.submit(new ManulTask(map));
    }
    

    public class AutoTask implements Runnable{


    	private Map<String, Object> map;
        public AutoTask(Map<String, Object> mapDatas) {
        	map = mapDatas;
		}

		@SuppressWarnings("unchecked")
		@Override
        public void run() {
			Message msg = handler.obtainMessage(SHOW_PROGRESS_DIALOG);
			msg.obj = "正在保存数据，请稍候...";
			handler.sendMessage(msg);
			RegisteredUser patient = MeasurECGApplication.getInstance().getRegisterUser();
			List<int[]> ecgDataBuffer = (List<int[]>) map.get("ecgDataBuffer");
			int[] paceBuffer = (int[]) map.get("paceBuffer");
			paceBuffer = (int[]) map.get("paceBuffer");
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
			int[] ecgAnas = StringUtil.getEcgDataINTs(ecgDataBuffer);
			//自动分析结论
			ecgAnalyse.initEcgAnalyseLib(3495.2533333f * 3);
			ecgAnalyse.analyseEcgData(ecgAnalyse, ecgAnas, 5000, 12, paceBuffer, 0, 0);
			
			int[] axis = ecgAnalyse.Axis;
			int[] resultCode = ecgAnalyse.ecgResult;
			Set<String> ECGResult = ECGData_Analyse.getAnalyseResult(resultCode);
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
			String Auto_Result = "";
			for (String str : ECGResult) {
				Auto_Result=Auto_Result+" "+str;
			}
			analyses.put("Auto_Result", Auto_Result);
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
			
			Map<Integer, int[]> datas = StringUtil.praseIntData(ecgDataBuffer);
			Map<String, StringBuffer> dataString = StringUtil.praseIntDataToString(ecgDataBuffer);
			xmlFilePath = FileUtil.saveToXMLFile(listMaps, dataString);
			ECGDao.getECGIntance(patient.getIdCardNo()).saveECGDataByMap(listMaps);
			pdfUtils.savePDF(MeasurECGApplication.getInstance(), listMaps, datas, waveGain, waveSpeed, waveSingleDisplay_Switch);
			msg = handler.obtainMessage(SAVE_PDFXML_FILE);
			msg.obj = xmlFilePath;
			handler.sendMessage(msg);
        }
    }
    
    public class ManulTask implements Runnable{


    	private Map<String, Object> map;
        public ManulTask(Map<String, Object> mapDatas) {
        	map = mapDatas;
		}

		@Override
        public void run() {

			RegisteredUser patient = MeasurECGApplication.getInstance().getRegisterUser();
			
			long ecg2DeviceData_time = (Long) map.get("ecg2DeviceData_time");
			int saveFileManualCount = (Integer) map.get("saveFileManualCount");
			int timeSecondCount = (Integer) map.get("timeSecondCount");
			
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
				
				int[] axis = ecgAnalyse.Axis;
//				int[] resultCode = ecgAnalyse.ecgResult;
//				Set<String> ECGResult = ECGData_Analyse.getAnalyseResult(resultCode);
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
//				String Auto_Result = "";
//				for (String str : ECGResult) {
//					Auto_Result=Auto_Result+" "+str;
//				}
//				analyses.put("Auto_Result", Auto_Result);
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
				
				pdfUtils.savePDFIntegers(MeasurECGApplication.getInstance(), listMaps, datas, waveGain, waveSpeed, waveSingleDisplay_Switch);
			}
			handler.sendEmptyMessage(SAVE_PDFXML_FILE);
		
        }
    }
}
