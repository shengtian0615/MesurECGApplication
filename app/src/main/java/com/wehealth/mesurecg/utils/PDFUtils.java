package com.wehealth.mesurecg.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopyFields;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;
import com.wehealth.mesurecg.R;

@SuppressWarnings("deprecation")
@SuppressLint("SimpleDateFormat")
public class PDFUtils {

	private int LEADNUM = 12;
	private String leadName[] = { "avF", "avL", "avR", "III", "II", "I", "V6",
			"V5", "V4", "V3", "V2", "V1" };
	float oldY[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	int currentX = 0;
	int baseLine;
	int scrSize;
	private int baseY[];
	private int baseX[];
	int MARGIN = 4;
	int GRID_SIZE = 3;
	int xGridNum;
	int yGridNum;
	int screenWidth = 810;
	int screenHeight = 460;
	private SampleDot sampleDot[];
	private final int ECG_SAMPLE_RATE = 500;
	private final int DESTINATION_SAMPlE_RATE = 70;
	private final int DataMaxValue = 0xfff;
	private final int DataBaseLine = 0;
	private int WaveGain = 80;

	SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy年MM月dd日");
	SimpleDateFormat sdfNoaml = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdfSimple = new SimpleDateFormat("yyyyMMddHHmmss");
	
	private int titleHeight = 740;
	private int checkDateHeight = 700;
	private int infoHeight = 671;
	private int infoLineTopHeight = 690;
	private int infoLineBottomHeight = 660;
	private int ecgParamsHeight = 625;
	private int ecgParamsHeight1 = 600;
	private int ecgParamsHeight2 = 580;
	private int ecgParamsHeight3 = 560;
	private int ecgParamsHeight4 = 540;
	private int ecgAutoReportHeight = 500;
	private int ecgDoctorReportHeight = 260;
	private float nomalFontSize = 12f;
	private float ecgParamLineBottom = 5f;
	private BaseFont btChina;
	
	
	@SuppressWarnings("deprecation")
	public String savePDF(Context context,
			Map<String, Map<String, String>> listMaps,
			Map<String, String> strMaps,
			Map<Integer, short[]> ecgDataMaps){
		Map<String, String> pInfo = listMaps.get("personInfo");
		String id = pInfo.get("ID");
		String longTime = strMaps.get("ecg_checktime");
		Date checkDate = new Date(Long.valueOf(longTime));
		String time = sdfSimple.format(checkDate);
		
		File file = new File(android.os.Environment.getExternalStorageDirectory()
				+ File.separator + "ECGDATA" + File.separator + "Report");
		
		if (!file.exists()) {
			file.mkdir();
		}
		
		String path =  file.getAbsolutePath()+ File.separator
				+ id+"_"+time+".pdf";
		String ecgWaveTemPath = android.os.Environment.getExternalStorageDirectory()
				+ File.separator + "ECGDATA"
				+ File.separator + "PDF"
				+ File.separator + id+"_"+time+".pdf";
		
		File tempECG = new File(ecgWaveTemPath);
		if (!tempECG.exists()) {//华清心仪保存的PDF不存在
			ecgWaveTemPath = saveECGWavePDF(context, listMaps, strMaps, ecgDataMaps);
		}
		String reportTemPath = saveReportPDF(context, listMaps, strMaps);
		
		try {
			File eTemp = new File(ecgWaveTemPath);
			File rTemp = new File(reportTemPath);
			
			PdfReader reader1 = new PdfReader(new FileInputStream(eTemp));
			PdfReader reader2 = new PdfReader(new FileInputStream(rTemp));
			
			PdfCopyFields copy = new PdfCopyFields(new FileOutputStream(path));
			copy.addDocument(reader2);
			copy.addDocument(reader1);
			copy.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		file = new File(ecgWaveTemPath);
		if (!tempECG.exists()) {
			if (file.exists()) {
				file.delete();
			}
		}
		file = new File(reportTemPath);
		if (file.exists()) {
			file.delete();
		}
		return path;
	}

	public String saveECGWavePDF(Context context,
			Map<String, Map<String, String>> listMaps,
			Map<String, String> strMaps, Map<Integer, short[]> ecgDataMaps) {

		Map<String, String> pInfo = listMaps.get("personInfo");
		Map<String, String> analysis = listMaps.get("analysis");
		String ecg_result = strMaps.get("ecg_result");
		String ecg_class = strMaps.get("ecg_class");
		String ecg_version = strMaps.get("ecg_version");
		Document doc = new Document(PageSize.A4.rotate());
		FileOutputStream fos;

		String path = android.os.Environment.getExternalStorageDirectory()
				+ File.separator + "ECGDATA"
				+ File.separator + "ecgwave_temp.pdf";
		try {
			fos = new FileOutputStream(path);
			PdfWriter writer = PdfWriter.getInstance(doc, fos);
			doc.open();
			doc.setPageCount(1);
			BaseFont font = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
					BaseFont.NOT_EMBEDDED);

			TextField tf = new TextField(writer, new Rectangle(320, 560,
					320 + 200, 560 + 20), "Dickens");
			// tf.setBackgroundColor(BaseColor.WHITE);
			tf.setFont(font);
			tf.setTextColor(BaseColor.BLACK);
			tf.setFontSize(13);
			tf.setText("12导同步心电图报告单（附页）");
			tf.setBackgroundColor(BaseColor.WHITE);
			tf.setAlignment(Element.ALIGN_CENTER);
			tf.setOptions(TextField.MULTILINE | TextField.REQUIRED);
			tf.setRotation(90);
			PdfFormField field = tf.getTextField();
			writer.addAnnotation(field);

			tf = new TextField(writer, new Rectangle(30, 535, 30 + 100,
					535 + 15), "Dickens");
			// tf.setBackgroundColor(BaseColor.WHITE);
			tf.setTextColor(BaseColor.BLACK);
			tf.setFontSize(10);
			tf.setFont(font);
			tf.setText("6ch × 2");
			tf.setAlignment(Element.ALIGN_LEFT);
			// tf.setOptions(TextField.MULTILINE | TextField.REQUIRED);
			tf.setRotation(90);
			field = tf.getTextField();
			writer.addAnnotation(field);

			tf = new TextField(writer,
					new Rectangle(30, 490, 30 + 30, 490 + 40), "Lists");
			// tf.setBackgroundColor(BaseColor.WHITE);
			tf.setTextColor(BaseColor.BLACK);
			tf.setFontSize(8);
			tf.setFont(font);
			tf.setChoices(new String[] { "姓名：", "ID：", "性别：", "生日：" });
			tf.setRotation(90);
			// tf.setChoiceSelection(4);
			field = tf.getListField();
			writer.addAnnotation(field);

			tf = new TextField(writer,
					new Rectangle(60, 490, 60 + 90, 490 + 40), "Lists");
			tf.setTextColor(BaseColor.BLACK);
			tf.setFontSize(8);
			tf.setFont(font);
			tf.setAlignment(Element.ALIGN_RIGHT);
			tf.setOptions(TextField.REQUIRED);
			String name = pInfo.get("Name");
			String id = pInfo.get("ID");
			String gender = pInfo.get("Gender");
			String age = pInfo.get("AGE");
			tf.setChoices(new String[] { name, id, gender, age });
			tf.setRotation(90);
			field = tf.getListField();
			writer.addAnnotation(field);

			tf = new TextField(writer, new Rectangle(155, 535, 155 + 100,
					535 + 15), "Dickens");
			tf.setTextColor(BaseColor.BLACK);
			tf.setFontSize(10);
			tf.setFont(font);
			tf.setText("【心电图特征】");
			tf.setAlignment(Element.ALIGN_RIGHT);
			// tf.setOptions(TextField.MULTILINE | TextField.REQUIRED);
			tf.setRotation(90);
			field = tf.getTextField();
			writer.addAnnotation(field);

			tf = new TextField(writer, new Rectangle(155, 490, 155 + 45,
					490 + 40), "Lists");
			tf.setTextColor(BaseColor.BLACK);
			tf.setFontSize(8);
			tf.setFont(font);
			tf.setChoices(new String[] { "HR           :", "P/QRS/T:",
					"RV5         :", "SV1          :" });
			tf.setRotation(90);
			// tf.setChoiceSelection(4);
			field = tf.getListField();
			writer.addAnnotation(field);

			tf = new TextField(writer, new Rectangle(205, 490, 205 + 60,
					490 + 40), "Lists");
			tf.setTextColor(BaseColor.BLACK);
			tf.setFontSize(8);
			tf.setFont(font);
			tf.setAlignment(Element.ALIGN_RIGHT);
			tf.setOptions(TextField.REQUIRED);
			String HR = analysis.get("HR");
			String PQRST = analysis.get("PQRST");
			String RV5 = analysis.get("RV5");
			String SV1 = analysis.get("SV1");
			tf.setChoices(new String[] { HR, PQRST, RV5, SV1 });
			tf.setRotation(90);
			// tf.setChoiceSelection(4);
			field = tf.getListField();
			writer.addAnnotation(field);

			tf = new TextField(writer, new Rectangle(270, 490, 270 + 65,
					490 + 40), "Lists");
			tf.setTextColor(BaseColor.BLACK);
			tf.setFontSize(8);
			tf.setFont(font);
			tf.setAlignment(Element.ALIGN_LEFT);
			tf.setChoices(new String[] { "PR           间期:", "QRS        时限:",
					"QT/QTc间期:", "RV5   +   SV1:" });
			tf.setRotation(90);
			// tf.setChoiceSelection(4);
			field = tf.getListField();
			writer.addAnnotation(field);

			tf = new TextField(writer, new Rectangle(340, 490, 340 + 45,
					490 + 40), "Lists");
			tf.setTextColor(BaseColor.BLACK);
			tf.setFontSize(8);
			tf.setFont(font);
			tf.setAlignment(Element.ALIGN_RIGHT);
			tf.setOptions(TextField.REQUIRED);
			String PR = analysis.get("PR");
			String QRS = analysis.get("QRS");
			String QTQTC = analysis.get("QTQTC");
			String RV5SV1 = analysis.get("RV5SV1");
			tf.setChoices(new String[] { PR, QRS, QTQTC, RV5SV1 });
			tf.setRotation(90);
			// tf.setChoiceSelection(4);
			field = tf.getListField();
			writer.addAnnotation(field);

			tf = new TextField(writer, new Rectangle(390, 535, 390 + 90,
					535 + 15), "Dickens");
			tf.setTextColor(BaseColor.BLACK);
			tf.setFontSize(10);
			tf.setFont(font);
			tf.setText("【心电图结论】");
			tf.setAlignment(Element.ALIGN_LEFT);
			tf.setOptions(TextField.REQUIRED);
			tf.setRotation(90);
			field = tf.getTextField();
			writer.addAnnotation(field);

			tf = new TextField(writer, new Rectangle(490, 535, 490 + 120,
					535 + 15), "Dickens");
			tf.setTextColor(BaseColor.BLACK);
			tf.setFontSize(10);
			tf.setFont(font);
			tf.setText(ecg_class);
			tf.setAlignment(Element.ALIGN_LEFT);
			// tf.setOptions(TextField.MULTILINE | TextField.REQUIRED);
			tf.setRotation(90);
			field = tf.getTextField();
			writer.addAnnotation(field);

			tf = new TextField(writer, new Rectangle(400, 490, 400 + 220,
					490 + 40), "Dickens");
			tf.setTextColor(BaseColor.BLACK);
			tf.setFontSize(8);
			tf.setFont(font);
			tf.setText(ecg_result);
			tf.setAlignment(Element.ALIGN_LEFT);
			tf.setOptions(TextField.MULTILINE | TextField.REQUIRED);
			tf.setRotation(90);
			field = tf.getTextField();
			writer.addAnnotation(field);

			writerLine(writer, 30f, 30f, 810f, 30f, 0);

			tf = new TextField(writer, new Rectangle(550, 13, 610 + 200,
					13 + 16), "Dickens");
			tf.setTextColor(BaseColor.BLACK);
			tf.setFontSize(10);
			tf.setFont(font);
			tf.setText("注：所有参数和结论需经医生最终确认");
			tf.setAlignment(Element.ALIGN_RIGHT);
			// tf.setOptions(TextField.MULTILINE | TextField.REQUIRED);
			tf.setRotation(90);
			field = tf.getTextField();
			writer.addAnnotation(field);

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Bitmap logobitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.wuweikang);
			logobitmap.compress(Bitmap.CompressFormat.PNG /* FileType */,
			                        100 /* Ratio */, stream);
			Image logoPNG = Image.getInstance(stream.toByteArray());
			logoPNG.scalePercent(30);
			logoPNG.setAbsolutePosition(30, 16);
			doc.add(logoPNG);

			tf = new TextField(writer, new Rectangle(68, 15, 68 + 130,
					15 + 15), "Dickens");
			tf.setTextColor(BaseColor.BLACK);
			tf.setFontSize(12);
			tf.setFont(font);
			tf.setText(ecg_version);
			tf.setAlignment(Element.ALIGN_LEFT);
			// tf.setOptions(TextField.MULTILINE | TextField.REQUIRED);
			tf.setRotation(90);
			field = tf.getTextField();
			writer.addAnnotation(field);

			baseY = new int[LEADNUM];
			baseX = new int[LEADNUM];
			for (int i = 0; i < LEADNUM; i++) {
				baseY[i] = screenHeight / LEADNUM * 2 * (i % 6 + 1) - 60;
			}
			for (int i = 0; i < LEADNUM; i++) {
				if (i < 6) {
					baseX[i] = 50;
				} else {
					baseX[i] = screenWidth / 2 + MARGIN + 30;
				}
			}
			baseLine = screenHeight / 8;
			scrSize = screenHeight / 3 - 30;
			pdfDrawHorizontaLine(writer, 35f, 490f);

			pdfDrawVerticaLine(writer, 29f, 811f);
			pdfDraWaveTag(writer, leadName);
			
			sampleDot = new SampleDot[12];
			for (int i = 0; i < LEADNUM; i++) {
				sampleDot[i] = new SampleDot(ECG_SAMPLE_RATE,
						DESTINATION_SAMPlE_RATE);
			}
			pdfDrawWaveLine(writer, ecgDataMaps);

			// 一定要记得关闭document对象
			doc.close();
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	/**
	 * 保存成PDF文件
	 * 
	 * @param context
	 * @param listMaps
	 *            测量人的信息和分析参数
	 * @param strMaps
	 *            自动分析结果和检查时间
	 * @return
	 */
	public String saveReportPDF(Context context,
			Map<String, Map<String, String>> listMaps,
			Map<String, String> strMaps) {
		Map<String, String> pInfo = listMaps.get("personInfo");
		Map<String, String> analysis = listMaps.get("analysis");
		// step 1: creation of a document-object
		Document document = new Document();

		String longTime = strMaps.get("ecg_checktime");
		Date checkDate = new Date(Long.valueOf(longTime));
		
		String path = android.os.Environment.getExternalStorageDirectory()
				+ File.separator + "ECGDATA"
				+ File.separator + "report_temp.pdf";
		try {
			// step 2: creation of the writer
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));

			// step 3: we open the document
			document.open();
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.water_stamp);
			bitmap.compress(Bitmap.CompressFormat.PNG /* FileType */,
					100 /* Ratio */, stream);
			Image image = Image.getInstance(stream.toByteArray());
			image.setAbsolutePosition(0f, 0f);
			image.scaleAbsolute(600f, 840f);
			document.add(image);

			ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
			Bitmap bp = BitmapFactory.decodeResource(context.getResources(), R.drawable.water_param_bg);
			bp.compress(Bitmap.CompressFormat.PNG /* FileType */,
					100 /* Ratio */, stream1);
			Image img = Image.getInstance(stream1.toByteArray());
			img.scaleAbsolute(220f, 80f);
			img.setAbsolutePosition(40, 535);
			document.add(img);
			img.setAbsolutePosition(295, 535);
			document.add(img);
			
			// step 4: we grab the ContentByte and do some stuff with it
			PdfContentByte cb = writer.getDirectContent();

			cb.setLineWidth(0f);
			//最顶部的表格线
			cb.moveTo(40, infoLineTopHeight);
			cb.lineTo(550, infoLineTopHeight);
			cb.moveTo(40, infoLineBottomHeight);
			cb.lineTo(550, infoLineBottomHeight);
			cb.moveTo(40, infoLineBottomHeight);
			cb.lineTo(40, infoLineTopHeight);
			cb.moveTo(100, infoLineBottomHeight);
			cb.lineTo(100, infoLineTopHeight);
			cb.moveTo(240, infoLineBottomHeight);
			cb.lineTo(240, infoLineTopHeight);
			cb.moveTo(290, infoLineBottomHeight);
			cb.lineTo(290, infoLineTopHeight);
			cb.moveTo(360, infoLineBottomHeight);
			cb.lineTo(360, infoLineTopHeight);
			cb.moveTo(410, infoLineBottomHeight);
			cb.lineTo(410, infoLineTopHeight);
			cb.moveTo(550, infoLineBottomHeight);
			cb.lineTo(550, infoLineTopHeight);
			
			//中间心电特征参数表格
			cb.moveTo(40, ecgParamsHeight1 + 15);
			cb.lineTo(550, ecgParamsHeight1 + 15);
			cb.moveTo(40, ecgParamsHeight1 - ecgParamLineBottom);
			cb.lineTo(550, ecgParamsHeight1 - ecgParamLineBottom);
			cb.moveTo(40, ecgParamsHeight2 - ecgParamLineBottom);
			cb.lineTo(550, ecgParamsHeight2 - ecgParamLineBottom);
			cb.moveTo(40, ecgParamsHeight3-ecgParamLineBottom);
			cb.lineTo(550, ecgParamsHeight3-ecgParamLineBottom);
			cb.moveTo(40, ecgParamsHeight4-ecgParamLineBottom);
			cb.lineTo(550, ecgParamsHeight4-ecgParamLineBottom);
			cb.moveTo(40, ecgParamsHeight1 + 15);
			cb.lineTo(40, ecgParamsHeight4-ecgParamLineBottom);
			cb.moveTo(550, ecgParamsHeight1 + 15);
			cb.lineTo(550, ecgParamsHeight4-ecgParamLineBottom);
			cb.moveTo(295, ecgParamsHeight1 + 15);
			cb.lineTo(295, ecgParamsHeight4 - ecgParamLineBottom);
			
			//最底部的横线
			cb.moveTo(30, 30);
			cb.lineTo(560, 30);
			cb.stroke();
			// we tell the ContentByte we're ready to draw text
			cb.beginText();
			
			btChina = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

			String hospital = PreferUtils.getIntance().getHospial();//"北京康济医院"
			if (TextUtils.isEmpty(hospital)) {//医院为空，将标题上移
				cb.setFontAndSize(btChina, 24);
				String text = "12导心电图报告单";
				cb.showTextAligned(PdfContentByte.ALIGN_CENTER, text, 300, titleHeight+25, 0);
			}else {
				cb.setFontAndSize(btChina, 21);
				cb.showTextAligned(PdfContentByte.ALIGN_CENTER, hospital, 300, titleHeight+33, 0);
				cb.setFontAndSize(btChina, 24);
				String text = "12导心电图报告单";
				cb.showTextAligned(PdfContentByte.ALIGN_CENTER, text, 300, titleHeight, 0);
			}
			

			cb.setFontAndSize(btChina, nomalFontSize);
			String text = "检查日期：" + sdfYMD.format(checkDate);
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, 40, checkDateHeight, 0);

			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "姓名", 52, infoHeight, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "性别", 255, infoHeight, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "年龄", 370, infoHeight, 0);

			text = "ID：" + pInfo.get("ID");
			cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, text, 540, checkDateHeight, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, pInfo.get("Name"), 110, infoHeight, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, pInfo.get("Gender"), 330, infoHeight, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, pInfo.get("AGE"), 480, infoHeight, 0);

			cb.setFontAndSize(btChina, 20);
			text = "心电图波形特征：";
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, 40, ecgParamsHeight, 0);

			cb.setFontAndSize(btChina, nomalFontSize);
			text = analysis.get("HR");
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "HR：", 60, ecgParamsHeight1, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, 150, ecgParamsHeight1, 0);
			text = analysis.get("PQRST");
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "P/QRS/T：", 60, ecgParamsHeight2, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, 150, ecgParamsHeight2, 0);
			text = analysis.get("RV5");
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "RV5：", 60, ecgParamsHeight3, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, 150, ecgParamsHeight3, 0);
			text = analysis.get("SV1");
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "SV1：", 60, ecgParamsHeight4, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, 150, ecgParamsHeight4, 0);

			text = analysis.get("PR");
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "PR间期：", 310, ecgParamsHeight1, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, 410, ecgParamsHeight1, 0);
			text = analysis.get("QRS");
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "QRS时限：", 310, ecgParamsHeight2, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, 410, ecgParamsHeight2, 0);
			text = analysis.get("QTQTC");
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "QT/QTc间期：", 310, ecgParamsHeight3, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, 410, ecgParamsHeight3, 0);
			text = analysis.get("RV5SV1");
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "RV5+SV1：", 310, ecgParamsHeight4, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, 410, ecgParamsHeight4, 0);

			cb.setFontAndSize(btChina, 20);
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "心电图自动分析报告：", 40, ecgAutoReportHeight, 0);

			cb.setFontAndSize(btChina, nomalFontSize);
			text = strMaps.get("ecg_class_auto");
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, 60,480, 0);
			text = strMaps.get("ecg_result_auto");
			String[] sts = text.split(" ");
			for (int i = 0; i < sts.length; i++) {
				cb.showTextAligned(PdfContentByte.ALIGN_LEFT, sts[i], 60, 462 - i * 16, 0);
			}

			cb.setFontAndSize(btChina, 20);
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "读图结论：", 40, ecgDoctorReportHeight, 0);

			cb.setFontAndSize(btChina, nomalFontSize);
			text = strMaps.get("ecg_class");
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, 60, 240, 0);
			text = strMaps.get("ecg_result");
			String[] stsd = text.split(" ");
			for (int i = 0; i < stsd.length; i++) {
				cb.showTextAligned(PdfContentByte.ALIGN_LEFT, stsd[i], 60, 222 - i * 16, 0);
			}
			
			text = strMaps.get("doctorName");
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "报告人：" + text, 390, 35, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "（心电图见附页）", 253, 15, 0);
			
			// we tell the contentByte, we've finished drawing text
			cb.endText();
			cb.sanityCheck();
		} catch (DocumentException de) {
			de.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		// step 5: we close the document
		document.close();
		return path;
	}

	/**
	 * 画水平线
	 * 
	 * @param writer
	 * @param stop
	 * @param start
	 */
	private void pdfDrawHorizontaLine(PdfWriter writer, float start, float stop) {
		float bigSpace = (stop - start) / 32;
		float smallSpace = bigSpace / 5;
		for (int i = 0; i <= 32; i++) {
			float currentY = 35 + i * bigSpace;
			writerLine(writer, 29f, currentY, 811f, currentY, 1);
			if (i == 32) {
				return;
			}
			for (int j = 1; j < 5; j++) {
				float currentSY = j * smallSpace;
				writerLine(writer, 29f, currentY + currentSY, 811f, currentY
						+ currentSY, 2);
			}
		}
	}

	/**
	 * 画垂直线
	 * 
	 * @param writer
	 * @param start
	 * @param stop
	 */
	private void pdfDrawVerticaLine(PdfWriter writer, float start, float stop) {
		float bigSpace = (stop - start) / 55;
		float smallSpace = bigSpace / 5;
		for (int i = 0; i <= 55; i++) {
			float currentX = 29f + i * bigSpace;
			writerLine(writer, currentX, 35f, currentX, 490f, 1);
			if (i == 55) {
				return;
			}
			for (int j = 1; j < 5; j++) {
				float currentS_X = j * smallSpace;
				writerLine(writer, currentX + currentS_X, 35f, currentX
						+ currentS_X, 490f, 2);
			}
		}
	}

	/**
	 * 画全部的导联名称
	 * 
	 * @param writer
	 * @param leadName
	 */
	private void pdfDraWaveTag(PdfWriter writer, String[] leadName) {
		for (int i = 0; i < leadName.length; i++) {
			pdfAddTag(writer, leadName[i], i);
		}
	}

	/**
	 * 画单个导联名称
	 * 
	 * @param writer
	 * @param tag
	 *            导联名称
	 * @param i
	 */
	private void pdfAddTag(PdfWriter writer, String tag, int i) {
		try {
			TextField tf = new TextField(writer, new Rectangle(MARGIN
					+ baseX[i] - 20, baseY[i] + 70, MARGIN + baseX[i],
					baseY[i] + 70 + 12), "Dickens");
			tf.setTextColor(BaseColor.BLACK);
			tf.setFontSize(10);
			tf.setText(tag);
			tf.setAlignment(Element.ALIGN_LEFT);
			tf.setRotation(90);
			PdfFormField field = tf.getTextField();
			writer.addAnnotation(field);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 曲线图
	 * 
	 * @param writer
	 * @param dataMap
	 *            十二导联的数据
	 */
	private void pdfDrawWaveLine(PdfWriter writer, Map<Integer, short[]> dataMap) {
		for (int i = 0; i < dataMap.size(); i++) {
			short[] dataSrc = dataMap.get(i);
			short[] dataDes = sampleReadEcgData(dataSrc, i);
			pdfDrawLine(writer, dataDes, i);
		}
	}

	/**
	 * 心电曲线图
	 * 
	 * @param writer
	 * @param source
	 *            数据源
	 * @param leadNum
	 *            导联
	 */
	private void pdfDrawLine(PdfWriter writer, short[] source, int leadNum) {
		int oldX;
		float desPoint[] = new float[source.length / 2];

		oldX = currentX;
		for (int i = 0; i < source.length / 2; i++) {
			desPoint[i] = changeToScreenPosition(source[i]);
			if ((desPoint[i]+baseY[leadNum]) > 490f) {
				desPoint[i] = 490f-baseY[leadNum];
			}
			if (i != 0) {
				writerLine(writer, (oldX + MARGIN + baseX[leadNum]),
						oldY[leadNum] + baseY[leadNum], i + MARGIN
								+ baseX[leadNum], desPoint[i] + baseY[leadNum],
						0);
			}
			oldX = i;
			oldY[leadNum] = desPoint[i];
		}
	}

	/**
	 * 画线
	 * 
	 * @param writer
	 * @param startX 起始点
	 * @param startY
	 * @param stopX
	 * @param stopY
	 * @param type
	 *            为0时表示心电曲线 为1时表示背景的粗线格子 为2时表示细线格子
	 */
	private void writerLine(PdfWriter writer, float startX, float startY,
			float stopX, float stopY, int type) {
		PdfContentByte pdfCB = writer.getDirectContent();
		if (type == 0) {
			pdfCB.setColorStroke(new BaseColor(0, 0, 0));
			pdfCB.setLineWidth(1f);
		} else if (type == 1) {
			pdfCB.setColorStroke(new BaseColor(251, 173, 191));
			pdfCB.setLineWidth(0.8f);
		} else if (type == 2) {
			pdfCB.setColorStroke(new BaseColor(251, 214, 222));
			pdfCB.setLineWidth(0.2f);
		}
		pdfCB.moveTo(stopX, stopY);
		pdfCB.lineTo(startX, startY);
		pdfCB.stroke();
		pdfCB.beginText();
		pdfCB.endText();
		pdfCB.sanityCheck();
	}

	private short[] sampleReadEcgData(short[] srcData, int lead){
		int len = srcData.length;
		int[] src = new int[len];
		int[] outData   = new int[DESTINATION_SAMPlE_RATE * 10];

		int ret;
		for(int i = 0; i < len; i++){
			src[i] = srcData[i];
		}

		ret = sampleDot[lead].SnapshotSample(src, src.length, outData, DESTINATION_SAMPlE_RATE * 10);
		
		short[] data = new short[ret];
		
		for(int i = 0;i < ret;i++){
			data[i] = (short) outData[i];
		}
		
		return data;
	}

	
	private float changeToScreenPosition(int data) {
		return ((scrSize - ((DataBaseLine - data) * scrSize * WaveGain) / DataMaxValue / 100) - (scrSize / 2));
	}
	
}
//private void createPDF() {
	// Map<String, Map<String, String>> listMaps = new HashMap<String,
	// Map<String,String>>();
	// Map<String, String> strMaps = new HashMap<String, String>();
	//
	// Map<String, String> personInfo = new HashMap<String, String>();
	// Map<String, String> analysis = new HashMap<String, String>();
	// personInfo.put("Name", "王五");
	// personInfo.put("ID", "test0606");
	// personInfo.put("Gender", "男");
	// personInfo.put("AGE", "1970-01-01(47岁)");
	// analysis.put("HR", "HR：80bpm");
	// analysis.put("PQRST", "P/QRS/T：11/16/11°");
	// analysis.put("RV5", "RV5：1.190mV");
	// analysis.put("SV1", "-0.411mV");
	// analysis.put("PR", "PR间期：162ms");
	// analysis.put("QRS", "QRS时限：96ms");
	// analysis.put("QTQTC", "QT/QTc间期：356/410ms");
	// analysis.put("RV5SV1", "RV5+SV1：1.601mV");
	//
	// listMaps.put("personInfo", personInfo);
	// listMaps.put("analysis", analysis);
	//
	// strMaps.put("ecg_result_auto", "窦性心律");
	// strMaps.put("ecg_class_auto", "** 正常心电图 **");
	// strMaps.put("ecg_result", "窦性心律");
	// strMaps.put("ecg_class", "** 正常心电图 **");
	// strMaps.put("comment", "** 正常心电图 **");
	// strMaps.put("doctorName", "王梅");
	// strMaps.put("hospital", "北京五维康医院");
	// strMaps.put("ecg_checktime", String.valueOf(new Date().getTime()));
	// strMaps.put("ecg_version", "ECG v2.2");
	// boolean b = PDFUtils.savePDF(this, listMaps, strMaps);
	// Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
	// }
