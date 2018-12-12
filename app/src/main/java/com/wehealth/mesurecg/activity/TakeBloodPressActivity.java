package com.wehealth.mesurecg.activity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.wehealth.mesurecg.BTBPressActivity;
import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.adapter.WheelBloodAdapter;
import com.wehealth.mesurecg.utils.CommUtils;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.mesurecg.utils.ToastUtil;
import com.wehealth.mesurecg.view.wheel.WheelView;
import com.wehealth.model.domain.BloodModel;
import com.wehealth.model.domain.model.AuthToken;
import com.wehealth.model.domain.model.BloodPressure;
import com.wehealth.model.domain.model.ResultPassHelper;
import com.wehealth.model.interfaces.inter_register.WeHealthPatient;
import com.wehealth.model.util.DateUtils;
import com.wehealth.model.util.NetWorkService;
import com.wehealth.urion.Data;
import com.wehealth.urion.Head;
import com.wehealth.urion.IBean;
import com.wehealth.urion.Msg;
import com.wehealth.urion.Error;
import com.wehealth.urion.Pressure;

public class TakeBloodPressActivity extends BTBPressActivity implements OnClickListener{
	
	private final int BP_UPLOAD_SUCCESS= 1500;
	private final int BP_UPLOAD_ERROR = 1501;
	private final int DISCOVER_BOOTH = 100;
	private TextView bpHigh, bpLow, bpHeart, tesTime, wheelTitleTV;
	private TextView bpHighTV, bpLowTV, bpHearTV, tesTimeTV;
	private Button okBtn, cancleBtn, saveBtn;
	private WheelView wheelView;
	private WheelBloodAdapter wheelAdapter;
	private List<BloodModel> bloodMs;
	private ProgressBar progressBar;
	private RelativeLayout wheelRelaLayout, pageLayout, titleLayout;
	private LinearLayout take_bp_right;
	private ScrollView scrollView;
	/**选择的对象是高压、低压和心率**/
	private int type = -1;
	private Button measureBtn, recordBtn;
	private BloodPressure bp;
	private String highBP, lowBP, heartBP;
	private IntentFilter disConnectedFilter;
	private String uploadSucessed, uploadFailed, idCardNo;
	
