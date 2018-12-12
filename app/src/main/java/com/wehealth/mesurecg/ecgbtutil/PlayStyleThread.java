package com.wehealth.mesurecg.ecgbtutil;//package com.wehealth.ecgequipment.btservice;
//
//
//
//import android.os.Handler;
//import android.util.Log;
//
//import com.wehealth.ecgequipment.btservice.EcgDataParser24.EcgDataGetListener;
//import com.wehealth.ecgequipment.util.EcgDatas;
//import com.wehealth.ecgequipment.util.EcgDatas2;
//
//public class PlayStyleThread extends Thread {
//	private final int DRAW_ECG_WAVE = 1000;
////	private final int DRAWBACKGROUND = 999;
//	private final int BT_CONNECTED = 996;
//	EcgDataParser24 ecgParser24;
//	Handler handler;
//	byte[][] data = new byte[375][];
//	private static boolean RUN_FLAG = false;
//	
//	public PlayStyleThread(Handler hd, EcgDataGetListener ecgDataListener) {
//		handler = hd;
//		ecgParser24 = new EcgDataParser24(ecgDataListener);
//		ecgParser24.EcgDataParserInit();
//		RUN_FLAG = true;
//	}
//	@Override
//	public void run() {
//		super.run();
//		try {
//			for (int i = 0; i < EcgDatas.data.length; i++) {
//				data[i] = EcgDatas.data[i];
//			}
//			int j = EcgDatas.data.length;
//			for (int i = j; i < j+EcgDatas2.data.length; i++) {
//				data[i] = EcgDatas2.data[i-j];
//			}
//			handler.sendEmptyMessage(BT_CONNECTED);
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			
////			handler.sendEmptyMessage(DRAWBACKGROUND);
//			try {
//				Thread.sleep(200);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			handler.sendEmptyMessage(DRAW_ECG_WAVE);
//			ecgParser24.setModle();
//			int k = 0;
//			while (RUN_FLAG){
//				byte[] tmp;
//
//				
//					tmp = data[k++];
//					ecgParser24.EcgParserPacket(tmp, tmp.length);
//					try {
//						Thread.sleep(2);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				
//				
////				Log.e("TestPlay",  "k " + k + " data " + data.length );
//				if (k>=data.length) {
//					k = 0;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void stopPlayStyle(){
//		try {
//			ecgParser24.stopInit();
//			Thread.sleep(10);
//			interrupt();
//			RUN_FLAG = false;
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
//	
//}
