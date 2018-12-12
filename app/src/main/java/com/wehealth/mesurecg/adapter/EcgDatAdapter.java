package com.wehealth.mesurecg.adapter;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.util.FileUtils;
import com.wehealth.mesurecg.MeasurECGApplication;
import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.dao.AppNotificationMessageDao;
import com.wehealth.mesurecg.dao.ECGDao;
import com.wehealth.mesurecg.utils.PDFUtils;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.mesurecg.view.UIProgressDialog;
import com.wehealth.model.domain.enumutil.ECGDataDiagnosisType;
import com.wehealth.model.domain.enumutil.Gender;
import com.wehealth.model.domain.enumutil.NotifyDoctorAskStatus;
import com.wehealth.model.domain.model.AppNotificationMessage;
import com.wehealth.model.domain.model.ECGData;
import com.wehealth.model.domain.model.EcgDataParam;
import com.wehealth.model.domain.model.RegisteredUser;
import com.wehealth.model.util.DateUtils;
import com.wehealth.model.util.ECGDataUtil;
import com.wehealth.model.util.FileUtil;
import com.wehealth.model.util.StringUtil;

public class EcgDatAdapter extends BaseAdapter {

	private final int SAVE_REPORT_PDF = 201;
	private final int ECGDATA_FILE_MISS = 401;
	
	private Context context;
	private List<ECGData> dataList;
	private LayoutInflater inflater;
	private UIProgressDialog progressDialog;
	private EcgDataParam localECG;
	private PDFUtils pdfUtils;
	private RegisteredUser pat;
	private String idCardNo;
	private String sdk = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ECGDATA/XML/";
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SAVE_REPORT_PDF:
				if (progressDialog!=null) {
					progressDialog.dismiss();
				}
				try {
					String path = (String) msg.obj;
					openPDF(path);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(context, "请先安装pdf阅读器软件", Toast.LENGTH_LONG).show();
				}
				break;
			case ECGDATA_FILE_MISS:
				if (progressDialog!=null) {
					progressDialog.dismiss();
				}
				Toast.makeText(context, "本地心电数据文件已经删除，不能保存成PDF", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};
	
	public EcgDatAdapter(Context ctx){
		context = ctx;
		inflater = LayoutInflater.from(context);
		dataList = new ArrayList<ECGData>();
		pdfUtils = new PDFUtils();
		progressDialog = new UIProgressDialog(context);
		progressDialog.setCancelable(false);
		idCardNo = PreferUtils.getIntance().getIdCardNo();
		pat = MeasurECGApplication.getInstance().getRegisterUser();
	}
	
	public List<ECGData> getDataList() {
		return dataList;
	}

