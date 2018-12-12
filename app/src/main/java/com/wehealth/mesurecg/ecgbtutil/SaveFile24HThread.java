package com.wehealth.mesurecg.ecgbtutil;

import java.util.LinkedList;

import android.os.Handler;

import com.wehealth.mesurecg.utils.ByteFileUtil;

public class SaveFile24HThread extends Thread {
	
	private final int SAVE_PDFXML_FILE = 897;
	public boolean saveFileThreadFLAG;
	private Handler handler;
	private long ecg2Device_time;
	public LinkedList<Integer[]> queue = new LinkedList<Integer[]>();
	public SaveFile24HThread(Handler mHandler, long ecg2DeviceData_time){
		saveFileThreadFLAG = true;
		ecg2Device_time = ecg2DeviceData_time;
		handler = mHandler;
	}
	
	public void addToQueue(Integer[] dataInts){
		synchronized (queue) {
			queue.add(dataInts);
		}
	}
	
	@Override
	public void run() {
		super.run();
		try {
			while(saveFileThreadFLAG){
				if (queue.isEmpty()) {
					sleep(5*1000);
				}else {
					synchronized (queue) {
						Integer[] lists = queue.removeFirst();
						ByteFileUtil.save24HInts(ecg2Device_time, lists);
						handler.sendEmptyMessageDelayed(SAVE_PDFXML_FILE, 100);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
