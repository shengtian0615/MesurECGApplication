//package com.wehealth.model.util;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.Config;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.os.Environment;
//import android.text.Layout;
//import android.text.StaticLayout;
//import android.text.TextPaint;
//
//import com.wehealth.mesurecg.dao.PatientUserDao;
//import com.wehealth.model.domain.model.ECGData;
//import com.wehealth.model.domain.model.EcgDataParam;
//import com.wehealth.model.domain.model.PatientUser;
//
//import java.text.DecimalFormat;
//import java.util.Date;
//
//public class ECGIMGUtil {
//
//	private String hrStr, prStr, qrsStr, qtdStr, paxisStr, r5s1Str, r5_s1Str,
//			analysClass, analysResult;
//
//	private int width, heigth;// 图片的宽和高
//	private int[] leadX, baseX;
//	private int[] baseY;
//	private int[] oldX;
//	private int baseYData = 60;
//	private int countY = 500;
//	private Canvas canvas;
//	private Paint paint, pacePaint;
//	private int iiBaseX;
//	private int Gain = 12;// 24=5mmV 12=10mmV 6=20mmV
//	private int paceMarker = 32767;
//
//	final static int LEADNUM = 12;
//	private SampleDot sampleDot[];
//	private final int ECG_SAMPLE_RATE = 500;
//	private final int DESTINATION_SAMPlE_RATE = 206;// 6*2ch等于138；
//	private short[] vfshort, vlshort, vrshort, v1short, v2short, v3short,
//			v4short, v5short, v6short, vishort, viishort, viiishort;
//	private String[] leadName = new String[LEADNUM];
//	private String[] paramInfo = new String[13];
//	private String[] paramResult = new String[12];
//	private String sdk = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ECGDATA/XML/";
//
//
//	public Bitmap createBitMap(Context context, ECGData ecgData, int gain) {
//		sampleDot = new SampleDot[12];
//        for (int i = 0; i < LEADNUM; i++) {
//        	sampleDot[i] = new SampleDot(ECG_SAMPLE_RATE, DESTINATION_SAMPlE_RATE);
//		}
//        if (gain==5) {
//			Gain = 24;
//		}else if (gain==10) {
//			Gain = 12;
//		}else {
//			Gain = 6;
//		}
//        paint = new Paint();
//		paint.setTextSize(22f);
//		pacePaint = new Paint();
//		width = 600;
//		heigth = 3050;//
//		baseY = new int[4];
//		leadX = new int[4];
//		baseX = new int[3];
//		oldX = new int[LEADNUM];
//		for (int i = 0; i < leadX.length; i++) {
//			leadX[i] = width/4*i + 66;
//		}
//		baseX[0] = leadX[1]-10;
//		baseX[1] = leadX[2]-20;
//		baseX[2] = leadX[3]-30;
//		iiBaseX = leadX[0];
//		for (int i = 0; i < baseY.length; i++) {
//			baseY[i] = i*countY + baseYData + 10;
//		}
//		EcgDataParam edp = FileUtil.parserEcgParam(sdk+DateUtils.sdf_yyyyMMddHHmmss.format(ecgData.getTime())+".xml");
//		String serialNo = PreferUtils.getIntance().getSerialNo();
//		PatientUser pu = PatientUserDao.getInstance(context, serialNo).getUserByPhone(ecgData.getPatientId());
//
//		paramInfo[0]="RV5+SV1：";
//		paramInfo[1]="RV5/SV1 ：";
//		paramInfo[2]="P/QRS/T ：";
//		paramInfo[3]="QT/QTC   ：";
//		paramInfo[4]="QRS          ：";
//		paramInfo[5]="PR             ：";
//		paramInfo[6]="HR             ：";
//		paramInfo[7]="门诊号：";
//		paramInfo[8]="住院号：";
//		paramInfo[9]="性别：";
//		paramInfo[10]="年龄：";
//		paramInfo[11]="姓名：";
//		hrStr = edp.getHeartRate()+" bpm";
//		prStr = edp.getPRInterval()+" ms";
//		qrsStr = edp.getQRSDuration()+" ms";
//		qtdStr = edp.getQTD()+"/"+edp.getQTC()+" ms";
//		paxisStr = edp.getPAxis()+"/"+edp.getQRSAxis()+"/"+edp.getTAxis()+" °";
//		r5s1Str = edp.getRV5()+"/"+edp.getSV1()+" mV";
//		Double r_s = 0.0;
//		try {
//			r_s = Double.valueOf(edp.getRV5SV1());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		DecimalFormat df =new DecimalFormat("#.000");
//		String rs = df.format(r_s);
//		r5_s1Str = rs+" mV";
//		paramResult[0] = r5_s1Str;
//		paramResult[1] = r5s1Str;
//		paramResult[2] = paxisStr;
//		paramResult[3] = qtdStr;
//		paramResult[4] = qrsStr;
//		paramResult[5] = prStr;
//		paramResult[6] = hrStr;
//		paramResult[7] = "";
//		paramResult[8] = "";
//		if (pu!=null) {
//			paramInfo[12]="ID："+pu.getPhoneId();
//			if (pu.getGender()==0) {
//				paramResult[9] = "男";
//			}else {
//				paramResult[9] = "女";
//			}
//			paramResult[10] = pu.getAge()+"岁";
//			paramResult[11] = pu.getName();
//		}else {
//			paramInfo[12]="ID：";
//			paramResult[9]= "";
//			paramResult[10]= "";
//			paramResult[11]= "";
//		}
//		analysClass = ECGDataUtil.getClassByJson(edp.getAutoDiagnosisResult());
//		analysResult = ECGDataUtil.getResuByJson(edp.getAutoDiagnosisResult());
//
//		vfshort = sampleReadEcgData(edp.getAvF(), 0);
//    	vlshort = sampleReadEcgData(edp.getAvL(), 1);
//    	vrshort = sampleReadEcgData(edp.getAvR(), 2);
//		v1short = sampleReadEcgData(edp.getV1(), 3);
//		v2short = sampleReadEcgData(edp.getV2(), 4);
//		v3short = sampleReadEcgData(edp.getV3(), 5);
//		v4short = sampleReadEcgData(edp.getV4(), 6);
//		v5short = sampleReadEcgData(edp.getV5(), 7);
//		v6short = sampleReadEcgData(edp.getV6(), 8);
//		vishort = sampleReadEcgData(edp.getI(), 9);
//		viishort = sampleReadEcgData(edp.getIi(), 10);
//		viiishort = sampleReadEcgData(edp.getIii(), 11);
//		Bitmap bitmap = Bitmap.createBitmap(width, heigth, Config.ARGB_8888);
//		canvas = new Canvas(bitmap);
//		canvas.drawColor(Color.WHITE);
//		leadName[2] = "I";
//		leadName[1] = "II";
//		leadName[0] = "III";
//		leadName[5] = "avR";
//		leadName[4] = "avL";
//		leadName[3] = "avF";
//		leadName[8] = "V1";
//		leadName[7] = "V2";
//		leadName[6] = "V3";
//		leadName[11] = "V4";
//		leadName[10] = "V5";
//		leadName[9] = "V6";
//		drawWaveLine(vishort, 2);
//		drawWaveLine(viishort, 1);
//		drawWaveLine(viiishort, 0);
//		drawWaveLine(vrshort, 5);
//		drawWaveLine(vlshort, 4);
//		drawWaveLine(vfshort, 3);
//		drawWaveLine(v1short, 8);
//		drawWaveLine(v2short, 7);
//		drawWaveLine(v3short, 6);
//		drawWaveLine(v4short, 11);
//		drawWaveLine(v5short, 10);
//		drawWaveLine(v6short, 9);
//		drawWaveLineII(viishort);
//
//		drawGainType();
//
//		for (int i = 0; i < paramInfo.length; i++) {
//			drawText(paramInfo[i], leadX[0]+i*38, baseY[3]+countY+80);
//		}
//		for (int i = 0; i < paramResult.length; i++) {
//			drawText(paramResult[i], leadX[0]+i*38, baseY[3]+countY+200);
//		}
//
//		drawText(edp.getFHP()+"—"+edp.getFLP()+"    AC "+edp.getFNotch()+"    25mm/s     "+gain+"mm/mV", leadX[3]+48, baseY[0]);
//		drawText("测量时间："+DateUtils.sdf_yyyy_MM_dd_HH_mm_ss.format(ecgData.getTime()), leadX[3]+10, baseY[3]+countY+420);
//		drawText("打印时间："+DateUtils.sdf_yyyy_MM_dd_HH_mm_ss.format(new Date()), leadX[3]-15, baseY[3]+countY+420);
//		drawText("《分析结果》", leadX[2]+80, baseY[3]+countY+420);
//		drawText(analysClass, leadX[2]+50, baseY[3]+countY+420);
//		drawText("需要经过医师证实。医师姓名：_______", leadX[0], baseY[3]+countY+420);
//
//		canvas.rotate(90, leadX[2]+36, baseY[3]+countY+420);
//		TextPaint textPaint = new TextPaint();
//		textPaint.setTextSize(20f);
//		textPaint.setColor(Color.BLACK);
//		//getWidth()表示绘制多宽后换行
//		StaticLayout sl = new StaticLayout(analysResult, textPaint, 380, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
//		//从0,0开始绘制
//		canvas.translate(leadX[2]+36, baseY[3]+countY+420);
//		sl.draw(canvas);
//		canvas.rotate(-90, leadX[2]+36, baseY[3]+countY+420);
//		canvas.save(Canvas.ALL_SAVE_FLAG);
//		canvas.restore();
//		return bitmap;
//	}
//	private void drawGainType() {
//		if (Gain==24) {//5mmV
//			canvas.drawLine(leadX[1], 10, leadX[1], 20, paint);
//			canvas.drawLine(leadX[1], 20, leadX[1]+3*Gain/2+5, 20, paint);
//			canvas.drawLine(leadX[1]+3*Gain/2+5, 20, leadX[1]+3*Gain/2+5, 45, paint);
//			canvas.drawLine(leadX[1]+3*Gain/2+5, 45, leadX[1], 45, paint);
//			canvas.drawLine(leadX[1], 45, leadX[1], 55, paint);
//		}else if (Gain==12) {//10mmV
//			canvas.drawLine(leadX[1], 10, leadX[1], 20, paint);
//			canvas.drawLine(leadX[1], 20, leadX[1]+6*Gain+10, 20, paint);
//			canvas.drawLine(leadX[1]+6*Gain+10, 20, leadX[1]+6*Gain+10, 45, paint);
//			canvas.drawLine(leadX[1]+6*Gain+10, 45, leadX[1], 45, paint);
//			canvas.drawLine(leadX[1], 45, leadX[1], 55, paint);
//		}else if (Gain==6) {//20mmV
//			canvas.drawLine(leadX[1], 10, leadX[1], 20, paint);
//			canvas.drawLine(leadX[1], 20, leadX[1]+12*Gain*2+20, 20, paint);
//			canvas.drawLine(leadX[1]+12*Gain*2+20, 20, leadX[1]+12*Gain*2+20, 45, paint);
//			canvas.drawLine(leadX[1]+12*Gain*2+20, 45, leadX[1], 45, paint);
//			canvas.drawLine(leadX[1], 45, leadX[1], 55, paint);
//		}
//	}
//
//	private short[] sampleReadEcgData(short[] srcData, int lead){
//		int len = srcData.length;
//		int[] src = new int[len];
//		int[] outData = new int[DESTINATION_SAMPlE_RATE * 10];
//
//		int ret;
//		for(int i = 0; i < len; i++){
//			src[i] = srcData[i];
//		}
//
//		ret = sampleDot[lead].SnapshotSample(src, src.length, outData, DESTINATION_SAMPlE_RATE * 10);
//
//		short[] data = new short[ret];
//
//		for(int i = 0;i < ret;i++){
//			data[i] = (short) outData[i];
//		}
//
//		return data;
//	}
//	/**
//	 * 绘制II导
//	 * @param data
//	 */
//	private void drawWaveLineII(short[] data) {
//		int x = 0;
//		int oldXValue = 0;
//		int y = baseY[0];
//		int yTotal = countY*4;
//
//		if (yTotal>data.length) {
//			yTotal = data.length;
//		}
//		for (int i = 0; i < yTotal; i++) {
//			x = iiBaseX + data[i] /Gain;
//			if (i!=0) {
//				if (data[i]==paceMarker) {
//					canvas.drawLine(oldXValue-20, i+baseY[0], oldXValue+20, i+baseY[0], pacePaint);
//					x = oldXValue;
//				}else {
//					canvas.drawLine(oldXValue, y, x, i+baseY[0], paint);
//				}
//			}
//			oldXValue = x;
//			y = i+baseY[0];
//		}
//
//		//旋转文字
//		drawText("II", iiBaseX+20, baseY[0]);
//	}
//
//	/**
//	 * 绘制12导联曲线
//	 * @param data
//	 * @param lead
//	 */
//	private void drawWaveLine(short[] data, int lead) {
//		int x = 0;
//		int y = baseY[lead/3];
//		for (int i = 0; i < countY; i++) {
//			x = baseX[lead%3] + data[i] /Gain;
//			if (i!=0) {
//				if (data[i] == paceMarker) {
//					canvas.drawLine(oldX[lead]-20, i+baseY[lead/3], oldX[lead]+20, i+baseY[lead/3], pacePaint);
//					x = oldX[lead];
//				}else {
//					canvas.drawLine(oldX[lead], y, x, i+baseY[lead/3], paint);
//				}
//			}
//			oldX[lead] = x;
//			y = i+baseY[lead/3];
//		}
//
//		//旋转文字
//		drawText(leadName[lead], baseX[lead%3]+20, baseY[lead/3]);
//	}
//
//	/**
//	 * 画文字信息
//	 * @param text
//	 * @param x
//	 * @param y
//	 */
//	private void drawText(String text, int x, int y){
//		canvas.rotate(90, x, y);
//		canvas.drawText(text, x, y, paint);
//		canvas.rotate(-90, x, y);
//	}
//}