	public void setDataList(List<ECGData> dataList) {
		this.dataList = dataList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (dataList==null) {
			return 0;
		}else {
			return dataList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = inflater.inflate(R.layout.row_ecg_data, null);
			vh.timeTV = (TextView) convertView.findViewById(R.id.textView_collectionTime_normal);
			vh.heartNum = (TextView) convertView.findViewById(R.id.textView_heartbeat_number);
			vh.classType = (TextView) convertView.findViewById(R.id.textView_diag_class);
			vh.classResult = (TextView) convertView.findViewById(R.id.textView_diag_result);
			vh.uploadState = (TextView) convertView.findViewById(R.id.textView_ecgdata_upload_state);
			vh.delBtn = (Button) convertView.findViewById(R.id.ecg_data_del);
			vh.repoTV = (TextView) convertView.findViewById(R.id.ecg_data_report);
			vh.repoImg = (TextView) convertView.findViewById(R.id.ecg_data_reportimg);
			vh.printBtn = (Button) convertView.findViewById(R.id.ecg_data_print);
			vh.repoLayout = (LinearLayout) convertView.findViewById(R.id.ecg_datal31);
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		vh.timeTV.setText(dataList.get(position).getTime().toLocaleString());
		vh.repoTV.setTextColor(context.getResources().getColor(R.color.set_edit_text_1));
		vh.timeTV.setTextColor(context.getResources().getColor(R.color.set_edit_text_1));
		vh.repoImg.setBackground(context.getResources().getDrawable(R.drawable.ecg_doct_repimg));

		vh.heartNum.setText(String.valueOf(dataList.get(position).getHeartRate()));
		if (dataList.get(position).getHeartRate() > 100 || dataList.get(position).getHeartRate() < 60) {
			vh.heartNum.setTextColor(context.getResources().getColor(R.color.text_red));
		} else {
			vh.heartNum.setTextColor(context.getResources().getColor(R.color.text_green));
		}
		if (dataList.get(position).getVersion()==0) {
			String analyClass = ECGDataUtil.getClassByJson(dataList.get(position).getAutoDiagnosisResult());
			if (TextUtils.isEmpty(analyClass)) {
				vh.classType.setText("分析失败");
			}else {
				vh.classType.setText(analyClass);
			}
			String analyResult =  ECGDataUtil.getResuByJson(dataList.get(position).getAutoDiagnosisResult());
			if (TextUtils.isEmpty(analyResult)) {
				vh.classResult.setText("分析失败");
			}else {
				vh.classResult.setText(analyResult);
			}	
		}else if(dataList.get(position).getVersion()==1){
			vh.classResult.setText(dataList.get(position).getAutoDiagnosisResult());
		}
		if (dataList.get(position).getRequestedDiagnosisType() == ECGDataDiagnosisType.uploaded.ordinal()) {
			vh.uploadState.setText("已上传");
		} else {
			vh.uploadState.setText("   ");
		}
//		if (isPrintVisiable) {
//			vh.printBtn.setVisibility(View.VISIBLE);
//		}else {
//			vh.printBtn.setVisibility(View.GONE);
//		}
		vh.printBtn.setVisibility(View.GONE);
		if (dataList.get(position).getScore()==1) {
			vh.classType.setTextColor(context.getResources().getColor(R.color.color_green_3D7149));
			vh.classResult.setBackgroundColor(context.getResources().getColor(R.color.color_green_3D7149));
		}else if (dataList.get(position).getScore()==2) {
			vh.classType.setTextColor(context.getResources().getColor(R.color.color_yellow_e6a500));
			vh.classResult.setBackgroundColor(context.getResources().getColor(R.color.color_yellow_e6a500));
		}else if(dataList.get(position).getScore()==3){
			vh.classType.setTextColor(context.getResources().getColor(R.color.color_red_AD0010));
			vh.classResult.setBackgroundColor(context.getResources().getColor(R.color.color_red_AD0010));
		}
		vh.delBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				delECGDateNotify(dataList.get(position));
			}
		});
		
		if (TextUtils.isEmpty(dataList.get(position).getManulDiagnosisResult())) {
			vh.repoLayout.setVisibility(View.GONE);
		}else {
			vh.repoLayout.setVisibility(View.VISIBLE);
		}
		vh.repoLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (dataList.get(position).getTime()==null) {
					Toast.makeText(context, "本条数据为空，不能查看", Toast.LENGTH_SHORT).show();
					return;
				}
				File orgiFile = new File(Environment.getExternalStorageDirectory()
						+ File.separator + "ECGDATA"
						+ File.separator + "Report"
						+ File.separator + dataList.get(position).getPatientId()+"_"+DateUtils.sdf_yyyyMMddHHmmss.format(dataList.get(position).getTime())+".pdf");
				AppNotificationMessage msg = AppNotificationMessageDao.getAppInstance(idCardNo).getMessageByTime(dataList.get(position).getTime().getTime());
				if (msg==null || TextUtils.isEmpty(msg.getMsgOther())) {
					if (orgiFile.exists()) {
						openPDF(orgiFile.getAbsolutePath());
						return;
					}
					saveECGReport(dataList.get(position));
					return;
				}
				File file = new File(msg.getMsgOther());
				if (file.exists()) {
					openPDF(msg.getMsgOther());
					return;
				}
				saveECGReport(dataList.get(position));
			}
		});
		
