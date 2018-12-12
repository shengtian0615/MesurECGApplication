/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *
 */

package com.wehealth.mesurecg.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wehealth.mesurecg.BaseFragmentActivity;
import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.fragment.ECGHistoryFragment;
import com.wehealth.mesurecg.fragment.ECGLongHistoryFragment;
import com.wehealth.mesurecg.fragment.PressHistoryFragment;
import com.wehealth.mesurecg.fragment.SugarHistoryFragment;
import com.wehealth.mesurecg.utils.ToastUtil;

public class EcgHistoryActivity extends BaseFragmentActivity {// implements IOCallBack

	public static final String TAG = "EcgHistoryActivity";
	
	private final int PRINTER_START = 1000;
	private final int PRINTER_MESSAGE = 1001;
	private final int PRINTER_FAILED = 1002;
	private final int PRINTER_END = 1003;

	private ListView listView;
	private ECGMenuAdapter ecgMenuAdapter;
	private RelativeLayout titleLayout;
	private List<String> profileList = new ArrayList<String>();
	private int colorYellow, colorWhite, colorBlue;
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case PRINTER_START:
				loaDialog.show();
				ToastUtil.showShort(EcgHistoryActivity.this, "正在连接打印机，请不要再次点击");
				break;
			case PRINTER_MESSAGE:
				String message = (String) msg.obj;
				loaDialog.setLoadText(message);
				break;
			case PRINTER_FAILED:
				String reson = (String) msg.obj;
				if (!isFinishing()) {
					loaDialog.dismiss();
					isWait("打印失败："+reson);
				}
				break;
			case PRINTER_END:
				if (!isFinishing()) {
					loaDialog.dismiss();
					ToastUtil.showShort(EcgHistoryActivity.this, "打印成功！");
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
		setContentView(R.layout.activity_history);

		titleLayout = findViewById(R.id.title_layout);
		colorYellow = getResources().getColor(R.color.yellow_style_f86031);
		colorWhite = getResources().getColor(R.color.text_white);
		colorBlue = getResources().getColor(R.color.page_title_bar_color);
//		userName = (TextView) findViewById(R.id.ecg_user);
//		testedMemeber = ClientApp.getInstance().getPatient();
//		if (testedMemeber != null && testedMemeber.getName() != null) {
//			userName.setText("用户：" + testedMemeber.getName());
//		}

		TextView titleView = (TextView) findViewById(R.id.page_title);
		titleView.setText(R.string.health_file);

		listView = findViewById(R.id.listview_ecg_history);

		String[] profile = getResources().getStringArray(R.array.ecg_profile);
		for (String namECG : profile) {
			profileList.add(namECG);
		}

		ecgMenuAdapter = new ECGMenuAdapter(this, 1, profileList);
		listView.setAdapter(ecgMenuAdapter);
		showFragment(0);
		ecgMenuAdapter.setPosition(0);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String type = (String) arg0.getItemAtPosition(arg2);
				int i = -1;
				switch (type){
					case"心电档案":
						i = 0;
						break;
					case"血压档案":
						i = 1;
						break;
					case"血糖档案":
						i = 2;
						break;
					case"长时间心电":
						i = 3;
						break;
				}
				ecgMenuAdapter.setPosition(arg2);
				ecgMenuAdapter.notifyDataSetChanged();
				showFragment(i);
			}
		});

//		pos = new Pos();
//		mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
//		HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
//		Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
//		if (deviceList.size() ==1) {
//
//			UsbDevice device = deviceIterator.next();
//			if (mUsbManager.hasPermission(device)) {
//				int vendorId = device.getVendorId();
//				int productId = device.getProductId();
////				StringUtils.writException2File("/sdcard/usb.txt", "\n vendorId="+vendorId+"  productId="+productId);
//				if (vendorId==1157&&productId==30017 || vendorId==10473&&productId==649) {
//					initUSBPrint(device);
//				}else {
//					initBlueToothPrint();
//				}
//			} else {
//				IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
//				filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
//				filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
//				registerReceiver(mUsbReceiver, filter);
//				PendingIntent mPermissionIntent = PendingIntent.getBroadcast(
//									EcgHistoryActivity.this, 0,
//									new Intent(ACTION_USB_PERMISSION), 0);
//				mUsbManager.requestPermission(device, mPermissionIntent);
//			}
//		}else {
//			initBlueToothPrint();
//		}

		reflushStyle();
	}
	
