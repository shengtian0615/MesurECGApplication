package com.wehealth.mesurecg.usbutil;

import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.widget.Toast;

import com.wehealth.mesurecg.activity.ECGMeasureActivity;

public class USB_Operation {

	private Activity activity;
	private BroadcastReceiver usbPermisReceiver;
	private UsbManager usbManager;
	private UsbDevice usbDevice;
	private UsbDeviceConnection connection;
	private PendingIntent mPermissionIntent;
	private static String ACTION_USB_PERMISSION = "com.android.ecgequipment.USB_PERMISSION";
	private static String DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
	
	public USB_Operation(ECGMeasureActivity ctx){
		activity = ctx;
		mPermissionIntent = PendingIntent.getBroadcast(activity, 0, new Intent(ACTION_USB_PERMISSION), 0);
		
		usbPermisReceiver = new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (ACTION_USB_PERMISSION.equals(action)) {
					Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
					synchronized (this) {
						UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
						if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
							if (device != null) {
								openDev();
							}
						} else {
							activity.finish();
						}
					}
				}
				if (DETACHED.equals(action)) {
					Toast.makeText(activity, "USB设备已断开", Toast.LENGTH_SHORT).show();
					usbDevice = null;
					return;
				}
			}
		};
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		filter.addAction(DETACHED);
		activity.registerReceiver(usbPermisReceiver, filter);
	}
	
	public static boolean isDetectionDevice(UsbManager um) {
		if (um==null) {
			return false;
		}
		
		HashMap<String, UsbDevice> udMap = um.getDeviceList();
		if (udMap.isEmpty()) {
			return false;
		}
		Iterator<UsbDevice> udIt = udMap.values().iterator();
		if (!udIt.hasNext()) {
			return false;
		}
		UsbDevice localUsbDevice;
		while (udIt.hasNext()) {
			localUsbDevice = udIt.next();
			if ((localUsbDevice.getVendorId() == 4292) || ((localUsbDevice.getProductId() == 60001) || (localUsbDevice.getProductId() == 60000))) {
//				this.usbDevice = localUsbDevice;
				return true;
			}
		}
		return false;
	}
	
	public void closeDev() {
		if (this.connection != null) {
			this.activity.unregisterReceiver(this.usbPermisReceiver);
//	        this.mConnection.bulkTransfer(this.mReadEndpoint, this.mReadBuffer, this.mReadBuffer.length, 200);
//	        this.mConnection.close();
//	        AcquisitionOperation.isOpened = false;
	        this.connection = null;
	    }
	}
	  
	
	private void openDev() {
		if ((this.usbManager != null) && (this.usbDevice != null)) {
			if (this.usbManager.hasPermission(this.usbDevice)) {
				openDevice();
			}else {
				this.usbManager.requestPermission(this.usbDevice, mPermissionIntent);
			}
		}
	}

	public UsbDeviceConnection openDevice() {
		return usbManager.openDevice(usbDevice);
	}
}
