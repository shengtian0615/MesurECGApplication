/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 */
package com.wehealth.mesurecg.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.wehealth.mesurecg.BaseActivity;
import com.wehealth.mesurecg.MeasurECGApplication;
import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.dao.RegisterUserDao;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.mesurecg.utils.ToastUtil;
import com.wehealth.model.domain.model.AuthToken;
import com.wehealth.model.domain.model.RegisteredUser;
import com.wehealth.model.interfaces.inter_other.WeHealthToken;
import com.wehealth.model.interfaces.inter_register.WeHealthRegisteredUser;
import com.wehealth.model.util.Constant;
import com.wehealth.model.util.NetWorkService;
public class LogoActivity extends BaseActivity {

	private final int CONNECT_TIMEOUT = 10;
	private final int CONNECT_BAD_REQUEST = 11;
	private final int LOGIN_SUCCESS = 12;
	private final int NEED_REGISTER = 13;
	private final int LOGIN_EASE_FAILED = 14;
	private final int LOGIN_NEED = 15;

	private final int REQUEST_PERMISSION_RESULT = 20;

	private ProgressDialog pd;
	private ImageView logoImg;
	private RelativeLayout backgroundLayout;
	private String idCardNo, passWord, url;
	private RegisteredUser registeredUser;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case CONNECT_TIMEOUT:
				if (!isFinishing()) {
					pd.dismiss();
					finishDialog("网络不好，不能获取数据，请");
				}
				break;
			case LOGIN_SUCCESS:
				if (!isFinishing() && pd!=null) {
					pd.dismiss();
				}
//				if(PreferenceUtils.getInstance(LogoActivity.this).getVersion()){
//					ClientApp.getInstance().logInfo(idCardNo);
//				}
				Intent intent = new Intent(LogoActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
				break;
			case NEED_REGISTER:
				if (!isFinishing() && pd!=null) {
					pd.dismiss();
				}
				Intent intent13 = new Intent(LogoActivity.this, RegisterActivity.class);
				startActivity(intent13);
				finish();
				break;
			case CONNECT_BAD_REQUEST:
				if (!isFinishing() && pd!=null) {
					pd.dismiss();
				}
				if (!isFinishing()) {
					isWaitPrompt();
				}
				break;
			case LOGIN_EASE_FAILED:
				if (!isFinishing() && pd!=null) {
					pd.dismiss();
				}
				String easError = (String) msg.obj;
				if (!isFinishing()) {
					isWaveQuality(easError);
				}
				break;
			case LOGIN_NEED:
				if (!isFinishing() && pd!=null) {
					pd.dismiss();
				}
				ToastUtil.showShort(LogoActivity.this, "密码不对，请重新登录！");
				Intent bingIntent = new Intent(LogoActivity.this, BindDeviceActivity.class);
				bingIntent.putExtra(Constant.CHAT_ROBOT, 1);
				startActivity(bingIntent);
				finish();
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){  
            finish();  
            return;  
        } 
		setContentView(R.layout.activity_logo);
		
		logoImg = findViewById(R.id.logo_img);
		backgroundLayout = findViewById(R.id.background);
		
		pd = new ProgressDialog(LogoActivity.this);
		pd.setCanceledOnTouchOutside(false);
		pd.setCancelable(false);
		idCardNo = PreferUtils.getIntance().getIdCardNo();
		passWord = PreferUtils.getIntance().getPassword(idCardNo);
		url = PreferUtils.getIntance().getServerUrl();

		if (Build.VERSION.SDK_INT>=23){
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_RESULT);
			}else {
				nextStep();
			}
		}else{
			nextStep();
		}
	}

	private void nextStep() {
		if (TextUtils.isEmpty(idCardNo)) {
			isWaitPrompt();
			return;
		}

		if (NetWorkService.isNetWorkConnected(this)) {
			directLogin();
		}else {
			registeredUser = RegisterUserDao.getInstance(idCardNo).getIdCardNo(idCardNo);
			if (registeredUser == null) {
				ToastUtil.showShort(this, R.string.net_failed_unload_info);
				this.finish();
				return;
			}
			MeasurECGApplication.getInstance().setRegisterUser(registeredUser);
			startActivity(new Intent(LogoActivity.this, MainActivity.class));
			finish();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_PERMISSION_RESULT){
			if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
				ToastUtil.showShort(this, "需要您的定位权限");
			}
			if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
				ToastUtil.showShort(this, "需要保存心电数据");
			}
			nextStep();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void directLogin() {
		String info = getString(R.string.downloading_info);
		pd.setMessage(info);
		pd.show();
		getRegisterUser();
	}
	
	private void getRegisterUser() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
