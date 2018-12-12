package com.wehealth.mesurecg.ecgbtutil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.model.util.StringUtil;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BTConnectStreamThread extends Thread {
	
	private final String TAG = "BTCONNTHREAD";
	Handler handler;
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	EcgDataParser24 ecgParser24;
	
	private BluetoothSocket socket;
	private BluetoothDevice btDevice;
	private InputStream mmInStream;
	private OutputStream mmOutStream;
	
	private final int DRAW_ECG_WAVE = 1000;
	private final int BT_CONNECT_FAILED = 997;
	private final int BT_CONNECTED = 996;
	private final int BT_SENDMSG_FAILED = 993;
	private final int BLUETOOTH_DETACHED = 991;
	public boolean isReceiveBTData = false;
	private static final int BUFSIZ = 15000;

//	private int oldX = 0;
	private int socketConnectCount = 0;
	
	
	public BTConnectStreamThread(BluetoothDevice device, Handler h, EcgDataParser24.EcgDataGetListener ecgDataGetListener) {
		ecgParser24 = new EcgDataParser24(ecgDataGetListener);
		ecgParser24.EcgDataParserInit();
		handler = h;
		btDevice = device;
	}
	
	/**
	 * 给蓝牙模块发指令
	 * @param f
	 */
	private void btCThreadwrite(byte[] f) {
		if (mmOutStream != null){
			try {
				mmOutStream.write(f);
			} catch (IOException e) {
				e.printStackTrace();
				handler.sendEmptyMessage(BT_SENDMSG_FAILED);
			}
		}else {
			handler.sendEmptyMessage(BT_SENDMSG_FAILED);
		}
	}
	
	public boolean btCThreadisConnected(){
		if (socket!=null) {
			return true;
		}else {
			return false;
		}
	}
	
	public void stopBlueTooth(){
		try {
			int count = 0;
			isReceiveBTData = false;
			btCThreadwrite(EcgDataParser24.PackEcgDeivceStop());
			Thread.sleep(4);
			boolean readState = true;
			while (readState) {
				if (mmInStream==null) {
					readState = false;
				}else {
					int available = mmInStream.available();
					byte[] cmds = new byte[available];
					int numBytesRead = mmInStream.read(cmds);
					if (numBytesRead<=0) {
						readState = false;
					}
					count ++;
					StringUtil.writException2File("/sdcard/stop.txt", new Date().toGMTString()+"  count = "+count+"\n");
					btCThreadwrite(EcgDataParser24.PackEcgDeivceStop());
					Thread.sleep(2);
					cmds = null;
				}
			}
			btCThreadstop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ͣ停止连接
	 */
	private void btCThreadstop() {
		ecgParser24.stopInit();
		closeSocket();
	}
	
	/**关闭蓝牙Socket**/
	private void closeSocket() {
		if (socket != null) {
			mmInStream = null;
			mmOutStream = null;
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		socket = null;
	}

	@Override
	public void run() {
		super.run();
		createSocket();
		boolean connectState = false;
		int connectCount = 0;
		while (!connectState) {
			connectState = createSocketConnect();
			if (!connectState) {
				connectCount ++;
			}
			if (connectCount==20) {
				connectState = true;
			}
		}
		if (connectCount==20) {
			Message msg = handler.obtainMessage(BT_CONNECT_FAILED);
			msg.obj = "设备蓝牙没有连接成功";
			handler.sendMessage(msg);
			return;
		}
		getIOStream();
		byte[] buffer = new byte[BUFSIZ];
		while (isReceiveBTData) {	
			try {
				if (null==mmInStream) {
					continue;
				}
//				int availabe = mmInStream.available();
				int length = mmInStream.read(buffer);
//				if (length>0) {
//					byte[] tmp = new byte[length];
//					for(int i = 0; i < length; i++){
//						tmp[i] = buffer[i];
//					}
				ecgParser24.EcgParserPacket(buffer, length);
//					oldX=0;
//				}else{
//					oldX++;
//					if (oldX==400) {
//						isReceiveBTData = false;
//						handler.sendEmptyMessage(BLUETOOTH_DETACHED);
//						String content = null;
//						try {
//							content = "currentX的值："+ECGMeasureActivity.measureActivity.currentX+" 缓存ecgDataBuffer的size："+ECGMeasureActivity.measureActivity.ecgDataBuffer.size();
//							String time = new Date().toString();
//							StringUtils.writException2File("/sdcard/bt_log.txt", "\n length=="+length+"，  "+content+" 时间："+time+" socket.connected="+socket.isConnected());
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						oldX=0;
//					}
//				}
				try {
					Thread.sleep(8);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
				StringUtil.writeException(e, TAG);
				createSocket();
				try {
					sleep(20);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				try {
					if (socketConnectCount==0) {
						sleep(300);
						boolean connectOnceState = createSocketConnect();
						if (connectOnceState) {
							socketConnectCount = 0;
						}else {
							socketConnectCount++;
						}
					}
					if (socketConnectCount==1) {
						sleep(500);
						boolean connectOnceState = createSocketConnect();
						if (connectOnceState) {
							socketConnectCount = 0;
						}else {
							socketConnectCount++;
						}
					}
					if (socketConnectCount==2) {
						sleep(1000);
						boolean connectOnceState = createSocketConnect();
						if (connectOnceState) {
							socketConnectCount = 0;
						}else {
							socketConnectCount++;
						}
					}
					if (socketConnectCount==3) {
						isReceiveBTData = false;
						handler.sendEmptyMessage(BLUETOOTH_DETACHED);
//						Message msg = handler.obtainMessage(BLUETOOTH_DETACHED);
//						msg.obj = "socket.connect";
//						handler.sendMessage(msg);
						return;
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				getOnceIOStream();
			}
		}
		Log.e(TAG, "while循环结束");
	}

	/**再次获取输入流**/
	private void getOnceIOStream() {
		try {
			mmInStream = socket.getInputStream();
			mmOutStream = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * 创建Socket连接
	 * @return true为连接成功，否则连接失败
	 */
	private boolean createSocketConnect() {
		boolean connectState = false;
		if (socket!=null && !socket.isConnected()) {
			try {
				socket.connect();
				Thread.sleep(50);
				connectState = true;
			} catch (IOException e) {
				e.printStackTrace();
//				Message msg = handler.obtainMessage(BT_CONNECT_FAILED);
//				msg.obj = "socket.connect";
//				handler.sendMessage(msg);
				connectState = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
//				Message msg = handler.obtainMessage(BT_CONNECT_FAILED);
//				msg.obj = "socket.connect  interrupt()";
//				handler.sendMessage(msg);
				connectState = false;
			}
		}
		return connectState;
	}

	/**
	 * 供初始化调用 获取socket的IOS流
	 */
	private void getIOStream() {
		try {
			mmInStream = socket.getInputStream();
			mmOutStream = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			if (socket!=null && mmInStream!=null) {
				handler.sendEmptyMessage(BT_CONNECTED);
				btCThreadwrite(EcgDataParser24.PackEcgDeivceInfoCmd());
				sleep(300);
				int available = mmInStream.available();
				byte[] cmds = new byte[available];
				int numBytesRead = mmInStream.read(cmds);
				System.out.println("设备控制信息数据的长度："+numBytesRead);
				
				if (ecgParser24.EcgParserCMDInfo(cmds)) {//
					handler.sendEmptyMessage(DRAW_ECG_WAVE);
					btCThreadwrite(EcgDataParser24.PackEcgDeivceStart());
					ecgParser24.setModle();
					sleep(50);
					if (!isReceiveBTData) {
						isReceiveBTData = true;
					}
					PreferUtils.getIntance().setECGDeviceBTMAC(btDevice.getAddress());
				}else {
					Message msg = handler.obtainMessage(BT_CONNECT_FAILED);
					msg.obj = "没有获取到设备信息，不能与设备通信";
					handler.sendMessage(msg);
					isReceiveBTData = false;
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = handler.obtainMessage(BT_CONNECT_FAILED);
			msg.obj = "通过输出流发命令";
			handler.sendMessage(msg);
			return;
		}
	}

	/**
	 * 创建Socket
	 */
	private void createSocket() {
		try {
			socket = btDevice.createRfcommSocketToServiceRecord(MY_UUID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
