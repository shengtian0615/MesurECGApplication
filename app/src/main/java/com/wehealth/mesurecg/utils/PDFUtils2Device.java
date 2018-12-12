package com.wehealth.mesurecg.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;
import android.net.Uri;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;
import com.wehealth.mesurecg.R;
import com.wehealth.model.domain.model.PrintModel;
import com.wehealth.model.util.Constant;
import com.wehealth.model.util.DateUtils;
import com.wehealth.model.util.SampleDotIntNew;
import com.wehealth.model.util.StringUtil;

@SuppressLint("SimpleDateFormat")
public class PDFUtils2Device {
	private int LEADNUM = 12;
	private String leadName[] = { "I", "II", "III", "aVR", "aVL", "aVF", "V1",
			"V2", "V3", "V4", "V5", "V6" };
	float oldY[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	int currentX = 0;
	private int baseY[];
	private int baseX[];
	int MARGIN = 4;
	int GRID_SIZE = 3;
	int xGridNum;
	int yGridNum;
	int screenWidth = 1500;   //810;
	int screenHeight = 1100;  //460;
	
	private int titleHeight_1 = 1040;
	private int titleHeight_2 = 1015;
	private int titleHeight_3 = 945;
	
	private SampleDotIntNew sampleDot[];
	private final int paceMaker = 2147483647;
	private final int ECG_SAMPLE_RATE = 500;
	private final int DESTINATION_SAMPlE_RATE = 138;// 6*2ch等于138；
	private int waveGain = 2;
	private int displayStyle = 0;
	private int waveSpeed = 0;
	private int waveDisplay_LeadSwitch = 0;
	private BaseFont btChina;
	
	private List<PrintModel> pms;
	private PrintModel pm;
	
	/**
	 * 保存成PDF文件
	 * @param context
	 * @param listMaps 测量人的信息和分析参数
	 * @param ecgDataMaps 自动分析结果和检查时间
	 * @param ecgDataMaps 心电数据
	 * @param waveSingleDisplay_Switch 
	 * @return
	 */
	public boolean savePDF(Context context,
			Map<String, Map<String, String>> listMaps,
			Map<Integer, int[]> ecgDataMaps, int gain, int speed, int waveSingleDisplay_Switch) {//, List<Boolean> pace
		Map<String, String> pInfo = listMaps.get(Constant.ECG_PATIENT_INFO);
		Map<String, String> analysis = listMaps.get(Constant.ECG_ANALYSE_PARAM);
		String ecg_checktime = pInfo.get("ecg_checktime");
		//Document doc = new Document(PageSize.A4.rotate());
		displayStyle = PreferUtils.getIntance().getDisplayStyle();
		Document doc = new Document(new Rectangle(screenHeight, screenWidth).rotate());
		FileOutputStream fos;
		try {
			File fileDir = new File(Environment.getExternalStorageDirectory()+"/ECGDATA/PDF");
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			String patient_Id = pInfo.get("ID");
			String time = DateUtils.sdf_yyyyMMddHHmmss.format(new Date(Long.valueOf(ecg_checktime)));
			
//			try {
//				InputStream is = context.getResources().openRawResource(R.raw.freesans);
//				byte[] byteis = new byte[is.available()];
//				is.read(byteis);
//				btChina = BaseFont.createFont("freesans.ttf", BaseFont.IDENTITY_H, true, false, byteis, null);
//				if (is!=null) {
//					is.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
				btChina = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//				StringUtils.writeException(e, "BaseFont");
//			}
			fos = new FileOutputStream(new File(fileDir.getAbsolutePath() + File.separator + patient_Id + "_" + time + ".pdf"));
			PdfWriter writer = PdfWriter.getInstance(doc, fos);
			doc.open();
			doc.setPageCount(1);
			
			waveGain = gain;
			waveSpeed = speed;
			waveDisplay_LeadSwitch = waveSingleDisplay_Switch;
			
			pms = new ArrayList<PrintModel>();
			pm = new PrintModel(PdfContentByte.ALIGN_CENTER, "12导同步心电图报告单", 30, 730, titleHeight_1);
			pms.add(pm);
			
			String content = null;
			if (displayStyle == 0) {
				content = "6ch × 2";
			}else if (displayStyle == 1) {
				content = "12ch × 1";
			}else if (displayStyle == 2) {
				content = "1ch × 1";
			}
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, content, 20, 30, titleHeight_2);
			pms.add(pm);
			
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "姓名：", 13, 30, titleHeight_3+45);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "ID：", 13, 30, titleHeight_3+30);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "性别：", 13, 30, titleHeight_3+15);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "生日：", 13, 30, titleHeight_3);
			pms.add(pm);

		    //用户信息
			String name = pInfo.get("Name");
			String gender = pInfo.get("Gender");
			String age = pInfo.get("AGE");
			if (gender.equals("0")) {
				gender = "男";
			}else if (gender.equals("1")) {
				gender = "女";
			}
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, name, 13, 80, titleHeight_3+45);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, patient_Id, 13, 80, titleHeight_3+30);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, gender, 13, 80, titleHeight_3+15);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, age+"岁", 13, 80, titleHeight_3);
			pms.add(pm);

			//心电分析信息
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "【心电图特征】", 20, 250, titleHeight_2);
			pms.add(pm);

