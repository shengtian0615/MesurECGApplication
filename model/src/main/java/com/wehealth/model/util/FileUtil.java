package com.wehealth.model.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.wehealth.model.domain.model.ECGData;
import com.wehealth.model.domain.model.EcgDataParam;
import com.wehealth.model.domain.model.ResultPassHelper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {

	public static String saveToXMLFile(Map<String, Map<String, String>> listMaps,
									   Map<String, StringBuffer> ecgDataMaps){
		String path = null;
		try {
			Map<String, String> pInfo = listMaps.get(Constant.ECG_PATIENT_INFO);
			Map<String, String> ecgAttr = listMaps.get(Constant.ECG_ANALYSE_PARAM);
			long timeLong = Long.valueOf(pInfo.get("ecg_checktime"));
			String time = DateUtils.sdf_yyyyMMddHHmmss.format(new Date(timeLong));
			File fileDir = new File(Environment.getExternalStorageDirectory()+"/ECGDATA/XML");
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			path = fileDir.getAbsolutePath() + File.separator + time + ".xml";

			FileOutputStream fos = new FileOutputStream(path);
			XmlSerializer serXML = Xml.newSerializer();
			serXML.setOutput(fos, "UTF-8");
			serXML.startDocument("UTF-8", true);
			serXML.startTag(null, "ECG");
			serXML.attribute(null, "Version", "1");
			serXML.attribute(null, "LS", "12");
//			serXML.attribute(null, "SAMPLERATE", ecgAttr.get("SAMPLERATE"));
			serXML.attribute(null, "FNotch", ecgAttr.get("FilterAC"));
			serXML.attribute(null, "FHP", ecgAttr.get("FilterBase"));
			serXML.attribute(null, "FLP", ecgAttr.get("FilterMC"));
			serXML.startTag(null, "Patient");
			serXML.startTag(null, "Name");
			serXML.text(pInfo.get("Name"));
			serXML.endTag(null, "Name");
			serXML.startTag(null, "ID");
			serXML.text(pInfo.get("ID"));
			serXML.endTag(null, "ID");
			serXML.startTag(null, "Age");
			serXML.text(pInfo.get("AGE"));
			serXML.endTag(null, "Age");
			serXML.startTag(null, "Gender");
			serXML.text(pInfo.get("Gender"));
			serXML.endTag(null, "Gender");
//			serXML.startTag(null, "RecordSecond");
//			serXML.text(pInfo.get("RecordSecond"));
//			serXML.endTag(null, "RecordSecond");
			serXML.startTag(null, "CheckDateTime");
			serXML.text(DateUtils.sdf_yyyy_MM_dd_HH_mm_ss.format(new Date(timeLong)));
			serXML.endTag(null, "CheckDateTime");
			serXML.endTag(null, "Patient");
			if (ecgDataMaps!=null) {

				serXML.startTag(null, "Ch0");
				serXML.text(ecgDataMaps.get("Ch0").toString());
				serXML.endTag(null, "Ch0");
				serXML.startTag(null, "Ch1");
				serXML.text(ecgDataMaps.get("Ch1").toString());
				serXML.endTag(null, "Ch1");
				serXML.startTag(null, "Ch2");
				serXML.text(ecgDataMaps.get("Ch2").toString());
				serXML.endTag(null, "Ch2");
				serXML.startTag(null, "Ch3");
				serXML.text(ecgDataMaps.get("Ch3").toString());
				serXML.endTag(null, "Ch3");
				serXML.startTag(null, "Ch4");
				serXML.text(ecgDataMaps.get("Ch4").toString());
				serXML.endTag(null, "Ch4");
				serXML.startTag(null, "Ch5");
				serXML.text(ecgDataMaps.get("Ch5").toString());
				serXML.endTag(null, "Ch5");
				serXML.startTag(null, "Ch6");
				serXML.text(ecgDataMaps.get("Ch6").toString());
				serXML.endTag(null, "Ch6");
				serXML.startTag(null, "Ch7");
				serXML.text(ecgDataMaps.get("Ch7").toString());
				serXML.endTag(null, "Ch7");
				serXML.startTag(null, "Ch8");
				serXML.text(ecgDataMaps.get("Ch8").toString());
				serXML.endTag(null, "Ch8");
				serXML.startTag(null, "Ch9");
				serXML.text(ecgDataMaps.get("Ch9").toString());
				serXML.endTag(null, "Ch9");
				serXML.startTag(null, "Ch10");
				serXML.text(ecgDataMaps.get("Ch10").toString());
				serXML.endTag(null, "Ch10");
				serXML.startTag(null, "Ch11");
				serXML.text(ecgDataMaps.get("Ch11").toString());
				serXML.endTag(null, "Ch11");
			}
			serXML.startTag(null, "Parameter");
//			serXML.attribute(null, "AnalysisState", pms.get("AnalysisState"));
//			serXML.attribute(null, "PWidth", pms.get("PWidth"));
//			serXML.attribute(null, "PExist", pms.get("PExist"));
			serXML.attribute(null, "RRInterval", ecgAttr.get("RRInterval"));
			serXML.attribute(null, "HeartRate", ecgAttr.get("HeartRate"));
			serXML.attribute(null, "PRInterval", ecgAttr.get("PRInterval"));
			serXML.attribute(null, "QRSDuration", ecgAttr.get("QRSDuration"));
			serXML.attribute(null, "QTD", ecgAttr.get("QTD"));
			serXML.attribute(null, "QTC", ecgAttr.get("QTC"));
			serXML.attribute(null, "PAxis", ecgAttr.get("PAxis"));
			serXML.attribute(null, "QRSAxis", ecgAttr.get("QRSAxis"));
			serXML.attribute(null, "TAxis", ecgAttr.get("TAxis"));
			serXML.attribute(null, "RV5SV1", ecgAttr.get("RV5SV1"));
			serXML.attribute(null, "RV5", ecgAttr.get("RV5"));
			serXML.attribute(null, "SV1", ecgAttr.get("SV1"));
			serXML.endTag(null, "Parameter");
//			Map<String, String> dmd = listMaps.get("DMData");
//			serXML.startTag(null, "DMData");
//			serXML.attribute(null, "PStart", dmd.get("PStart"));
//			serXML.attribute(null, "PEnd", dmd.get("PEnd"));
//			serXML.attribute(null, "QRSStart", dmd.get("QRSStart"));
//			serXML.attribute(null, "QRSEnd", dmd.get("QRSEnd"));
//			serXML.attribute(null, "TEnd", dmd.get("TEnd"));
//			serXML.attribute(null, "DMDataCount", dmd.get("DMDataCount"));
//			serXML.endTag(null, "DMData");
			serXML.startTag(null, "AnalysisResult");
			String result = ecgAttr.get("Auto_Result");
			serXML.startTag(null, "Result");
			serXML.text(result);
			serXML.endTag(null, "Result");
			serXML.endTag(null, "AnalysisResult");
//			serXML.startTag(null, "WaveQuality");
//			serXML.text(analys.get("WaveQuality"));
//			serXML.endTag(null, "WaveQuality");
			serXML.endTag(null, "ECG");
			serXML.endDocument();
			fos.flush();
			fos.close();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}
	public static String[] parseResultAdvice(String name, InputStream is) {
		String result[] = {"二", "不常见"};
		try {
			XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();
			XmlPullParser pullParser = pullFactory.newPullParser();
			pullParser.setInput(is, "UTF-8");
			int eventType = pullParser.getEventType();
			while (XmlPullParser.END_DOCUMENT != eventType) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					System.out.println("name = "+pullParser.getAttributeValue(null, "name"));
					if (name.equals(pullParser.getAttributeValue(null, "name"))) {
						result[0] = pullParser.getAttributeValue(null, "level");
						result[1] = pullParser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					if (pullParser.getName().equals("ECG")) {
						Log.i("FileScan", "解析完毕");
					}
					break; 
				default:
					break;
				}
				eventType = pullParser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	/**
	 * 根据路径获取华清心仪测量后的数据
	 * 并解析成ECGData
	 * @param path
	 * @return
	 */
	public static ECGData parseECGbyInputStream(String path){
		ECGData ecgData = null;
		File file  = new File(path);
		try {
			InputStream inputStream = new FileInputStream(file);
			ecgData = parserEcgData(inputStream);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return ecgData;
	}
	
	public static ECGData parserEcgData(InputStream is){
		ECGData ecgData = new ECGData();
		ECGDataUtil.ECGDataResultHelper resultHelper = new ECGDataUtil.ECGDataResultHelper();
		boolean isfirst = false;//判断是否是最后DMData里的心电数据
		try {
			XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();
			XmlPullParser pullParser = pullFactory.newPullParser();
			pullParser.setInput(is, "UTF-8");
			int eventType = pullParser.getEventType();
			while (XmlPullParser.END_DOCUMENT != eventType) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if ("ECG".equals(pullParser.getName())) {
						ecgData.setFhp(pullParser.getAttributeValue(null, "FHP"));
						ecgData.setFlp(pullParser.getAttributeValue(null, "FLP"));
						ecgData.setFnotch(pullParser.getAttributeValue(null, "FNotch"));
					}
//					Log.i("FileScan", "标签名称 :  "+pullParser.getName());
					if ("DMData".equals(pullParser.getName())) {
						isfirst = true;
					}
					if ("Ch0".equals(pullParser.getName()) && !isfirst) {// I
						String Ch0_I= pullParser.nextText();
						short array[] = UnicodeStringtoByte.StringToByte(Ch0_I);
						ecgData.setvI(ZipUtil.gZip(DataUtil.toByteArray(array)));
					}
					if ("Ch1".equals(pullParser.getName()) && !isfirst) {// II
						String Ch1_II = pullParser.nextText();
						ecgData.setvII(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.StringToByte(Ch1_II))));
					}
					if ("Ch2".equals(pullParser.getName()) && !isfirst) {// III
						String Ch2_III = pullParser.nextText();
						ecgData.setvIII(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.StringToByte(Ch2_III))));
					}
					if ("Ch3".equals(pullParser.getName()) && !isfirst) {// avR
						String Ch3_avR = pullParser.nextText();
						ecgData.setaVr(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.StringToByte(Ch3_avR))));
					}
					if ("Ch4".equals(pullParser.getName()) && !isfirst) {// avL
						String Ch4_avL = pullParser.nextText();
						ecgData.setaVl(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.StringToByte(Ch4_avL))));
					}
					if ("Ch5".equals(pullParser.getName()) && !isfirst) {// avF
						String Ch5_avF = pullParser.nextText();
						ecgData.setaVf(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.StringToByte(Ch5_avF))));
					}
					if ("Ch6".equals(pullParser.getName()) && !isfirst) {// v1
						String Ch6_v1 = pullParser.nextText();
						ecgData.setV1(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.StringToByte(Ch6_v1))));
					}
					if ("Ch7".equals(pullParser.getName()) && !isfirst) {// v2
						String Ch7_v2 = pullParser.nextText();
						ecgData.setV2(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.StringToByte(Ch7_v2))));
					}
					if ("Ch8".equals(pullParser.getName()) && !isfirst) {// v3
						String Ch8_v3 = pullParser.nextText();
						ecgData.setV3(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.StringToByte(Ch8_v3))));
					}
					if ("Ch9".equals(pullParser.getName()) && !isfirst) {// v4
						String Ch9_v4 = pullParser.nextText();
						ecgData.setV4(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.StringToByte(Ch9_v4))));
					}
					if ("Ch10".equals(pullParser.getName()) && !isfirst) {// v5
						String Ch10_v5 = pullParser.nextText();
						Log.i("FileScan", "name : "+pullParser.getName()+"value ： "+ Ch10_v5);
						ecgData.setV5(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.StringToByte(Ch10_v5))));
					}
					if ("Ch11".equals(pullParser.getName()) && !isfirst) {// v6
						String Ch11_v6 = pullParser.nextText();
						ecgData.setV6(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.StringToByte(Ch11_v6))));
					}
					if ("Parameter".equals(pullParser.getName())) {
						ecgData.setHeartRate(Integer.valueOf(pullParser.getAttributeValue(null, "HeartRate")));
						ecgData.setPr(Integer.valueOf(pullParser.getAttributeValue(null, "PRInterval")));
						ecgData.setQrs(Integer.valueOf(pullParser.getAttributeValue(null, "QRSDuration")));
						ecgData.setQtc(Integer.valueOf(pullParser.getAttributeValue(null, "QTC")));
						ecgData.setQt(Integer.valueOf(pullParser.getAttributeValue(null, "QTD")));
						ecgData.setPaxis(Integer.valueOf(pullParser.getAttributeValue(null, "PAxis")));
						ecgData.setQrsaxis(Integer.valueOf(pullParser.getAttributeValue(null, "QRSAxis")));
						ecgData.setTaxis(Integer.valueOf(pullParser.getAttributeValue(null, "TAxis")));
						ecgData.setRv5sv1(Double.valueOf(pullParser.getAttributeValue(null, "RV5SV1")));
						ecgData.setRv5(Double.valueOf(pullParser.getAttributeValue(null, "RV5")));
						ecgData.setSv1(Double.valueOf(pullParser.getAttributeValue(null, "SV1")));
					}
