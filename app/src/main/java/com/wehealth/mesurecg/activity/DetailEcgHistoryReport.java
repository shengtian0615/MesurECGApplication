package com.wehealth.mesurecg.activity;

import java.text.DecimalFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.wehealth.mesurecg.BaseFragmentActivity;
import com.wehealth.mesurecg.MeasurECGApplication;
import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.dao.ECGDao;
import com.wehealth.mesurecg.fragment.ECGIntReadFragment;
import com.wehealth.mesurecg.fragment.ECGReadFragment;
import com.wehealth.mesurecg.utils.CommUtils;
import com.wehealth.mesurecg.utils.MyClickableSpan;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.mesurecg.utils.SampleDot;
import com.wehealth.model.domain.enumutil.AcFeq;
import com.wehealth.model.domain.enumutil.ECGDataDiagnosisType;
import com.wehealth.model.domain.enumutil.EmgFeq;
import com.wehealth.model.domain.enumutil.LowFeq;
import com.wehealth.model.domain.model.AuthToken;
import com.wehealth.model.domain.model.ECGData;
import com.wehealth.model.domain.model.ECGFilter;
import com.wehealth.model.domain.model.EcgDataParam;
import com.wehealth.model.domain.model.RegisteredUser;
import com.wehealth.model.domain.model.ResultPassHelper;
import com.wehealth.model.interfaces.inter_other.WeHealthECGData;
import com.wehealth.model.util.Constant;
import com.wehealth.model.util.DateUtils;
import com.wehealth.model.util.ECGDataUtil;
import com.wehealth.model.util.FileUtil;
import com.wehealth.model.util.NetWorkService;
import com.wehealth.model.util.StringUtil;

public class DetailEcgHistoryReport extends BaseFragmentActivity implements OnClickListener {
	
	private final int ECGDATA_UPLOAD_FAILED = 191;
	private final int ECGDATA_UPLOAD_SUCCESS = 190;
	private final int ECGDATA_UPLOAD_FAILED_RESON = 193;
	private final int REQUESTCODE_CONN_BT = 100;
	private long ecgdata_saveId = -1;
	
	int HISTORY_SIZE = 1000;
	/**模拟数据结果**/
//	private ECGData data;
	/**华清心仪测量结果**/
	private ECGData ecgData;
	/**重新解析的完整心电数据**/
	private ECGData parseXMLECGData;
	private LinearLayout backLayout, sympLayout;
	private RelativeLayout titleLayout;
	private String ECG_DATA_SEND_KEY = "ecgdata";
	private TextView ecgClass, ecgResult, upLoadState, symptoms, printBtn;
	private TextView hr,pr,qrs,qtd,paxis,rv5sv1,rv5_sv1;
	private ECGReadFragment readFragment;
	private ECGIntReadFragment intReadFragment;
	private ProgressDialog upLoadEcgDialog;
	private String hrStr, prStr, qrsStr, qtdStr, paxisStr, r5s1Str, r5_s1Str
				, analysClass, analysResult;
	
	private int width, heigth;//图片的宽和高
	private int[] leadX, baseX;
	private int[] baseY;
	private int[] oldX;
	private int baseYData = 60;
	private int countY = 500;
	private Canvas canvas;
	private Paint paint;
	private int iiBaseX;
	private int Gain = 12;// 24=5mmV 12=10mmV  6=20mmV
	
	final static int LEADNUM = 12;
	private SampleDot sampleDot[];
	private final int ECG_SAMPLE_RATE = 500;
	private final int DESTINATION_SAMPlE_RATE = 206;// 6*2ch等于138；
	private short[] vfshort, vlshort,vrshort,v1short,v2short,v3short,v4short,v5short,v6short,vishort,viishort,viiishort; 
	private String[] leadName = new String[LEADNUM];
	private String[] paramInfo = new String[13];
	private String[] paramResult = new String[12];
	private String reson = "";
	private String idCardNo, url;
	
