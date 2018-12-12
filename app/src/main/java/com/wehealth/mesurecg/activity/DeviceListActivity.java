package com.wehealth.mesurecg.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.wehealth.mesurecg.BaseActivity;
import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.adapter.DeviceArrayAdapter;

public class DeviceListActivity extends BaseActivity {

	public static String EXTRA_DEVICE_ADDRESS = "device_address";
	private BluetoothAdapter mBtAdapter;
	private DeviceArrayAdapter mPairedDevicesArrayAdapter;
	private DeviceArrayAdapter mNewDevicesArrayAdapter;
	private String noDevices, noPariedDevices;
	private LinearLayout deviceLayout;
	private TextView pariedTV, noPariedTV;
	Map<String, String> map;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.device_list);
		
		deviceLayout = (LinearLayout) findViewById(R.id.device_layout);
		pariedTV = (TextView) findViewById(R.id.device_title_paired);
		noPariedTV = (TextView) findViewById(R.id.device_title_noparied);
		
		noDevices = getResources().getText(R.string.none_found).toString();
		noPariedDevices = getResources().getText(R.string.none_paired).toString();

		setResult(Activity.RESULT_CANCELED); 

		Button scanButton = (Button) findViewById(R.id.device_scan_btn);
		scanButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doDiscovery();
				v.setVisibility(View.GONE);
			}
		});

		mPairedDevicesArrayAdapter = new DeviceArrayAdapter(this);
		mNewDevicesArrayAdapter = new DeviceArrayAdapter(this);

		ListView pairedListView = (ListView) findViewById(R.id.device_paired_devices);
		pairedListView.setAdapter(mPairedDevicesArrayAdapter);
		pairedListView.setOnItemClickListener(mDeviceClickListener);

		ListView newDevicesListView = (ListView) findViewById(R.id.device_noparied_devices);
		newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
		newDevicesListView.setOnItemClickListener(nmDeviceClickListener);
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);

		mBtAdapter = BluetoothAdapter.getDefaultAdapter();

		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				map = new HashMap<String, String>();
				map.put("title", device.getName());
				map.put("description", device.getAddress());
				mPairedDevicesArrayAdapter.adDeviceName(map);
			}
		} else {
			map = new HashMap<String, String>();
			String noDevices = getResources().getText(R.string.none_paired).toString();
			map.put("title", noDevices);
			map.put("description", "");
			mPairedDevicesArrayAdapter.adDeviceName(map);
		}
		
		reflushStyle();
	}

	private void reflushStyle() {
			deviceLayout.setBackgroundColor(getResources().getColor(R.color.page_background_color));
			pariedTV.setTextColor(getResources().getColor(R.color.text_white));
			noPariedTV.setTextColor(getResources().getColor(R.color.text_white));
	}

	protected void onDestroy() {
		super.onDestroy();

		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();
		}

		this.unregisterReceiver(mReceiver);
	}

	private void doDiscovery() {

		setProgressBarIndeterminateVisibility(true);
		setTitle(R.string.scanning);

		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}

		mBtAdapter.startDiscovery();
	}

	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			mBtAdapter.cancelDiscovery();

			String info = ((TextView) v).getText().toString();
			if (info.equals(noPariedDevices)) {
				return;
			}
			String address = info.substring(info.length() - 17);

			Intent intent = new Intent();
			intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	};
	private OnItemClickListener nmDeviceClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			mBtAdapter.cancelDiscovery();

			String info = ((TextView) v).getText().toString();
			if (info.equals(noDevices)) {
				return;
			}
			String address = info.substring(info.length() - 17);

			Intent intent = new Intent();
			intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

			setResult(300, intent);
			finish();
		}
	};
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					if (device.getName()==null) {
						return;
					}
					map = new HashMap<String, String>();
					map.put("title", device.getName());
					map.put("description", device.getAddress());
					mNewDevicesArrayAdapter.adDeviceName(map);
				}
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				setProgressBarIndeterminateVisibility(false);
				setTitle(R.string.select_device);
				if (mNewDevicesArrayAdapter.getCount() == 0) {
					map = new HashMap<String, String>();
					String noDevices = getResources().getText(R.string.none_found).toString();
					map.put("title", noDevices);
					map.put("description", "");
					mNewDevicesArrayAdapter.adDeviceName(map);
				}
			}
		}
	};
}
