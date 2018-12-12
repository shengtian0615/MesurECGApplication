package com.wehealth.mesurecg.activity;

import java.math.BigDecimal;
import java.text.ParseException;

import com.baidu.location.BDLocation;
import com.wehealth.mesurecg.BaseActivity;
import com.wehealth.mesurecg.MeasurECGApplication;
import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.dao.ECGDao;
import com.wehealth.mesurecg.utils.CommUtils;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.mesurecg.utils.ToastUtil;
import com.wehealth.mesurecg.view.UIProgressDialog;
import com.wehealth.model.domain.enumutil.ECGDataDiagnosisType;
import com.wehealth.model.domain.model.AuthToken;
import com.wehealth.model.domain.model.ECGData;
import com.wehealth.model.domain.model.RegisteredUser;
import com.wehealth.model.domain.model.ResultPassHelper;
import com.wehealth.model.interfaces.inter_other.WeHealthECGData;
import com.wehealth.model.util.Constant;
import com.wehealth.model.util.DateUtils;
import com.wehealth.model.util.FileUtil;
import com.wehealth.model.util.NetWorkService;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EcgReportActivity extends BaseActivity implements OnClickListener{

	private final int ECGDATA_UPLOAD_FAILED = 91;
	private final int ECGDATA_UPLOAD_SUCCESS = 90;
	private final int ECGDATA_UPLOAD_NO_OAUTH = 93;//没有获取到验证
	private final int ECGDATA_UPLOADIND = 92;//心电数据正在上传
	private final int MEASURE_HAVE_INTERFERENCE = 94;//测量中有干扰
	private final int REGULAR_SEND_FAILED = 95;
	private final int REGULAR_SEND_SUCCESS = 96;
	private final int FREE_CHECK_SEND_FAILED = 97;
	private final int FREE_CHECK_SEND_SUCCESS = 98;
	
	private ECGData collectEcgData, unUploadEcg;
	private RelativeLayout titleLayout;
	private LinearLayout backLayout, freeCheckLayout;
	private TextView result, heart, measureTimeView;
	private TextView t31, t32, adviceTV;
	private Button uploadBtn, overBtn;
	private UIProgressDialog uiProgressDialog;
	private long ecgdata_saveId = -1;
	private String filePath, idCardNo;
	private final String ECG_DATA_SAVE_NAME = "filename";
	ProgressDialog upLoadEcgDialog;

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ECGDATA_UPLOAD_FAILED://心电数据上传失败
				if (upLoadEcgDialog!=null && upLoadEcgDialog.isShowing()) {
					upLoadEcgDialog.dismiss();
				}
				uploadBtn.setVisibility(View.VISIBLE);
				uploadOrNetworkFail("心电数据上传失败");
				break;
			case ECGDATA_UPLOADIND://心电数据正在上传
				upLoadEcgDialog.setMessage("正在上传，请稍候...");
				upLoadEcgDialog.show();
				break;
			case MEASURE_HAVE_INTERFERENCE://测量中有干扰
				showDialog("友情提示","测量中有干扰，影响分析，请重试","确定");
				break;
			case ECGDATA_UPLOAD_NO_OAUTH:
				Log.e("", "没有通过oauth");
				break;
			case REGULAR_SEND_FAILED://常规复核发送失败了
				if (upLoadEcgDialog!=null && upLoadEcgDialog.isShowing()) {
					upLoadEcgDialog.dismiss();
				}
				flushUnEnabledView(2);
				isNoticeDialog("常规复核发送失败");
				break;
			case REGULAR_SEND_SUCCESS://常规复核发送成功
				if (upLoadEcgDialog!=null && upLoadEcgDialog.isShowing()) {
					upLoadEcgDialog.dismiss();
				}
				ResultPassHelper data = (ResultPassHelper) msg.obj;
				String value = data.getValue();
				if (value.equals("success")) {
					showDialog("友情提示", "发送成功，请等候医生短信通知","完成");
				}else {
					showDialog("友情提示", "没有发成功，因为"+value,"完成");
				}
				break;
			case FREE_CHECK_SEND_FAILED://免费复核发送失败
				if (upLoadEcgDialog!=null && upLoadEcgDialog.isShowing()) {
					upLoadEcgDialog.dismiss();
				}
				flushUnEnabledView(2);
				isNoticeDialog("免费复核发送失败");
				break;
			case FREE_CHECK_SEND_SUCCESS://免费复核发送成功
				if (upLoadEcgDialog!=null && upLoadEcgDialog.isShowing()) {
					upLoadEcgDialog.dismiss();
				}
				data = (ResultPassHelper) msg.obj;
				value = data.getValue();
				if (value.equals("success")) {
					showDialog("友情提示", "免费复核发送成功，12小时内有回复","完成");
				}else {
					showDialog("友情提示", ""+value,"完成");
				}
				break;

			default:
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setContentView(R.layout.activity_ecg_report);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		measureTimeView = (TextView) findViewById(R.id.report_collectionTime_normal);
		filePath = getIntent().getStringExtra(ECG_DATA_SAVE_NAME);
		idCardNo = PreferUtils.getIntance().getIdCardNo();

		uiProgressDialog = new UIProgressDialog(this);
		uiProgressDialog.setCanceledOnTouchOutside(false);
		uiProgressDialog.setCancelable(false);
		upLoadEcgDialog = new ProgressDialog(this);
		upLoadEcgDialog.setCancelable(false);
		upLoadEcgDialog.setCanceledOnTouchOutside(false);
		
		initView();
		reflushStyle();
		unUploadEcg = getPraseEcgData(filePath);
		initEcgView();
	}
	
	private void initView() {
		result = (TextView) findViewById(R.id.report_diag_result);
		t31 = (TextView) findViewById(R.id.report_free_tv1);
		t32 = (TextView) findViewById(R.id.report_free_tv2);
		heart = (TextView) findViewById(R.id.report_heartbeat_number);
		freeCheckLayout = (LinearLayout) findViewById(R.id.report_check_free);
		backLayout = (LinearLayout) findViewById(R.id.background);
		titleLayout =  (RelativeLayout) findViewById(R.id.title_bar);
		uploadBtn = (Button) findViewById(R.id.report_ecg_upload);
		overBtn = (Button) findViewById(R.id.report_ecg_over);
		adviceTV = (TextView) findViewById(R.id.report_wehealth_advice);
		result.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		uploadBtn.setOnClickListener(this);
		freeCheckLayout.setOnClickListener(this);
		overBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.report_ecg_upload://重新上传
			reUploadECGData();
			break;
		case R.id.report_check_free:
			freeCheck();
			break;
		case R.id.report_ecg_over:
			this.finish();
			break;

		default:
			break;
		}
	}
	
	/**模拟数据显示**/
	private void initEcgView(){
		if (unUploadEcg==null) {
			return;
		}
		measureTimeView.setText(unUploadEcg.getTime().toString());
		heart.setText(unUploadEcg.getHeartRate()+"");
		int h = Integer.valueOf(unUploadEcg.getHeartRate());
		if (h>100 || h<60) {
			adviceTV.setText("心率异常，建议找医生咨询。");
		}else {
			adviceTV.setText("心率正常，如还感觉不适，可找医生咨询。");
		}
		
//		String res= map.get("RESULT");
//		result.setText(res);
//		if (ecg != null) {
//			measureTimeView.setText(ecg.getTime().toLocaleString());
//			heart.setText(String.valueOf(ecg.getHeartRate()));
////			String resultStr = "";
////			String classStr = "";
////			classStr = ECGDataUtil.getClassByJson(ecg.getAutoDiagnosisResult());
////			resultStr = ECGDataUtil.getResuByJson(ecg.getAutoDiagnosisResult());
////			
////			freeCheckLayout.setEnabled(true);
////			freeCheckLayout.setAlpha(1.0f);
////			
////			if (TextUtils.isEmpty(resultStr)) {
////				classResult.setText("没有分析的结果");
////				result.setText("没有分析的结果");
////				return;
////			}
////			classResult.setText(classStr);
////			result.setText(resultStr);
////			if ("分析失败".equals(resultStr)) {
////				return;
////			}
////			ResultPassHelper rph = FileUtils.obtainWarnLevelMap(this, resultStr);
////			if (rph==null) {//没有找到对应的级别
////				rph = new ResultPassHelper();
////				if (classStr.equals("** 正常心电图 **")) {
////					rph.setName("一");
////					rph.setValue("心电图无明显异常，如有症状建议就医（心电图仅供参考）");
////				}else {
////					rph.setName("二");
////					rph.setValue("心电图提示异常，建议咨询专业医生。（心电图仅供参考）");
////				}
////				showECGWarn(rph);
////				return;
////			}
////			showECGWarn(rph);
//		}
	}
	