//			tf = new TextField(writer, new Rectangle(220, titleHeight_3, 220 + 70, titleHeight_3 + 60), "Lists");
//			tf.setTextColor(BaseColor.BLACK);
//			tf.setFontSize(13);
//			tf.setFont(btChina);
//			tf.setChoices(new String[] { "HR           :"});//, "P/QRS/T:", "RV5         :", "SV1          :" 
//			tf.setRotation(90);
//			// tf.setChoiceSelection(4);
//			field = tf.getListField();
//			writer.addAnnotation(field);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "SV1：", 13, 250, titleHeight_3);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "RV5：", 13, 250, titleHeight_3+15);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "P/QRS/T：", 13, 250, titleHeight_3+30);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "HR：", 13, 250, titleHeight_3+45);
			pms.add(pm);

			String PAxis = analysis.get("PAxis");
			String QRSAxis = analysis.get("QRSAxis");
			String TAxis = analysis.get("TAxis");
			String PQRST = PAxis+"/"+QRSAxis+"/"+TAxis+" °";
			String RV5 = analysis.get("RV5");
			String SV1 = analysis.get("SV1");
			String HR = analysis.get("HeartRate");
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, HR+" bpm", 13, 310, titleHeight_3+45);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, PQRST, 13, 310, titleHeight_3+30);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, RV5+" mV", 13, 310, titleHeight_3+15);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, SV1+" mV", 13, 310, titleHeight_3);
			pms.add(pm);

			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "PR间期：", 13, 400, titleHeight_3+45);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "QRS时限：", 13, 400, titleHeight_3+30);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "QT/QTc间期：", 13, 400, titleHeight_3+15);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "RV5 + SV1：", 13, 400, titleHeight_3);
			pms.add(pm);

			String PR = analysis.get("PRInterval");
			String QRS = analysis.get("QRSDuration");
			String QT = analysis.get("QTD");
			String QTC = analysis.get("QTC");
			String QTQTC = QT+"/"+QTC;
			String RV5SV1 = analysis.get("RV5SV1");
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, PR+" ms", 13, 490, titleHeight_3+45);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, QRS+" ms", 13, 490, titleHeight_3+30);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, QTQTC+" ms", 13, 490, titleHeight_3+15);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, RV5SV1+" mV", 13, 490, titleHeight_3);
			pms.add(pm);
			
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "【心电图诊断】  ", 20, 600, titleHeight_2);
			pms.add(pm);

//			tf = new TextField(writer, new Rectangle(490, titleHeight_2, 490 + 120, titleHeight_2 + 15), "Dickens");
//			tf.setTextColor(BaseColor.BLACK);
//			tf.setFontSize(10);
//			tf.setFont(btChina);
//			tf.setText(ecg_class);
//			tf.setAlignment(Element.ALIGN_LEFT);
//			// tf.setOptions(TextField.MULTILINE | TextField.REQUIRED);
//			tf.setRotation(90);
//			field = tf.getTextField();
//			writer.addAnnotation(field);

			TextField tf = new TextField(writer, new Rectangle(590, titleHeight_3, 590 + 400, titleHeight_3 + 60), "Dickens");
			tf.setTextColor(BaseColor.BLACK);
			tf.setFontSize(13);
			tf.setFont(btChina);
			tf.setText(analysis.get("Auto_Result"));
			tf.setAlignment(Element.ALIGN_LEFT);
			tf.setOptions(TextField.MULTILINE | TextField.REQUIRED);
			tf.setRotation(90);
			PdfFormField field = tf.getTextField();
			writer.addAnnotation(field);
			
