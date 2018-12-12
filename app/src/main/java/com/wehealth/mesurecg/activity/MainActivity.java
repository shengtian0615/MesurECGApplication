package com.wehealth.mesurecg.activity;

import java.util.Observable;
import java.util.Observer;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChatManager;
import com.easemob.util.NetUtils;
import com.wehealth.mesurecg.AppManager;
import com.wehealth.mesurecg.BaseActivity;
import com.wehealth.mesurecg.MeasurECGApplication;
import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.dao.AppNotificationMessageDao;
import com.wehealth.mesurecg.dao.RegisterUserDao;
import com.wehealth.mesurecg.service.BaseNotifyObserver;
import com.wehealth.mesurecg.usbutil.USB_Operation;
import com.wehealth.mesurecg.utils.CommUtils;
import com.wehealth.mesurecg.utils.LocationService;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.mesurecg.utils.ToastUtil;
import com.wehealth.mesurecg.utils.UpdateInfoService;
import com.wehealth.model.domain.enumutil.AppType;
import com.wehealth.model.domain.model.AppNotificationMessage;
import com.wehealth.model.domain.model.AppVersion;
import com.wehealth.model.domain.model.AuthToken;
import com.wehealth.model.domain.model.RegisteredUser;
import com.wehealth.model.interfaces.inter_other.WehealthTokenFree;
import com.wehealth.model.interfaces.inter_register.WeHealthRegisteredUser;
import com.wehealth.model.util.NetWorkService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements OnClickListener, Observer, BDLocationListener {

	private final int REQUEST_PERMISSION_BLUETOOTH = 1000;
	private final int REQUEST_TURNON_BLUETOOTH = 1001;

	private final int HQXY_APP_UPDATA = 103;
	private final int REFLUSH_INITDATA = 102;
	private final int UPDATE_VERSION = 101;
	private final int FLUSH_MESSAGE_STATE = 117;
	private ImageView photo, msgIMG,measurEcgBtn;
	private Button sugarBtn, pressBtn, rechaBtn, ecgFileBtn;
	private RelativeLayout errorItem;
	private TextView nameText, balanceTextView, unreadMsg, errorText, setBtn;
	private ProgressDialog progressDialog;
	private UpdateInfoService updateInfoService;
	private RegisteredUser registeredUser;
	private Dialog dialog;
	private boolean Net_state = false;
	private String idCardNo;
	
	private MyConnectionListener connectionListener;
	private LocationService locationService;
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case FLUSH_MESSAGE_STATE:
				showMsgState();
				break;
			case REFLUSH_INITDATA:
				setData();
				break;
			case HQXY_APP_UPDATA:
				AppVersion hqxyAV = (AppVersion) msg.obj;
				showUpdateDialog(hqxyAV);
				break;
			case UPDATE_VERSION:
				if (progressDialog!=null) {
					progressDialog.dismiss();
				}
				AppVersion version = (AppVersion) msg.obj;
				showUpdateDialog(version);
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_constraint);

		updateInfoService = new UpdateInfoService(this);
		connectionListener = new MyConnectionListener();
		locationService = MeasurECGApplication.getInstance().locationService;
		
	    dialog = new Dialog(this);
	    dialog.setTitle(R.string.friend_notify);
	    dialog.setCancelable(false);
	    dialog.setContentView(R.layout.longtime_testprompt);
	    Intent intent = getIntent();
	    Net_state = intent.getBooleanExtra("NET", false);
	    idCardNo = PreferUtils.getIntance().getIdCardNo();
	    
		nameText = (TextView) findViewById(R.id.textview_nickname);
		balanceTextView = (TextView) findViewById(R.id.textView_balance);
		msgIMG = (ImageView) findViewById(R.id.measure_msg_bg);
		measurEcgBtn = findViewById(R.id.measure_startbtn);
		sugarBtn = (Button) findViewById(R.id.measure_sugarbtn);
		pressBtn = (Button) findViewById(R.id.measure_pressbtn);
		rechaBtn = (Button) findViewById(R.id.main_recharge_btn);
		photo = (ImageView) findViewById(R.id.user_image);
		unreadMsg = (TextView) findViewById(R.id.main_msg_unread);
		errorItem = (RelativeLayout) findViewById(R.id.rl_error_item);
		errorText = (TextView) findViewById(R.id.tv_connect_errormsg);
		setBtn = (TextView) findViewById(R.id.main_set_btn);
		ecgFileBtn = findViewById(R.id.button_ecg_report);
		
		initData();
		String serialNo = PreferUtils.getIntance().getSerialNo();
		ToastUtil.showShort(this, "设备序列号："+serialNo);
		
		photo.setOnClickListener(this);
		sugarBtn.setOnClickListener(this);
		pressBtn.setOnClickListener(this);
		rechaBtn.setOnClickListener(this);
		setBtn.setOnClickListener(this);
		msgIMG.setOnClickListener(this);
		measurEcgBtn.setOnClickListener(this);
		ecgFileBtn.setOnClickListener(this);

		if (NetWorkService.isNetWorkConnected(this)) {
			//注册监听
			if (locationService!=null) {
				locationService.setLocationOption(locationService.getDefaultLocationClientOption());
				locationService.start();// 定位SDK
			}
		}

		