//	private void initUSBPrint(UsbDevice device) {
//		USBPrinting mUsbpt = new USBPrinting();
//		pos.Set(mUsbpt);
//		mUsbpt.SetCallBack(this);
//		printer = new Printer(null, mUsbpt, pos, handler);
//		printer.setUsbInfo(mUsbManager, device);
//	}
//
//	private void initBlueToothPrint() {
//		BTPrinting mBt = new BTPrinting();
//		pos.Set(mBt);
//		mBt.SetCallBack(this);
//		printer = new Printer(mBt, null, pos, handler);
//		initBroadcast();
//	}
//
//	private BroadcastReceiver mUsbReceiver = new BroadcastReceiver(){
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// TODO Auto-generated method stub
//            String action = intent.getAction();
//            if (ACTION_USB_PERMISSION.equals(action)) {
//            	if (intent.getBooleanExtra("permission", false)) {
//            		UsbDevice device= (UsbDevice)intent.getParcelableExtra("device");
//            		int vendorId = device.getVendorId();
//    				int productId = device.getProductId();
//    				if (vendorId==1157&&productId==30017 || vendorId==10473&&productId==649) {
//    					initUSBPrint(device);
//    				}else {
//    					initBlueToothPrint();
//    				}
//            	}else {
//                	ToastUtil.showShort(EcgHistoryActivity.this, "热敏打印机需要您的确认权限");
//                	initBlueToothPrint();
//				}
//            }
//            if (action.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
//            	ToastUtil.showShort(EcgHistoryActivity.this, "热敏打印机已拔出");
//				finish();
//			}
//		}
//	};

	/** 显示不同的健康档案 **/
	protected void showFragment(int i) {
		if (i == 0) {
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			fragmentTransaction.replace(R.id.history_framelayout,
					new ECGHistoryFragment()).commit();
		} else if (i == 1) {
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			fragmentTransaction.replace(R.id.history_framelayout,
					new PressHistoryFragment()).commit();
		} else if (i == 2) {
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			fragmentTransaction.replace(R.id.history_framelayout,
					new SugarHistoryFragment()).commit();
		} else if (i == 3) {
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			fragmentTransaction.replace(R.id.history_framelayout,
					new ECGLongHistoryFragment()).commit();
		}
	}

	private void reflushStyle() {
			titleLayout.setBackgroundColor(getResources().getColor(
					R.color.page_title_bar_color));
			listView.setBackgroundColor(getResources().getColor(
					R.color.page_title_bar_color));
	}

	@Override
	protected void onResume() {
		super.onResume();
		reflushStyle();
	}

	private class ECGMenuAdapter extends ArrayAdapter<String> {

		private int position;

		public ECGMenuAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
		}

		public void setPosition(int i) {
			position = i;
		}

		public int getPosition() {
			return position;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(getContext());
				convertView = inflater.inflate(R.layout.ecg_menu_item, parent,
						false);
			}


				if (getPosition() == arg0) {
					convertView.setBackgroundColor(colorYellow);
				} else {
					convertView.setBackgroundColor(colorBlue);
				}

			TextView t =  convertView.findViewById(R.id.ecg_menu_item_name);
			t.setText(getItem(arg0));

			t.setTextColor(getResources().getColor(R.color.set_edit_text_1));

			ImageView imageView = convertView.findViewById(R.id.ecg_menu_item_icon);
			if (arg0 == 0) {
				imageView.setImageResource(R.drawable.ecg_icon);
			} else if (arg0 == 1) {
				imageView.setImageResource(R.drawable.press_icon);
			} else if (arg0 == 2) {
				imageView.setImageResource(R.drawable.sugar_icon);
			} else if (arg0 == 3) {
				imageView.setImageResource(R.drawable.long_time_icon);
			}
			return convertView;
		}
	}

	public void onBackBottonClick(View view) {
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		uninitBroadcast();
	}

//	protected void ShowMsg(String text, boolean b) {
//		ToastUtil.showShort(this, text);
//		if (loaDialog.isShowing() && b) {
//			loaDialog.dismiss();
//		}
//	}
//
//	protected boolean checkData(List<Device> deviceList, Device d) {
//		for (Device device : deviceList) {
//			if (device.deviceAddress.equals(d.deviceAddress)) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	@Override
//	public void OnClose() {
//		runOnUiThread(new Runnable() {
//
//			@Override
//			public void run() {
//				ShowMsg("打印机已断开。。。", true);
//			}
//		});
//	}
//
//	@Override
//	public void OnOpen() {
//		Message msg = handler.obtainMessage(PRINTER_MESSAGE, "打印机已连接。。。");
//		handler.sendMessage(msg);
//	}
//
//	@Override
//	public void OnOpenFailed() {
//		runOnUiThread(new Runnable() {
//
//			@Override
//			public void run() {
//				ShowMsg("连接打印机失败。。。。", true);
//			}
//		});
//	}
//
//	private void initBroadcast() {
//		broadcastReceiver = new BroadcastReceiver() {
//
//			@Override
//			public void onReceive(Context context, Intent intent) {
//				// TODO Auto-generated method stub
//				String action = intent.getAction();
//				BluetoothDevice device = intent
//						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//
//				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//					if (device == null)
//						return;
//					final String address = device.getAddress();
//					String name = device.getName();
//					if (name != null && name.contains("Qsprinter")) {
//						//device.setPin(new byte[] {0, 0, 0, 0});
//						printer.setAddress(address);
//					}
//				} else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED
//						.equals(action)) {
//					ShowMsg("开始扫描打印机。。。", false);
//				} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
//						.equals(action)) {
//					ShowMsg("扫描完成。。。", false);
//				}
//			}
//		};
//		intentFilter = new IntentFilter();
//		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
//		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
//		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//		registerReceiver(broadcastReceiver, intentFilter);
//	}
//
//	private void uninitBroadcast() {
//		if (broadcastReceiver != null)
//			unregisterReceiver(broadcastReceiver);
//	}
}