//			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, analysis.get("Auto_Result"), 20, 590, titleHeight_3 + 45);
//			pms.add(pm);
			//滤波：工频滤波、基线滤波、肌电滤波  添加增益、纸速
			String FilterBase = analysis.get("FilterBase");
			String FilterMC = analysis.get("FilterMC");
			String FilterAC = analysis.get("FilterAC");
			String gainStr = analysis.get("Gain");
			String speedStr = analysis.get("Speed");
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "工频滤波：" + FilterAC, 15, 1000, titleHeight_3);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "基线滤波：" + FilterBase, 15, 1000, titleHeight_3+15);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "肌电滤波："+FilterMC, 15, 1000, titleHeight_3+30);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "纸速：          " + speedStr+"mm/s", 15, 1000, titleHeight_3+45);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "增益：          " + gainStr+"mm/mV", 15, 1000, titleHeight_3+60);
			pms.add(pm);
			

			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "诊断医生:____________________", 15, 1250, titleHeight_3);
			pms.add(pm);

			String checkTime = DateUtils.sdf_yyyy_MM_dd_HH_mm_ss.format(new Date(Long.valueOf(ecg_checktime)));
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "检查日期：" + checkTime, 15, 1250, titleHeight_3+30);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "检查日期：" + checkTime, 15, 1250, titleHeight_3+30+20);
			pms.add(pm);
			
			writerLine(writer, 20f, 30f, screenWidth - 20f, 30f, 0);

			pm = new PrintModel(PdfContentByte.ALIGN_RIGHT, "注：所有参数和结论需经医生最终确认", 13, 1400, 13);
			pms.add(pm);
			
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "V1.0.0", 13, 150, 13);
			pms.add(pm);
			
			prinText(writer);

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Bitmap logobitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.wuweikang);
			logobitmap.compress(Bitmap.CompressFormat.PNG /* FileType */,
			                        100 /* Ratio */, stream);
			Image logoPNG = Image.getInstance(stream.toByteArray());
			logoPNG.scalePercent(60);
			logoPNG.setAbsolutePosition(30, 5);
			doc.add(logoPNG);

			baseY = new int[LEADNUM];
			baseX = new int[LEADNUM];
			if (displayStyle==0) {
				for (int i = 0; i < LEADNUM; i++) {
					baseY[i] = (screenHeight - (screenHeight - 195) / LEADNUM * 2 * (i % 6 + 1)) - 90;
				}
				for (int i = 0; i < LEADNUM; i++) {
					if (i < 6) {
						baseX[i] = 45;
					} else {
						baseX[i] = screenWidth / 2 + MARGIN + 20;
					}
				}
			}else {// if (displayStyle==1)
				for (int i = 0; i < LEADNUM; i++) {
					baseY[i] = (screenHeight - (screenHeight - 195) / LEADNUM * (i % 12 + 1)) - 145;
				}
				for (int i = 0; i < LEADNUM; i++) {
					baseX[i] = 55;
				}
			}
			pdfDrawHorizontaLine(writer, 35f, screenHeight - 160f);

			pdfDrawVerticaLine(writer, 20f, screenWidth - 20f);
			pdfDraWaveTag(writer, leadName);
			
			sampleDot = new SampleDotIntNew[12+1];
			int des_rate;
			if (speed==2) {
				des_rate = DESTINATION_SAMPlE_RATE * 2;
			}else {
				des_rate = DESTINATION_SAMPlE_RATE/(speed+1);
			}
			for (int i = 0; i < LEADNUM+1; i++) {
				sampleDot[i] = new SampleDotIntNew(ECG_SAMPLE_RATE);
				sampleDot[i].setDesSampleDot(des_rate);
			}
			pdfDrawWaveLine(writer, ecgDataMaps);

			// 一定要记得关闭document对象
			doc.close();
			fos.flush();
			fos.close();
			
			/* 用于解决文件保存之后连上PC无法显示的问题   */
			MediaScannerConnection.scanFile(context,new String[] {Environment.getExternalStorageDirectory().getAbsolutePath()+"/ECGDATA/"},null,
			   new MediaScannerConnection.OnScanCompletedListener() {
			            public void onScanCompleted(String path, Uri uri) {        

			            }
			   });
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 保存成PDF文件
	 * @param context
	 * @param listMaps 测量人的信息和分析参数
	 * @param ecgDataMaps 心电数据为Integer
	 * @param waveSingleDisplay_Switch 
	 * @return
	 */
	public boolean savePDFIntegers(Context context,
			Map<String, Map<String, String>> listMaps,
			Map<Integer, Integer[]> ecgDataMaps, int gain, int speed, int waveSingleDisplay_Switch) {//, List<Boolean> pace
		Map<String, String> pInfo = listMaps.get(Constant.ECG_PATIENT_INFO);
		Map<String, String> analysis = listMaps.get(Constant.ECG_ANALYSE_PARAM);
		String ecg_checktime = pInfo.get("ecg_checktime");
		//Document doc = new Document(PageSize.A4.rotate());
		displayStyle = PreferUtils.getIntance().getDisplayStyle();
		Document doc = new Document(new Rectangle(screenHeight, screenWidth).rotate());
		FileOutputStream fos;
		try {
			File fileDir = new File(Environment.getExternalStorageDirectory()+"/ECGDATA/PDF");
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			String patient_Id = pInfo.get("ID");
			String time = DateUtils.sdf_yyyyMMddHHmmss.format(new Date(Long.valueOf(ecg_checktime)));
			
//			try {
//				InputStream is = context.getResources().openRawResource(R.raw.freesans);
//				byte[] byteis = new byte[is.available()];
//				is.read(byteis);
//				btChina = BaseFont.createFont("freesans.ttf", BaseFont.IDENTITY_H, true, false, byteis, null);
//				if (is!=null) {
//					is.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
				btChina = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//				StringUtils.writeException(e, "BaseFont");
//			}
			fos = new FileOutputStream(new File(fileDir.getAbsolutePath() + File.separator + patient_Id + "_" + time + ".pdf"));
			PdfWriter writer = PdfWriter.getInstance(doc, fos);
			doc.open();
			doc.setPageCount(1);
			
			waveGain = gain;
			waveSpeed = speed;
			waveDisplay_LeadSwitch = waveSingleDisplay_Switch;
			
			pms = new ArrayList<PrintModel>();
			pm = new PrintModel(PdfContentByte.ALIGN_CENTER, "12导同步心电图报告单", 30, 730, titleHeight_1);
			pms.add(pm);
			
			String content = null;
			if (displayStyle == 0) {
				content = "6ch × 2";
			}else if (displayStyle == 1) {
				content = "12ch × 1";
			}else if (displayStyle == 2) {
				content = "1ch × 1";
			}
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, content, 20, 30, titleHeight_2);
			pms.add(pm);
			
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "姓名：", 13, 30, titleHeight_3+45);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "ID：", 13, 30, titleHeight_3+30);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "性别：", 13, 30, titleHeight_3+15);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "生日：", 13, 30, titleHeight_3);
			pms.add(pm);

		    //用户信息
			String name = pInfo.get("Name");
			String gender = pInfo.get("Gender");
			String age = pInfo.get("AGE");
			if (gender.equals("0")) {
				gender = "男";
			}else if (gender.equals("1")) {
				gender = "女";
			}
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, name, 13, 80, titleHeight_3+45);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, patient_Id, 13, 80, titleHeight_3+30);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, gender, 13, 80, titleHeight_3+15);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, age+"岁", 13, 80, titleHeight_3);
			pms.add(pm);

			//心电分析信息
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "【心电图特征】", 20, 250, titleHeight_2);
			pms.add(pm);

			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "SV1：          ", 13, 250, titleHeight_3);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "RV5：         ", 13, 250, titleHeight_3+15);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "P/QRS/T：", 13, 250, titleHeight_3+30);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "HR：     ", 13, 250, titleHeight_3+45);
			pms.add(pm);

			String PAxis = analysis.get("PAxis");
			String QRSAxis = analysis.get("QRSAxis");
			String TAxis = analysis.get("TAxis");
			String PQRST = PAxis+"/"+QRSAxis+"/"+TAxis+" °";
			String RV5 = analysis.get("RV5");
			String SV1 = analysis.get("SV1");
			String HR = analysis.get("HeartRate");
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, HR+" bpm", 13, 320, titleHeight_3+45);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, PQRST, 13, 320, titleHeight_3+30);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, RV5+" mV", 13, 320, titleHeight_3+15);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, SV1+" mV", 13, 320, titleHeight_3);
			pms.add(pm);

			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "PR间期：           ", 13, 400, titleHeight_3+45);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "QRS时限：        ", 13, 400, titleHeight_3+30);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "QT/QTc间期：", 13, 400, titleHeight_3+15);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "RV5 + SV1：     ", 13, 400, titleHeight_3);
			pms.add(pm);

