package com.wehealth.mesurecg.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.wehealth.mesurecg.BaseActivity;
import com.wehealth.mesurecg.MeasurECGApplication;
import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.dao.RegisterUserDao;
import com.wehealth.mesurecg.utils.CommUtils;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.mesurecg.utils.UpdateInfoService;
import com.wehealth.model.domain.enumutil.AppType;
import com.wehealth.model.domain.enumutil.Gender;
import com.wehealth.model.domain.model.AppVersion;
import com.wehealth.model.domain.model.AuthToken;
import com.wehealth.model.domain.model.RegisteredUser;
import com.wehealth.model.domain.model.ResultPassHelper;
import com.wehealth.model.interfaces.inter_other.WeHealthToken;
import com.wehealth.model.interfaces.inter_other.WehealthTokenFree;
import com.wehealth.model.interfaces.inter_register.WeHealthRegisteredUser;
import com.wehealth.model.util.Constant;
import com.wehealth.model.util.IDCardValidator;
import com.wehealth.model.util.MD5Util;
import com.wehealth.model.util.NetWorkService;
import com.wehealth.model.util.RegexUtil;
import com.wehealth.model.util.StringUtil;

public class RegisterActivity extends BaseActivity implements OnClickListener{

	private final int UPDATE_VERSION = 100;
	private final int SIGN_UP_FAILED_NETWORK = 14;
	private final int SIGN_UP_FAILED_IDCARD_EXISTS = 21;
	private final int SIGN_UP_FAILED_IDCARD_ERROR = 22;
	private final int SIGN_UP_FAILED_PHONE_EXISTS = 32;
	private final int SIGN_UP_SUCCESS = 37;
	private final int SIGN_UP_FAILED_CONNECT_KEFU = 40;
	private final int SIGN_UP_FAILED_NEED_LOGIN=41;
	private final int SIGN_UP_FAILED_DEVICE_BINDED = 42;

	private EditText user_phone, userName, user_idcardno;
	private RegisteredUser user;
	private Button registerBtn, verifyBtn, ceriNumBtn, bindBtn, mainRegiBtn, mainLogBtn;
	private LinearLayout mainLayout;
	private ScrollView registerLayout;
	private TextView agreeInfo;
	private CheckBox agreeCheckBox;
	private ProgressDialog pd, progressDialog;
	private UpdateInfoService updateInfoService;
	private AuthToken token;
	private String phoneNum, idCardNo, userNameStr, url;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private RegisterUserDao registerUserDao;
	
	@SuppressLint("HandlerLeak")
	public Handler smsHandler = new Handler() {
		// 这里可以进行回调的操作
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SIGN_UP_FAILED_NETWORK:
				user = null;
				if (pd!=null && pd.isShowing())
					pd.dismiss();
				isWait("由于网络环境不好，注册失败，请换网络环境好的情况下注册！");
				break;
			case SIGN_UP_FAILED_IDCARD_EXISTS:
				user = null;
				if (pd!=null && pd.isShowing())
					pd.dismiss();
				String login = getString(R.string.sign_up_signin);
				isWaitLogin(login);
				break;
			case SIGN_UP_FAILED_IDCARD_ERROR:
				user = null;
				if (pd!=null && pd.isShowing())
					pd.dismiss();
				String error = getString(R.string.sign_up_idcard_error);
				isWait(error);
				break;
			case SIGN_UP_FAILED_PHONE_EXISTS:
				if (pd!=null && pd.isShowing())
					pd.dismiss();
				String phonExist = getString(R.string.sign_up_phone_exist);
				isWait(phonExist);
				break;
			case SIGN_UP_SUCCESS:
				if (pd!=null && pd.isShowing())
					pd.dismiss();
				Toast.makeText(RegisterActivity.this, R.string.sign_up_success, Toast.LENGTH_LONG).show();
				Intent intent37 = new Intent(RegisterActivity.this, LogoActivity.class);
				startActivity(intent37);
				finish();
				break;
			case UPDATE_VERSION:
				if (progressDialog!=null) {
					progressDialog.dismiss();
				}
				AppVersion version = (AppVersion) msg.obj;
				if (version!=null) {
					PreferUtils.getIntance().setVersion(true);
					showUpdateDialog(version);
				}
				break;
			case SIGN_UP_FAILED_CONNECT_KEFU:
				if (pd!=null && pd.isShowing())
					pd.dismiss();
				isWait("设备序列号有误，请您确保是否正确；\n如有疑问请联系客服解决，客服电话：400-901-2022");
				break;
			case SIGN_UP_FAILED_DEVICE_BINDED:
				if (pd!=null && pd.isShowing())
					pd.dismiss();
				isWait("该设备序列号已经被别人使用，请更改设备序列号；\n如有疑问请联系客服解决，客服电话：400-901-2022");
				break;
			case SIGN_UP_FAILED_NEED_LOGIN:
				if (pd!=null && pd.isShowing())
					pd.dismiss();
				login = getString(R.string.sign_up_signin);
				isWaitLogin(login);;
				break;
			default:
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		pd = new ProgressDialog(this);
		progressDialog = new ProgressDialog(this);
		updateInfoService = new UpdateInfoService(this);
		url = PreferUtils.getIntance().getServerUrl();

		initView();
//		checkVersion();
	}

