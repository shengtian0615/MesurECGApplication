//package com.wehealth.mesurecg.activity;
//
//import java.io.File;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.wehealth.mesurecg.BaseActivity;
//import com.wehealth.mesurecg.R;
//import com.wehealth.mesurecg.view.UIProgressDialog;
//import com.wehealth.model.domain.enumutil.Gender;
//import com.wehealth.model.domain.model.AppNotificationMessage;
//import com.wehealth.model.domain.model.EcgDataParam;
//import com.wehealth.model.domain.model.RegisteredUser;
//import com.wehealth.model.util.Constant;
//import com.wehealth.model.util.ECGDataUtil;
//
//import org.jivesoftware.smack.util.StringUtils;
//
//@SuppressLint("SimpleDateFormat")
//public class DetailMsgActivity extends BaseActivity {
//
//	private final int SAVE_REPORT_PDF = 200;
//	private final int ECGDATA_FILE_MISS = 400;
//
//	private AppNotificationMessage appMsg;
//
//	private TextView title;
//	private TextView idcodeTV, genderTV;//, ageTV, orderNoTV
//	private LinearLayout backLayout;
//	private RelativeLayout titleLayout;
//	private Button seeReport;
//
//	private UIProgressDialog progressDialog;
//	private EcgDataParam localECG;
//	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	private String sdk = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ECGDATA/XML/";
//	private PDFUtils pdfUtils;
//
//	@SuppressLint("HandlerLeak")
//	Handler handler = new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case SAVE_REPORT_PDF:
//				if (progressDialog!=null && !isFinishing()) {
//					progressDialog.dismiss();
//				}
//				try {
//					String path = (String) msg.obj;
//					File file = new File(path);
//					Intent intent = new Intent("android.intent.action.VIEW");
//					intent.addCategory("android.intent.category.DEFAULT");
//					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					Uri uri = Uri.fromFile(file);
//					intent.setDataAndType(uri, "application/pdf");//  文档格式
//					startActivity(intent);
//					finish();
//				} catch (Exception e) {
//					e.printStackTrace();
//					Toast.makeText(DetailMsgActivity.this, "请先安装pdf阅读器软件", Toast.LENGTH_LONG).show();
//				}
//				break;
//			case ECGDATA_FILE_MISS:
//				if (progressDialog!=null && !isFinishing()) {
//					progressDialog.dismiss();
//				}
//				Toast.makeText(DetailMsgActivity.this, "本地心电数据文件已经删除，不能保存成PDF", Toast.LENGTH_SHORT).show();
//				finish();
//				break;
//
//			default:
//				break;
//			}
//		}
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.detail_msg);
//
//		Intent intent = getIntent();
//		appMsg = (AppNotificationMessage) intent.getSerializableExtra("msg");
//		pdfUtils = new PDFUtils();
//		progressDialog = new UIProgressDialog(this);
//		progressDialog.setCancelable(false);
//		initView();
//		initData();
//		reflushStyle();
//	}
//
//	private void initView() {
//		title = (TextView) findViewById(R.id.detail_msg_title);
//		idcodeTV = (TextView) findViewById(R.id.detail_msg_idcode);
//		genderTV = (TextView) findViewById(R.id.detail_msg_gender);
//		seeReport = (Button) findViewById(R.id.detail_msg_pdfreport);
//
//		backLayout = (LinearLayout) findViewById(R.id.background);
//		titleLayout =  (RelativeLayout) findViewById(R.id.title_layout);
//	}
//
//	private void initData() {
//		if (appMsg==null) {
//			Toast.makeText(this, "消息为空", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		title.setText(sdf1.format(new Date(appMsg.getTime())));
//		if (Constant.MSG_ECG_Free_Check_Request.equals(appMsg.getSubject())) {
//			String content = "";
//			String comment = appMsg.getMsgComment();
//			content += "读图医生："+appMsg.getDoctorName();
//			content += "\n读图结果："+ECGDataUtil.getClassByJson(appMsg.getMessage())+"  "+ECGDataUtil.getResuByJson(appMsg.getMessage());
//			if (!TextUtils.isEmpty(comment)) {
//				content += "\n医生建议："+comment;
//			}
//			genderTV.setText(content);
//			genderTV.setGravity(Gravity.CENTER_HORIZONTAL);
//			seeReport.setVisibility(View.VISIBLE);
//		}else if (Constant.Manual_Diagnosis.equals(appMsg.getSubject())) {
//			idcodeTV.setText("读图费用");
//			genderTV.setText("心电读图扣费："+appMsg.getMessage());
//		}else if (Constant.Order_Diagnosis.equals(appMsg.getSubject())) {
//			idcodeTV.setText("咨询费用");
//			genderTV.setText("咨询医生扣费："+appMsg.getMessage());
//		}else if(Constant.MSG_ECG_NEW_TESTED_DATA.equals(appMsg.getSubject())){
//			idcodeTV.setText("自动分析报告");
//			String classStr = ECGDataUtil.getClassByJson(appMsg.getMessage());
//			String resutStr = ECGDataUtil.getResuByJson(appMsg.getMessage());
//			if (TextUtils.isEmpty(classStr) || TextUtils.isEmpty(resutStr)) {
//				genderTV.setText("自动分析结论"+" : "+appMsg.getMessage());
//			}else {
//				genderTV.setText("自动分析结论"+" : "+classStr+"  "+resutStr);
//			}
//		}else {
//			idcodeTV.setText(appMsg.getSubject());
//			genderTV.setText(appMsg.getMessage());
//		}
//	}
//
//	private void reflushStyle() {
//			backLayout.setBackgroundColor(getResources().getColor(R.color.page_background_color));
//			titleLayout.setBackgroundColor(getResources().getColor(R.color.page_title_bar_color));
//			idcodeTV.setTextColor(getResources().getColor(R.color.text_white));
//			genderTV.setTextColor(getResources().getColor(R.color.text_white));
//			seeReport.setTextColor(getResources().getColor(R.color.text_white));
//
//	}
//
//	public void seenECGReport(View view){
//		showFreeCheckMsg();
//	}
//
//	private void showFreeCheckMsg() {
//		if (!TextUtils.isEmpty(appMsg.getMsgOther())) {
//			File f1 = new File(appMsg.getMsgOther());
//			if (f1.exists()) {
//				openPDF(appMsg.getMsgOther());
//				return;
//			}
//		}
//		File orgiFile = new File(Environment.getExternalStorageDirectory()
//				+ File.separator + "ECGDATA"
//				+ File.separator + "Report"
//				+ File.separator + appMsg.getMsgPatientIdCardNo()+"_"+sdf.format(new Date(appMsg.getTestTime()))+".pdf");
//		if (orgiFile.exists()) {
//			openPDF(orgiFile.getAbsolutePath());
//			return;
//		}
//		progressDialog.setMessage("正在打开成PDF文件...");
//		progressDialog.show();
//		new Thread(new Runnable() {
//
//			@SuppressLint("UseSparseArrays")
//			@Override
//			public void run() {
//				localECG = FileUtils.parserEcgParam(sdk+sdf.format(appMsg.getTestTime())+".xml");
//				if (localECG==null) {
//					handler.sendEmptyMessage(ECGDATA_FILE_MISS);
//					return;
//				}
//
//				Map<String, Map<String, String>> listMaps = new HashMap<String, Map<String,String>>();
//				Map<String, String> strMaps = new HashMap<String, String>();
//				Map<Integer, short[]> ecgDatas = new HashMap<Integer, short[]>();
//
//				Map<String, String> personInfo = new HashMap<String, String>();
//				Map<String, String> analysis = new HashMap<String, String>();
//
//				RegisteredUser patient = ClientApp.getInstance().getRegisteredUser();
//				if (patient!=null) {
//					personInfo.put("Name", patient.getName());
//					if (Gender.male.ordinal()==patient.getGender().ordinal()) {
//						personInfo.put("Gender", "男");
//					}else {
//						personInfo.put("Gender", "女");
//					}
//					personInfo.put("AGE", StringUtils.getAge(patient.getIdCardNo())+"");
//				}else {
//					if ("0".equals(localECG.getGender())) {
//						personInfo.put("Gender", "男");
//					}else {
//						personInfo.put("Gender", "女");
//					}
//					personInfo.put("AGE", localECG.getAge());
//					personInfo.put("Name", "");
//				}
//				personInfo.put("ID", appMsg.getMsgPatientIdCardNo());
//				analysis.put("HR", localECG.getHeartRate()+" bpm");
//				analysis.put("PQRST", localECG.getPAxis()+"/"+localECG.getQRSAxis()+"/"+localECG.getTAxis()+" °");
//				analysis.put("RV5", localECG.getRV5()+" mV");
//				analysis.put("SV1", localECG.getSV1()+" mV");
//				analysis.put("PR", localECG.getPRInterval()+" ms");
//				analysis.put("QRS", localECG.getQRSAxis()+" ms");
//				analysis.put("QTQTC", localECG.getQTD()+"/"+localECG.getQTC()+" ms");
//				float r_s = 0;
//				try {
//					float rv5 = Float.valueOf(localECG.getRV5());
//					float sv1 = Float.valueOf(localECG.getSV1());
//					r_s = Math.abs(rv5)+Math.abs(sv1);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				DecimalFormat df =new DecimalFormat("#.000");
//				String rs = df.format(r_s);
//				analysis.put("RV5SV1", rs+" mV");
//
//				listMaps.put("personInfo", personInfo);
//				listMaps.put("analysis", analysis);
//
//				strMaps.put("comment", "** 正常心电图 **");
//				strMaps.put("doctorName", appMsg.getDoctorName());
//				strMaps.put("hospital", appMsg.getMsgHospital());
//				strMaps.put("ecg_checktime", String.valueOf(appMsg.getTestTime()));
//				PackageInfo info = StringUtils.getPackInfo(DetailMsgActivity.this);
//				if(info!=null) {
//					strMaps.put("ecg_version", "ECG "+info.versionName);
//				} else{
//					strMaps.put("ecg_version", "");
//				}
//
//				String jsonResult = appMsg.getMessage();
//				String docResultStr = ECGDataUtil.getResuByJson(jsonResult);
//				String docClassStr = ECGDataUtil.getClassByJson(jsonResult);
//				strMaps.put("ecg_result", docResultStr);
//				strMaps.put("ecg_class", docClassStr);
//
//				String autoResultStr = ECGDataUtil.getResuByJson(localECG.getAutoDiagnosisResult());
//				String autoClassStr = ECGDataUtil.getClassByJson(localECG.getAutoDiagnosisResult());
//				strMaps.put("ecg_result_auto", autoResultStr);
//				strMaps.put("ecg_class_auto", autoClassStr);
//				ecgDatas.put(0, localECG.getAvF());
//				ecgDatas.put(1, localECG.getAvL());
//				ecgDatas.put(2, localECG.getAvR());
//				ecgDatas.put(3, localECG.getIii());
//				ecgDatas.put(4, localECG.getIi());
//				ecgDatas.put(5, localECG.getI());
//				ecgDatas.put(6, localECG.getV6());
//				ecgDatas.put(7, localECG.getV5());
//				ecgDatas.put(8, localECG.getV4());
//				ecgDatas.put(9, localECG.getV3());
//				ecgDatas.put(10, localECG.getV2());
//				ecgDatas.put(11, localECG.getV1());
//
//				String reportPath = pdfUtils.savePDF(DetailMsgActivity.this, listMaps, strMaps, ecgDatas);//"/sdcard/ECGDATA/Report/test0601_20170614140748.pdf";
//				appMsg.setMsgOther(reportPath);
////				appMsg.setAskStatus(NotifyDoctorAskStatus.UNASK);
//				AppNotificationMessageDao.getAppInstance(DetailMsgActivity.this).updateMessage(appMsg);
//				Message msg = handler.obtainMessage(SAVE_REPORT_PDF);
//				msg.obj = reportPath;
//				handler.sendMessage(msg);
//			}
//		}).start();
//	}
//
//	private void openPDF(String path){
//		try {
//			File file = new File(path);
//			Intent intent = new Intent("android.intent.action.VIEW");
//			intent.addCategory("android.intent.category.DEFAULT");
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			Uri uri = Uri.fromFile(file);
//			intent.setDataAndType(uri, "application/pdf");//  文档格式
//			startActivity(intent);
//			this.finish();
//		} catch (Exception e) {
//			e.printStackTrace();
//			Toast.makeText(DetailMsgActivity.this, "请先安装pdf阅读器软件", Toast.LENGTH_LONG).show();
//		}
//	}
//
//	public void onBackBottonClick(View view) {
//		finish();
//	}
//}
//
//
///**
//
//	ResultPassHelper rph = FileUtils.obtainWarnLevelMap(this, autoResultStr);
//	if (rph==null) {//没有找到对应的级别
//		rph = new ResultPassHelper();
//		if (autoClassStr.equals("** 正常心电图 **")) {
//			rph.setName("一");
//			rph.setValue("心电图无明显异常，如有症状建议就医（心电图仅供参考）");
//		}else {
//			rph.setName("二");
//			rph.setValue("心电图提示异常，建议咨询专业医生。（心电图仅供参考）");
//		}
//	}
//	showECGWarn(rph);
//	idcodeTV.setText("用户ID："+appMsg.getMsgPatientIdCardNo());
//	if ("0".equals(localECG.getGender())) {
//		genderTV.setText("性别：男");
//	}else {
//		genderTV.setText("性别：女");
//	}
//	ageTV.setText("年龄："+localECG.getAge()+"岁");
//
//	autoClassfly.setText(ECGDataUtil.getClassByJson(localECG.getAutoDiagnosisResult()));
//	autoResult.setText(ECGDataUtil.getResuByJson(localECG.getAutoDiagnosisResult()));
//
//	paramHR.setText("HR："+localECG.getHeartRate()+" bpm");
//	paramPR.setText("PR间期："+localECG.getPRInterval()+" ms");
//	paramQRS.setText("QRS时限："+localECG.getQRSAxis()+" ms");
//	paramQT_QTC.setText("QT/QTc间期："+localECG.getQTD()+"/"+localECG.getQTC()+" ms");
//	paramP_QRS_T.setText("P/QRS/T轴："+localECG.getPAxis()+"/"+localECG.getQRSAxis()+"/"+localECG.getTAxis()+" °");
//	paramRV5.setText("RV5："+localECG.getRV5()+" mV");
//	paramSV1.setText("SV1："+localECG.getSV1()+" mV");
//	r_s = 0;
//	try {
//		float rv5 = Float.valueOf(localECG.getRV5());
//		float sv1 = Float.valueOf(localECG.getSV1());
//		r_s = Math.abs(rv5)+Math.abs(sv1);
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
//	rs = df.format(r_s);
//	paramRV5_SV1.setText("RV5+SV1："+rs+" mV");
//
//	hospital.setText("医院："+appMsg.getMsgHospital());
//	doctorName.setText("医生："+appMsg.getDoctorName());
//	doctorCheckResult.setText("医生读图结果：\n"+autoClassStr+"\n"+autoResultStr);
//	if (!TextUtils.isEmpty(appMsg.getMsgComment())) {
//		doctorComment.setText("医生的建议：\n"+appMsg.getMsgComment());
//	}else {
//		doctorComment.setVisibility(View.GONE);
//	}
//private void showECGWarn(ResultPassHelper rph) {
//	if ("三".equals(rph.getName())) {
//		levelLayout.setBackgroundColor(getResources().getColor(R.color.color_red_AD0010));
//		ecgParamTV.setBackgroundColor(getResources().getColor(R.color.color_red_AD0010));
//		autoTV.setBackgroundColor(getResources().getColor(R.color.color_red_AD0010));
//		doctorTV.setBackgroundColor(getResources().getColor(R.color.color_red_AD0010));
//	}
//	if ("二".equals(rph.getName())) {
//		levelLayout.setBackgroundColor(getResources().getColor(R.color.color_yellow_fbc619));
//		ecgParamTV.setTextColor(getResources().getColor(R.color.color_yellow_fbc619));
//		autoTV.setTextColor(getResources().getColor(R.color.color_yellow_fbc619));
//		doctorTV.setTextColor(getResources().getColor(R.color.color_yellow_fbc619));
//	}
//	if ("一".equals(rph.getName())) {
//		levelLayout.setBackgroundColor(getResources().getColor(R.color.color_green_3D7149));
//		ecgParamTV.setTextColor(getResources().getColor(R.color.color_green_3D7149));
//		autoTV.setTextColor(getResources().getColor(R.color.color_green_3D7149));
//		doctorTV.setTextColor(getResources().getColor(R.color.color_green_3D7149));
//	}
//}
// **/
