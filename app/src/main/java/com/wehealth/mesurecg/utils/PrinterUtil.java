//package com.wehealth.mesurecg.utils;
//
//import android.bluetooth.BluetoothAdapter;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.hardware.usb.UsbDevice;
//import android.hardware.usb.UsbManager;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.util.Log;
//
//import com.wehealth.model.domain.model.ECGData;
//import com.wehealth.model.posprint.io.BTPrinting;
//import com.wehealth.model.posprint.io.Pos;
//import com.wehealth.model.posprint.io.USBPrinting;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class PrinterUtil {
//
//	private final int PRINTER_START = 1000;
////	private final int PRINTER_MESSAGE = 1001;
//	private final int PRINTER_FAILED = 1002;
//	private final int PRINTER_END = 1003;
//
//	private String address;
//	private UsbManager usbManager;
//	private UsbDevice usbDevice;
//
//	private Pos pos = null;
//	private BTPrinting mBt = null;
//	private USBPrinting usbpt = null;
//	private boolean isReady = false;
//
//	private Handler handler;
//	private int type = 0;
//	private ExecutorService es = Executors.newSingleThreadScheduledExecutor();
//
//	UsbDevice dev = null;
//
//	public PrinterUtil(BTPrinting bt, USBPrinting ust, Pos pos, Handler handle) {
//		if (ust==null && bt!=null) {
//			type = 1;
//			this.mBt = bt;
//		}else if (bt==null && ust!=null) {
//			type = 2;
//			this.usbpt = ust;
//		}
//		this.pos = pos;
//		this.handler = handle;
//	}
//
//	public String getAddress() {
//		return address;
//	}
//
//	public void setAddress(String address) {
//		this.address = address;
//		if(address != null) {
//			isReady = true;
//		}
//	}
//
//	public void discovery() {
//		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
//		if (null == adapter) {
//			return;
//		}
//
//		if (!adapter.isEnabled()) {
//			if (adapter.enable()) {
//				while (!adapter.isEnabled())
//					;
//				Log.i("PrinterUtil", "打开蓝牙");
//			} else {
//				return;
//			}
//		}
//		adapter.startDiscovery();
//	}
//
//
//	private int printImg(Context context, Bitmap bitmap) {
//		int printResult =0;
//		promtMsg(context, "开始打印(如果是首次打印，准备时间较长，请耐心等待)。。。。");
//		pos.printImage(bitmap);
//		return printResult;
//	}
//
//	public boolean open(Context context) {
//		if (type==1) {
//			return mBt.Open(address, context);
//		}else {
//			return usbpt.Open(usbManager, usbDevice, context);
//		}
//	}
//
//	public void close() {
//		if (type==1) {
//			mBt.Close();
//		}else {
//			usbpt.Close();
//		}
//	}
//
//	public boolean isReady() {
//		return isReady;
//	}
//
//	public void setReady(boolean isReady) {
//		this.isReady = isReady;
//	}
//
//	public void setUsbInfo(UsbManager mUsbManager, UsbDevice device) {
//		this.usbManager = mUsbManager;
//		this.usbDevice = device;
//	}
//
//	public void startToPrint(ECGData data, Context ctx, int gain) {
//		if (data == null)
//			return;
//		handler.sendEmptyMessageDelayed(PRINTER_START, 300);
//		es.submit(new TaskPrint(this, data, ctx, gain));
//	}
//
//
//	private void promtMsg(final Context context, final String msg) {
//		Handler mainHandler = new Handler(Looper.getMainLooper());
//	    mainHandler.post(new Runnable() {
//	        @Override
//	        public void run() {
//	           ToastUtil.showShort(context, msg);
//	        }
//	    });
//	}
//
//	public static String ResultCodeToString(int code) {
//		switch (code) {
//		case 0:
//			return "打印成功";
//		case -1:
//			return "连接断开";
//		case -2:
//			return "写入失败";
//		case -3:
//			return "读取失败";
//		case -4:
//			return "打印机脱机";
//		case -5:
//			return "打印机缺纸";
//		case -7:
//			return "实时状态查询失败";
//		case -8:
//			return "查询状态失败";
//		case -6:
//		default:
//			return "未知错误";
//		}
//	}
//
//	public class TaskPrint implements Runnable {
//		PrinterUtil printer = null;
//		Context context = null;
//		private int bPrintResult;
//		private ECGData data;
//		private int Gain;
//
//		public TaskPrint(PrinterUtil printer, ECGData data, Context context, int gain) {
//			this.printer = printer;
//			this.context = context;
//			this.data = data;
//			this.Gain = gain;
//		}
//
//		@Override
//		public void run() {
//			if (type==1) {
//				if (!isReady()) {
//					discovery();
//				}
//
//				for(int i=0; i < 50; i++) {
//					if(isReady()) break;
//					try {
//						Thread.sleep(100);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				if(!isReady()) {
//					Message msg = handler.obtainMessage(PRINTER_FAILED, "没发现打印机， 请确保打印机电源打开");
//					handler.sendMessage(msg);
////					promtMsg(context, "没发现打印机， 请确保打印机电源打开。。。。。。。");
//					return;
//				}
//			}
//			boolean stat = printer.open(context);
//			if (!stat){
//				Message msg = handler.obtainMessage(PRINTER_FAILED, "连接打印机失败，请检查设备蓝牙是否开启或者USB线是否连接");
//				handler.sendMessage(msg);
//				return;
//			}
//			if (data == null) {
//				printer.close();
//				return;
//			}
//			promtMsg(context, "准备生成图片。。。。。。。");
//			ECGIMGUtil ecgimgUtil = new ECGIMGUtil();
//			Bitmap bitmap = ecgimgUtil.createBitMap(context, data, Gain);
//			bPrintResult = 0;
//			if (bitmap != null) {
//				//PrintTicket(context);
//				promtMsg(context, "图片已生成。。。。。。。");
////					byte[] nongdu = new byte[]{0x1B, 0x23, 0x23, 0x53, 0x54, 0x44, 0x50, 0x27};
////					int s = usbpt.Write(nongdu, 0, nongdu.length);
////					StringUtil.writException2File("/sdcard/usbs.txt", "纸速："+s);
//				bPrintResult = printImg(context, bitmap);
//			}
//			else {
//				promtMsg(context, "图片生成失败。。。。。。。");
//			}
//			printer.close();
//
////			promtMsg(context, (bPrintResult == 0) ? "打印成功。。。"
////					: ResultCodeToString(bPrintResult));
//
//			if (bPrintResult == 0) {
//				handler.sendEmptyMessage(PRINTER_END);
//			}else {
//				Message msg = handler.obtainMessage(PRINTER_FAILED, ResultCodeToString(bPrintResult));
//				handler.sendMessage(msg);
//			}
//		}
//	}
//}