	private void initView() {
		userName = (EditText) findViewById(R.id.user_name);
		registerBtn = (Button) findViewById(R.id.register_btn);
		agreeCheckBox = (CheckBox) findViewById(R.id.register_agree_cb);
		agreeInfo = (TextView) findViewById(R.id.register_agree_info);
		verifyBtn = (Button) findViewById(R.id.username_veriay);
		ceriNumBtn = (Button) findViewById(R.id.register_phone_ceribtn);
		user_phone = (EditText) findViewById(R.id.register_phone);
		user_idcardno = (EditText) findViewById(R.id.idcardno);
		bindBtn = (Button) findViewById(R.id.regist_bind);
		mainLayout = (LinearLayout) findViewById(R.id.register_main);
		registerLayout = (ScrollView) findViewById(R.id.register_layout);
		mainLogBtn = (Button) findViewById(R.id.register_btnl);
		mainRegiBtn = (Button) findViewById(R.id.register_btnr);
		
		registerBtn.setOnClickListener(this);
		agreeInfo.setOnClickListener(this);
		verifyBtn.setOnClickListener(this);
		ceriNumBtn.setOnClickListener(this);
		bindBtn.setOnClickListener(this);
		mainRegiBtn.setOnClickListener(this);
		mainLogBtn.setOnClickListener(this);
	}
	