//	private void showECGWarn(ResultPassHelper rph) {
//		if ("三".equals(rph.getName())) {
//			adviceTV.setText(rph.getValue());
//			bg_l2.setBackgroundColor(getResources().getColor(R.color.color_red_AD0010));
//		}
//		if ("二".equals(rph.getName())) {
//			adviceTV.setText(rph.getValue());
//			bg_l2.setBackgroundColor(getResources().getColor(R.color.color_yellow_fbc619));
//		}
//		if ("一".equals(rph.getName())) {
//			adviceTV.setText(rph.getValue());
//			bg_l2.setBackgroundColor(getResources().getColor(R.color.color_green_3D7149));
//		}
//	}

	
	/**华清心仪测量的结果上传
	 * @param ecgdata **/
	public void upLoadECGData(final ECGData ecgdata){
		if (!NetWorkService.isNetWorkConnected(this)) {
			uploadOrNetworkFail("网络不可用，不能上传心电数据");
			return;
		}
		mHandler.sendEmptyMessage(ECGDATA_UPLOADIND);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				ResultPassHelper rph = null;
				try {
					AuthToken token = CommUtils.refreshToken();
					rph = NetWorkService.createByteLongApi(WeHealthECGData.class, PreferUtils.getIntance().getServerUrl()).createECGData(NetWorkService.bear+token.getAccess_token(), ecgdata);
				}catch (Exception e){
					e.printStackTrace();
				}
				if (rph==null) {
					mHandler.sendEmptyMessage(ECGDATA_UPLOAD_FAILED);
					return;
				}
				if (Constant.NO_TOKEN.equals(rph.getValue())) {
					mHandler.sendEmptyMessage(ECGDATA_UPLOAD_NO_OAUTH);
					return;
				}
				if (Constant.FAILED.equals(rph.getName())) {
					mHandler.sendEmptyMessage(ECGDATA_UPLOAD_FAILED);
					return;
				}
				if (Constant.SUCCESS.equals(rph.getName())) {
					Message msg = mHandler.obtainMessage(ECGDATA_UPLOAD_SUCCESS);
					ecgdata.setRequestedDiagnosisType(ECGDataDiagnosisType.uploaded.ordinal());
					if (ecgdata_saveId!=-1) {
						ECGDao.getECGIntance(idCardNo).updatEcgData(ecgdata_saveId, ecgdata);
					}
					ecgdata.setId(Long.valueOf(rph.getValue()));
					msg.obj = ecgdata;
					mHandler.sendMessage(msg);
				}
			}
		}).start();
	}
	
	private ECGData getPraseEcgData(String path) {
		ECGData newData = FileUtil.parse2Data(path);
		RegisteredUser user = MeasurECGApplication.getInstance().getRegisterUser();
		newData.setEquipmentSerialNo(PreferUtils.getIntance().getSerialNo());
		newData.setPatientId(user.getIdCardNo());
		newData.setVersion(1);
		newData.setRegisteredUserId(user.getIdCardNo());
		newData.setRequestedDiagnosisType(ECGDataDiagnosisType.auto.ordinal());
		String timeStr = path.substring(path.lastIndexOf("/")+1).replace(".xml", "");
		try {
			newData.setTime(DateUtils.sdf_yyyyMMddHHmmss.parse(timeStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		BDLocation location = MeasurECGApplication.getInstance().getLocation();
		BigDecimal bd;
		if (location != null) {
			bd = new BigDecimal(location.getLatitude());   
			bd = bd.setScale(6, BigDecimal.ROUND_HALF_UP);  
			newData.setLatitude(bd.doubleValue());
			bd = new BigDecimal(location.getLongitude());   
			bd = bd.setScale(6, BigDecimal.ROUND_HALF_UP); 
			newData.setLongitude(bd.doubleValue());
			if (location.getAddrStr()!=null) {
				newData.setAddress(location.getAddrStr());
			}else {
				String address = PreferUtils.getIntance().getAddress();
				if (!TextUtils.isEmpty(address)) {
					newData.setAddress(address);
				}else {
					newData.setAddress("地址由百度提供，没有获取到，抱歉!");
				}
			}
		} else {
			String address = PreferUtils.getIntance().getAddress();
			String latitu = PreferUtils.getIntance().getLatitude();
			String longit = PreferUtils.getIntance().getLongitude();
			if (!TextUtils.isEmpty(address)) {
				newData.setAddress(address);
			}else {
				newData.setAddress("地址由百度提供，没有获取到，抱歉!");
			}
			if (!TextUtils.isEmpty(longit)) {
				newData.setLongitude(Double.valueOf(longit));
			}else {
				newData.setLongitude(-1);
			}
			if (!TextUtils.isEmpty(latitu)) {
				newData.setLatitude(Double.valueOf(latitu));
			}else {
				newData.setLatitude(-1);
			}
		}
		return newData;
	}

	@Override
	protected void onResume() {
		super.onResume();
		reflushStyle();
	}

	private void reflushStyle() {
			t31.setTextColor(getResources().getColor(R.color.text_white));
			t32.setTextColor(getResources().getColor(R.color.text_white));
			backLayout.setBackgroundColor(getResources().getColor(R.color.page_background_color));
			titleLayout.setBackgroundColor(getResources().getColor(R.color.page_title_bar_color));
//			freeCheckLayout.setBackground(getResources().getDrawable(R.drawable.btn_free_b_selector));
	}
	
	//自动上传失败，点击后重新上传
	public void reUploadECGData(){
		if (!NetWorkService.isNetWorkConnected(this)) {
			isNoticeDialog("网络不可用，不能上传并复核");
			return;
		}
		if (unUploadEcg == null) {
			ToastUtil.showShort(this, "心电数据为空");
			return ;
		}
		upLoadECGData(unUploadEcg);
	}
	
	/**12小时免费复核**/
	private void freeCheck() {
		if (!NetWorkService.isNetWorkConnected(this)) {
			ToastUtil.showShort(this, "网络不可用");
			return;
		}
		if (collectEcgData==null) {
			Toast.makeText(this, "心电数据没有上传，医生无法复核", Toast.LENGTH_LONG).show();
			return ;
		}
		flushUnEnabledView(1);
		uiProgressDialog.setMessage("正在发送，请稍候...");
		uiProgressDialog.show();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				AuthToken token = CommUtils.refreshToken();
				if (token==null) {
					mHandler.sendEmptyMessage(FREE_CHECK_SEND_FAILED);
					return;
				}
				ResultPassHelper rph;
				try {
					rph = NetWorkService.createLongApi(WeHealthECGData.class, PreferUtils.getIntance().getServerUrl()).requestFreeCheck("Bearer " + token.getAccess_token(), String.valueOf(collectEcgData.getId()), null, null);
				} catch (Exception e) {
					e.printStackTrace();
					rph = null;
					return;
				}
				if (rph==null) {
					mHandler.sendEmptyMessage(FREE_CHECK_SEND_FAILED);
				}else {
					Message msg = mHandler.obtainMessage(FREE_CHECK_SEND_SUCCESS);
					msg.obj = rph;
					msg.sendToTarget();
				}
			}
		}).start();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			if (upLoadEcgDialog.isShowing()) {
				return false;
			}
			if (uiProgressDialog.isShowing()) {
				return false;
			}
			isWait("友情提示");
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void isWait(String string){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(string);
		builder.setMessage("测完心电，是否复核？");
		builder.setPositiveButton("无需复核", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (uiProgressDialog!=null && uiProgressDialog.isShowing()) {
					uiProgressDialog.dismiss();
				}
				EcgReportActivity.this.finish();
			}
		});
		
		builder.setNegativeButton("我要复核", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setCancelable(false);
		builder.show();
	}
	
	private void showDialog(String title, String message, String posiBtn) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setPositiveButton(posiBtn,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						EcgReportActivity.this.finish();
					}

				});
		builder.show();
	}
	
	private void isNoticeDialog(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.friend_notify);
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	private void uploadOrNetworkFail(String str){
		isNoticeDialog(str);
		freeCheckLayout.setAlpha(0.1f);
		freeCheckLayout.setEnabled(false);
	}
	
	/**复核按钮Enabled状态
	 * type=1时，点击按钮后,所有复核按钮不可以点击
	 * type=2时，复核发送失败，可以再次点击所有的复核**/
	private void flushUnEnabledView(int type){
		if (type==1) {
			freeCheckLayout.setEnabled(false);
			overBtn.setEnabled(false);
			
			freeCheckLayout.setAlpha(0.1f);
			overBtn.setAlpha(0.1f);
			return;
		}
		if(type==2){
			freeCheckLayout.setEnabled(true);
			overBtn.setEnabled(true);
			
			freeCheckLayout.setAlpha(1.0f);
			overBtn.setAlpha(1.0f);
			return;
		}
	}
}
