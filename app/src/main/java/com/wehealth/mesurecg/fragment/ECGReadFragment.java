/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Dec 26, 2014
 */

package com.wehealth.mesurecg.fragment;

import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.utils.SampleDot;
import com.wehealth.mesurecg.view.LineReadView;
import com.wehealth.model.util.DataUtil;
import com.wehealth.model.util.ZipUtil;

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


public class ECGReadFragment extends Fragment implements OnClickListener, OnCheckedChangeListener{
	
	private RadioGroup ecgWGRadioGroup;
//	private RadioButton ecgWG_5, ecgWG_10, ecgWG_20;
	private Button ecgBtn1,ecgBtn2, ecgBtn3, ecgBtn4;
	private LineReadView line0,line1,line2;//,line3,line4,line5,line6,line7,line8,line9,line10,line11;
	private static final int DataMaxValue = 0xfff;
	private static final int DataBaseLine = 0;
	private int WaveGain = 85;
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
	private short[] vfshort, vlshort,vrshort,v1short,v2short,v3short,v4short,v5short,v6short,vishort,viishort,viiishort; 
	
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
					short[] vi = bundle.getShortArray("vi");
					short[] vii = bundle.getShortArray("vii");
					short[] viii = bundle.getShortArray("viii");
					line0.setLinePoint(vi);
					line1.setLinePoint(vii);
					line2.setLinePoint(viii);
					name1.setText("I");
					name2.setText("II");
					name3.setText("III");
				}else if (type==2) {
					short[] avf = bundle.getShortArray("avf");
					short[] avl = bundle.getShortArray("avl");
					short[] avr = bundle.getShortArray("avr");
					line0.setLinePoint(avr);
					line1.setLinePoint(avl);
					line2.setLinePoint(avf);
					name1.setText("aVR");
					name2.setText("aVL");
					name3.setText("aVF");
				}else if (type==3) {
					short[] v1 = bundle.getShortArray("v1");
					short[] v2 = bundle.getShortArray("v2");
					short[] v3 = bundle.getShortArray("v3");
					line0.setLinePoint(v1);
					line1.setLinePoint(v2);
					line2.setLinePoint(v3);
					name1.setText("V1");
					name2.setText("V2");
					name3.setText("V3");
				}else if (type==4) {
					short[] v4 = bundle.getShortArray("v4");
					short[] v5 = bundle.getShortArray("v5");
					short[] v6 = bundle.getShortArray("v6");
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
//	//获取Fragment依附的activity  
//    @Override  
//    public void onAttach(Activity activity) {  
//        super.onAttach(activity);  
//        this.callback= (Handler.Callback) activity;  
//    }  
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.layout_ecg_read, container, false);
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
		line0 = (LineReadView) getView().findViewById(R.id.read_line0);
		line1 = (LineReadView) getView().findViewById(R.id.read_line1);
		line2 = (LineReadView) getView().findViewById(R.id.read_line2);
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
//		if (parentBundle == null) {
//			Intent intent = getActivity().getIntent();
//			ecg_vf = intent.getByteArrayExtra("avf");
//			ecg_vl = intent.getByteArrayExtra("avl");
//			ecg_vr = intent.getByteArrayExtra("avr");
//			ecg_v1 = intent.getByteArrayExtra("v1");
//			ecg_v2 = intent.getByteArrayExtra("v2");
//			ecg_v3 = intent.getByteArrayExtra("v3");
//			ecg_v4 = intent.getByteArrayExtra("v4");
//			ecg_v5 = intent.getByteArrayExtra("v5");
//			ecg_v6 = intent.getByteArrayExtra("v6");
//			ecg_vi = intent.getByteArrayExtra("vi");
//			ecg_vii = intent.getByteArrayExtra("vii");
//			ecg_viii = intent.getByteArrayExtra("viii");
//			fhp = intent.getStringExtra("fhp");
//			flp = intent.getStringExtra("flp");
//			if (!TextUtils.isEmpty(fhp) && TextUtils.isEmpty(flp)) {
//				flpfhpLayout.setVisibility(View.VISIBLE);
//				fhpTV.setText(fhp);
//				flpTV.setText(flp);
//			}
//		}else {
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
//		}
		short[] avf = DataUtil.toShortArray(ZipUtil.unGZip(ecg_vf));
		vfshort = sampleReadEcgData(avf, 0);
		short[] avl = DataUtil.toShortArray(ZipUtil.unGZip(ecg_vl));
		vlshort = sampleReadEcgData(avl, 1);
		short[] avr = DataUtil.toShortArray(ZipUtil.unGZip(ecg_vr));
		vrshort = sampleReadEcgData(avr, 2);
		short[] v1 = DataUtil.toShortArray(ZipUtil.unGZip(ecg_v1));
		v1short = sampleReadEcgData(v1, 3);
		short[] v2 = DataUtil.toShortArray(ZipUtil.unGZip(ecg_v2));
		v2short = sampleReadEcgData(v2, 4);
		short[] v3 = DataUtil.toShortArray(ZipUtil.unGZip(ecg_v3));
		v3short = sampleReadEcgData(v3, 5);
		short[] v4 = DataUtil.toShortArray(ZipUtil.unGZip(ecg_v4));
		v4short = sampleReadEcgData(v4, 6);
		short[] v5 = DataUtil.toShortArray(ZipUtil.unGZip(ecg_v5));
		v5short = sampleReadEcgData(v5, 7);
		short[] v6 = DataUtil.toShortArray(ZipUtil.unGZip(ecg_v6));
		v6short = sampleReadEcgData(v6, 8);
		short[] vi = DataUtil.toShortArray(ZipUtil.unGZip(ecg_vi));
		vishort = sampleReadEcgData(vi, 9);
		short[] vii = DataUtil.toShortArray(ZipUtil.unGZip(ecg_vii));
		viishort = sampleReadEcgData(vii, 10);
		short[] viii = DataUtil.toShortArray(ZipUtil.unGZip(ecg_viii));
		viiishort = sampleReadEcgData(viii, 11);
	}

	protected void initLineView(boolean s, int wavegain) {
		line0.SetDrawMaxValue(DataMaxValue);
	    line0.SetBaseLine(DataBaseLine);
	    line0.SetWaveGain(wavegain);
	    line0.clearView();
	    line0.setStyle(s);
//	    line0.setWight(wight);
//	    line0.SetLineViewLeadName("I");
		
	    line1.SetDrawMaxValue(DataMaxValue);
	    line1.SetBaseLine(DataBaseLine);
	    line1.SetWaveGain(wavegain);
	    line1.clearView();
	    line1.setStyle(s);
//	    line1.setWight(wight);
//	    line1.SetLineViewLeadName("II");
		
	    line2.SetDrawMaxValue(DataMaxValue);
	    line2.SetBaseLine(DataBaseLine);
	    line2.SetWaveGain(wavegain);
	    line2.clearView();
	    line2.setStyle(s);
//	    line2.setWight(wight);
//	    line2.SetLineViewLeadName("III");
		/**
	    line3.SetDrawMaxValue(DataMaxValue);
	    line3.SetBaseLine(DataBaseLine);
	    line3.SetWaveGain(WaveGain);
	    line3.clearView();
	    line3.setStyle(s);
//	    line3.setWight(wight);
//	    line3.SetLineViewLeadName("aVR");
		
	    line4.SetDrawMaxValue(DataMaxValue);
	    line4.SetBaseLine(DataBaseLine);
	    line4.SetWaveGain(WaveGain);
	    line4.clearView();
	    line4.setStyle(s);
//	    line4.setWight(wight);
//	    line4.SetLineViewLeadName("aVL");
		
	    line5.SetDrawMaxValue(DataMaxValue);
	    line5.SetBaseLine(DataBaseLine);
	    line5.SetWaveGain(WaveGain);
	    line5.clearView();
	    line5.setStyle(s);
//	    line5.setWight(wight);
//	    line5.SetLineViewLeadName("aVF");
	    
	    line6.SetDrawMaxValue(DataMaxValue);
	    line6.SetBaseLine(DataBaseLine);
	    line6.SetWaveGain(WaveGain);
	    line6.clearView();
	    line6.setStyle(s);
//	    line6.setWight(wight);
//	    line6.SetLineViewLeadName("V1");
		
	    line7.SetDrawMaxValue(DataMaxValue);
	    line7.SetBaseLine(DataBaseLine);
	    line7.SetWaveGain(WaveGain);
	    line7.clearView();
	    line7.setStyle(s);
//	    line7.setWight(wight);
//	    line7.SetLineViewLeadName("V2");
		
	    line8.SetDrawMaxValue(DataMaxValue);
	    line8.SetBaseLine(DataBaseLine);
	    line8.SetWaveGain(WaveGain);
	    line8.clearView();
	    line8.setStyle(s);
//	    line8.setWight(wight);
//	    line8.SetLineViewLeadName("V3");
	    
	    line9.SetDrawMaxValue(DataMaxValue);
	    line9.SetBaseLine(DataBaseLine);
	    line9.SetWaveGain(WaveGain);
	    line9.clearView();
	    line9.setStyle(s);
//	    line9.setWight(wight);
//	    line9.SetLineViewLeadName("V4");
		
	    line10.SetDrawMaxValue(DataMaxValue);
	    line10.SetBaseLine(DataBaseLine);
	    line10.SetWaveGain(WaveGain);
	    line10.clearView();
	    line10.setStyle(s);
//	    line10.setWight(wight);
//	    line10.SetLineViewLeadName("V5");
		
	    line11.SetDrawMaxValue(DataMaxValue);
	    line11.SetBaseLine(DataBaseLine);
	    line11.SetWaveGain(WaveGain);
	    line11.clearView();
	    line11.setStyle(s);
//	    line11.setWight(wight);
//	    line11.SetLineViewLeadName("V6");*/
	}
	
	private short[] sampleReadEcgData(short[] srcData, int lead){
		int len = srcData.length;
		int[] src = new int[len];
		int[] outData   = new int[DESTINATION_SAMPlE_RATE * 10];

		int ret;
		for(int i = 0; i < len; i++){
			src[i] = srcData[i];
		}

		ret = sampleDot[lead].SnapshotSample(src, src.length, outData, DESTINATION_SAMPlE_RATE * 10);
		
		short[] data = new short[ret];
		
		for(int i = 0;i < ret;i++){
			data[i] = (short) outData[i];
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
			bundle.putShortArray("vi", vishort);
			bundle.putShortArray("vii", viishort);
			bundle.putShortArray("viii", viiishort);
			bundle.putInt("type", i);
		}else if (i==2) {
			bundle.putShortArray("avf", vfshort);
			bundle.putShortArray("avl", vlshort);
			bundle.putShortArray("avr", vrshort);
			bundle.putInt("type", i);
		}else if (i==3) {
			bundle.putShortArray("v1", v1short);
			bundle.putShortArray("v2", v2short);
			bundle.putShortArray("v3", v3short);
			bundle.putInt("type", i);
		}else if (i==4) {
			bundle.putShortArray("v4", v4short);
			bundle.putShortArray("v5", v5short);
			bundle.putShortArray("v6", v6short);
			bundle.putInt("type", i);
		}
		msg.setData(bundle);
		handler.sendMessageDelayed(msg, 1000);
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
//		Handler handler = new Handler(callback);  
//		Message message = Message.obtain();  
		if (checkedId==R.id.ecg_wg5) {
			WaveGain = 85;
//            message.what = 5;  
//            handler.sendMessage(message);  
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
			WaveGain = 170;
//			message.what = 10;  
//            handler.sendMessage(message);  
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
			WaveGain = 340;
//			message.what = 20;  
//            handler.sendMessage(message);  
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
	/**
	private void updatePlot(int i) {
		Bundle parentBundle = getArguments();
		if (parentBundle == null) {
			Intent intent = getActivity().getIntent();
			short[] avf = DataUtil.toShortArray(ZipUtil.unGZip(intent.getByteArrayExtra("avf")));
			short[] avl = DataUtil.toShortArray(ZipUtil.unGZip(intent.getByteArrayExtra("avl")));
			short[] avr = DataUtil.toShortArray(ZipUtil.unGZip(intent.getByteArrayExtra("avr")));
			short[] v1 = DataUtil.toShortArray(ZipUtil.unGZip(intent.getByteArrayExtra("v1")));
			short[] v2 = DataUtil.toShortArray(ZipUtil.unGZip(intent.getByteArrayExtra("v2")));
			short[] v3 = DataUtil.toShortArray(ZipUtil.unGZip(intent.getByteArrayExtra("v3")));
			short[] v4 = DataUtil.toShortArray(ZipUtil.unGZip(intent.getByteArrayExtra("v4")));
			short[] v5 = DataUtil.toShortArray(ZipUtil.unGZip(intent.getByteArrayExtra("v5")));
			short[] v6 = DataUtil.toShortArray(ZipUtil.unGZip(intent.getByteArrayExtra("v6")));
			short[] vi = DataUtil.toShortArray(ZipUtil.unGZip(intent.getByteArrayExtra("vi")));
			short[] vii = DataUtil.toShortArray(ZipUtil.unGZip(intent.getByteArrayExtra("vii")));
			short[] viii = DataUtil.toShortArray(ZipUtil.unGZip(intent.getByteArrayExtra("viii")));
			
			Message msg = handler.obtainMessage(1);
			Bundle bundle = new Bundle();
			bundle.putShortArray("avf", sampleReadEcgData(avf, 0));
			bundle.putShortArray("avl", sampleReadEcgData(avl, 1));
			bundle.putShortArray("avr", sampleReadEcgData(avr, 2));
			bundle.putShortArray("v1", sampleReadEcgData(v1, 3));
			bundle.putShortArray("v2", sampleReadEcgData(v2, 4));
			bundle.putShortArray("v3", sampleReadEcgData(v3, 5));
			bundle.putShortArray("v4", sampleReadEcgData(v4, 6));
			bundle.putShortArray("v5", sampleReadEcgData(v5, 7));
			bundle.putShortArray("v6", sampleReadEcgData(v6, 8));
			bundle.putShortArray("vi", sampleReadEcgData(vi, 9));
			bundle.putShortArray("vii", sampleReadEcgData(vii, 10));
			bundle.putShortArray("viii", sampleReadEcgData(viii, 11));
			msg.setData(bundle);
			handler.sendMessage(msg);
		}else {
			short[] avf = DataUtil.toShortArray(ZipUtil.unGZip(parentBundle.getByteArray("avf")));
			short[] avl = DataUtil.toShortArray(ZipUtil.unGZip(parentBundle.getByteArray("avl"))) ;
			short[] avr = DataUtil.toShortArray(ZipUtil.unGZip(parentBundle.getByteArray("avr"))) ;
			short[] v1 = DataUtil.toShortArray(ZipUtil.unGZip(parentBundle.getByteArray("v1"))) ;
			short[] v2 = DataUtil.toShortArray(ZipUtil.unGZip(parentBundle.getByteArray("v2"))) ;
			short[] v3 = DataUtil.toShortArray(ZipUtil.unGZip(parentBundle.getByteArray("v3"))) ;
			short[] v4 = DataUtil.toShortArray(ZipUtil.unGZip(parentBundle.getByteArray("v4"))) ;
			short[] v5 = DataUtil.toShortArray(ZipUtil.unGZip(parentBundle.getByteArray("v5"))) ;
			short[] v6 = DataUtil.toShortArray(ZipUtil.unGZip(parentBundle.getByteArray("v6"))) ;
			short[] vi = DataUtil.toShortArray(ZipUtil.unGZip(parentBundle.getByteArray("vi"))) ;
			short[] vii = DataUtil.toShortArray(ZipUtil.unGZip(parentBundle.getByteArray("vii"))) ;
			short[] viii = DataUtil.toShortArray(ZipUtil.unGZip(parentBundle.getByteArray("viii"))) ;
			Message msg = handler.obtainMessage(1);
			Bundle bundle = new Bundle();
			bundle.putShortArray("avf", sampleReadEcgData(avf, 0));
			bundle.putShortArray("avl", sampleReadEcgData(avl, 1));
			bundle.putShortArray("avr", sampleReadEcgData(avr, 2));
			bundle.putShortArray("v1", sampleReadEcgData(v1, 3));
			bundle.putShortArray("v2", sampleReadEcgData(v2, 4));
			bundle.putShortArray("v3", sampleReadEcgData(v3, 5));
			bundle.putShortArray("v4", sampleReadEcgData(v4, 6));
			bundle.putShortArray("v5", sampleReadEcgData(v5, 7));
			bundle.putShortArray("v6", sampleReadEcgData(v6, 8));
			bundle.putShortArray("vi", sampleReadEcgData(vi, 9));
			bundle.putShortArray("vii", sampleReadEcgData(vii, 10));
			bundle.putShortArray("viii", sampleReadEcgData(viii, 11));
			msg.setData(bundle);
			handler.sendMessage(msg);
		}
	}**/

}