//			tf = new TextField(writer, new Rectangle(480, titleHeight_3, 480 + 90, titleHeight_3 + 60), "Lists");
//			tf.setTextColor(BaseColor.BLACK);
//			tf.setFontSize(13);
//			tf.setFont(btChina);
//			tf.setAlignment(Element.ALIGN_RIGHT);
//			tf.setOptions(TextField.REQUIRED);
			String PR = analysis.get("PRInterval");
			String QRS = analysis.get("QRSDuration");
			String QT = analysis.get("QTD");
			String QTC = analysis.get("QTC");
			String QTQTC = QT+"/"+QTC;
			String RV5SV1 = analysis.get("RV5SV1");
//			tf.setChoices(new String[] { PR+" ms", QRS+" ms", QTQTC+" ms", RV5SV1+" mV" });
//			tf.setRotation(90);
//			// tf.setChoiceSelection(4);
//			field = tf.getListField();
//			writer.addAnnotation(field);
			
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, PR+" ms", 13, 500, titleHeight_3+45);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, QRS+" ms", 13, 500, titleHeight_3+30);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, QTQTC+" ms", 13, 500, titleHeight_3+15);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, RV5SV1+" mV", 13, 500, titleHeight_3);
			pms.add(pm);

			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "【心电图诊断】  ", 20, 600, titleHeight_2);
			pms.add(pm);

			//滤波：工频滤波、基线滤波、肌电滤波  添加增益、纸速
			String FilterBase = analysis.get("FilterBase");
			String FilterMC = analysis.get("FilterMC");
			String FilterAC = analysis.get("FilterAC");
			String gainStr = analysis.get("Gain");
			String speedStr = analysis.get("Speed");
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "工频滤波：" + FilterAC, 15, 1000, titleHeight_3);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "基线滤波：" + FilterBase, 15, 1000, titleHeight_3+15);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "肌电滤波："+FilterMC, 15, 1000, titleHeight_3+30);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "纸速：        " + speedStr+"mm/s", 15, 1000, titleHeight_3+45);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "增益：        " + gainStr+"mm/mV", 15, 1000, titleHeight_3+60);
			pms.add(pm);
			

			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "诊断医生:____________________", 15, 1250, titleHeight_3);
			pms.add(pm);

			String checkTime = DateUtils.sdf_yyyy_MM_dd_HH_mm_ss.format(new Date(Long.valueOf(ecg_checktime)));
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "检查日期：" + checkTime, 15, 1250, titleHeight_3+30);
			pms.add(pm);
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "检查日期：" + checkTime, 15, 1250, titleHeight_3+30+20);
			pms.add(pm);
			
			writerLine(writer, 20f, 30f, screenWidth - 20f, 30f, 0);

			pm = new PrintModel(PdfContentByte.ALIGN_RIGHT, "注：所有参数和结论需经医生最终确认", 13, 1400, 13);
			pms.add(pm);
			
			pm = new PrintModel(PdfContentByte.ALIGN_LEFT, "V1.0.0", 13, 150, 13);
			pms.add(pm);
			
			prinText(writer);

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Bitmap logobitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.wuweikang);
			logobitmap.compress(Bitmap.CompressFormat.PNG /* FileType */,
			                        100 /* Ratio */, stream);
			Image logoPNG = Image.getInstance(stream.toByteArray());
			logoPNG.scalePercent(60);
			logoPNG.setAbsolutePosition(30, 5);
			doc.add(logoPNG);

			baseY = new int[LEADNUM];
			baseX = new int[LEADNUM];
			if (displayStyle==0) {
				for (int i = 0; i < LEADNUM; i++) {
					baseY[i] = (screenHeight - (screenHeight - 195) / LEADNUM * 2 * (i % 6 + 1)) - 90;
				}
				for (int i = 0; i < LEADNUM; i++) {
					if (i < 6) {
						baseX[i] = 45;
					} else {
						baseX[i] = screenWidth / 2 + MARGIN + 20;
					}
				}
			}else {// if (displayStyle==1)
				for (int i = 0; i < LEADNUM; i++) {
					baseY[i] = (screenHeight - (screenHeight - 195) / LEADNUM * (i % 12 + 1)) - 145;
				}
				for (int i = 0; i < LEADNUM; i++) {
					baseX[i] = 55;
				}
			}
			pdfDrawHorizontaLine(writer, 35f, screenHeight - 160f);

			pdfDrawVerticaLine(writer, 20f, screenWidth - 20f);
			pdfDraWaveTag(writer, leadName);
			
			sampleDot = new SampleDotIntNew[12+1];
			int des_rate;
			if (speed==2) {
				des_rate = DESTINATION_SAMPlE_RATE * 2;
			}else {
				des_rate = DESTINATION_SAMPlE_RATE/(speed+1);
			}
			for (int i = 0; i < LEADNUM+1; i++) {
				sampleDot[i] = new SampleDotIntNew(ECG_SAMPLE_RATE);
				sampleDot[i].setDesSampleDot(des_rate);
			}