//		vh.printBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				EcgHistoryActivity.printer.startToPrint(dataList.get(position), context, 10);
//			}
//		});
		return convertView;
	}

	
	protected void delECGDateNotify(final ECGData ecgData) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.friend_notify);
		builder.setMessage(R.string.sure_delete);
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AppNotificationMessage msg = AppNotificationMessageDao.getAppInstance(idCardNo).getMessageByTime(ecgData.getTime().getTime());
				if (msg==null) {//医生还没有复核，就不删除本地数据
					ECGDao.getECGIntance(idCardNo).deleteECGData(String.valueOf(ecgData.getId()));
				}else if (msg.getAskStatus()==NotifyDoctorAskStatus.UNASK) {
					ECGDao.getECGIntance(idCardNo).deleteECGData(String.valueOf(ecgData.getId()));
					StringUtil.deleteECGFile(ecgData.getPatientId(), ecgData.getTime());
				}else {
					ECGDao.getECGIntance(idCardNo).deleteECGData(String.valueOf(ecgData.getId()));
				}
				List<ECGData> ecgDatas = ECGDao.getECGIntance(idCardNo).getAllECGData();
				Collections.sort(ecgDatas);
				setDataList(ecgDatas);
			}
		});
		builder.show();
	}
	
	protected void saveECGReport(final ECGData ecgData){
		progressDialog.setMessage("正在打开pdf...");
		progressDialog.show();
		new Thread(new Runnable() {
			
			@SuppressLint("UseSparseArrays")
			@Override
			public void run() {
				AppNotificationMessage appMsg = AppNotificationMessageDao.getAppInstance(idCardNo).getMessageByTime(ecgData.getTime().getTime());
				localECG = FileUtil.parserEcgParam(sdk+DateUtils.sdf_yyyyMMddHHmmss.format(ecgData.getTime())+".xml");
				if (localECG==null) {
					handler.sendEmptyMessage(ECGDATA_FILE_MISS);
					return;
				}
				
				Map<String, Map<String, String>> listMaps = new HashMap<String, Map<String,String>>();
				Map<String, String> strMaps = new HashMap<String, String>();
				Map<Integer, short[]> ecgDatas = new HashMap<Integer, short[]>();
				
				Map<String, String> personInfo = new HashMap<String, String>();
				Map<String, String> analysis = new HashMap<String, String>();
				
				if (appMsg!=null) {
					strMaps.put("doctorName", appMsg.getDoctorName());
					strMaps.put("hospital", appMsg.getMsgHospital());
					String jsonResult = appMsg.getMessage();
					String docResultStr = ECGDataUtil.getResuByJson(jsonResult);
					String docClassStr = ECGDataUtil.getClassByJson(jsonResult);
					strMaps.put("ecg_result", docResultStr);
					strMaps.put("ecg_class", docClassStr);
				}else {
					String jsonResult = ecgData.getManulDiagnosisResult();
					String docResultStr = ECGDataUtil.getResuByJson(jsonResult);
					String docClassStr = ECGDataUtil.getClassByJson(jsonResult);
					String dInfo = ecgData.getDoctorId();
					try {
						JSONObject jsonObject = new JSONObject(dInfo);
						strMaps.put("doctorName", jsonObject.getString("name"));
						strMaps.put("hospital", jsonObject.getString("hospital"));
					} catch (JSONException e) {
						e.printStackTrace();
						strMaps.put("doctorName", "");
						strMaps.put("hospital", "");
					}
					strMaps.put("ecg_result", docResultStr);
					strMaps.put("ecg_class", docClassStr);
				}
				strMaps.put("ecg_checktime", String.valueOf(ecgData.getTime().getTime()));
				personInfo.put("ID", ecgData.getPatientId());
				
				if (pat!=null) {
					personInfo.put("Name", pat.getName());
					if (Gender.male.ordinal()==pat.getGender().ordinal()) {
						personInfo.put("Gender", "男");
					}else {
						personInfo.put("Gender", "女");
					}
					personInfo.put("AGE", StringUtil.getGender(pat.getIdCardNo()));
				}else {
					personInfo.put("Gender", "");
					personInfo.put("AGE", "");
					personInfo.put("Name", "");
				}
				personInfo.put("AGE", localECG.getAge());
				analysis.put("HR", localECG.getHeartRate()+" bpm");
				analysis.put("PQRST", localECG.getPAxis()+"/"+localECG.getQRSAxis()+"/"+localECG.getTAxis()+" °");
				analysis.put("RV5", localECG.getRV5()+" mV");
				analysis.put("SV1", localECG.getSV1()+" mV");
				analysis.put("PR", localECG.getPRInterval()+" ms");
				analysis.put("QRS", localECG.getQRSAxis()+" ms");
				analysis.put("QTQTC", localECG.getQTD()+"/"+localECG.getQTC()+" ms");
				float r_s = 0;
				try {
					float rv5 = Float.valueOf(localECG.getRV5());
					float sv1 = Float.valueOf(localECG.getSV1());
					r_s = Math.abs(rv5)+Math.abs(sv1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				DecimalFormat df =new DecimalFormat("#.000");  
				String rs = df.format(r_s);
				analysis.put("RV5SV1", rs+" mV");
				
				listMaps.put("personInfo", personInfo);
				listMaps.put("analysis", analysis);
				
				strMaps.put("comment", "** 正常心电图 **");
				
				PackageInfo info = StringUtil.getPackInfo(context);
				if (info!=null) {
					strMaps.put("ecg_version", "ECG "+info.versionName);
				}else {
					strMaps.put("ecg_version", "");
				}
				
				String autoResultStr = ECGDataUtil.getResuByJson(localECG.getAutoDiagnosisResult());
				String autoClassStr = ECGDataUtil.getClassByJson(localECG.getAutoDiagnosisResult()); 
				strMaps.put("ecg_result_auto", autoResultStr);
				strMaps.put("ecg_class_auto", autoClassStr);
				ecgDatas.put(0, localECG.getAvF());
				ecgDatas.put(1, localECG.getAvL());
				ecgDatas.put(2, localECG.getAvR());
				ecgDatas.put(3, localECG.getIii());
				ecgDatas.put(4, localECG.getIi());
				ecgDatas.put(5, localECG.getI());
				ecgDatas.put(6, localECG.getV6());
				ecgDatas.put(7, localECG.getV5());
				ecgDatas.put(8, localECG.getV4());
				ecgDatas.put(9, localECG.getV3());
				ecgDatas.put(10, localECG.getV2());
				ecgDatas.put(11, localECG.getV1());
				
				String reportPath = pdfUtils.savePDF(context, listMaps, strMaps, ecgDatas);//"/sdcard/ECGDATA/Report/test0601_20170614140748.pdf";
				if (appMsg!=null) {
					appMsg.setMsgOther(reportPath);
					AppNotificationMessageDao.getAppInstance(idCardNo).updateMessage(appMsg);
				}
				Message msg = handler.obtainMessage(SAVE_REPORT_PDF);
				msg.obj = reportPath;
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	private void openPDF(String path){
		File file = new File(path);
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/pdf");//  文档格式   
		context.startActivity(intent);
	}

	class ViewHolder{
		TextView heartNum;
		TextView timeTV;
		TextView classType;
		TextView classResult;
		TextView uploadState;
		Button delBtn;
		Button printBtn;
		TextView repoTV;
		TextView repoImg;
		LinearLayout repoLayout;
	}

}
