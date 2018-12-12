package com.wehealth.mesurecg.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.wehealth.mesurecg.BaseActivity;
import com.wehealth.mesurecg.MeasurECGApplication;
import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.utils.LocationService;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.mesurecg.utils.ToastUtil;
import com.wehealth.mesurecg.view.UIProgressDialog;
import com.wehealth.model.util.NetWorkService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SetActivity extends BaseActivity implements OnClickListener, BDLocationListener{

	private final int DISCOVER_BOOTH = 100;
	private final int SAVEFILE_STYLE = 101;
	private RelativeLayout titleLayout, update, playVideoLayout, locLayout, helpLayout;
	private ImageView playVideON, playVideOff;
	private CheckBox stylEcgAuto;
	private LinearLayout pageLayout, iccidLayout;
	private TextView tv2, version, playVideoMsg, stylEcgSenior, sim_iccid, simTV, playVideoTV, locDetail, helpTV;
	private Button unBindBtn;
	private int saveFile;
	private UIProgressDialog pDialog;
	private LocationService locationService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_base_infomation_set);
		
		pageLayout = (LinearLayout) findViewById(R.id.backgroud);
		titleLayout = (RelativeLayout) findViewById(R.id.title_layout);
		
		iccidLayout = (LinearLayout) findViewById(R.id.set_iccid);
		update = (RelativeLayout) findViewById(R.id.set_update);
		playVideoLayout = (RelativeLayout) findViewById(R.id.set_playvideo);
		locLayout = (RelativeLayout) findViewById(R.id.set_loc_layout);
		helpLayout = (RelativeLayout) findViewById(R.id.set_help);
		
		playVideOff = (ImageView) findViewById(R.id.set_play_video_off);
		playVideON = (ImageView) findViewById(R.id.set_play_video_on);
		stylEcgAuto = (CheckBox) findViewById(R.id.style_ecgstyle_auto);
		stylEcgSenior = (TextView) findViewById(R.id.set_ecgstyle_senior);
		tv2 = (TextView) findViewById(R.id.app_update_textView);
		version = (TextView) findViewById(R.id.device_version);
		playVideoMsg = (TextView) findViewById(R.id.set_play_video_msg);
		simTV = (TextView) findViewById(R.id.sim_iccid_tv);
		sim_iccid = (TextView) findViewById(R.id.sim_iccid);
		unBindBtn = (Button) findViewById(R.id.set_bp_unbindbtn);
		playVideoTV = (TextView) findViewById(R.id.set_playvideo_tv);
		locDetail = (TextView) findViewById(R.id.set_loc_detail);
		helpTV = (TextView) findViewById(R.id.set_help_info);
		
		pDialog = new UIProgressDialog(this);
		pDialog.setCanceledOnTouchOutside(false);
		locationService = MeasurECGApplication.getInstance().locationService;
		
		update.setOnClickListener(this);
		unBindBtn.setOnClickListener(this);
		playVideoLayout.setOnClickListener(this);
		locLayout.setOnClickListener(this);
		helpLayout.setOnClickListener(this);
		stylEcgSenior.setOnClickListener(this);
		
		if (PreferUtils.getIntance().getPlayVideo()) {
			playVideON.setVisibility(View.INVISIBLE);
			playVideOff.setVisibility(View.VISIBLE);
			playVideoMsg.setText(R.string.play_video_off);
		}else {
			playVideON.setVisibility(View.VISIBLE);
			playVideOff.setVisibility(View.INVISIBLE);
			playVideoMsg.setText(R.string.play_video_on);
		}
		
		PackageManager pm = this.getPackageManager();
		PackageInfo info = null;
		try {
			info = pm.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		String current_version = getString(R.string.current_version);
		version.setText(current_version+info.versionName);

		saveFile = PreferUtils.getIntance().getSaveFileStyle();
		if (saveFile == 0) {
			stylEcgAuto.setChecked(true);
		}else {
			stylEcgAuto.setChecked(false);
		}
		stylEcgAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				stylEcgAuto.setChecked(isChecked);
				if (isChecked) {
					PreferUtils.getIntance().setSaveFileStyle(0);
					PreferUtils.getIntance().setDisplayStyle(0);
				}
			}
		});
		reflushBackgroud();
	}
	
	/** 更改主题风格  **/
	private void reflushBackgroud() {
			pageLayout.setBackgroundColor(getResources().getColor(R.color.page_background_color));
			titleLayout.setBackgroundColor(getResources().getColor(R.color.page_title_bar_color));
			tv2.setTextColor(getResources().getColor(R.color.text_white));
			version.setTextColor(getResources().getColor(R.color.text_white));
			sim_iccid.setTextColor(getResources().getColor(R.color.text_white));
			simTV.setTextColor(getResources().getColor(R.color.text_white));
			unBindBtn.setTextColor(getResources().getColor(R.color.text_white));
			playVideoMsg.setTextColor(getResources().getColor(R.color.text_white));
			playVideoTV.setTextColor(getResources().getColor(R.color.text_white));
			locDetail.setTextColor(getResources().getColor(R.color.text_white));
			helpTV.setTextColor(getResources().getColor(R.color.text_white));
	}

	public void onBackBottonClick(View view){
		 finish();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.set_ecgstyle_senior:
			Intent senIntent = new Intent(this, SetECGStyleActivity.class);
			startActivityForResult(senIntent, SAVEFILE_STYLE);
			break;
		case R.id.set_update:
			break;
		case R.id.set_bp_unbindbtn:
			if (PreferUtils.getIntance().getBPBlueToothMac()==null) {
				Toast.makeText(this, "您还未添加血压计", Toast.LENGTH_SHORT).show();
				return;
			}
			PreferUtils.getIntance().setBPBlueToothMac(null);
			String bPrompt = getString(R.string.set_change_bp_prompt);
			isNoticeDialog(bPrompt);
			break;
		case R.id.set_playvideo:
			if (playVideON.getVisibility() == View.VISIBLE) {
				playVideON.setVisibility(View.INVISIBLE);
				playVideOff.setVisibility(View.VISIBLE);
				PreferUtils.getIntance().setPlayVideo(true);
				playVideoMsg.setText(R.string.play_video_off);
			}else {
				playVideON.setVisibility(View.VISIBLE);
				playVideOff.setVisibility(View.INVISIBLE);
				PreferUtils.getIntance().setPlayVideo(false);
				playVideoMsg.setText(R.string.play_video_on);
			}
			break;
		case R.id.set_loc_layout:
			if (!NetWorkService.isNetWorkConnected(this)) {
				ToastUtil.showShort(this, "网络不可用");
				return;
			}
			//注册监听
			if (locationService!=null) {
				locationService.setLocationOption(locationService.getDefaultLocationClientOption());
				locationService.start();// 定位SDK
				pDialog.setMessage("正在获取当前位置...");
				pDialog.show();
			}
			break;
		case R.id.set_help:
			try {
				File file = new File(Environment.getExternalStorageDirectory().getPath()+"/wehealthPaitent/Cardiovascular_knowledge.pdf");
				if (!file.exists()) {
					try {
						File fileDir = new File(Environment.getExternalStorageDirectory().getPath()+"/wehealthPaitent");
						if (!fileDir.exists()) {
							fileDir.mkdir();
						}
						InputStream is = getAssets().open("Cardiovascular_knowledge.pdf");
						FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/wehealthPaitent/Cardiovascular_knowledge.pdf");
						// 2.定义存储空间
						byte[] buffer = new byte[2048];
						// 3.开始读文件
						int len = -1;
						while ((len = is.read(buffer)) != -1) {
							fos.write(buffer, 0, len);// 将Buffer中的数据写到outputStream对象中
						}
						fos.close();
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				Intent intent = new Intent("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Uri uri = Uri.fromFile(file);
				intent.setDataAndType(uri, "application/pdf");//  文档格式   
				startActivity(intent);
			} catch (Exception e) {
				ToastUtil.showShort(this, "请先安装pdf阅读器软件");
			}
			break;

		default:
			break;
		}
	}
	
	private void isNoticeDialog(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.friend_notify);
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent(SetActivity.this, DeviceListActivity.class);
				startActivityForResult(intent, DISCOVER_BOOTH);
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==DISCOVER_BOOTH) {
			if (resultCode == Activity.RESULT_OK) {
				String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				PreferUtils.getIntance().setBPBlueToothMac(address);
			}else if (resultCode == 300) {
				String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				PreferUtils.getIntance().setBPBlueToothMac(address);
			}
		}
		if (requestCode == SAVEFILE_STYLE) {
			saveFile = PreferUtils.getIntance().getSaveFileStyle();
			if (saveFile==0) {
				stylEcgAuto.setChecked(true);
			}else {
				stylEcgAuto.setChecked(false);
			}
		}
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
		if (pDialog!=null && pDialog.isShowing()) {
			pDialog.dismiss();
		}
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
	}
	
	@Override
	public void onReceiveLocation(BDLocation lastLocation) {
		if (locationService!=null) {
			locationService.stop();
		}
		if (pDialog!=null && !isFinishing()) {
			pDialog.dismiss();
		}
		MeasurECGApplication.getInstance().setLocation(lastLocation);
		if (lastLocation==null || null==lastLocation.getAddrStr()) {
			ToastUtil.showShort(this, "地址由百度提供，没有获取到，抱歉!");
		}else {
			locDetail.setText("当前位置："+lastLocation.getAddrStr());
		}
	}
}