//		if (NetWorkService.isNetWorkConnected(this)) {
//			if (!CommUtils.checkPackage(this, "com.ecgmac.ecgtab")) {
//				checkECG_HQXY(1);
//				return;
//			}
//			checkVersion();
//			int versionCode = CommUtils.getVersionCode(this, "com.ecgmac.ecgtab");
//
//			if (versionCode!=-1) {
//				checkECG_HQXY(versionCode);
//			}
//		}
	}
	private void initData() {
		if (NetWorkService.isNetWorkConnected(this)&&Net_state==false) {
			registeredUser = MeasurECGApplication.getInstance().getRegisterUser();
		}else {
			registeredUser = RegisterUserDao.getInstance(idCardNo).getIdCardNo(idCardNo);
			MeasurECGApplication.getInstance().setRegisterUser(registeredUser);
		}
		setData();
	}
	
	private void setData() {
		if (registeredUser!=null){

			nameText.setText(registeredUser.getName());
			double balance = registeredUser.getBalance();
			balanceTextView.setText(Html.fromHtml(this.getResources().getString(R.string.main_page_balance, String.valueOf(balance))));
		}
	}
	
	private void showMsgState(){
		int unRead = AppNotificationMessageDao.getAppInstance(idCardNo).getUnreadMsgCount();
		if (unRead > 0) {
			unreadMsg.setVisibility(View.VISIBLE);
			if (unRead>99) {
				unreadMsg.setText(String.valueOf(unRead+"+"));
			}else {
				unreadMsg.setText(String.valueOf(unRead));
			}
		}else {
			unreadMsg.setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (NetWorkService.isNetWorkConnected(this)) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try{
						AuthToken token = CommUtils.refreshToken();
						registeredUser = NetWorkService.createApi(WeHealthRegisteredUser.class, PreferUtils.getIntance().getServerUrl()).getRegisteredUser(NetWorkService.bear+token.getAccess_token(), idCardNo);
						if (registeredUser != null) {
							Message message = handler.obtainMessage(REFLUSH_INITDATA);
							MeasurECGApplication.getInstance().setRegisterUser(registeredUser);
							handler.sendMessage(message);
						}
					}catch (Exception e){

					}
				}
			}).start();
		}
		showMsgState();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		// -----------location config ------------
		//获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
		//注册监听
		if (locationService!=null) {
			locationService.registerListener(this);
			locationService.setLocationOption(locationService.getDefaultLocationClientOption());
		}
		BaseNotifyObserver.getInstance().addObserver(this);
		EMChatManager.getInstance().addConnectionListener(connectionListener);
	}
	/***
	 * Stop location service
	 */
	@Override
	protected void onStop() {
		super.onStop();
		if (locationService!=null) {
			locationService.unregisterListener(this); //注销掉监听
			locationService.stop(); //停止定位服务
		}
		BaseNotifyObserver.getInstance().deleteObserver(this);
		EMChatManager.getInstance().removeConnectionListener(connectionListener);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Process.killProcess(Process.myPid());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_image:
			startActivity(new Intent(this, PersonalInfoActivity.class));
			break;
		case R.id.measure_pressbtn:
			Intent press = new Intent(this, TakeBloodPressActivity.class);
			startActivity(press);
			break;
		case R.id.measure_sugarbtn:
			Intent sugar = new Intent(this, BloodSugarActivity.class);
			startActivity(sugar);
			break;
		case R.id.main_recharge_btn:
			if (!NetWorkService.isNetWorkConnected(this)) {
				ToastUtil.showShort(this, R.string.network_is_not_available);
				return;
			}
//			Intent rechar = new Intent(this, WXPayEntryActivity.class);
//			startActivity(rechar);
			break;
		case R.id.measure_msg_bg:
			Intent msgLy = new Intent(this, MessageActivity.class);
			startActivity(msgLy);
			break;
		case R.id.main_set_btn:
			startActivity(new Intent(this, SetActivity.class));
			break;
		case R.id.measure_startbtn:
			if (Build.VERSION.SDK_INT >= 23){
				if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
					ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH}, REQUEST_PERMISSION_BLUETOOTH);
				}else{
					directOpenECGMeasure();
				}
			}else if (Build.VERSION.SDK_INT<=22){
				directOpenECGMeasure();
			}

			break;
		case R.id.button_ecg_report:
			Intent intent = new Intent(this, EcgHistoryActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	private void directOpenECGMeasure() {
		UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
		BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!USB_Operation.isDetectionDevice(mUsbManager)) {
			if (btAdapter==null) {
				ToastUtil.showShort(this, "该设备不支持蓝牙");
				return;
			}
			if (!btAdapter.isEnabled()){
				Intent btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				// 设置 Bluetooth 设备可见时间
				btIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 10);
				startActivityForResult(btIntent, REQUEST_TURNON_BLUETOOTH);
				return;
			}
			startActivity(new Intent(this, ECGMeasureActivity.class));
		}
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data){
	    if(requestCode == REQUEST_TURNON_BLUETOOTH){
	        if (resultCode == Activity.RESULT_OK){
                startActivity(new Intent(this, ECGMeasureActivity.class));
            }
            if (resultCode == Activity.RESULT_CANCELED){
                ToastUtil.showShort(this, "测心电需要您打开蓝牙");
            }
        }
    }

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_PERMISSION_BLUETOOTH){
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
				directOpenECGMeasure();
			}else{
				ToastUtil.showShort(this, "需要获得您位置信息的权限");
			}
		}
	}

	private class MyConnectionListener implements ConnectionListener {

		@Override
		public void onConnected() {
			errorItem.setVisibility(View.INVISIBLE);
		}

		@Override
		public void onDisConnected(String errorString) {
			if (isFinishing()){
				return;
			}
			if (errorString != null && errorString.contains("conflict")) {
				// 显示帐号在其他设备登陆
				ToastUtil.showShort(MainActivity.this, "您的账号再别的设备上登录了，请注意安全");
				PreferUtils.getIntance().setPassword(idCardNo, "");
				AppManager.getAppManager().finishAllActivity();
			} else {
				errorItem.setVisibility(View.VISIBLE);
				if(NetUtils.hasNetwork(MainActivity.this)){
					errorText.setText("连接不到消息服务器");
				}else{
					errorText.setText("网络连接不可用");
				}
			}
		}

		@Override
		public void onReConnected() {
			errorItem.setVisibility(View.INVISIBLE);
		}

		@Override
		public void onReConnecting() { }

		@Override
		public void onConnecting(String progress) { }
	}
	
	//显示是否要更新的对话框
	private void showUpdateDialog(final AppVersion version) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.version_update_prompt);
		builder.setMessage(version.getNote());
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.update_immediate, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					downFile(version);
				} else {
					ToastUtil.showShort(MainActivity.this, R.string.sdcard_no_avaiable);
				}
			}
		});
		builder.create().show();
	}

	private void downFile(AppVersion version) { 
		progressDialog = new ProgressDialog(MainActivity.this);    //进度条，在下载的时候实时更新进度，提高用户友好度
		progressDialog.setCancelable(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setTitle(R.string.download_latest_version);
		String load_prompt = getString(R.string.download_prompt);
		progressDialog.setMessage(load_prompt);
		progressDialog.setProgress(0);
		progressDialog.show();
		updateInfoService.downLoadFile(version, progressDialog);
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof AppNotificationMessage) {
			handler.sendEmptyMessageDelayed(FLUSH_MESSAGE_STATE, 100);
		}
	}
	
	@Override
	public void onReceiveLocation(BDLocation lastLocation) {
		if (locationService!=null) {
			locationService.stop();
		}
		MeasurECGApplication.getInstance().setLocation(lastLocation);
	}
	
	private void checkECG_HQXY(final int versionCode) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				AppVersion version = null;
				try {
					AuthToken token = CommUtils.refreshToken();
					int versionCode = CommUtils.getVersionCode(MainActivity.this, MainActivity.this.getPackageName());
					if (versionCode!=-1){
						version = NetWorkService.createApi(WehealthTokenFree.class, PreferUtils.getIntance().getServerUrl()).getNewVersion(AppType.hqxy, versionCode);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
				if (version != null) {
					Message msg = handler.obtainMessage(HQXY_APP_UPDATA);
					msg.obj = version;
					msg.sendToTarget();
				}
			}
		}).start();
	}
	
	private void checkVersion() {
		if (NetWorkService.isNetWorkConnected(this)) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					AppVersion version = null;
					try {
						AuthToken token = CommUtils.refreshToken();
						int versionCode = CommUtils.getVersionCode(MainActivity.this, MainActivity.this.getPackageName());
						if (versionCode!=-1){
							version = NetWorkService.createApi(WehealthTokenFree.class, PreferUtils.getIntance().getServerUrl()).getNewVersion(AppType.deviceApp, versionCode);
						}
					}catch (Exception e){
						e.printStackTrace();
					}
					if (version != null) {//有新的版本，弹出提示框，版本更新
						PreferUtils.getIntance().setVersion(true);
						Message msg = handler.obtainMessage(UPDATE_VERSION);
						msg.obj = version;
						handler.sendMessage(msg);
						return;
					}else {
						PreferUtils.getIntance().setVersion(false);
					}
				}
			}).start();
		}
	}

}