//			StringUtils.writException2File("/sdcard/manual.txt", "\n 开始画心电曲线");
			
			pdfDrawWaveLineInteger(writer, ecgDataMaps);

			// 一定要记得关闭document对象
			doc.close();
			fos.flush();
			fos.close();
			
			/* 用于解决文件保存之后连上PC无法显示的问题   */
			MediaScannerConnection.scanFile(context,new String[] {Environment.getExternalStorageDirectory().getAbsolutePath()+"/ECGDATA/"},null,
			   new MediaScannerConnection.OnScanCompletedListener() {
			            public void onScanCompleted(String path, Uri uri) {        

			            }
			   });
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			StringUtil.writeException(e1, "FileNotFoundException");
		} catch (DocumentException e) {
			e.printStackTrace();
			StringUtil.writeException(e, "FileNotFoundException");
		} catch (IOException e) {
			e.printStackTrace();
			StringUtil.writeException(e, "FileNotFoundException");
		}
		return false;
	}
	
	
	
	/**画文字信息**/
	private void prinText(PdfWriter writer) {
		PdfContentByte pdfCB = writer.getDirectContent();
		pdfCB.beginText();

		for (PrintModel pm : pms) {
			pdfCB.setFontAndSize(btChina, pm.getTextSize());
			// alignment
			pdfCB.showTextAligned(pm.getAlign(), pm.getContent(), pm.getX(), pm.getY(), 0);
		}
		
		// we tell the contentByte, we've finished drawing text
		pdfCB.endText();

		pdfCB.sanityCheck();
	}


	/**
	 * 画水平线
	 * @param writer
	 * @param stop 
	 * @param start 
	 */
	private void pdfDrawHorizontaLine(PdfWriter writer, float start, float stop) {
		int divPart = 33;
		float bigSpace = (stop-start)/divPart;
		float smallSpace = bigSpace/5;
		for (int i = 0; i <= divPart; i++) {
			float currentY = 35+i*bigSpace;
			writerLine(writer, 20f, currentY, screenWidth - 20f, currentY, 1);
			if (i == divPart) {
				break;
			}
			for (int j = 1; j < 5; j++) {
				float currentSY = j*smallSpace;
				writerLine(writer, 20f, currentY+currentSY,screenWidth - 20f, currentY+currentSY, 2);
			}
		}
		float wgY = bigSpace*(4 / waveGain);
		if (waveGain == 8) {
			wgY = 12.5f;
		}
		if (displayStyle==2) {
			writerLine(writer, baseX[2]+50f-75, 220f+310f, baseX[2]+55f-75, 220f+310f, 0);
			writerLine(writer, baseX[2]+55f-75, 220f+310f, baseX[2]+55f-75, 220+wgY+310f, 0);
			writerLine(writer, baseX[2]+55f-75, 220+wgY+310f, baseX[2]+65f-75, 220+wgY+310f, 0);
			writerLine(writer, baseX[2]+65f-75, 220f+310f, baseX[2]+65f-75, 220+wgY+310f, 0);
			writerLine(writer, baseX[2]+65f-75, 220f+310f, baseX[2]+70f-75, 220f+310f, 0);
		}
		if (displayStyle==1) {
			writerLine(writer, baseX[3]-25, baseY[3]-25, baseX[2]-20, baseY[3]-25, 0);
			writerLine(writer, baseX[3]-20, baseY[3]-25, baseX[2]-20, baseY[3]-25+wgY, 0);
			writerLine(writer, baseX[3]-20, baseY[3]-25+wgY, baseX[2]-10, baseY[3]-25+wgY, 0);
			writerLine(writer, baseX[3]-10, baseY[3]-25, baseX[2]-10, baseY[3]-25+wgY, 0);
			writerLine(writer, baseX[3]-10, baseY[3]-25, baseX[2]-5, baseY[3]-25, 0);
		}
		if (displayStyle==0) {
			writerLine(writer, baseX[2]+50f-75, 220f+310f, baseX[2]+55f-75, 220f+310f, 0);
			writerLine(writer, baseX[2]+55f-75, 220f+310f, baseX[2]+55f-75, 220+wgY+310f, 0);
			writerLine(writer, baseX[2]+55f-75, 220+wgY+310f, baseX[2]+65f-75, 220+wgY+310f, 0);
			writerLine(writer, baseX[2]+65f-75, 220f+310f, baseX[2]+65f-75, 220+wgY+310f, 0);
			writerLine(writer, baseX[2]+65f-75, 220f+310f, baseX[2]+70f-75, 220f+310f, 0);
			
			writerLine(writer, baseX[8]+50f-75, 220f+310f, baseX[8]+55f-75, 220f+310f, 0);
			writerLine(writer, baseX[8]+55f-75, 220f+310f, baseX[8]+55f-75, 220+wgY+310f, 0);
			writerLine(writer, baseX[8]+55f-75, 220+wgY+310f, baseX[8]+65f-75, 220+wgY+310f, 0);
			writerLine(writer, baseX[8]+65f-75, 220f+310f, baseX[8]+65f-75, 220+wgY+310f, 0);
			writerLine(writer, baseX[8]+65f-75, 220f+310f, baseX[8]+70f-75, 220f+310f, 0);
		}
		
	}

	/**
	 * 画垂直线
	 * @param writer
	 * @param start
	 * @param stop
	 */
	private void pdfDrawVerticaLine(PdfWriter writer, float start, float stop) {
		int divPart = 52;
		float bigSpace = (stop - start) / divPart;
		float smallSpace = bigSpace/5;
		for (int i = 0; i <= divPart; i++) {
			float currentX = 20f + i * bigSpace;
			writerLine(writer, currentX, 35f, currentX, screenHeight - 160f, 1);
			if (i==divPart) {
				return;
			}
			for (int j = 1; j < 5; j++) {
				float currentS_X = j*smallSpace;
				writerLine(writer, currentX+currentS_X, 35f, currentX+currentS_X, screenHeight - 160f, 2);
			}
		}
	}

	/**
	 * 画全部的导联名称
	 * @param writer
	 * @param leadName
	 */
	private void pdfDraWaveTag(PdfWriter writer, String[] leadName) {
		if (displayStyle==2) {
			pdfAddTag(writer, leadName[waveDisplay_LeadSwitch], 4);
		}else {
			for (int i = 0; i < leadName.length; i++) {
				pdfAddTag(writer, leadName[i], i);
			}
		}
	}

	/**
	 * 画单个导联名称
	 * @param writer
	 * @param tag 导联名称
	 * @param i 
	 */
	private void pdfAddTag(PdfWriter writer, String tag, int i) {
		try {
			int startX = 0, endX = 0;
			if (displayStyle==0) {
				startX = MARGIN + baseX[i] - 25;
				endX = MARGIN + baseX[i] + 15;
			}else {// if (displayStyle==1) 
				startX = MARGIN + baseX[i] - 35;
				endX = MARGIN + baseX[i] + 5;
			}
			TextField tf = new TextField(writer, 
										new Rectangle(startX,
													baseY[i] + 35, 
													endX,
													baseY[i] + 35 + 18),
										"Dickens");
			tf.setTextColor(BaseColor.BLACK);
			tf.setFontSize(13);
			tf.setText(tag);
			tf.setAlignment(Element.ALIGN_LEFT);
			tf.setRotation(90);
			PdfFormField field = tf.getTextField();
			writer.addAnnotation(field);
		} catch (Exception e) {
			e.printStackTrace();
			StringUtil.writeException(e, "pdfAddTag");
		}
	}

	/***
	 * 曲线图 
	 * @param writer
	 * @param dataMap 十二导联的数据
	 * 0:I; 1:II; 2:III; 3:aVR; 4:aVL 5:aVF; 6:v1; 7:v2; 8:v3; 9:v4; 10:v5; 11:v6
	 */
	private void pdfDrawWaveLine(PdfWriter writer, Map<Integer, int[]> dataMap) {
		if (displayStyle==2) {//单导数据
			int[] dataSrc = dataMap.get(waveDisplay_LeadSwitch);
			List<Integer> dataDes = sampleDot[12].SnapshotSample(dataSrc);//sampleReadEcgData(dataSrc, i);
			int tmp[];
			tmp  = new int[dataDes.size()];
			
			for(int j = 0; j < dataDes.size(); j++){
				tmp[j] = dataDes.get(j);
			}
			pdfDrawLine(writer, tmp, 5);
		}else {//十二导数据
			for (int i = 0; i < dataMap.size(); i++) {
				int[] dataSrc = dataMap.get(i);
				List<Integer> dataDes = sampleDot[i].SnapshotSample(dataSrc);//sampleReadEcgData(dataSrc, i);
				
				int tmp[];
				tmp  = new int[dataDes.size()];
				
				for(int j = 0; j < dataDes.size(); j++){
					tmp[j] = dataDes.get(j);
				}
				Log.e("---", "每导数据点数："+dataDes.size());
				pdfDrawLine(writer, tmp, i);
			}
		}
	}
	
	/***
	 * 曲线图 
	 * @param writer
	 * @param dataMap 十二导联的数据
	 * 0:I; 1:II; 2:III; 3:aVR; 4:aVL 5:aVF; 6:v1; 7:v2; 8:v3; 9:v4; 10:v5; 11:v6
	 */
	private void pdfDrawWaveLineInteger(PdfWriter writer, Map<Integer, Integer[]> dataMap) {
		if (displayStyle==2) {//单导数据
			Integer[] dataSrc = dataMap.get(waveDisplay_LeadSwitch);
			int[] intS = new int[dataSrc.length];
			for (int i = 0; i < intS.length; i++) {
				intS[i] = dataSrc[i];
			}
			List<Integer> dataDes = sampleDot[12].SnapshotSample(intS);//sampleReadEcgData(dataSrc, i);
			int tmp[];
			tmp  = new int[dataDes.size()];
			
			for(int j = 0; j < dataDes.size(); j++){
				tmp[j] = dataDes.get(j);
			}
			pdfDrawLine(writer, tmp, 5);
		}else {//十二导数据
			for (int i = 0; i < dataMap.size(); i++) {
				Integer[] dataSrc = dataMap.get(i);
				int[] intS = new int[dataSrc.length];
//				StringUtils.writException2File("/sdcard/manual.txt", "\n pdf画线时，第 "+i+" 导拆箱开始   intS.length = "+intS.length);
				for (int j = 0; j < intS.length; j++) {
					intS[j] = dataSrc[j];
				}
//				StringUtils.writException2File("/sdcard/manual.txt", "\n pdf画线时，第 "+i+" 导拆箱完成");
				
				List<Integer> dataDes = sampleDot[i].SnapshotSample(intS);//sampleReadEcgData(dataSrc, i);
				
				int tmp[];
				tmp  = new int[dataDes.size()];
				
				for(int j = 0; j < dataDes.size(); j++){
					tmp[j] = dataDes.get(j);
				}
				pdfDrawLine(writer, tmp, i);
			}
		}
	}

	/**
	 * 心电曲线图
	 * @param writer
	 * @param source 数据源
	 * @param leadNum 导联
	 */
	private void pdfDrawLine(PdfWriter writer, int[] source, int leadNum) {
		int oldX;
		int length = 0;
		if (displayStyle==0 && waveSpeed==0) {// 6*2ch 速度为25mm/s  10秒钟数据抽点后需要690/2的点数 
			length = source.length;
			if (length>690) {
				length = 690;
			}
			float desPoint[] = new float[length];
			
			oldX = currentX;
			for (int i = 0; i < length; i++) {
				desPoint[i] =  changeToScreenPosition(source[i]);
				if (i!=0) {
					if (source[i] == paceMaker) {
						desPoint[i] = oldY[leadNum];
						writerLine(writer,
								oldX + MARGIN + baseX[leadNum],
								baseY[leadNum]-15,
								oldX + MARGIN + baseX[leadNum],
								baseY[leadNum]+15,
								3);
					}else {
						writerLine(writer,
								(oldX + MARGIN + baseX[leadNum]),
								oldY[leadNum] + baseY[leadNum],
								i + MARGIN + baseX[leadNum],
								desPoint[i] + baseY[leadNum],
								0);
					}
				}
				oldX = i;
				oldY[leadNum] = desPoint[i];
			}
		}else{//12*1ch  或者12.5mm/s  10秒钟数据 需要1380个点
			length = source.length;
			if (length>1380) {
				length = 1380;
			}
			float desPoint[] = new float[length];
			
			oldX = currentX;
			
			for (int i = 0; i < length; i++) {
				desPoint[i] =  changeToScreenPosition(source[i]);
				if (i!=0) {
					if (source[i] == paceMaker) {
						desPoint[i] = oldY[leadNum];
						writerLine(writer,
								oldX + MARGIN + baseX[leadNum],
								baseY[leadNum]-15,
								oldX + MARGIN + baseX[leadNum],
								baseY[leadNum]+15,
								3);
					}else {
						writerLine(writer,
								(oldX + MARGIN + baseX[leadNum]),
								oldY[leadNum] + baseY[leadNum],
								i + MARGIN + baseX[leadNum],
								desPoint[i] + baseY[leadNum],
								0);
					}
					
				}
				oldX = i;
				oldY[leadNum] = desPoint[i];
			}
		}
	}

	/**画线
	 * @param writer
	 * @param startX 起始点
	 * @param startY
	 * @param stopX
	 * @param stopY
	 * @param type 为0时表示心电曲线  为1时表示背景的粗线格子 为2时表示细线格子  为3时表示起搏位置
	 */
	private void writerLine(PdfWriter writer, float startX, float startY, float stopX, float stopY,  int type) {
		PdfContentByte pdfCB = writer.getDirectContent();
		if (type==0) {
			pdfCB.setColorStroke(new BaseColor(0, 0, 0));
			pdfCB.setLineWidth(1f);
		}else if (type==1) {
			pdfCB.setColorStroke(new BaseColor(251, 173, 191));
			pdfCB.setLineWidth(0.8f);
		}else if (type==2) {
			pdfCB.setColorStroke(new BaseColor(251, 214, 222));
			pdfCB.setLineWidth(0.2f);
		}else if (type==3) {
			pdfCB.setColorStroke(new BaseColor(255, 0, 0));
			pdfCB.setLineWidth(1f);
		}
		pdfCB.moveTo(stopX, stopY);
		pdfCB.lineTo(startX, startY);
		pdfCB.stroke();
		pdfCB.sanityCheck();
	}
	
	private float changeToScreenPosition(int data) {
		return (float) ((data / (103 * waveGain)) * (460 / 430.0));
//		 return (float) ((data / (10 * waveGain)) * (460 / 430.0));
	}
	
}