//				String serverPath = PreferenceUtils.getInstance(LogoActivity.this).getNomalRequestServerPath();
//				ResultPassHelper serverHost = UIHelper.getAppServiceHost(imei);
//				if (serverHost!=null && serverHost.getName().equals(Constant.SUCCESS)) {
//					PreferenceUtils.getInstance(LogoActivity.this).setNomalRequestServer(serverHost.getValue()+serverPath);
//				}
//				System.out.println("服务器Host："+serverHost);

				AuthToken token = null;
				try {
					token = NetWorkService.createByteApi(WeHealthToken.class, url).authorize(NetWorkService.client_credentials, Constant.RegisteredUser, idCardNo, passWord);
				}catch (Exception e){
					e.printStackTrace();
					if (e!=null && e.getLocalizedMessage()!=null && e.getLocalizedMessage().contains("failed to connect to")) {
						handler.sendEmptyMessage(CONNECT_TIMEOUT);
						return ;
					}
					if (e!=null && e.getLocalizedMessage()!=null && e.getLocalizedMessage().contains("Unable to resolve host")) {
						handler.sendEmptyMessage(CONNECT_TIMEOUT);
						return ;
					}
					if (e!=null && e.getLocalizedMessage()!=null && e.getLocalizedMessage().contains("Connection timed out")) {
						handler.sendEmptyMessage(CONNECT_TIMEOUT);
						return ;
					}
					if (e!=null && e.getLocalizedMessage()!=null && e.getLocalizedMessage().contains("SSLProtocolException")) {
						handler.sendEmptyMessage(CONNECT_TIMEOUT);
						return ;
					}
					if (e!=null && e.getLocalizedMessage()!=null && e.getLocalizedMessage().contains("400 Bad Request")) {
						handler.sendEmptyMessage(CONNECT_BAD_REQUEST);
						return ;
					}
				}

				if (token==null){
					handler.sendEmptyMessage(CONNECT_TIMEOUT);
					return ;
				}
				
				RegisteredUser registeredUser = null;
				try {
					registeredUser = NetWorkService.createApi(WeHealthRegisteredUser.class, url).getRegisteredUser("Bearer " + token.getAccess_token(), idCardNo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if (registeredUser!=null && registeredUser.getIdCardNo()!=null) {
					PreferUtils.getIntance().setIdCardNo(registeredUser.getIdCardNo());
					if (TextUtils.isEmpty(passWord)) {
						try {
							PreferUtils.getIntance().setPassword(registeredUser.getDevice().getSerialNo(), registeredUser.getPassword());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else {
						if (!passWord.equals(registeredUser.getPassword())) {
							handler.sendEmptyMessage(LOGIN_NEED);
							return;
						}
					}
//					UIHelper.getECGCheckFee(LogoActivity.this);
//					UIHelper.getPDFHospital(LogoActivity.this);
					
					RegisterUserDao.getInstance(idCardNo).saveRegisterUser(registeredUser);
					MeasurECGApplication.getInstance().setRegisterUser(registeredUser);
					MeasurECGApplication.getInstance().setToken(token);
					EMChatManager.getInstance().login(registeredUser.getEasemobUserName(), registeredUser.getEasemobPassword(), emCallBack);
				}else {
					handler.sendEmptyMessage(NEED_REGISTER);
				}
			}
		}).start();
	}
	
	EMCallBack emCallBack = new EMCallBack() {
		
		@Override
		public void onSuccess() {
			handler.sendEmptyMessage(LOGIN_SUCCESS);
		}
		
		@Override
		public void onProgress(int arg0, String arg1) { }
		
		@Override
		public void onError(int arg0, String arg1) {
			Message msg = handler.obtainMessage(LOGIN_EASE_FAILED);
			msg.obj = arg1;
			handler.sendMessage(msg);
		}
	};
	
	private void isWaveQuality(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.friend_notify);
		builder.setMessage(message);
		builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				LogoActivity.this.finish();
			}
		});
		builder.setCancelable(false);
		builder.show();
	}
	
	private void isWaitPrompt(){
		String message;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		message = "由于您的设备首次使用，请联系客服处理"+"\n"+"客服电话：400-901-2022"+"\n您是否选择登录或者注册？";
		builder.setTitle(R.string.friend_notify);
		builder.setMessage(message);
		builder.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				LogoActivity.this.finish();
			}
		});
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent(LogoActivity.this, RegisterActivity.class);
				startActivity(intent);
				LogoActivity.this.finish();
			}
		});
		builder.setCancelable(false);
		builder.show();
	}
	
//	private void isCloseWifi(String message){
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setTitle(R.string.friend_notify);
//		builder.setMessage(message);
//		builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				wifiManager.setWifiEnabled(false);
//				closeMobileNet();
//				Intent intent = new Intent(LogoActivity.this, MainActivity.class);
//				intent.putExtra("NET", true);
//				startActivity(intent);
//				finish();
//				dialog.dismiss();
//			}
//		});
//		builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				LogoActivity.this.finish();
//			}
//		});
//		builder.setCancelable(false);
//		builder.show();
//	}
	
//	/**关闭数据流量**/
//	protected void closeMobileNet() {
//		try {
//			Method method = connectivityManager.getClass().getMethod("setMobileDataEnabled", new Class[] { boolean.class });
//			method.invoke(connectivityManager, false);
//		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
//		}
//	}

}
