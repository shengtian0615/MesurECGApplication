/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *
 */

package com.wehealth.mesurecg.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.utils.SampleDot;
import com.wehealth.mesurecg.view.LineIntReadView;
import com.wehealth.model.util.DataUtil;
import com.wehealth.model.util.ZipUtil;


public class ECGIntReadFragment extends Fragment implements OnClickListener, OnCheckedChangeListener{
	
	private RadioGroup ecgWGRadioGroup;
//	private RadioButton ecgWG_5, ecgWG_10, ecgWG_20;
	private Button ecgBtn1,ecgBtn2, ecgBtn3, ecgBtn4;
	private LineIntReadView line0,line1,line2;//,line3,line4,line5,line6,line7,line8,line9,line10,line11;
	private static final int DataMaxValue = 0xfff;
	private static final int DataBaseLine = 0;
	private int WaveGain = 30;
	private RelativeLayout backLayout;
	private LinearLayout flpfhpLayout;
	private boolean style;
	private int waveType = -1;
	
	private int wight;
	
	private static final int drawWaveNum = 12;
	private static final int ECG_SAMPLE_RATE = 500;
	private static final int DESTINATION_SAMPlE_RATE = 150;
	private SampleDot sampleDot[];
	
	private byte[] ecg_vf,ecg_vl,ecg_vr,ecg_v1,ecg_v2,ecg_v3,ecg_v4,ecg_v5,ecg_v6,ecg_vi,ecg_vii,ecg_viii;
	private int[] vfshort, vlshort,vrshort,v1short,v2short,v3short,v4short,v5short,v6short,vishort,viishort,viiishort; 
	
	private TextView name1, name2, name3, waveGainTV, flpTV, fhpTV;
	private ProgressDialog progressDialog;
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				line0.clearView();
				line1.clearView();
				line2.clearView();
				Bundle bundle = msg.getData();
				int type = bundle.getInt("type");
				if (type==1) {
					int[] vi = bundle.getIntArray("vi");
					int[] vii = bundle.getIntArray("vii");
					int[] viii = bundle.getIntArray("viii");
					line0.setLinePoint(vi);
					line1.setLinePoint(vii);
					line2.setLinePoint(viii);
					name1.setText("I");
					name2.setText("II");
					name3.setText("III");
				}else if (type==2) {
					int[] avf = bundle.getIntArray("avf");
					int[] avl = bundle.getIntArray("avl");
					int[] avr = bundle.getIntArray("avr");
					line0.setLinePoint(avr);
					line1.setLinePoint(avl);
					line2.setLinePoint(avf);
					name1.setText("aVR");
					name2.setText("aVL");
					name3.setText("aVF");
				}else if (type==3) {
					int[] v1 = bundle.getIntArray("v1");
					int[] v2 = bundle.getIntArray("v2");
					int[] v3 = bundle.getIntArray("v3");
					line0.setLinePoint(v1);
					line1.setLinePoint(v2);
					line2.setLinePoint(v3);
					name1.setText("V1");
					name2.setText("V2");
					name3.setText("V3");
				}else if (type==4) {
					int[] v4 = bundle.getIntArray("v4");
					int[] v5 = bundle.getIntArray("v5");
					int[] v6 = bundle.getIntArray("v6");
					line0.setLinePoint(v4);
					line1.setLinePoint(v5);
					line2.setLinePoint(v6);
					name1.setText("V4");
					name2.setText("V5");
					name3.setText("V6");
				}
				break;
			case 2:
				boolean s = msg.getData().getBoolean("style");
				initLineView(s, WaveGain);
				break;

