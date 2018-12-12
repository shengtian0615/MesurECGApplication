package com.wehealth.mesurecg.activity;

import com.wehealth.mesurecg.BaseActivity;
import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.mesurecg.utils.ToastUtil;
import com.wehealth.model.domain.model.AuthToken;
import com.wehealth.model.domain.model.RegisteredUser;
import com.wehealth.model.interfaces.inter_other.WeHealthToken;
import com.wehealth.model.interfaces.inter_register.WeHealthRegisteredUser;
import com.wehealth.model.util.Constant;
import com.wehealth.model.util.IDCardValidator;
import com.wehealth.model.util.MD5Util;
import com.wehealth.model.util.NetWorkService;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BindDeviceActivity extends BaseActivity implements OnClickListener{

	private Button loginBtn;
	private EditText idCardNoET, psdET;
	private ProgressDialog progressDialog;
	private String s1, s2;
	
	private final int SIGN_IN_SUCCESS = 10;
	private final int SIGN_IN_FAIL_NO_INFO = 11;
	private final int SIGN_IN_FAIL = 12;
	private final int NOT_SIGN_IN_SIGN_UP_FAIL = 13;
	private final int IDCARDNO_OR_PASSWORD_ERROR = 14;
	private final int SIGN_IN_FAIL_NOT_GET_INFO = 15;
	private final int YOU_BINDED_SING_IN = 16;
	private final int NETWORK_NOT_AVAILABILITY = 17;
	private final int YOU_IS_COMMERCIAL = 18;
	private final int SIGN_IN_FAIL_NOT_MYDEVICE = 19;
	private final int SIGN_IN_FAIL_CONNECT_KEFU = 20;
	private final int SIGN_IN_FAIL_DEVICE_ISBINDED = 21;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SIGN_IN_SUCCESS:
				if (progressDialog!=null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				ToastUtil.showShort(BindDeviceActivity.this, R.string.sign_in_success);
				Intent intent = new Intent(BindDeviceActivity.this, LogoActivity.class);
				startActivity(intent);
				BindDeviceActivity.this.finish();
				break;
			case SIGN_IN_FAIL_NO_INFO:
				if (progressDialog!=null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				ToastUtil.showShort(BindDeviceActivity.this, R.string.sign_in_fail_no_info);
				break;
			case SIGN_IN_FAIL:
				if (progressDialog!=null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				ToastUtil.showShort(BindDeviceActivity.this, R.string.sign_in_fail);
				break;
			case NOT_SIGN_IN_SIGN_UP_FAIL:
				if (progressDialog!=null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				ToastUtil.showShort(BindDeviceActivity.this, R.string.no_sign_up_sign_in_fail);
				break;
			case IDCARDNO_OR_PASSWORD_ERROR:
				if (progressDialog!=null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				ToastUtil.showShort(BindDeviceActivity.this, R.string.idcardno_password_error);
				break;
			case SIGN_IN_FAIL_NOT_GET_INFO:
				if (progressDialog!=null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				ToastUtil.showShort(BindDeviceActivity.this, R.string.sign_in_fail_no_get_info);
				break;
			case YOU_BINDED_SING_IN:
				if (progressDialog!=null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				isWaveQuality(s1, true);
				break;
			case NETWORK_NOT_AVAILABILITY:
				if (progressDialog!=null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				isWaveQuality(s2, false);
				break;
			case YOU_IS_COMMERCIAL:
				if (progressDialog!=null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				isWaveQuality("您的登录账户是商业版账户，不可以登录家庭版", true);
				break;
			case SIGN_IN_FAIL_NOT_MYDEVICE:
				if (progressDialog!=null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				isWait("您输入的身份证号和输入的设备序列号不属于同一台设备；\n如有疑问请联系客服解决，客服电话：400-901-2022");
				break;
			case SIGN_IN_FAIL_CONNECT_KEFU:
				if (progressDialog!=null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				isWait("您输入的设备序列号在服务端查询不到设备信息；\n如有疑问请联系客服解决，客服电话：400-901-2022");
				break;
			case SIGN_IN_FAIL_DEVICE_ISBINDED:
				if (progressDialog!=null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				isWait("您输入的设备序列号已经被其他用户绑定，请您确保设备序列号正确；\n如有疑问请联系客服解决，客服电话：400-901-2022");
				break;
				
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bind_device);
		
		progressDialog = new ProgressDialog(this);
		s1 = getResources().getString(R.string.you_are_binded);
		s2 = getResources().getString(R.string.network_signal_not_good);
		initView();
		Intent intent = getIntent();
		if (intent==null) {
			return;
		}
		if (intent.getIntExtra(Constant.CHAT_ROBOT, 0)==1) {
			String idCardNo = PreferUtils.getIntance().getIdCardNo();
			if (TextUtils.isEmpty(idCardNo)) {
				idCardNoET.setText(idCardNo);
			}
		}
	}

	private void initView() {
		idCardNoET = (EditText) findViewById(R.id.bind_device_idcardno);
		psdET = (EditText) findViewById(R.id.bind_device_psd);
		loginBtn = (Button) findViewById(R.id.bind_device_logbtn);
		loginBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() ==R.id.bind_device_logbtn) {
			if (!NetWorkService.isNetWorkConnected(this)) {
				ToastUtil.showShort(this, R.string.net_failed);
				return;
			}
			final String idCardNoStr = idCardNoET.getText().toString().trim();
			final String psdStr = psdET.getText().toString().trim();
			if (!IDCardValidator.isValidateIDCard(idCardNoStr)) {
				ToastUtil.showShort(this, R.string.idcard_error);
				return;
			}
			if (TextUtils.isEmpty(psdStr)) {
				ToastUtil.showShort(this, R.string.password_not_empty);
				return;
			}
			String s3 = getResources().getString(R.string.sign_in_notice);
			progressDialog.setMessage(s3);
			progressDialog.setCancelable(false);
			progressDialog.show();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					RegisteredUser registeredUser;
					AuthToken token;

					try {
						token = NetWorkService.createApi(WeHealthToken.class, PreferUtils.getIntance().getServerUrl()).authorize("client_credentials", Constant.RegisteredUser,
								idCardNoStr, MD5Util.md5(psdStr));
						if(token == null) {
							handler.sendEmptyMessage(IDCARDNO_OR_PASSWORD_ERROR);
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
						if (e!=null && e.getLocalizedMessage()!=null && e.getLocalizedMessage().contains("failed to connect to")) {
							handler.sendEmptyMessage(NETWORK_NOT_AVAILABILITY);
							return;
						}
						if (e!=null && e.getLocalizedMessage()!=null && e.getLocalizedMessage().contains("Unable to resolve host")) {
							handler.sendEmptyMessage(NETWORK_NOT_AVAILABILITY);
							return;
						}
						if (e!=null && e.getLocalizedMessage()!=null && e.getLocalizedMessage().contains("Connection timed out")) {
							handler.sendEmptyMessage(NETWORK_NOT_AVAILABILITY);
							return;
						}
						if (e!=null && e.getLocalizedMessage()!=null && e.getLocalizedMessage().contains("SSLProtocolException")) {
							handler.sendEmptyMessage(NETWORK_NOT_AVAILABILITY);
							return;
						}
						handler.sendEmptyMessage(IDCARDNO_OR_PASSWORD_ERROR);
						return;
					}
					try {
						registeredUser = NetWorkService.createApi(WeHealthRegisteredUser.class, PreferUtils.getIntance().getServerUrl()).getRegisteredUser("Bearer " + token.getAccess_token(), idCardNoStr);
					} catch (Exception e) {
						e.printStackTrace();
						handler.sendEmptyMessage(SIGN_IN_FAIL_NOT_GET_INFO);
						return;
					}
					if (registeredUser==null) {
						handler.sendEmptyMessage(NOT_SIGN_IN_SIGN_UP_FAIL);
						return;
					}
					
					if (!registeredUser.getPassword().equals(MD5Util.md5(psdStr))) {
						handler.sendEmptyMessage(IDCARDNO_OR_PASSWORD_ERROR);
						return;
					}
					if (registeredUser.getRegType()==1) {
						handler.sendEmptyMessage(YOU_IS_COMMERCIAL);
						return;
					}
					if (registeredUser != null) {
//						MeasurECGApplication.getInstance().logInfo(registeredUser.getIdCardNo());
						handler.sendEmptyMessage(SIGN_IN_SUCCESS);
						PreferUtils.getIntance().setIdCardNo(registeredUser.getIdCardNo());
						PreferUtils.getIntance().setPassword(registeredUser.getIdCardNo(), registeredUser.getPassword());
					}
				}
			}).start();
		}
	}
	

	private void isWaveQuality(String message, final boolean b){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.friend_notify);
		builder.setMessage(message);
		builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (b) {
					idCardNoET.setText("");
					psdET.setText("");
				}
			}
		});
		builder.setCancelable(false);
		builder.show();
	}
	
}