	@SuppressLint("HandlerLeak")
	Handler bpHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case BP_UPLOAD_SUCCESS:
				if (!isFinishing()) {
					halfDialog.dismiss();
					changeDefaultShow(true);
					ToastUtil.showShort(TakeBloodPressActivity.this, uploadSucessed);
				}
				break;
			case BP_UPLOAD_ERROR:
				if (!isFinishing() && halfDialog!=null) {
					halfDialog.dismiss();
				}
				if (!isFinishing()) {
					isNoticeDialog(uploadFailed);
				}
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_bp);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		idCardNo = PreferUtils.getIntance().getIdCardNo();
		initView();
		initData();
		reflushStyle();
		changeDefaultShow(true);
	}
	
	private void initView() {
		scrollView = (ScrollView) findViewById(R.id.take_bp_scrollview);
		take_bp_right = (LinearLayout) findViewById(R.id.take_bp_right);
		pageLayout = (RelativeLayout) findViewById(R.id.backgroud);
		titleLayout = (RelativeLayout) findViewById(R.id.title_layout);
		measureBtn = (Button) findViewById(R.id.take_bp_measure);
		recordBtn = (Button) findViewById(R.id.take_bp_record);
		bpHeart = (TextView) findViewById(R.id.take_bp_heart);
		bpHigh = (TextView) findViewById(R.id.take_bp_high);
		bpLow = (TextView) findViewById(R.id.take_bp_low);
		wheelRelaLayout = (RelativeLayout) findViewById(R.id.take_bp_numlayout);
		tesTime = (TextView) findViewById(R.id.take_bp_testime);
		wheelTitleTV = (TextView) findViewById(R.id.take_bp_wheeltitle);
		wheelView = (WheelView) findViewById(R.id.wheelView_num);
		okBtn = (Button) findViewById(R.id.take_bp_numok);
		cancleBtn = (Button) findViewById(R.id.take_bp_numcancel);
		saveBtn = (Button) findViewById(R.id.btn_save);
		progressBar = (ProgressBar) findViewById(R.id.take_bp_progress);
		bpHighTV = (TextView) findViewById(R.id.take_bp_high_tv);
		bpHearTV = (TextView) findViewById(R.id.take_bp_heart_tv);
		bpLowTV = (TextView) findViewById(R.id.take_bp_low_tv);
		tesTimeTV = (TextView) findViewById(R.id.take_bp_testime_tv);
		
		measureBtn.setOnClickListener(this);
		recordBtn.setOnClickListener(this);
		okBtn.setOnClickListener(this);
		cancleBtn.setOnClickListener(this);
		bpHeart.setOnClickListener(this);
		bpHigh.setOnClickListener(this);
		bpLow.setOnClickListener(this);
		tesTime.setOnClickListener(this);
		saveBtn.setOnClickListener(this);
		saveBtn.setEnabled(false);
	}
	
	private void initData() {
		uploadSucessed = getString(R.string.upload_successed);
		uploadFailed = getString(R.string.upload_failed);
		progressBar.setMax(300);
		setData();
		disConnectedFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);

		wheelAdapter = new WheelBloodAdapter(this, bloodMs, 2);
		wheelView.setViewAdapter(wheelAdapter);
	}
	/**初始化血压值**/
	private void setData() {
		bloodMs = new ArrayList<BloodModel>();
		for (int i = 50; i < 300; i++) {
			BloodModel bm = new BloodModel();
			bm.setIntValue(i + 1);
			bm.setName("第 " + i + " 项");
			bloodMs.add(bm);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==DISCOVER_BOOTH) {
			if (resultCode == Activity.RESULT_OK) {
				String address = data.getExtras().getString(
						DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
				halfDialog.setMessage("正在测量");
				halfDialog.show();
				rbxt.getService().setDevice(device);				
				rbxt.getService().connectSend();
//				starTakeBP();
			}else if (resultCode == 300) {
				String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
				halfDialog.setMessage("正在测量");
				halfDialog.show();
				rbxt.getService().setDevice(device);
				rbxt.getService().connectSend();
//				starTakeBP();
			}else {
				changeDefaultShow(false);
				saveBtn.setEnabled(true);
				recordBtn.setEnabled(true);
			}
		}
	}
	
	public void onBackBottonClick(View view) {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.take_bp_high:
			wheelRelaLayout.setVisibility(View.VISIBLE);
			type = 1;
			wheelView.setCurrentItem(69);
			wheelTitleTV.setText("收缩压选择值(mmHg)");
			scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
			break;
		case R.id.take_bp_low:
			wheelRelaLayout.setVisibility(View.VISIBLE);
			type = 2;
			wheelView.setCurrentItem(29);
			wheelTitleTV.setText("舒张压选择值(mmHg)");
			scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
			break;
		case R.id.take_bp_heart:
			wheelRelaLayout.setVisibility(View.VISIBLE);
			type = 3;
			wheelView.setCurrentItem(19);
			wheelTitleTV.setText("心率选择值(次/分钟)");
			scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
			break;
		case R.id.take_bp_measure:
			changeDefaultShow(true);
			String bpMac = PreferUtils.getIntance().getBPBlueToothMac();
			if (!TextUtils.isEmpty(bpMac)) {
				halfDialog.setMessage("正在测量");
				halfDialog.show();
				BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(bpMac);
				rbxt.getService().setDevice(device);				
				rbxt.getService().connectSend();
//				starTakeBP();
			}else {
				Intent intent = new Intent(this, DeviceListActivity.class);
				startActivityForResult(intent, DISCOVER_BOOTH);
			}
			saveBtn.setEnabled(false);
			recordBtn.setEnabled(false);
			registerBoradcastReceiver();
			break;
		case R.id.take_bp_record:
			changeDefaultShow(false);
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String t = DateUtils.sdf_HH_mm.format(curDate);
			tesTime.setText(t);
			saveBtn.setEnabled(true);
			break;
		case R.id.take_bp_numok:
			BloodModel bm = wheelAdapter.getBM(wheelView.getCurrentItem());
			if (type == 1) {
				bpHigh.setText(bm.getIntValue() + "");
			} else if (type == 2) {
				bpLow.setText(bm.getIntValue() + "");
			} else if (type == 3) {
				bpHeart.setText(bm.getIntValue() + "");
			}
			wheelRelaLayout.setVisibility(View.INVISIBLE);
			scrollView.fullScroll(ScrollView.FOCUS_UP);//滚动到顶部
			break;
		case R.id.take_bp_numcancel:
			wheelRelaLayout.setVisibility(View.INVISIBLE);
			scrollView.fullScroll(ScrollView.FOCUS_UP);//滚动到顶部
			break;
		case R.id.take_bp_testime:
			setDateTime("选择时间", tesTime);
			break;
		case R.id.btn_save:
			highBP = bpHigh.getText().toString();
			lowBP = bpLow.getText().toString();
			heartBP = bpHeart.getText().toString();
			if (!NetWorkService.isNetWorkConnected(this)) {
				Toast.makeText(this, "网络不可用，请检查网络", Toast.LENGTH_LONG).show();
				return;
			}
			
			if (TextUtils.isEmpty(highBP)) {
				Toast.makeText(this, "收缩压值为空", Toast.LENGTH_LONG).show();
				return;
			}
			if (TextUtils.isEmpty(lowBP)) {
				Toast.makeText(this, "舒张压值为空", Toast.LENGTH_LONG).show();
				return;
			}
			if (TextUtils.isEmpty(heartBP)) {
				Toast.makeText(this, "心率值为空", Toast.LENGTH_LONG).show();
				return;
			}

			bp = new BloodPressure();
			Date testDate = null;
			try {
				if (!TextUtils.isEmpty(tesTime.getText().toString())) {
					Date date = new Date();
					String d1 = DateUtils.sdf_yyyy_MM_dd.format(date);
					testDate = DateUtils.sdf_yyyy_MM_dd_HHmm.parse(d1+tesTime.getText().toString());
				} else {
					testDate = new Date();
				}
			} catch (ParseException e) {
				e.printStackTrace();
				testDate = new Date();
			}
			Date d = new Date();
			if (testDate.getTime()>d.getTime()) {
				isNoticeDialog("您的血压测量时间不应超过当前时间！");
				return;
			}
			
			bp.setTestTime(testDate);
			bp.setCreateTime(new Date());
			bp.setUpdateTime(new Date());
			bp.setHigh(Double.valueOf(highBP));
			bp.setLow(Double.valueOf(lowBP));
			bp.setHeartRate(Integer.valueOf(heartBP));
			bp.setPatientId(idCardNo);
//			progressDialog.setMessage("正在上传数据...");
//			progressDialog.show();
			halfDialog.setMessage("正在上传数据...");
			halfDialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					ResultPassHelper rph = null;
					try{
						AuthToken token = CommUtils.refreshToken();
						rph = NetWorkService.createApi(WeHealthPatient.class, PreferUtils.getIntance().getServerUrl()).createBloodPressure(NetWorkService.bear+token.getAccess_token(), bp);
					}catch (Exception e){
						e.printStackTrace();
					}
					if (rph!=null) {
						Message bpMsg = bpHandler.obtainMessage(BP_UPLOAD_SUCCESS);
						bpMsg.obj = rph;
						bpHandler.sendMessage(bpMsg);
					}else {
						bpHandler.sendEmptyMessage(BP_UPLOAD_ERROR);
					}
				}
			}).start();
			break;
			
		default:
			break;
		}
	}

	@SuppressLint("SimpleDateFormat")
	public void onReceive(IBean bean) {
		super.onReceive(bean);
		switch (bean.getHead().getType()) {
		case Head.TYPE_PRESSURE:
			progressBar.setProgress(((Pressure) bean).getPressure());
			break;
		case Head.TYPE_RESULT:
			unRegisterBoradcastReceiver();
			Data data = (Data) bean;
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String t = DateUtils.sdf_yyyy_MM_dd_HHmm.format(curDate);
			int h_p = data.getSys();
			int l_p = data.getDia();
			int h_b = data.getPul();

			bpHigh.setText(h_p+"");
			bpHeart.setText(h_b+"");
			bpLow.setText(l_p+"");
			tesTime.setText(t);
			if (halfDialog!=null && halfDialog.isShowing()) {
				halfDialog.dismiss();
				rbxt.getService().stop();
			}
			saveBtn.setEnabled(true);
			recordBtn.setEnabled(true);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onMessage(Msg message) {
		super.onMessage(message);
		System.err.println("点击测量按钮操作："+message.toString());
	}

	public void onError(Error error) {
		super.onError(error);
		if (halfDialog!=null && halfDialog.isShowing()) {
			unRegisterBoradcastReceiver();
			halfDialog.dismiss();
			rbxt.getService().stop();
		}
		saveBtn.setEnabled(true);
		recordBtn.setEnabled(true);
		if (error.getHead() != null){
			switch (error.getError()) {
			case Error.ERROR_EEPROM:
				showPromptDialog("血压计异常 ，联系你的经销商");
				break;
			case Error.ERROR_HEART:
				showPromptDialog("测量错误，请根据说明书，重新带好袖带，保持安静，重新测量");
				break;
			case Error.ERROR_DISTURB:
				// Toast.makeText(this, "E-2 杂讯干扰!", Toast.LENGTH_SHORT).show();
				showPromptDialog("测量错误，请根据说明书，重新带好袖带，保持安静，重新测量");
				break;
			case Error.ERROR_GASING:
				// Toast.makeText(this, "E-3 充气时间过长!", Toast.LENGTH_SHORT).show();
				showPromptDialog("测量错误，请根据说明书，重新带好袖带，保持安静，重新测量");
				break;
			case Error.ERROR_TEST:
				showPromptDialog("测量错误，请根据说明书，重新带好袖带，保持安静，重新测量");
				break;
			case Error.ERROR_REVISE:
				// Toast.makeText(this, "E-C 校正异常!", Toast.LENGTH_SHORT).show();
				showPromptDialog("测量错误，请根据说明书，重新带好袖带，保持安静，重新测量");
				break;
			case Error.ERROR_POWER:
				// Toast.makeText(this, "E-B 电源低电压!", Toast.LENGTH_SHORT).show();
				showPromptDialog("电池电量低，请更换电池");
				break;
			default:
				
				break;
			}
		}else {
			if (error.getError_code()==Error.ERROR_SEND_INSTRUCT_FAILED) {
				showPromptDialog("连接指定设备失败，请确保设备蓝牙打开");
//				Toast.makeText(this, "连接指定设备失败", Toast.LENGTH_SHORT).show();
//				Toast.makeText(this, "发送指令失败", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void reflushStyle() {
			take_bp_right.setBackgroundResource(R.drawable.measure_presure_num_bg1);
			pageLayout.setBackgroundColor(getResources().getColor(R.color.page_background_color));
			titleLayout.setBackgroundColor(getResources().getColor(R.color.page_title_bar_color));
			bpHearTV.setTextColor(getResources().getColor(R.color.text_white));
			bpHighTV.setTextColor(getResources().getColor(R.color.text_white));
			bpLowTV.setTextColor(getResources().getColor(R.color.text_white));
			tesTimeTV.setTextColor(getResources().getColor(R.color.text_white));
			bpHeart.setTextColor(getResources().getColor(R.color.text_white));
			bpHigh.setTextColor(getResources().getColor(R.color.text_white));
			bpLow.setTextColor(getResources().getColor(R.color.text_white));
			tesTime.setTextColor(getResources().getColor(R.color.text_white));
			wheelTitleTV.setTextColor(getResources().getColor(R.color.text_white));

	}
	
	/**测量时间选择**/
	protected void setDateTime(String string, final TextView tv) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(this, R.layout.date_time_dialog, null);
		final DatePicker datePicker = (DatePicker) view
				.findViewById(R.id.date_picker);
		final TimePicker timePicker = (TimePicker) view
				.findViewById(R.id.time_picker);
		builder.setView(view);
		datePicker.setVisibility(View.GONE);

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH), null);

		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(Calendar.MINUTE);
		final int inType = tv.getInputType();
		tv.setInputType(InputType.TYPE_NULL);
		tv.setInputType(inType);

		builder.setTitle(string);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						StringBuffer sb = new StringBuffer();
//						sb.append(String.format("%d-%02d-%02d",
//								datePicker.getYear(),
//								datePicker.getMonth() + 1,
//								datePicker.getDayOfMonth()));
						sb.append(" ");
						sb.append(timePicker.getCurrentHour()).append(":")
								.append(timePicker.getCurrentMinute());
						tv.setText(sb);
						dialog.cancel();
					}
				});
		Dialog dialog = builder.create();
		dialog.show();
	}
	
	private void registerBoradcastReceiver() {
		registerReceiver(stateChangeReceiver, disConnectedFilter);
	}
	
	private void unRegisterBoradcastReceiver(){
		try {
			unregisterReceiver(stateChangeReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private BroadcastReceiver stateChangeReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
	            showchange(action);
	        }
	    }
	};

	protected void showchange(String action) {
		if (halfDialog!=null && halfDialog.isShowing()) {
			halfDialog.dismiss();
			rbxt.getService().stop();
		}
		recordBtn.setEnabled(true);
		String prompt = getString(R.string.bp_close_prompt);
		isNoticeDialog(prompt);
	}
	/***
	 * 更改血糖血压心率的默认值
	 * @param b 为true时点击了测量，为false时点击了记录
	 */
	private void changeDefaultShow(boolean b){
		if (b) {//点击测量后显示的默认值
			bpHeart.setEnabled(false);
			bpHigh.setEnabled(false);
			bpLow.setEnabled(false);
			bpHigh.setText("");
			bpHeart.setText("");
			bpLow.setText("");
			tesTime.setText("");
			bpHigh.setHint("");
			bpHeart.setHint("");
			bpLow.setHint("");
		}else {//点击记录后显示默认值
			bpHeart.setEnabled(true);
			bpHigh.setEnabled(true);
			bpLow.setEnabled(true);
			bpHigh.setText("");
			bpHeart.setText("");
			bpLow.setText("");
			bpHigh.setHint(R.string.bp_input_hint);
			bpHeart.setHint(R.string.bp_input_hint);
			bpLow.setHint(R.string.bp_input_hint);
		}
	}
	
}
