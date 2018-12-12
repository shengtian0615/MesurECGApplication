package com.wehealth.mesurecg.ecgbtutil;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.hardware.usb.UsbDeviceConnection;
import android.os.Handler;
import android.os.Message;

import com.wehealth.mesurecg.usbutil.UsbSerialPort;

public class UsbSerialThread extends Thread {

	private final int DRAW_ECG_WAVE = 1000;
//	private final int DRAWBACKGROUND = 999;
	private final int BT_CONNECT_FAILED = 997;
	private final int BT_CONNECTED = 996;
	private final int USB_DETACHED = 995;
	private static final int READ_WAIT_MILLIS = 50;
	private static final int WRITE_WAIT_MILLIS = 50;
	private static final int BUFSIZ = 15000;
	
	UsbDeviceConnection usbDConn;
	UsbSerialPort usPort;
	EcgDataParser24 ecgParser24;
	Handler handler;
	private static boolean RUN_FLAG = false;
	private int count = 0;
	
	public UsbSerialThread(UsbDeviceConnection usbConnection, UsbSerialPort usbSerialPort, Handler handle, EcgDataParser24.EcgDataGetListener ecgDataListener) {
		usbDConn = usbConnection;
		usPort = usbSerialPort;
		ecgParser24 = new EcgDataParser24(ecgDataListener);
		ecgParser24.EcgDataParserInit();
		handler = handle;
		RUN_FLAG = true;
	}

	@Override
	public void run() {
		super.run();
		try {
			handler.sendEmptyMessage(BT_CONNECTED);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			usPort.open(usbDConn);
//			handler.sendEmptyMessage(DRAWBACKGROUND);
			usPort.setParameters(921600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
			//usPort.purgeHwBuffers(false, true);
			usPort.write(EcgDataParser24.PackEcgDeivceInfoCmd(), WRITE_WAIT_MILLIS);
			byte buffer[] = new byte[BUFSIZ];
//			usPort.getPortNumber();
//			usPort.getSerial();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			int numBytesRead = usPort.read(buffer, READ_WAIT_MILLIS);
			System.out.println("设备控制信息数据的长度："+numBytesRead);
			byte[] tmpCMD = new byte[numBytesRead];
			for(int i = 0; i < numBytesRead; i++){
				tmpCMD[i] = buffer[i];
			}
			if (ecgParser24.EcgParserCMDInfo(tmpCMD)) {
				handler.sendEmptyMessage(DRAW_ECG_WAVE);
				ecgParser24.setModle();
				usPort.write(EcgDataParser24.PackEcgDeivceStart(), WRITE_WAIT_MILLIS);
			}else {
				Message msg = handler.obtainMessage(BT_CONNECT_FAILED);
				msg.obj = "没有获取到设备信息";
				handler.sendMessage(msg);
				return;
			}
			
			byte[] data = new byte[BUFSIZ];
			while (RUN_FLAG) {
				int available = usPort.read(data, READ_WAIT_MILLIS);
				if (available > 0) {
					ecgParser24.EcgParserPacket(data, available);
					count = 0;
				}else {
					count++;
					if (count==800) {
						RUN_FLAG = false;
						handler.sendEmptyMessage(USB_DETACHED);
					}
				}
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(BT_CONNECT_FAILED);
		}
	}
	
	@SuppressLint("SdCardPath") 
	public void stopSerial(){
		if (usPort!=null) {
			try {
				ecgParser24.stopInit();
				usPort.write(EcgDataParser24.PackEcgDeivceStop(), WRITE_WAIT_MILLIS);
				Thread.sleep(2);
				usPort.write(EcgDataParser24.PackEcgDeivceStop(), WRITE_WAIT_MILLIS);
				boolean readState = true;
				while (readState) {
					byte buffer[] = new byte[BUFSIZ*50];
					int len = usPort.read(buffer, READ_WAIT_MILLIS);
					if (len<=0) {
						readState = false;
					}
					buffer = null;
					usPort.write(EcgDataParser24.PackEcgDeivceStop(), WRITE_WAIT_MILLIS);
				}
				interrupt();
				RUN_FLAG = false;
//				handler.sendEmptyMessage(DISMISS_PROGRESS_DIALOG);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