	private void checkVersion() {
		if (NetWorkService.isNetWorkConnected(this)) {
			if (progressDialog!=null) {
				progressDialog.setMessage("正在初始化");
				progressDialog.show();
			}
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					AppVersion version = null;
					try {
						AuthToken token = CommUtils.refreshToken();
						int versionCode = CommUtils.getVersionCode(RegisterActivity.this, RegisterActivity.this.getPackageName());
						if (versionCode!=-1){
							version = NetWorkService.createApi(WehealthTokenFree.class, url).getNewVersion(AppType.deviceApp, versionCode);
						}
					}catch (Exception e){
						e.printStackTrace();
					}

					Message msg = smsHandler.obtainMessage(UPDATE_VERSION);
					//有新的版本，弹出提示框，版本更新
					msg.obj = version;
					smsHandler.sendMessage(msg);
					return;
				}
			}).start();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_btn:
			register();
			break;
		case R.id.register_agree_info:
			Intent intent = new Intent(this, AboutActivity.class);
			intent.putExtra("title", "agree");
			startActivity(intent);
			break;
		case R.id.regist_bind:
			Intent bingIntent = new Intent(this, BindDeviceActivity.class);
			startActivity(bingIntent);
			this.finish();
			break;
		case R.id.register_btnl:
			Intent logIntent = new Intent(this, BindDeviceActivity.class);
			startActivity(logIntent);
			this.finish();
			break;
		case R.id.register_btnr:
			mainLayout.setVisibility(View.GONE);
			registerLayout.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}
	
	/**
	 * 注册
	 */
	public void register() {
		phoneNum = user_phone.getText().toString().trim();
		idCardNo = user_idcardno.getText().toString().trim();
		userNameStr = userName.getText().toString().trim();
		if (!NetWorkService.isNetWorkConnected(this)) {
			Toast.makeText(this, R.string.net_failed, Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(userNameStr)) {
			Toast.makeText(this, R.string.name_isempty, Toast.LENGTH_SHORT).show();
			userName.requestFocus();
			return;
		}
		if (!StringUtil.verifyName(userNameStr)) {
			String msg = getString(R.string.name_is_en_zh);
			isWait(msg);
			userName.requestFocus();
			return;
		}
		if (TextUtils.isEmpty(idCardNo)) {
			Toast.makeText(this, R.string.idcard_empty, Toast.LENGTH_SHORT).show();
			user_idcardno.requestFocus();
			return;
		}
		if (!IDCardValidator.isValidateIDCard(idCardNo)) {
			Toast.makeText(this, R.string.idcard_error, Toast.LENGTH_SHORT).show();
			user_idcardno.requestFocus();
			return ;
		}
		if (TextUtils.isEmpty(phoneNum)) {
			Toast.makeText(this, R.string.register_phone_is_empty, Toast.LENGTH_SHORT).show();
			user_phone.requestFocus();
			return;
		}
		if (!RegexUtil.phone(phoneNum)) {
			Toast.makeText(this, R.string.register_phone_error, Toast.LENGTH_SHORT).show();
			user_phone.requestFocus();
			return;
		}
		if(!agreeCheckBox.isChecked()){
			Toast.makeText(this, R.string.sign_up_agreement, Toast.LENGTH_SHORT).show();
			return;
		}

		user = new RegisteredUser();
		user.setName(userNameStr);
//		user.setUsername(username);
		user.setCellPhone(phoneNum);
		user.setIdCardNo(idCardNo);
		String psd = StringUtil.getIdCardPsd(idCardNo);
		user.setPassword(MD5Util.md5(psd));
		try {
			user.setDateOfBirth(sdf.parse(StringUtil.getBirthDay(idCardNo)));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		if (StringUtil.getGender(idCardNo).equals("男")) {
			user.setGender(Gender.male);
		}else {
			user.setGender(Gender.female);
		}
		
		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.friend_notify);
		String ap = getString(R.string.sign_up_psd_prompt);
		builder.setMessage(ap);
		builder.setPositiveButton(R.string.title_register, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				registerNoticeDialog(idCardNo);
			}
		});
		builder.setCancelable(false);
		builder.show();
	}

	private void registerNoticeDialog(final String idCardNo) {
		String s = getString(R.string.sign_up_ing);
		pd.setMessage(s);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					ResultPassHelper idCardVerify;
					idCardVerify = NetWorkService.createApi(WehealthTokenFree.class, url).checkIDCardExists(idCardNo);
					if (idCardVerify==null) {
						smsHandler.sendEmptyMessage(SIGN_UP_FAILED_NETWORK);
						return;
					}
					if (idCardVerify.getName().equals(Constant.ID_EXIST)) {
						smsHandler.sendEmptyMessage(SIGN_UP_FAILED_IDCARD_EXISTS);
						return;
					}
					if (idCardVerify.getValue()!=null && idCardVerify.getValue().equals(Constant.ID_FORMAT_ERROR)) {
						smsHandler.sendEmptyMessage(SIGN_UP_FAILED_IDCARD_ERROR);
						return;
					}
					String psd = MD5Util.md5(idCardNo+"DeviceUserRegister");
					token = NetWorkService.createApi(WeHealthToken.class, url).authorize(NetWorkService.client_credentials, "DeviceUserRegister", idCardNo, psd);//UIHelper.regisNewAuthToken("DeviceUserRegister", idCardNo);
					if(token == null){
						smsHandler.sendEmptyMessage(SIGN_UP_FAILED_NETWORK);
						return;
					}
					
					ResultPassHelper phoneVerify = NetWorkService.createApi(WeHealthRegisteredUser.class, url).checkIfPhoneExists(NetWorkService.bear+token.getAccess_token(), phoneNum);//UIHelper.checkPhoneExist(token, phoneNum);
					if (phoneVerify==null) {
						smsHandler.sendEmptyMessage(SIGN_UP_FAILED_NETWORK);
						return;
					}
					if (Constant.ID_EXIST.equals(phoneVerify.getName())) {
						smsHandler.sendEmptyMessage(SIGN_UP_FAILED_PHONE_EXISTS);
						return;
					}

					RegisteredUser registerUser = NetWorkService.createApi(WeHealthRegisteredUser.class, url).registerRegisteredUser("Bearer " + token.getAccess_token(), user, true, false);
					
					if (registerUser != null) {
						registerUserDao.saveRegisterUser(registerUser);
						MeasurECGApplication.getInstance().setRegisterUser(registerUser);
//						ClientApp.getInstance().logInfo(registerUser.getIdCardNo());
						Message msg = smsHandler.obtainMessage(SIGN_UP_SUCCESS);
						msg.obj = registerUser;
						smsHandler.sendMessage(msg);
					}else {
						smsHandler.sendEmptyMessage(SIGN_UP_FAILED_NETWORK);
					}
				} catch (final Exception e) {
					e.printStackTrace();
					smsHandler.sendEmptyMessage(SIGN_UP_FAILED_NETWORK);
				}
			}
		}).start();
	}

	public void isWait(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.friend_notify);
		builder.setMessage(message);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setCancelable(false);
		builder.show();
	}
	
	public void isWaitLogin(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.friend_notify);
		builder.setMessage(message);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent(RegisterActivity.this, BindDeviceActivity.class);
				startActivity(intent);
				RegisterActivity.this.finish();
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				RegisterActivity.this.finish();
			}
		});
		builder.setCancelable(false);
		builder.show();
	}

	/**显示是否要更新的对话框**/
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
					Toast.makeText(RegisterActivity.this, R.string.sdcard_no_avaiable, Toast.LENGTH_SHORT).show();
				}
			}
		});
		builder.create().show();
	}

	private void downFile(AppVersion version) { 
		progressDialog = new ProgressDialog(RegisterActivity.this);    //进度条，在下载的时候实时更新进度，提高用户友好度
		progressDialog.setCancelable(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setTitle(R.string.download_latest_version);
		String load_prompt = getString(R.string.download_prompt);
		progressDialog.setMessage(load_prompt);
		progressDialog.setProgress(0);
		progressDialog.show();
		updateInfoService.downLoadFile(version, progressDialog);
	}

}