	private String sdk = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ECGDATA/XML/";

	
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ECGDATA_UPLOAD_SUCCESS://心电数据上传成功
				if (upLoadEcgDialog!=null && upLoadEcgDialog.isShowing()) {
					upLoadEcgDialog.dismiss();
				}
				Toast.makeText(DetailEcgHistoryReport.this, "本条心电数据上传成功！", Toast.LENGTH_SHORT).show();
				upLoadState.setText("已上传");
				upLoadState.setEnabled(false);
				break;
			case ECGDATA_UPLOAD_FAILED://心电数据上传失败
				if (upLoadEcgDialog!=null && upLoadEcgDialog.isShowing()) {
					upLoadEcgDialog.dismiss();
				}
				isWait("由于网络等原因，上传失败，请稍候再试！");
				
				upLoadState.setText("上传");
				upLoadState.setEnabled(true);
				break;
			case ECGDATA_UPLOAD_FAILED_RESON:
				if (upLoadEcgDialog!=null && upLoadEcgDialog.isShowing()) {
					upLoadEcgDialog.dismiss();
				}
				isWait("上传失败，原因："+reson);
				upLoadState.setText("上传");
				upLoadState.setEnabled(true);
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_ecg_history_report);

		backLayout = (LinearLayout) findViewById(R.id.backlayout);
		titleLayout = (RelativeLayout) findViewById(R.id.title_bar);
		sympLayout = (LinearLayout) findViewById(R.id.measure_chart_l1);
		hr = (TextView) findViewById(R.id.textview_heart_HR);
		pr = (TextView) findViewById(R.id.textview_heart_PRInterval);
		qrs = (TextView) findViewById(R.id.textview_heart_QRSDuration);
		qtd = (TextView) findViewById(R.id.textview_heart_qtd);
		paxis = (TextView) findViewById(R.id.textview_heart_PAxis);
		symptoms = (TextView) findViewById(R.id.measure_symptoms_content);
		rv5sv1 = (TextView) findViewById(R.id.textview_heart_rv5);
		rv5_sv1 = (TextView) findViewById(R.id.textview_heart_rv5_sv1);
		ecgClass = (TextView) findViewById(R.id.textview_heart_class);
		ecgResult = (TextView) findViewById(R.id.textview_heart_result);
		upLoadState = (TextView) findViewById(R.id.textview_ecg_upload);
		printBtn = (TextView) findViewById(R.id.textview_print);

		idCardNo = PreferUtils.getIntance().getIdCardNo();
		url = PreferUtils.getIntance().getServerUrl();
		reflushBackgroud();
		
		TextView titleView = (TextView) findViewById(R.id.page_title);
		titleView.setText(R.string.ecg_history_report);
		
		upLoadEcgDialog = new ProgressDialog(this);
		upLoadEcgDialog.setCancelable(false);

		ecgData = (ECGData) getIntent().getSerializableExtra(ECG_DATA_SEND_KEY);
		if (ecgData!=null) {
			initData();
			ecgdata_saveId = ecgData.getId();
		}else {
			Toast.makeText(this, "本地数据被删除了", Toast.LENGTH_SHORT).show();
			finish();
//			initDataDemo();
		}
		upLoadState.setOnClickListener(this);
		printBtn.setOnClickListener(this);
	}

	/**华清心仪的显示结果**/
	private void initData() {
		if (ecgData.getVersion()==0) {
			parseXMLECGData = FileUtil.parseECGbyInputStream(sdk+DateUtils.sdf_yyyyMMddHHmmss.format(ecgData.getTime())+".xml");
		}else if (ecgData.getVersion()==1) {
			parseXMLECGData = FileUtil.parse2Data(sdk+DateUtils.sdf_yyyyMMddHHmmss.format(ecgData.getTime())+".xml");
		}
		
		if (parseXMLECGData==null) {
			Toast.makeText(this, "本地心电数据文件已经删除", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		hrStr = parseXMLECGData.getHeartRate()+" bpm";
		prStr = parseXMLECGData.getPr()+" ms";
		qrsStr = parseXMLECGData.getQrs()+" ms";
		qtdStr = parseXMLECGData.getQt()+"/"+parseXMLECGData.getQtc()+" ms";
		paxisStr = parseXMLECGData.getPaxis()+"/"+parseXMLECGData.getQrsaxis()+"/"+parseXMLECGData.getTaxis()+" °";
		r5s1Str = parseXMLECGData.getRv5()+"/"+parseXMLECGData.getSv1()+" mV";
		hr.setText(hrStr);
		pr.setText(prStr);
		qrs.setText(qrsStr);
		qtd.setText(qtdStr);
		paxis.setText(paxisStr);
		rv5sv1.setText(r5s1Str);
		float r_s = 0;
		try {
			float rv5 = (float) parseXMLECGData.getRv5();
			float sv1 = (float) parseXMLECGData.getSv1();
			r_s = Math.abs(rv5)+Math.abs(sv1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		DecimalFormat df =new DecimalFormat("#.000");  
		String rs = df.format(r_s);
		r5_s1Str = rs+" mV";
		rv5_sv1.setText(r5_s1Str);
		if (ecgData.getRequestedDiagnosisType()==ECGDataDiagnosisType.uploaded.ordinal()) {
			upLoadState.setText("已上传");
			upLoadState.setEnabled(false);
		}else {
			upLoadState.setText("上传");
			upLoadState.setEnabled(true);
		}
		
		String sym = ecgData.getSymptoms();
		if (TextUtils.isEmpty(sym)) {
			sympLayout.setVisibility(View.GONE);
		}else {
			sympLayout.setVisibility(View.VISIBLE);
		}
		symptoms.setText(sym);
		
		parseXMLECGData.setTime(ecgData.getTime());
		parseXMLECGData.setRequestedDiagnosisType(ecgData.getRequestedDiagnosisType());
		parseXMLECGData.setPatientId(ecgData.getPatientId());
		parseXMLECGData.setRegisteredUserId(ecgData.getRegisteredUserId());
		parseXMLECGData.setEquipmentSerialNo(ecgData.getEquipmentSerialNo());
		
		ecgData.setaVf(parseXMLECGData.getaVf());
		ecgData.setaVl(parseXMLECGData.getaVl());
		ecgData.setaVr(parseXMLECGData.getaVr());
		ecgData.setV1(parseXMLECGData.getV1());
		ecgData.setV2(parseXMLECGData.getV2());
		ecgData.setV3(parseXMLECGData.getV3());
		ecgData.setV4(parseXMLECGData.getV4());
		ecgData.setV5(parseXMLECGData.getV5());
		ecgData.setV6(parseXMLECGData.getV6());
		ecgData.setvI(parseXMLECGData.getvI());
		ecgData.setvII(parseXMLECGData.getvII());
		ecgData.setvIII(parseXMLECGData.getvIII());
		
		if (ecgData.getVersion()==0) {
			analysClass = ECGDataUtil.getClassByJson(parseXMLECGData.getAutoDiagnosisResult());
			analysResult = ECGDataUtil.getResuByJson(parseXMLECGData.getAutoDiagnosisResult());
			if (analysClass.contains("分析失败")) {
				ecgClass.setText(analysClass);
				ecgResult.setText(analysResult);
			}else {
				ecgClass.append(getSpanString(analysClass));
				ecgClass.setMovementMethod(LinkMovementMethod.getInstance());
				if (analysResult.contains("ST & T异常")) {
					ecgResult.append(getSpanString("ST & T异常"));
					analysResult = analysResult.replace("ST & T异常", "");
				}
				if (analysResult.contains(" ")) {
					String[] rs1 = analysResult.split(" ");
					for (int i = 0; i < rs1.length; i++) {
						if (!TextUtils.isEmpty(rs1[i])) {
							ecgResult.append(getSpanString(rs1[i]+" "));
						}
					}
				}else {
					ecgResult.append(getSpanString(analysResult));
				}
				ecgResult.setMovementMethod(LinkMovementMethod.getInstance());
			}
			readFragment = new ECGReadFragment();
			Bundle bundle = new Bundle();
			bundle.putByteArray("avf", ecgData.getaVf());
			bundle.putByteArray("avl", ecgData.getaVl());
			bundle.putByteArray("avr", ecgData.getaVr());
			bundle.putByteArray("v1", ecgData.getV1());
			bundle.putByteArray("v2", ecgData.getV2());
			bundle.putByteArray("v3", ecgData.getV3());
			bundle.putByteArray("v4", ecgData.getV4());
			bundle.putByteArray("v5", ecgData.getV5());
			bundle.putByteArray("v6", ecgData.getV6());
			bundle.putByteArray("vi", ecgData.getvI());
			bundle.putByteArray("vii", ecgData.getvII());
			bundle.putByteArray("viii", ecgData.getvIII());
			bundle.putString("fhp", parseXMLECGData.getFhp());
			bundle.putString("flp", parseXMLECGData.getFlp());
			readFragment.setArguments(bundle);
			getSupportFragmentManager().beginTransaction()
			.add(R.id.listview_chart, readFragment).show(readFragment)
			.commit();
		}else if(ecgData.getVersion()==1){
			ecgResult.setText(ecgData.getAutoDiagnosisResult());
			intReadFragment = new ECGIntReadFragment();
			Bundle bundle = new Bundle();
			bundle.putByteArray("avf", ecgData.getaVf());
			bundle.putByteArray("avl", ecgData.getaVl());
			bundle.putByteArray("avr", ecgData.getaVr());
			bundle.putByteArray("v1", ecgData.getV1());
			bundle.putByteArray("v2", ecgData.getV2());
			bundle.putByteArray("v3", ecgData.getV3());
			bundle.putByteArray("v4", ecgData.getV4());
			bundle.putByteArray("v5", ecgData.getV5());
			bundle.putByteArray("v6", ecgData.getV6());
			bundle.putByteArray("vi", ecgData.getvI());
			bundle.putByteArray("vii", ecgData.getvII());
			bundle.putByteArray("viii", ecgData.getvIII());
			bundle.putString("fhp", parseXMLECGData.getFhp());
			bundle.putString("flp", parseXMLECGData.getFlp());
			intReadFragment.setArguments(bundle);
			getSupportFragmentManager().beginTransaction()
			.add(R.id.listview_chart, intReadFragment).show(intReadFragment)
			.commit();
		}
	}
	
	private SpannableString getSpanString(String str) {
		SpannableString spStr = new SpannableString(" "+str+" ");
		ClickableSpan clickSpan = new MyClickableSpan(this, str, Color.GREEN);
		spStr.setSpan(clickSpan, 0, str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return spStr;
	}
	
	@SuppressWarnings("unused")
	private void initPrintData() {
		sampleDot = new SampleDot[12];
        for (int i = 0; i < LEADNUM; i++) {
        	sampleDot[i] = new SampleDot(ECG_SAMPLE_RATE, DESTINATION_SAMPlE_RATE);
		}
        paint = new Paint();
		paint.setTextSize(22f);
		paint.setStrokeWidth(2f);
		width = 600;
		heigth = 3050;
		baseY = new int[4];
		leadX = new int[4];
		baseX = new int[3];
		oldX = new int[LEADNUM];
		for (int i = 0; i < leadX.length; i++) {
			leadX[i] = width/4*i + 66;
		}
		baseX[0] = leadX[1]-10;
		baseX[1] = leadX[2]-20;
		baseX[2] = leadX[3]-30;
		iiBaseX = leadX[0];
		for (int i = 0; i < baseY.length; i++) {
			baseY[i] = i*countY + baseYData + 10;
		}
		RegisteredUser testedMemeber = MeasurECGApplication.getInstance().getRegisterUser();
		
		paramInfo[0]="RV5+SV1：";
		paramInfo[1]="RV5/SV1 ：";
		paramInfo[2]="P/QRS/T ：";
		paramInfo[3]="QT/QTC   ：";
		paramInfo[4]="QRS          ：";
		paramInfo[5]="PR             ：";
		paramInfo[6]="HR             ：";
		paramInfo[7]="门诊号：";
		paramInfo[8]="住院号：";
		paramInfo[9]="性别：";
		paramInfo[10]="年龄：";
		paramInfo[11]="姓名：";
		paramResult[0] = r5_s1Str;
		paramResult[1] = r5s1Str;
		paramResult[2] = paxisStr;
		paramResult[3] = qtdStr;
		paramResult[4] = qrsStr;
		paramResult[5] = prStr;
		paramResult[6] = hrStr;
		paramResult[7] = "";
		paramResult[8] = "";
		if (testedMemeber!=null) {
			paramInfo[12]="ID："+testedMemeber.getIdCardNo();
			paramResult[9] = StringUtil.getGender(testedMemeber.getIdCardNo());
			paramResult[10] = StringUtil.getAge(testedMemeber.getIdCardNo())+"岁";
			paramResult[11] = testedMemeber.getName();
		}else {
			paramInfo[12]="ID：";
			paramResult[9]= "";
			paramResult[10]= "";
			paramResult[11]= "";
		}
	}
	private void reflushBackgroud() {
			backLayout.setBackgroundColor(getResources().getColor(
					R.color.page_background_color));
			titleLayout.setBackgroundColor(getResources().getColor(
					R.color.page_title_bar_color));
	}

	public void onBackBottonClick(View view) {
		finish();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textview_ecg_upload:
			String text = upLoadState.getText().toString();
			if ("已上传".equals(text)) {
				return;
			}
			if (!NetWorkService.isNetWorkConnected(this)) {
				isNoticeDialog("网络不可用，不能上传", false);
				return;
			}
			if (parseXMLECGData==null) {
				Toast.makeText(this, "心电数据为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if (upLoadEcgDialog != null) {
				upLoadEcgDialog.setMessage("正在上传数据，请稍候...");
				upLoadEcgDialog.show();
			}
			
			// 设置滤波
			ECGFilter ecgFilter = new ECGFilter();
			ecgFilter.setEmgFeq(EmgFeq.level_25);
			ecgFilter.setAcFeq(AcFeq.level_50);
			ecgFilter.setLowFeq(LowFeq.level_70);

			parseXMLECGData.setEcgFilter(ecgFilter);

			BDLocation location = MeasurECGApplication.getInstance().getLocation();
			if (location != null) {
				parseXMLECGData.setLatitude(location.getLatitude());
				parseXMLECGData.setLongitude(location.getLongitude());
				parseXMLECGData.setAddress(location.getAddrStr()+" "+location.getSemaAptag());
			} else {
				parseXMLECGData.setLatitude(-1);
				parseXMLECGData.setLongitude(-1);
			}
			parseXMLECGData.setDoctorComment(null);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					int level = ecgData.getScore();
					parseXMLECGData.setScore(null);

					AuthToken token = CommUtils.refreshToken();
					if (token==null){
//						String idCardNo = PreferUtils.getIntance().getIdCardNo();
//						String psd = PreferUtils.getIntance().getPassword(idCardNo);
//						NetWorkService.createApi(WeHealthToken.class).authorize();
					}
					ResultPassHelper rph = NetWorkService.createByteLongApi(WeHealthECGData.class, url).createECGData("Bearer " + token.getAccess_token(), parseXMLECGData);
					if (rph==null) {
						mHandler.sendEmptyMessage(ECGDATA_UPLOAD_FAILED);
						return;
					}
					if (Constant.FAILED.equals(rph.getName())) {
						reson = rph.getValue();
						mHandler.sendEmptyMessage(ECGDATA_UPLOAD_FAILED_RESON);
						return;
					}
					if (Constant.SUCCESS.equals(rph.getName())) {
						parseXMLECGData.setRequestedDiagnosisType(ECGDataDiagnosisType.uploaded.ordinal());
						if (ecgdata_saveId!=-1) {
							ECGDao.getECGIntance(idCardNo).updatEcgData(ecgdata_saveId, parseXMLECGData);
						}
						mHandler.sendEmptyMessage(ECGDATA_UPLOAD_SUCCESS);
					}
				}
			}).start();
			break;
		case R.id.textview_print:
//			if (EcgHistoryActivity.printerUtil==null) {
//				isBTConnDialog();
//				return;
//			}
//			if(EcgHistoryActivity.printerUtil.getState()!=PrinterClass.STATE_CONNECTED){
//				isBTConnDialog();
//				return;
//			}
//			Toast.makeText(this, "正在打印，请稍候...", Toast.LENGTH_SHORT).show();
//			Bitmap bitmap = createImg();
//			if (bitmap!=null) {
//				//居中指令
//				EcgHistoryActivity.printerUtil.write(new byte[] { 0x1b, 0x61,0x01});
//				EcgHistoryActivity.printerUtil.printImage(bitmap);
////				//居左指令
//				EcgHistoryActivity.printerUtil.write(new byte[] { 0x1b, 0x61,0x00});
//			}
			break;

		default:
			break;
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==REQUESTCODE_CONN_BT) {
//			if (data==null) {
//				isNoticeDialog("您没有连接到蓝牙打印机", true);
//				return;
//			}
//			Toast.makeText(this, "已连接到打印机，请点击打印按钮", Toast.LENGTH_SHORT).show();
		}
	}
	
	protected Bitmap createImg() {
		EcgDataParam edp = FileUtil.parserEcgParam(sdk+DateUtils.sdf_yyyyMMddHHmmss.format(ecgData.getTime())+".xml");
		vfshort = sampleReadEcgData(edp.getAvF(), 0);
    	vlshort = sampleReadEcgData(edp.getAvL(), 1);
    	vrshort = sampleReadEcgData(edp.getAvR(), 2);
		v1short = sampleReadEcgData(edp.getV1(), 3);
		v2short = sampleReadEcgData(edp.getV2(), 4);
		v3short = sampleReadEcgData(edp.getV3(), 5);
		v4short = sampleReadEcgData(edp.getV4(), 6);
		v5short = sampleReadEcgData(edp.getV5(), 7);
		v6short = sampleReadEcgData(edp.getV6(), 8);
		vishort = sampleReadEcgData(edp.getI(), 9);
		viishort = sampleReadEcgData(edp.getIi(), 10);
		viiishort = sampleReadEcgData(edp.getIii(), 11);
		Bitmap bitmap = Bitmap.createBitmap(width, heigth, Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		canvas.drawColor(Color.WHITE);
		leadName[2] = "I";
		leadName[1] = "II";
		leadName[0] = "III";
		leadName[5] = "avR";
		leadName[4] = "avL";
		leadName[3] = "avF";
		leadName[8] = "V1";
		leadName[7] = "V2";
		leadName[6] = "V3";
		leadName[11] = "V4";
		leadName[10] = "V5";
		leadName[9] = "V6";
		drawWaveLine(vishort, 2);
		drawWaveLine(viishort, 1);
		drawWaveLine(viiishort, 0);
		drawWaveLine(vrshort, 5);
		drawWaveLine(vlshort, 4);
		drawWaveLine(vfshort, 3);
		drawWaveLine(v1short, 8);
		drawWaveLine(v2short, 7);
		drawWaveLine(v3short, 6);
		drawWaveLine(v4short, 11);
		drawWaveLine(v5short, 10);
		drawWaveLine(v6short, 9);
		drawWaveLineII(viishort);
		
		drawGainType();
		
		for (int i = 0; i < paramInfo.length; i++) {
			drawText(paramInfo[i], leadX[0]+i*38, baseY[3]+countY+80);
		}
		for (int i = 0; i < paramResult.length; i++) {
			drawText(paramResult[i], leadX[0]+i*38, baseY[3]+countY+200);
		}
		
		drawText(edp.getFHP()+"—"+edp.getFLP()+"    AC "+edp.getFNotch()+"    25mm/s "+"    10mm/mV", leadX[3]+48, baseY[0]);
		drawText("测量时间："+DateUtils.sdf_yyyy_MM_dd_HH_mm_ss.format(ecgData.getTime()), leadX[3]+10, baseY[3]+countY+420);
		drawText("打印时间："+DateUtils.sdf_yyyy_MM_dd_HH_mm_ss.format(new Date()), leadX[3]-15, baseY[3]+countY+420);
		drawText("《分析结果》", leadX[2]+80, baseY[3]+countY+420);
		drawText(analysClass, leadX[2]+50, baseY[3]+countY+420);
		drawText("需要经过医师证实。医师姓名：_______", leadX[0], baseY[3]+countY+420);
		
		canvas.rotate(90, leadX[2]+36, baseY[3]+countY+420);
		TextPaint textPaint = new TextPaint();
		textPaint.setTextSize(20f);
		textPaint.setColor(Color.BLACK);
		//getWidth()表示绘制多宽后换行
		StaticLayout sl = new StaticLayout(analysResult, textPaint, 380, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
		//从0,0开始绘制
		canvas.translate(leadX[2]+36, baseY[3]+countY+420);
		sl.draw(canvas);
		canvas.rotate(-90, leadX[2]+36, baseY[3]+countY+420);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return bitmap;
	}
	
	private void drawGainType() {
		int x = 0;
//		if (Gain==24) {
//			canvas.drawLine(leadX[2]+x, 30, leadX[2]+x, 40, paint);
//			canvas.drawLine(leadX[2]+x, 40, leadX[2]+x+24/Gain*2*Gain, 40, paint);
//			canvas.drawLine(leadX[2]+x+24/Gain*2*Gain, 40, leadX[2]+x+24/Gain*2*Gain, 65, paint);
//			canvas.drawLine(leadX[2]+x+24/Gain*2*Gain, 65, leadX[2]+x, 65, paint);
//			canvas.drawLine(leadX[2]+x, 65, leadX[2]+x, 75, paint);
//		}
		if (Gain==12) {
			canvas.drawLine(leadX[1]+x, 10, leadX[1]+x, 20, paint);
			canvas.drawLine(leadX[1]+x, 20, leadX[1]+x+6*Gain+10, 20, paint);
			canvas.drawLine(leadX[1]+x+6*Gain+10, 20, leadX[1]+x+6*Gain+10, 45, paint);
			canvas.drawLine(leadX[1]+x+6*Gain+10, 45, leadX[1]+x, 45, paint);
			canvas.drawLine(leadX[1]+x, 45, leadX[1]+x, 55, paint);
		}
//		if (Gain==6) {
//			canvas.drawLine(leadX[2]+x, 30, leadX[2]+x, 40, paint);
//			canvas.drawLine(leadX[2]+x, 40, leadX[2]+x+24/Gain*2*Gain, 40, paint);
//			canvas.drawLine(leadX[2]+x+24/Gain*2*Gain, 40, leadX[2]+x+24/Gain*2*Gain, 65, paint);
//			canvas.drawLine(leadX[2]+x+24/Gain*2*Gain, 65, leadX[2]+x, 65, paint);
//			canvas.drawLine(leadX[2]+x, 65, leadX[2]+x, 75, paint);
//		}
	}
	
	private short[] sampleReadEcgData(short[] srcData, int lead){
		int len = srcData.length;
		int[] src = new int[len];
		int[] outData = new int[DESTINATION_SAMPlE_RATE * 10];

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
	/**
	 * 绘制II导
	 * @param data
	 */
	private void drawWaveLineII(short[] data) {
		int x = 0;
		int oldXValue = 0;
		int y = baseY[0];
		int yTotal = countY*4;
		
		if (yTotal>data.length) {
			yTotal = data.length;
		}
		for (int i = 0; i < yTotal; i++) {
			x = iiBaseX + data[i] /Gain;
			if (i!=0) {
				canvas.drawLine(oldXValue, y, x, i+baseY[0], paint);
			}
			oldXValue = x;
			y = i+baseY[0];
		}
		
		//旋转文字
		drawText("II", iiBaseX+20, baseY[0]);
	}

	/**
	 * 绘制12导联曲线
	 * @param data
	 * @param lead
	 */
	private void drawWaveLine(short[] data, int lead) {
		int x = 0;
		int y = baseY[lead/3];
		for (int i = 0; i < countY; i++) {
			x = baseX[lead%3] + data[i] /Gain;
			if (i!=0) {
				canvas.drawLine(oldX[lead], y, x, i+baseY[lead/3], paint);
			}
			oldX[lead] = x;
			y = i+baseY[lead/3];
		}

		//旋转文字
		drawText(leadName[lead], baseX[lead%3]+20, baseY[lead/3]);
	}
	
	/**
	 * 画文字信息
	 * @param text
	 * @param x
	 * @param y
	 */
	private void drawText(String text, int x, int y){
		canvas.rotate(90, x, y);
		canvas.drawText(text, x, y, paint);
		canvas.rotate(-90, x, y);
	}


	private void isNoticeDialog(String message, final boolean b){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.friend_notify);
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (b) {
					DetailEcgHistoryReport.this.finish();
				}
			}
		});
		builder.show();
	}
	
	/**华清心仪的显示结果**/
	/**
	private void initDataDemo() {
		InputStream is = null;
		try {
			is = getAssets().open("20170306155158.xml");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (is==null) {
			Toast.makeText(this, "本地心电数据文件已经删除", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		parseXMLECGData = FileUtils.parserEcgData(is);
		
		if (parseXMLECGData==null) {
			Toast.makeText(this, "本地心电数据文件已经删除", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		
		ecgData = new ECGData();
		
		hr.setText(parseXMLECGData.getHeartRate()+" bpm");
		pr.setText(parseXMLECGData.getPr()+" ms");
		qrs.setText(parseXMLECGData.getQrs()+" ms");
		qtd.setText(parseXMLECGData.getQt()+"/"+parseXMLECGData.getQtc()+" ms");
		paxis.setText(parseXMLECGData.getPaxis()+"/"+parseXMLECGData.getQrsaxis()+"/"+parseXMLECGData.getTaxis()+" °");
		rv5sv1.setText(parseXMLECGData.getRv5()+"/"+parseXMLECGData.getSv1()+" mV");
		float r_s = 0;
		try {
			float rv5 = (float) parseXMLECGData.getRv5();
			float sv1 = (float) parseXMLECGData.getSv1();
			r_s = Math.abs(rv5)+Math.abs(sv1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		DecimalFormat df =new DecimalFormat("#.000");  
		String rs = df.format(r_s);
		rv5_sv1.setText(rs+" mV");
		if (ecgData.getRequestedDiagnosisType()==ECGDataDiagnosisType.uploaded.ordinal()) {
			upLoadState.setText("已上传");
			upLoadState.setEnabled(false);
		}else {
			upLoadState.setText("上传");
			upLoadState.setEnabled(true);
		}
		
		String sym = ecgData.getSymptoms();
		if (TextUtils.isEmpty(sym)) {
			sympLayout.setVisibility(View.GONE);
		}else {
			sympLayout.setVisibility(View.VISIBLE);
		}
		symptoms.setText(sym);
		
		parseXMLECGData.setTime(ecgData.getTime());
		parseXMLECGData.setRequestedDiagnosisType(ecgData.getRequestedDiagnosisType());
		parseXMLECGData.setPatientId(ecgData.getPatientId());
		parseXMLECGData.setRegisteredUserId(ecgData.getRegisteredUserId());
		parseXMLECGData.setEquipmentSerialNo(ecgData.getEquipmentSerialNo());
		ecgClass.setText(ECGDataUtil.getClassByJson(parseXMLECGData.getAutoDiagnosisResult()));
		ecgResult.setText(ECGDataUtil.getResuByJson(parseXMLECGData.getAutoDiagnosisResult()));
		
		ecgData.setaVf(parseXMLECGData.getaVf());
		ecgData.setaVl(parseXMLECGData.getaVl());
		ecgData.setaVr(parseXMLECGData.getaVr());
		ecgData.setV1(parseXMLECGData.getV1());
		ecgData.setV2(parseXMLECGData.getV2());
		ecgData.setV3(parseXMLECGData.getV3());
		ecgData.setV4(parseXMLECGData.getV4());
		ecgData.setV5(parseXMLECGData.getV5());
		ecgData.setV6(parseXMLECGData.getV6());
		ecgData.setvI(parseXMLECGData.getvI());
		ecgData.setvII(parseXMLECGData.getvII());
		ecgData.setvIII(parseXMLECGData.getvIII());
		readFragment = new ECGReadFragment();
		Bundle bundle = new Bundle();
		bundle.putByteArray("avf", ecgData.getaVf());
		bundle.putByteArray("avl", ecgData.getaVl());
		bundle.putByteArray("avr", ecgData.getaVr());
		bundle.putByteArray("v1", ecgData.getV1());
		bundle.putByteArray("v2", ecgData.getV2());
		bundle.putByteArray("v3", ecgData.getV3());
		bundle.putByteArray("v4", ecgData.getV4());
		bundle.putByteArray("v5", ecgData.getV5());
		bundle.putByteArray("v6", ecgData.getV6());
		bundle.putByteArray("vi", ecgData.getvI());
		bundle.putByteArray("vii", ecgData.getvII());
		bundle.putByteArray("viii", ecgData.getvIII());
		bundle.putBoolean("style", !PreferenceUtils.getInstance(this).getChangeStyle());
		bundle.putString("fhp", parseXMLECGData.getFhp());
		bundle.putString("flp", parseXMLECGData.getFlp());
		readFragment.setArguments(bundle);
		getSupportFragmentManager().beginTransaction()
		.add(R.id.listview_chart, readFragment).show(readFragment)
		.commit();
	}**/

}