			default:
				break;
			}
		}
	};


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.layout_ecgint_read, container, false);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        wight = wm.getDefaultDisplay().getWidth();
        Log.i("ECGAdapter", "屏幕宽度："+wight);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("正在加载...");
        backLayout = (RelativeLayout) getView().findViewById(R.id.ecg_read_bgl);
        name1 = (TextView) getView().findViewById(R.id.ecg_read_name1);
        name2 = (TextView) getView().findViewById(R.id.ecg_read_name2);
        name3 = (TextView) getView().findViewById(R.id.ecg_read_name3);
		line0 = (LineIntReadView) getView().findViewById(R.id.read_line0);
		line1 = (LineIntReadView) getView().findViewById(R.id.read_line1);
		line2 = (LineIntReadView) getView().findViewById(R.id.read_line2);
		ecgBtn1 = (Button) getView().findViewById(R.id.ecg_read_i3);
		ecgBtn2 = (Button) getView().findViewById(R.id.ecg_read_a3);
		ecgBtn3 = (Button) getView().findViewById(R.id.ecg_read_v1);
		ecgBtn4 = (Button) getView().findViewById(R.id.ecg_read_v4);
		waveGainTV = (TextView) getView().findViewById(R.id.ecg_read_wg);
		ecgWGRadioGroup = (RadioGroup) getView().findViewById(R.id.ecg_wgr);
		flpTV = (TextView) getView().findViewById(R.id.ecg_read_flp);
		fhpTV = (TextView) getView().findViewById(R.id.ecg_read_fhp);
		flpfhpLayout = (LinearLayout) getView().findViewById(R.id.ecg_read_fhpflp);

		ecgWGRadioGroup.setOnCheckedChangeListener(this);
		ecgBtn1.setOnClickListener(this);
		ecgBtn2.setOnClickListener(this);
		ecgBtn3.setOnClickListener(this);
		ecgBtn4.setOnClickListener(this);
		sampleDot = new SampleDot[12];
		for(int i = 0; i < drawWaveNum; i++){
			sampleDot[i] = new SampleDot(ECG_SAMPLE_RATE, DESTINATION_SAMPlE_RATE);
		}
		
		getEcgByte();
		
		Bundle parentBundle = getArguments();
		style = parentBundle.getBoolean("style", false);
		if (style) {
			backLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.white_color));
		}else {
			backLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.black_deep));
		}
		String fhp = parentBundle.getString("fhp");
		String flp = parentBundle.getString("flp");
		if (!TextUtils.isEmpty(fhp) && !TextUtils.isEmpty(flp)) {
			flpfhpLayout.setVisibility(View.VISIBLE);
			fhpTV.setText(fhp);
			flpTV.setText(flp);
		}
		
		Message msg = handler.obtainMessage(2);
		Bundle bud = msg.getData();
		bud.putBoolean("style", style);
		msg.setData(bud);
		handler.sendMessage(msg);
		waveType = 1;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				updateLines(1);
			}
		}).start();
	}
	
	private void getEcgByte() {
		Bundle parentBundle = getArguments();
		ecg_vf = parentBundle.getByteArray("avf");
		ecg_vl = parentBundle.getByteArray("avl");
		ecg_vr = parentBundle.getByteArray("avr");
		ecg_v1 = parentBundle.getByteArray("v1");
		ecg_v2 = parentBundle.getByteArray("v2");
		ecg_v3 = parentBundle.getByteArray("v3");
		ecg_v4 = parentBundle.getByteArray("v4");
		ecg_v5 = parentBundle.getByteArray("v5");
		ecg_v6 = parentBundle.getByteArray("v6");
		ecg_vi = parentBundle.getByteArray("vi");
		ecg_vii = parentBundle.getByteArray("vii");
		ecg_viii = parentBundle.getByteArray("viii");
		int[] avf = DataUtil.toIntArray(ZipUtil.unGZip(ecg_vf));
		vfshort = sampleReadEcgData(avf, 0);
		int[] avl = DataUtil.toIntArray(ZipUtil.unGZip(ecg_vl));
		vlshort = sampleReadEcgData(avl, 1);
		int[] avr = DataUtil.toIntArray(ZipUtil.unGZip(ecg_vr));
		vrshort = sampleReadEcgData(avr, 2);
		int[] v1 = DataUtil.toIntArray(ZipUtil.unGZip(ecg_v1));
		v1short = sampleReadEcgData(v1, 3);
		int[] v2 = DataUtil.toIntArray(ZipUtil.unGZip(ecg_v2));
		v2short = sampleReadEcgData(v2, 4);
		int[] v3 = DataUtil.toIntArray(ZipUtil.unGZip(ecg_v3));
		v3short = sampleReadEcgData(v3, 5);
		int[] v4 = DataUtil.toIntArray(ZipUtil.unGZip(ecg_v4));
		v4short = sampleReadEcgData(v4, 6);
		int[] v5 = DataUtil.toIntArray(ZipUtil.unGZip(ecg_v5));
		v5short = sampleReadEcgData(v5, 7);
		int[] v6 = DataUtil.toIntArray(ZipUtil.unGZip(ecg_v6));
		v6short = sampleReadEcgData(v6, 8);
		int[] vi = DataUtil.toIntArray(ZipUtil.unGZip(ecg_vi));
		vishort = sampleReadEcgData(vi, 9);
		int[] vii = DataUtil.toIntArray(ZipUtil.unGZip(ecg_vii));
		viishort = sampleReadEcgData(vii, 10);
		int[] viii = DataUtil.toIntArray(ZipUtil.unGZip(ecg_viii));
		viiishort = sampleReadEcgData(viii, 11);
	}

	protected void initLineView(boolean s, int wavegain) {
		line0.SetDrawMaxValue(DataMaxValue);
	    line0.SetBaseLine(DataBaseLine);
	    line0.SetWaveGain(wavegain);
	    line0.clearView();
	    line0.setStyle(s);
		
	    line1.SetDrawMaxValue(DataMaxValue);
	    line1.SetBaseLine(DataBaseLine);
	    line1.SetWaveGain(wavegain);
	    line1.clearView();
	    line1.setStyle(s);
		
	    line2.SetDrawMaxValue(DataMaxValue);
	    line2.SetBaseLine(DataBaseLine);
	    line2.SetWaveGain(wavegain);
	    line2.clearView();
	    line2.setStyle(s);
	}
	
	private int[] sampleReadEcgData(int[] srcData, int lead){
		int len = srcData.length;
		int[] src = new int[len];
		int[] outData   = new int[DESTINATION_SAMPlE_RATE * 10];

		int ret;
		for(int i = 0; i < len; i++){
			src[i] = srcData[i];
		}

		ret = sampleDot[lead].SnapshotSample(src, src.length, outData, DESTINATION_SAMPlE_RATE * 10);
		
		int[] data = new int[ret];
		
		for(int i = 0;i < ret;i++){
			data[i] = outData[i];
		}
		
		return data;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ecg_read_i3) {
			waveType = 1;
			progressDialog.show();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					updateLines(1);
					
				}
			}).start();
		}
		if (v.getId() == R.id.ecg_read_a3) {
			waveType = 2;
			progressDialog.show();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					updateLines(2);
				}
			}).start();
		}
		if (v.getId() == R.id.ecg_read_v1) {
			progressDialog.show();
			waveType = 3;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					updateLines(3);
				}
			}).start();
		}
		if (v.getId() == R.id.ecg_read_v4) {
			progressDialog.show();
			waveType = 4;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					updateLines(4);
				}
			}).start();
		}
	}

	protected void updateLines(int i) {
		Message msg = handler.obtainMessage(1);
		Bundle bundle = new Bundle();
		if (i==1) {
			bundle.putIntArray("vi", vishort);
			bundle.putIntArray("vii", viishort);
			bundle.putIntArray("viii", viiishort);
			bundle.putInt("type", i);
		}else if (i==2) {
			bundle.putIntArray("avf", vfshort);
			bundle.putIntArray("avl", vlshort);
			bundle.putIntArray("avr", vrshort);
			bundle.putInt("type", i);
		}else if (i==3) {
			bundle.putIntArray("v1", v1short);
			bundle.putIntArray("v2", v2short);
			bundle.putIntArray("v3", v3short);
			bundle.putInt("type", i);
		}else if (i==4) {
			bundle.putIntArray("v4", v4short);
			bundle.putIntArray("v5", v5short);
			bundle.putIntArray("v6", v6short);
			bundle.putInt("type", i);
		}
		msg.setData(bundle);
		handler.sendMessageDelayed(msg, 1000);
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId==R.id.ecg_wg5) {
			WaveGain = 15;
			initLineView(style, WaveGain);
			waveGainTV.setText("5mm/mv");
			progressDialog.show();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					updateLines(waveType);
				}
			}).start();
		}else if (checkedId==R.id.ecg_wg10) {
			WaveGain = 30;
			initLineView(style, WaveGain);
			waveGainTV.setText("10mm/mv");
			progressDialog.show();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					updateLines(waveType);
				}
			}).start();
		}else if (checkedId==R.id.ecg_wg20) {
			WaveGain = 60;
			initLineView(style, WaveGain);
			waveGainTV.setText("20mm/mv");
			progressDialog.show();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					updateLines(waveType);
				}
			}).start();
		}
		
	}
}
