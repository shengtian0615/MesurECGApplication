package com.wehealth.mesurecg;

import android.app.AlertDialog;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.wehealth.mesurecg.view.TransparentPDialog;
import com.wehealth.urion.IBean;
import com.wehealth.urion.ICallback;
import com.wehealth.urion.Msg;
import com.wehealth.urion.Error;

public class BTBPressActivity extends BaseActivity implements ICallback {
	
//	public static final int REQUEST_CONNECT_DEVICE = 1;
//	public static final int REQUEST_ENABLE_BT = 2;
	public static String TAG = "URION_BLUETOOTH";
	
	public BluetoothAdapter mBluetoothAdapter;
	public MeasurECGApplication rbxt;
	
	public TransparentPDialog halfDialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Application app = getApplication();
		if(app instanceof MeasurECGApplication)
			rbxt = (MeasurECGApplication)app;
		if(mBluetoothAdapter == null)
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(!mBluetoothAdapter.isEnabled()){
			//打开蓝牙请求提示框
//			Intent enableIntent = new Intent(
//					BluetoothAdapter.ACTION_REQUEST_ENABLE);
//			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			//强制打开蓝牙
			mBluetoothAdapter.enable();
		}
		
		halfDialog = new TransparentPDialog(this, R.style.halfTransparentDialog);//
		Window wd = halfDialog.getWindow();
		WindowManager.LayoutParams lp = wd.getAttributes();
		lp.alpha = 0.6f;
		wd.setAttributes(lp);
		halfDialog.setCanceledOnTouchOutside(false);
		halfDialog.setCancelable(false);
	}
	
	protected void onStart() {
		super.onStart();
		rbxt.setCall(this);
	}
	@Override
	protected void onDestroy() {
           super.onDestroy();
	 //      rbxt.getService().stop();
	}

	public void onError(Error error) {
		
	}

	public void onMessage(Msg message) {
		
	}

	public void onReceive(IBean bean) {
		
	}
	
	/**血压计测量时，测量错误提示**/
	protected void showPromptDialog(String message){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setMessage(message);
		alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});
		alertDialog.show();
	}
	
	protected void isNoticeDialog(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.friend_notify);
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
}
