package com.wehealth.urion;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.wehealth.mesurecg.utils.PreferUtils;

public class BluetoothService {

	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	/*
	 * private static final UUID MY_UUID = UUID
	 * .fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
	 */
	private BluetoothSocket socket;
	private BluetoothDevice device;
	private InputStream mmInStream;
	private OutputStream mmOutStream;
	private Handler mHandler;
	private ConnectedThread mConnectedThread;
	private ConnectSendThread connectSendThread;
	private int mState;

	public BluetoothService() {
		mState = Msg.MESSAGE_STATE_NONE;
	}

	public int getmState() {
		return mState;
	}

	public void setHandler(Handler handler) {
		mHandler = handler;
	}

	public void setDevice(BluetoothDevice device) {
		this.device = device;
	}

	/**
	 * 发送消息到前台
	 * 
	 * @param code
	 * @param bean
	 */
	private void send(int code, IBean bean) {
		if (mHandler != null) {
			System.out.println("发送消息到前台");
			Message msg = mHandler.obtainMessage(code);
			Bundle bundle = new Bundle();
			bundle.putParcelable("bean", bean);
			msg.setData(bundle);
			msg.sendToTarget();
		}
	}

	/**
	 * 连接方法
	 */
	public void connect() {
		if (device != null) {
			try {
				socket = device.createRfcommSocketToServiceRecord(MY_UUID);
				socket.connect();
				mState = Msg.MESSAGE_STATE_CONNECTING;
				send(IBean.MESSAGE, new Msg(mState, device.getName()));
				connected();
			} catch (IOException e) {
				e.printStackTrace();
				connectionFailed();
			}
		}
	}

	/**
	 * 发送数据
	 * 
	 * @param msg
	 */
	public void write(byte[] msg) {
		if (mmOutStream != null)
			try {
				mmOutStream.write(msg);
			} catch (IOException e) {
				e.printStackTrace();
				sendBlueToothInstruct();
			}
	}

	/**
	 * 连接成功后启动接收数据的线程
	 */
	private void connected() {
		if (socket != null) {
			mConnectedThread = new ConnectedThread();
			mConnectedThread.start();
			mState = Msg.MESSAGE_STATE_CONNECTED;
			send(IBean.MESSAGE, new Msg(mState, device.getName()));
		}
	}

	/**
	 * 停止
	 */
	public void stop() {
		try {
			if (socket != null) {
				socket.close();
				mState = Msg.MESSAGE_STATE_NONE;
				send(IBean.MESSAGE, new Msg(mState, device.getName()));
			}

			if (mConnectedThread != null) {
				mConnectedThread.interrupt();
		 	 //   mConnectedThread.destroy();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void connectionFailed() {
		send(IBean.ERROR, new Error(Error.ERROR_CONNECTION_FAILED));
	}

	private void connectionLost() {
		send(IBean.ERROR, new Error(Error.ERROR_CONNECTION_LOST));
	}
	
	private void sendBlueToothInstruct(){
		send(IBean.ERROR, new Error(Error.ERROR_SEND_INSTRUCT_FAILED));
	}

	private class ConnectedThread extends Thread {
		public ConnectedThread() {
			try {
				mmInStream = socket.getInputStream();
				mmOutStream = socket.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			byte[] buffer = new byte[16];
			while (true) {
				try {
					if (mmInStream.available() > 0) {// 如果流中有数据就进行解析
						Head head = new Head();
						mmInStream.read(buffer);
						int[] f = CodeFormat.bytesToHexStringTwo(buffer, 6);
						head.analysis(f);
						if (head.getType() == Head.TYPE_ERROR) {
							// APP接收到血压仪的错误信息
							Error error = new Error();
							error.analysis(f);
							error.setHead(head);
							// 前台根据错误编码显示相应的提示
							send(IBean.ERROR, error);
						}
						if (head.getType() == Head.TYPE_RESULT) {
							// APP接收到血压仪的测量结果
							Data data = new Data();
							data.analysis(f);
							data.setHead(head);
							// 前台根据测试结果来画线性图
							send(IBean.DATA, data);
						}

						if (head.getType() == Head.TYPE_MESSAGE) {
							// APP接收到血压仪开始测量的通知
							Msg msg = new Msg();
							msg.analysis(f);
							msg.setHead(head);
							send(IBean.MESSAGE, msg);
						}
						if (head.getType() == Head.TYPE_PRESSURE) {
							// APP接受到血压仪测量的压力数据
							Pressure pressure = new Pressure();
							pressure.analysis(f);
							pressure.setHead(head);
							// 每接收到一条数据就发送到前台，以改变进度条的显示
							send(IBean.DATA, pressure);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					connectionLost();
					interrupt();
					break;
				}
			}
		}
	}
	
	public void connectSend(){
		connectSendThread = new ConnectSendThread();
		connectSendThread.start();
	}
	
	private class ConnectSendThread extends Thread {

		private byte[] sendMsg = { (-3), (-3), -6, 5, 13, 10 };
		public ConnectSendThread(){
			if (device!=null) {
				try {
					socket = device.createRfcommSocketToServiceRecord(MY_UUID);
					mState = Msg.MESSAGE_STATE_CONNECTING;
					send(IBean.MESSAGE, new Msg(mState, device.getName()));
				} catch (IOException e) {
					e.printStackTrace();
					connectionLost();
					interrupt();
				}
			}
		}
		@Override
		public void run() {
//			if (device!=null && paried) {
//				ClsUtils.pair(device.getAddress(), "1234");
//			}
			if (socket!=null && !socket.isConnected()) {
				try {
					socket.connect();
				} catch (IOException e) {
					e.printStackTrace();
					connectionFailed();
					interrupt();
				}
			}
			
			try {
				mmInStream = socket.getInputStream();
				mmOutStream = socket.getOutputStream();
			} catch (IOException e1) {
				e1.printStackTrace();
				connectionFailed();
			}
			
			mState = Msg.MESSAGE_STATE_CONNECTED;
			send(IBean.MESSAGE, new Msg(mState, device.getName()));
			
			write(sendMsg);
			
			PreferUtils.getIntance().setBPBlueToothMac(device.getAddress());
			
			byte[] buffer = new byte[16];
			while (true) {
				try {
					if (mmInStream.available() > 0) {// 如果流中有数据就进行解析
						Head head = new Head();
						mmInStream.read(buffer);
						int[] f = CodeFormat.bytesToHexStringTwo(buffer, 6);
						head.analysis(f);
						if (head.getType() == Head.TYPE_ERROR) {
							// APP接收到血压仪的错误信息
							Error error = new Error();
							error.analysis(f);
							error.setHead(head);
							// 前台根据错误编码显示相应的提示
							send(IBean.ERROR, error);
						}
						if (head.getType() == Head.TYPE_RESULT) {
							// APP接收到血压仪的测量结果
							Data data = new Data();
							data.analysis(f);
							data.setHead(head);
							// 前台根据测试结果来画线性图
							send(IBean.DATA, data);
						}

						if (head.getType() == Head.TYPE_MESSAGE) {
							// APP接收到血压仪开始测量的通知
							Msg msg = new Msg();
							msg.analysis(f);
							msg.setHead(head);
							send(IBean.MESSAGE, msg);
						}
						if (head.getType() == Head.TYPE_PRESSURE) {
							// APP接受到血压仪测量的压力数据
							Pressure pressure = new Pressure();
							pressure.analysis(f);
							pressure.setHead(head);
							// 每接收到一条数据就发送到前台，以改变进度条的显示
							send(IBean.DATA, pressure);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					connectionLost();
					interrupt();
					break;
				}
			}
		}
	}
}