//					if ("ResultString".equals(pullParser.getName())) {
//						String ResCode = pullParser.nextText();
//						Log.i("FileScan", "name : "+pullParser.getName()+"value ： "+ ResCode);
//						ecgData.setAutoDiagnosisResult(ResCode);
//					}
					if ("Classfly".equals(pullParser.getName())) {
						String classCode = pullParser.getAttributeValue(null, "Code");
						String className = pullParser.nextText();
						resultHelper.getClassfy().put(classCode, className);
					}
					if ("Result".equals(pullParser.getName())) {
						String resultCode = pullParser.getAttributeValue(null, "Code");
						String resultName = pullParser.nextText();
						resultHelper.getResult().put(resultCode, resultName);
					}
					if ("WaveQuality".equals(pullParser.getName())) {
						String waveQuality = pullParser.nextText();
						if (!TextUtils.isEmpty(waveQuality)) {
							ecgData.setDoctorComment(waveQuality);
						}
					}
					
					//<Parameter AnalysisState="1" PWidth="49" PExist="1" RRInterval="0" HeartRate="80" PRInterval="82" 
					//QRSDuration="48" QTD="178" QTC="205" PAxis="18" QRSAxis="18" TAxis="17" RV5V6="0.000" SV1V2="0.000"
					//RV1SV5="0.000" RV5SV1="1583.0001" RV5="1172.0" SV1="-411.0">
					break;
				case XmlPullParser.END_TAG:
					if (pullParser.getName().equals("ECG")) {
						Log.i("FileScan", "解析完毕");
					}
					break; 
				default:
					break;
				}
				eventType = pullParser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				is.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		String autoResult = ECGDataUtil.encodeResult(resultHelper);
		ecgData.setAutoDiagnosisResult(autoResult);
		return ecgData;
	}

	public static ECGData parse2Data(String path){
		ECGData ecgData = null;
		File file  = new File(path);
		try {
			InputStream inputStream = new FileInputStream(file);
			XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();
			XmlPullParser pullParser = pullFactory.newPullParser();
			pullParser.setInput(inputStream, "UTF-8");
			int eventType = pullParser.getEventType();
			ecgData = new ECGData();
			ecgData.setVersion(1);
			while (XmlPullParser.END_DOCUMENT != eventType) {
				switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						if ("Ch0".equals(pullParser.getName())) {// I
							String Ch0_I= pullParser.nextText();
							int array[] = UnicodeStringtoByte.string2Int(Ch0_I);
							ecgData.setvI(ZipUtil.gZip(DataUtil.toByteArray(array)));
						}
						if ("Ch1".equals(pullParser.getName())) {// II
							String Ch1_II = pullParser.nextText();
							ecgData.setvII(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.string2Int(Ch1_II))));
						}
						if ("Ch2".equals(pullParser.getName())) {// III
							String Ch2_III = pullParser.nextText();
							ecgData.setvIII(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.string2Int(Ch2_III))));
						}
						if ("Ch3".equals(pullParser.getName())) {// avR
							String Ch3_avR = pullParser.nextText();
							ecgData.setaVr(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.string2Int(Ch3_avR))));
						}
						if ("Ch4".equals(pullParser.getName())) {// avL
							String Ch4_avL = pullParser.nextText();
							ecgData.setaVl(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.string2Int(Ch4_avL))));
						}
						if ("Ch5".equals(pullParser.getName())) {// avF
							String Ch5_avF = pullParser.nextText();
							ecgData.setaVf(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.string2Int(Ch5_avF))));
						}
						if ("Ch6".equals(pullParser.getName())) {// v1
							String Ch6_v1 = pullParser.nextText();
							ecgData.setV1(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.string2Int(Ch6_v1))));
						}
						if ("Ch7".equals(pullParser.getName())) {// v2
							String Ch7_v2 = pullParser.nextText();
							ecgData.setV2(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.string2Int(Ch7_v2))));
						}
						if ("Ch8".equals(pullParser.getName())) {// v3
							String Ch8_v3 = pullParser.nextText();
							ecgData.setV3(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.string2Int(Ch8_v3))));
						}
						if ("Ch9".equals(pullParser.getName())) {// v4
							String Ch9_v4 = pullParser.nextText();
							ecgData.setV4(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.string2Int(Ch9_v4))));
						}
						if ("Ch10".equals(pullParser.getName())) {// v5
							String Ch10_v5 = pullParser.nextText();
							ecgData.setV5(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.string2Int(Ch10_v5))));
						}
						if ("Ch11".equals(pullParser.getName())) {// v6
							String Ch11_v6 = pullParser.nextText();
							ecgData.setV6(ZipUtil.gZip(DataUtil.toByteArray(UnicodeStringtoByte.string2Int(Ch11_v6))));
						}
						if ("Parameter".equals(pullParser.getName())) {
							ecgData.setHeartRate(Integer.valueOf(pullParser.getAttributeValue(null, "HeartRate")));
							ecgData.setPr(Integer.valueOf(pullParser.getAttributeValue(null, "RRInterval")));
							ecgData.setQrs(Integer.valueOf(pullParser.getAttributeValue(null, "QRSDuration")));
							ecgData.setQtc(Integer.valueOf(pullParser.getAttributeValue(null, "QTC")));
							ecgData.setQt(Integer.valueOf(pullParser.getAttributeValue(null, "QTD")));
							ecgData.setPaxis(Integer.valueOf(pullParser.getAttributeValue(null, "PAxis")));
							ecgData.setQrsaxis(Integer.valueOf(pullParser.getAttributeValue(null, "QRSAxis")));
							ecgData.setTaxis(Integer.valueOf(pullParser.getAttributeValue(null, "TAxis")));
							ecgData.setRv5sv1(Double.valueOf(pullParser.getAttributeValue(null, "RV5SV1")));
							ecgData.setRv5(Double.valueOf(pullParser.getAttributeValue(null, "RV5")));
							ecgData.setSv1(Double.valueOf(pullParser.getAttributeValue(null, "SV1")));
						}
						if ("Result".equals(pullParser.getName())) {
							String resultName = pullParser.nextText();
							ecgData.setAutoDiagnosisResult(resultName);
						}

						break;
					case XmlPullParser.END_TAG:
						if (pullParser.getName().equals("ECG")) {
							Log.i("FileScan", "解析完毕");
						}
						break;
					default:
						break;
				}
				eventType = pullParser.next();
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ecgData;
	}
	
	/**
	 * 获取结论的分级建议
	 * @param context
	 * @param result
	 * @return 级别建议
	 */
	public static ResultPassHelper obtainWarnLevelMap(Context context, String result) {
		ResultPassHelper rph = new ResultPassHelper();
		InputStream is = null;
		boolean is_replace = false;
		if (result.contains("ST & T异常")) {
			result = result.replace("ST & T异常", "");
			is_replace = true;
		}
		try {
			is = context.getAssets().open("ecgdata_resultadvice.xml");
			Map<String, String> map = parserLevelAdvice(result, is);
			if (map==null || map.isEmpty()) {
				return null;
			}
			if (map.containsKey("三")) {//含有危险级别最高
				rph.setName("三");
				return rph;
			}
			if (map.containsKey("二")) {//含有危险级别次之
				rph.setName("二");
				return rph;
			}
			if (is_replace) {//不含有危险级别次之，但被替换了结论
				rph.setName("二");
				return rph;
			}
			if (map.containsKey("一")) {//正常心电图
				rph.setName("一");
				return rph;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally{
			try {
				if (is!=null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 解析文件
	 * @param result
	 * @param is
	 * @return
	 */
	@SuppressLint("UseSparseArrays")
	public static Map<String, String> parserLevelAdvice(String result, InputStream is) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();
			XmlPullParser pullParser = pullFactory.newPullParser();
			pullParser.setInput(is, "UTF-8");
			int eventType = pullParser.getEventType();
			while (XmlPullParser.END_DOCUMENT != eventType) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					String[] r = result.split(" ");
					for (int i = 0; i < r.length; i++) {
						String name = pullParser.getAttributeValue(null, "name");
						String level = pullParser.getAttributeValue(null, "level");
						if (r[i].equals(name)) {
							String strLevel = level;
							map.put(strLevel, "");
							break;
						}
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				default:
					break;
				}
				eventType = pullParser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	/**
	 * 向/sdcard/apmt路径下添加文本
	 * @param text 文本信息
	 */
	public static void addFile(String text){
		addFile(text,Environment.getExternalStorageDirectory().getPath()+"/apmt/","wechat_pay.txt");
	}

	/**
	 * 想该路径添加文本
	 * @param text 文本信息
	 * @param path 路径
	 */
	public static void addFile(String text, String path, String fileName){
		File file = new File(path+fileName);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}

		FileWriter fileWriter = null;
		try {
			fileWriter =new FileWriter(path+fileName);
			fileWriter.write(text);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(fileWriter!=null){
					fileWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileFormat(String fileName) {
		if (StringUtil.isEmpty(fileName))
			return "";

		int point = fileName.lastIndexOf('.');
		return fileName.substring(point + 1);
	}
	
	/**
	 * 清空文件夹或删除文件
	 * @param file
	 */
	public static void delete(File file) {
		if (file.isFile()) {  
			file.delete();  
			return;  
		}  
		if(file.isDirectory()){  
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {  
				file.delete();  
				return;  
			}  

			for (int i = 0; i < childFiles.length; i++) {  
				delete(childFiles[i]);  
			}  
			file.delete();  
		}
	} 
	/**
	 * 获取手机剩余内存
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static long getSDFreeSize(){
		//取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		//获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize(); 
		//空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		//返回SD卡空闲大小
		//return freeBlocks * blockSize;  //单位Byte
		//return (freeBlocks * blockSize)/1024;   //单位KB
		return (freeBlocks * blockSize)/1024 /1024; //单位MB
	}
	
	/**
	 * 根据文件绝对路径获取文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (StringUtil.isEmpty(filePath))
			return "";
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}
	
	/**
	 * 4.4版本以上处理选择图片Uri的问题
	 * @param context
	 * @param uri
	 * @return
	 */
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {
	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	    // DocumentProvider
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	        // ExternalStorageProvider
	        if (isExternalStorageDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];
	            if ("primary".equalsIgnoreCase(type)) {
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }
	            // TODO handle non-primary volumes
	        }
	        // DownloadsProvider
	        else if (isDownloadsDocument(uri)) {
	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
	            return getDataColumn(context, contentUri, null, null);
	        }
	        // MediaProvider
	        else if (isMediaDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];
	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }
	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };
	            return getDataColumn(context, contentUri, selection, selectionArgs);
	        }
	    }
	    // MediaStore (and general)
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {
	        // Return the remote address
	        if (isGooglePhotosUri(uri))
	            return uri.getLastPathSegment();
	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }
	    return null;
	}
	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };
	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}
	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}
	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}
	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}
	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}
	/**
		 * 根据心电档案查询华清心仪的数据参数
		 * @param path
		 * @return
		 */
	public static EcgDataParam parserEcgParam(String path) {
		EcgDataParam edp;
		File file  = new File(path);
		try {
			InputStream inputStream = new FileInputStream(file);
			edp = getECGDP(inputStream);
		}catch (FileNotFoundException e){
			e.printStackTrace();
			return null;
		}
		return edp;
	}
	
	public static EcgDataParam getECGDP(InputStream is){
		if (is == null) {
			return null;
		}
		EcgDataParam edp = new EcgDataParam();
		ECGDataUtil.ECGDataResultHelper resultHelper = new ECGDataUtil.ECGDataResultHelper();
		boolean isfirst = false;//判断是否是最后DMData里的心电数据
		try {
			XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();
			XmlPullParser pullParser = pullFactory.newPullParser();
			pullParser.setInput(is, "UTF-8");
			int eventType = pullParser.getEventType();
			while (XmlPullParser.END_DOCUMENT != eventType) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if ("Age".equals(pullParser.getName())) {
						edp.setAge(pullParser.nextText());
					}
					if ("Gender".equals(pullParser.getName())) {
						edp.setGender(pullParser.nextText());
					}
					if ("Name".equals(pullParser.getName())) {
						edp.setName(pullParser.nextText());
					}
					if ("ECG".equals(pullParser.getName())) {
						edp.setFHP(pullParser.getAttributeValue(null, "FHP"));
						edp.setFLP(pullParser.getAttributeValue(null, "FLP"));
					}
					if ("DMData".equals(pullParser.getName())) {
						isfirst = true;
					}
					if ("Ch0".equals(pullParser.getName()) && !isfirst) {// I
						String Ch0_I= pullParser.nextText();
						edp.setI(UnicodeStringtoByte.StringToByte(Ch0_I));
					}
					if ("Ch1".equals(pullParser.getName()) && !isfirst) {// II
						String Ch1_II = pullParser.nextText();
						edp.setIi(UnicodeStringtoByte.StringToByte(Ch1_II));
					}
					if ("Ch2".equals(pullParser.getName()) && !isfirst) {// III
						String Ch2_III = pullParser.nextText();
						edp.setIii(UnicodeStringtoByte.StringToByte(Ch2_III));
					}
					if ("Ch3".equals(pullParser.getName()) && !isfirst) {// avR
						String Ch3_avR = pullParser.nextText();
						edp.setAvR(UnicodeStringtoByte.StringToByte(Ch3_avR));
					}
					if ("Ch4".equals(pullParser.getName()) && !isfirst) {// avL
						String Ch4_avL = pullParser.nextText();
						edp.setAvL(UnicodeStringtoByte.StringToByte(Ch4_avL));
					}
					if ("Ch5".equals(pullParser.getName()) && !isfirst) {// avF
						String Ch5_avF = pullParser.nextText();
						edp.setAvF(UnicodeStringtoByte.StringToByte(Ch5_avF));
					}
					if ("Ch6".equals(pullParser.getName()) && !isfirst) {// v1
						String Ch6_v1 = pullParser.nextText();
						edp.setV1(UnicodeStringtoByte.StringToByte(Ch6_v1));
					}
					if ("Ch7".equals(pullParser.getName()) && !isfirst) {// v2
						String Ch7_v2 = pullParser.nextText();
						edp.setV2(UnicodeStringtoByte.StringToByte(Ch7_v2));
					}
					if ("Ch8".equals(pullParser.getName()) && !isfirst) {// v3
						String Ch8_v3 = pullParser.nextText();
						edp.setV3(UnicodeStringtoByte.StringToByte(Ch8_v3));
					}
					if ("Ch9".equals(pullParser.getName()) && !isfirst) {// v4
						String Ch9_v4 = pullParser.nextText();
						edp.setV4(UnicodeStringtoByte.StringToByte(Ch9_v4));
					}
					if ("Ch10".equals(pullParser.getName()) && !isfirst) {// v5
						String Ch10_v5 = pullParser.nextText();
						edp.setV5(UnicodeStringtoByte.StringToByte(Ch10_v5));
					}
					if ("Ch11".equals(pullParser.getName()) && !isfirst) {// v6
						String Ch11_v6 = pullParser.nextText();
						edp.setV6(UnicodeStringtoByte.StringToByte(Ch11_v6));
					}
					
					if ("Parameter".equals(pullParser.getName())) {
						edp.setAnalysisState(pullParser.getAttributeValue(null, "AnalysisState"));
						edp.setPWidth(pullParser.getAttributeValue(null, "PWidth"));
						edp.setPExist(pullParser.getAttributeValue(null, "PExist"));
						edp.setRRInterval(pullParser.getAttributeValue(null, "RRInterval"));
						edp.setHeartRate(pullParser.getAttributeValue(null, "HeartRate"));
						edp.setPRInterval(pullParser.getAttributeValue(null, "PRInterval"));
						edp.setQRSDuration(pullParser.getAttributeValue(null, "QRSDuration"));
						edp.setQTC(pullParser.getAttributeValue(null, "QTC"));
						edp.setQTD(pullParser.getAttributeValue(null, "QTD"));
						edp.setPAxis(pullParser.getAttributeValue(null, "PAxis"));
						edp.setQRSAxis(pullParser.getAttributeValue(null, "QRSAxis"));
						edp.setTAxis(pullParser.getAttributeValue(null, "TAxis"));
						edp.setRV5SV1(pullParser.getAttributeValue(null, "RV5SV1"));
						edp.setRV5(pullParser.getAttributeValue(null, "RV5"));
						edp.setSV1(pullParser.getAttributeValue(null, "SV1"));
					}
					if ("Classfly".equals(pullParser.getName())) {
						String classCode = pullParser.getAttributeValue(null, "Code");
						String className = pullParser.nextText();
						resultHelper.getClassfy().put(classCode, className);
					}
					if ("Result".equals(pullParser.getName())) {
						String resultCode = pullParser.getAttributeValue(null, "Code");
						String resultName = pullParser.nextText();
						resultHelper.getResult().put(resultCode, resultName);
					}
					break;
				case XmlPullParser.END_TAG:
					if (pullParser.getName().equals("ECG")) {
						Log.i("FileScan", "解析完毕");
					}
					break; 
				default:
					break;
				}
				eventType = pullParser.next();
			}
		}catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				is.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		String autoResult = ECGDataUtil.encodeResult(resultHelper);
		edp.setAutoDiagnosisResult(autoResult);
		return edp;
	}
}
