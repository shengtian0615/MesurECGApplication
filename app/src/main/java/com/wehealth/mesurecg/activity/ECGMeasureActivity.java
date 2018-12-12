package com.wehealth.mesurecg.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wehealth.ecg.jni.analyse.EcgAnalyse;
import com.wehealth.ecg.jni.filter.EcgFilter;
import com.wehealth.ecg.jni.heartrate.EcgHRDetect;
import com.wehealth.mesurecg.BaseActivity;
import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.dao.ECGDataLong2DeviceDao;
import com.wehealth.mesurecg.ecgbtutil.BTConnectStreamThread;
import com.wehealth.mesurecg.ecgbtutil.EcgDataParser24;
import com.wehealth.mesurecg.ecgbtutil.ExecutorThreadUtils;
import com.wehealth.mesurecg.ecgbtutil.SaveFile24HThread;
import com.wehealth.mesurecg.ecgbtutil.UsbSerialThread;
import com.wehealth.mesurecg.usbutil.USB_Operation;
import com.wehealth.mesurecg.usbutil.UsbSerialDriver;
import com.wehealth.mesurecg.usbutil.UsbSerialPort;
import com.wehealth.mesurecg.usbutil.UsbSerialProber;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.mesurecg.utils.ToastUtil;
import com.wehealth.model.util.SampleDotIntNew;
import com.wehealth.model.util.StringUtil;

public class ECGMeasureActivity extends BaseActivity implements OnClickListener, EcgDataParser24.EcgDataGetListener {
	
	public static ECGMeasureActivity measureActivity;
	private final String TAG = "ECGMeasure";
	private static String DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
	private static String ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
	private TextView speedTV, gainTV, filter1TV, hrTV, filter2TV, filter3TV, filter4TV, timeStarTV, waveLeadSwitchTV;
	private Button speedBtn, startBtn, gainBtn, filterBtn, displayBtn, soundBtn;//, filterOkBtn, filterCancelBtn
	private RadioGroup hp_group, ac_group, mc_group, lp_group;
	public LinkedList<int[]> ecgDataBuffer = new LinkedList<int[]>();
	private List<int[]> mdlists = new ArrayList<int[]>();
	private int[] paceRecBuffer;

	SurfaceView sfvWave;//, sfvBackGround
	SurfaceHolder sfhWave;//, sfhBackGround
	final static int STEP = 10;

	final static int ErasureStep = 12;
	final static int GRID_SIZE = 10;
	final static int MARGIN = 4;
	final static int LEADNUM = 12;
	private int waveGain = 2;
	private int waveDisplayType = 0;
	private int waveSingleDisplay_Switch = 0;
//	private boolean waveGainChangeFlag = false;
	private int waveSpeed = 0;
	private int stepCount = 0;
	private SoundPool sndPool = null;
	private int[] soundPoolId = new int[5];
	private boolean soundOpen = true; 
	int baseY[], baseX[];
	public int currentX, screenWidth, screenHeight;
	private int paceMaker = 2147483647;
	int oldY[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	Canvas canvas, waveCanvas;
	Paint mPaint, pacePaint, greenPaint, subGridPaint, topGridPaint;
	private Bitmap backMap, waveMap, bodyLeadMap;
	private SampleDotIntNew sampleDot[];
	String leadName[] = { "I", "II", "III", "avR", "avL", "avF", "V1", "V2", "V3", "V4", "V5", "V6" };
	private float[] leadX = new float[]{317, 345, 367, 382, 187, 372, 351, 262, 300, 203};
    private float[] leadY = new float[]{148, 161, 163, 161, 35, 35, 250, 123, 123, 250};
	private List<Integer> dot0, dot1, dot2, dot3, dot4, dot5, dot6, dot7, dot8, dot9, dot10, dot11, dot13;
	private List<int[]> bufferList = new ArrayList<int[]>();
	private List<int[]> bufferAuto = new ArrayList<int[]>();
	private List<Integer> buf0, buf1, buf2, buf3, buf4, buf5, buf6, buf7, buf8, buf9, buf10, buf11;
	private List<Integer> buffer24H = new LinkedList<Integer>();
	private int paceVisible;
    private float bodyImgW, bodyImgH, percentW, percentH;

    private Timer mTimer;
    private MyTimeTask mTimerTask;
    
    private ProgressDialog progressDialog;
	private BluetoothAdapter mBtAdapter;
	
	private boolean MyService_RunFlag = false;
    private BTConnectStreamThread btConnecThread;
	private UsbManager mUsbManager;
	private List<UsbSerialDriver> listUSDs;
	private UsbSerialDriver usbSerialDriver;
	private UsbSerialPort usbSerialPort;
	private UsbDeviceConnection usbConnection;
	private UsbSerialThread usbSerialThread;
	private SaveFile24HThread saveFile24HThread;
	private long ecg2Device_time = -1;
	
	private boolean isUnRegisterBR = false;
	private static boolean bodyLeadState = false;//导联状态 脱落为true
	private static boolean bodyLeadState_GREEN = false;//导联首次全部连上  为true 
	private static int bodyLeadState_GREEN_First = 0; //导联全部连上，等于9时，表示首次连上，显示导联图
	private static boolean isRestartDraw = false;//是否重新绘制背景、心电曲线
	private static boolean isDrawECGWave = false;//绘制开关
	private boolean[] isFirstDrawWave = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false}; //是否首次绘制
	private boolean bodyLeadStateOff = false;//导联脱落标识
	private boolean isFileSave = false;//保存文件标识
	private boolean bodyLeadStateSkip = false;//不管导联状态，直接跳过，进入心电曲线画图
	private int playBodyLeadOff_ID = -1;//循环播放导联脱落的音频ID
	private int playStartECGMearsure = -1;//播放开始测量音频的ID
	private boolean[] bodyLeaData = new boolean[9];
	private boolean[] leadStateOld = new boolean[]{true, true, true, true, true, true, true, true, true};
	private int saveCount = 0;
	private int saveFileTotalCount;
	private long bodyLeadStateStart = 0;
	private int timeSecondCount, timeMinuteCount, timeHourCount;
	private int baseLineState = -1;//基线是否稳定  0为稳定
	private int saveFile_AutoManual = 0;//2为24小时；1为人工Manual；0为自动 Auto:10秒数据
	public int saveFileManualCount = 0;
	private int fenbianlv = 1;
	private boolean hour24State = false;
	private ExecutorThreadUtils eThreadUtils;
	private String idCardNo;
	static{
		System.loadLibrary("ecglib");
	}
	private int callData_count = 0;
	private EcgFilter ecgFilter;
	private short FilterBase, FilterMC, FilterAC, FilterLP;
	private EcgHRDetect hrDetect;
	private EcgAnalyse ecgAnalyse;
	
	private final int DRAW_ECG_WAVE = 1000;
	private final int BT_CONNECT_FAILED = 997;
	private final int BT_CONNECTED = 996;
	private final int USB_DETACHED = 995;
	private final int BLUETOOTH_DETACHED = 991;
	private final int HEART_NUM = 992;
	private final int BODY_LEAD_STATE_OFF = 898;
	private final int SAVE_PDFXML_FILE = 897;
	private final int SAVE_PDFXML_TIMECOUNT = 896;
	private final int SWITCH_SPEED_DRAW_WAVE = 804;
	private final int DRAW_BODY_LEAD_STATE = 803;
	private final int DRAW_BODY_LEAD_START = 802;
	private final int DISMISS_PROGRESS_DIALOG = 801;
	private final int SHOW_PROGRESS_DIALOG = 800;
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case DRAW_ECG_WAVE://开始测量心电，并计时
				Log.e(TAG, "start Timer");
				if (!isFinishing() && progressDialog!=null) {
					progressDialog.dismiss();
				}
				clearCache();
				mTimerTask = new MyTimeTask();
				mTimer.schedule(mTimerTask, 4, 15);
				isRestartDraw = true;
				break;
			case BT_CONNECT_FAILED://连接失败
				if (!isFinishing() && progressDialog!=null) {
					progressDialog.dismiss();
				}
				String reson = (String) msg.obj;
				ToastUtil.showShort(ECGMeasureActivity.this, "连接失败 : "+reson);
				if (reson.contains("socket.connect")) {
					PreferUtils.getIntance().setECGDeviceBTMAC("");
				}
				finish();
				break;
			case BT_CONNECTED:
				if (!isFinishing() && progressDialog!=null) {
					progressDialog.dismiss();
				}
				break;
			case HEART_NUM://心率显示
				int heart = (Integer) msg.obj;
				if(heart > 0){
					hrTV.setText(String.valueOf(heart));
				}else if(heart < 0){
					hrTV.setText("---");
				}
				if(soundOpen){
					sndPool.play(soundPoolId[0], 1.0F, 1.0F, 1, 0, 1.0F);
				}
				break;
			case SAVE_PDFXML_FILE://保存完成文件后的处理
				if (!hour24State && !isFinishing() && progressDialog!=null) {
					progressDialog.dismiss();
				}
				if(saveFile_AutoManual==2){
					Log.e(TAG, "保存的次数："+saveFileManualCount);
					if (saveFileManualCount == 1440) {//hour24State     saveFileManualCount == 1440
						isFileSave = false;
						isDrawECGWave = true;
						stopDataSource();
						updataTimeCount();
						if (!isFinishing() && progressDialog!=null && progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
						if (!isFinishing()) {
							finishDialog("24小时测量完毕");
						}
					}else {
						updataTimeCount();
					}
				}else if (saveFile_AutoManual==1) {//手动模式
					if ("停止".equals(startBtn.getText().toString())) {
						startBtn.setText("采集");
						timeStarTV.setText("");
						ToastUtil.showShort(measureActivity, "一分钟数据采集完成");
					}
				}else if(saveFile_AutoManual==0){
					Intent intent = new Intent(ECGMeasureActivity.this, EcgReportActivity.class);
					String xmlFilePath = (String) msg.obj;
					intent.putExtra("filename", xmlFilePath);
					startActivity(intent);
					finish();
				}
				break;
			case SAVE_PDFXML_TIMECOUNT://测量心电时的计时显示
				if (saveFile_AutoManual==2) {
					if (timeSecondCount==60) {
						timeMinuteCount++;
						timeSecondCount = 0;
					}
					if (timeMinuteCount==60) {
						timeMinuteCount = 0;
						timeHourCount++;
					}
					timeStarTV.setText(timeHourCount+"时"+timeMinuteCount+"分"+timeSecondCount+"秒");
				}else {
					timeStarTV.setText(String.valueOf(timeSecondCount)+"秒");
				}
				break;
			case SHOW_PROGRESS_DIALOG:
				if (!isFinishing() && progressDialog!=null) {
					String dialog_message = (String) msg.obj;
					progressDialog.setMessage(dialog_message);
					if (!progressDialog.isShowing()) {
						progressDialog.show();
					}
				}
				break;
			case DISMISS_PROGRESS_DIALOG:
				if (!isFinishing() && progressDialog!=null) {
					progressDialog.dismiss();
				}
				break;
			case DRAW_BODY_LEAD_STATE://导联连接成功后， 延时两秒钟
				long end = System.currentTimeMillis();
				if (end - bodyLeadStateStart>=1990) {//是延时两秒钟
					baseLineState = -1;
					isDrawECGWave = false;
					bodyLeadState_GREEN = false;
					isRestartDraw = true;
					clearCache();
					setButtonEnabled(true);
					if (saveFile_AutoManual==2) {
						displayBtn.setEnabled(false);
					}
					if (saveFile_AutoManual==2 || waveDisplayType==2) {
						waveLeadSwitchTV.setVisibility(View.VISIBLE);
					}
				}
				break;
			case DRAW_BODY_LEAD_START://导联连接成功，开始测量心电
				if (playBodyLeadOff_ID != -1) {
					sndPool.stop(playBodyLeadOff_ID);
					playBodyLeadOff_ID = -1;
				}
				if (playStartECGMearsure!=-1) {
					sndPool.stop(playStartECGMearsure);
				}
				playStartECGMearsure = sndPool.play(soundPoolId[2], 1.0F, 1.0F, 1, 0, 1.0F);
				break;
			case BODY_LEAD_STATE_OFF://导联脱落时播放音频  绘制导联脱落的画面
				timeStarTV.setText("");
				isFileSave=false;
				if (saveFile_AutoManual==1) {
					startBtn.setText("采集");
					deleteLongTime();
				}
				if (saveFile_AutoManual==2) {
					updataTimeCount();
					setButtonEnabled(true);
				}
				waveLeadSwitchTV.setVisibility(View.INVISIBLE);
				if (playStartECGMearsure!=-1) {
					sndPool.stop(playStartECGMearsure);
					playStartECGMearsure = -1;
				}
				if (playBodyLeadOff_ID != -1) {
					sndPool.stop(playBodyLeadOff_ID);
				}
				playBodyLeadOff_ID = sndPool.play(soundPoolId[1], 1.0F, 1.0F, 1, -1, 1.0F);
				break;
			case SWITCH_SPEED_DRAW_WAVE://切换纸速
				if (!isFinishing() && progressDialog!=null) {
					progressDialog.dismiss();
				}
				speedBtn.setEnabled(true);
				isDrawECGWave = false;
				break;
			case USB_DETACHED://USB连接断开
				if (playBodyLeadOff_ID != -1) {
					sndPool.stop(playBodyLeadOff_ID);
				}
				stopDataSource();
				if (!isFinishing()) {
					finishDialog("USB连接已断开，请检查并重新连接");
				}
				break;
			case BLUETOOTH_DETACHED:
				if (playBodyLeadOff_ID != -1) {
					sndPool.stop(playBodyLeadOff_ID);
				}
				stopDataSource();
				if (!isFinishing()) {
					finishDialog("蓝牙连接已断开，请检查并重新连接");
				}
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.surface_measure);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		measureActivity = this;
		idCardNo = PreferUtils.getIntance().getIdCardNo();
		initView();
		initData();
	}

	private void initView() {
		sfvWave = (SurfaceView) this.findViewById(R.id.surfaceView1);

		speedTV = (TextView) findViewById(R.id.ecg_speed);
		gainTV = (TextView) findViewById(R.id.ecg_gain);
		
		gainTV.setText("10");
		filter1TV = (TextView) findViewById(R.id.ecg_filter_1);
		filter2TV = (TextView) findViewById(R.id.ecg_filter_2);
		filter3TV = (TextView) findViewById(R.id.ecg_filter_3);
		filter4TV = (TextView) findViewById(R.id.ecg_filter_4);
		timeStarTV = (TextView) findViewById(R.id.ecg_record_start);
		
		filter1TV.setText("50Hz");
		filter2TV.setText("0.50Hz");
		filter3TV.setText("25Hz");
		filter4TV.setText("25Hz");
		
		speedTV = (TextView) findViewById(R.id.ecg_speed);
		speedTV.setText("25");
		hrTV = (TextView) findViewById(R.id.ecg_heart_beat);
		
		speedBtn = (Button) findViewById(R.id.ecgbtn_speed);
		startBtn = (Button) findViewById(R.id.ecgbtn_start);
		soundBtn = (Button) findViewById(R.id.ecgbtn_sound);
		gainBtn = (Button) findViewById(R.id.ecgbtn_gain);
		filterBtn = (Button) findViewById(R.id.ecgbtn_filter);
		displayBtn = (Button) findViewById(R.id.ecgbtn_display);
		waveLeadSwitchTV = (TextView) findViewById(R.id.ecg_wavelead_switch);
		speedBtn.setOnClickListener(this);
		startBtn.setOnClickListener(this);
		soundBtn.setOnClickListener(this);
		gainBtn.setOnClickListener(this);
		filterBtn.setOnClickListener(this);
		displayBtn.setOnClickListener(this);
		waveLeadSwitchTV.setOnClickListener(this);
		int height = getResources().getDisplayMetrics().heightPixels;
		if (height<=800) {
			fenbianlv = 1;
		}else if (height >= 960) {
			fenbianlv = 2;
		}
	}

	private void initData() {
		baseY = new int[LEADNUM];
		currentX = 30;
		baseX = new int[LEADNUM];
		eThreadUtils = new ExecutorThreadUtils(handler);
		initCanvas();
		initSampleDot();
		
		hrDetect = new EcgHRDetect();
		hrDetect.initHr(3495.2533333f * 3);
		
		ecgAnalyse = new EcgAnalyse();
		ecgAnalyse.Axis = new int[3];
    	ecgAnalyse.ecgResult = new int[12];
    	
		ecgFilter = new EcgFilter();
		FilterBase = 4;
		FilterMC = 14;
		FilterLP = 37;
		FilterAC = 22;
		ecgFilter.initFilter(FilterBase, FilterMC, FilterAC, FilterLP);
		ecgFilter.initBaseLineJudge();
		
		sndPool = new SoundPool(3, 3, 0);
		
		try {
			soundPoolId[0] = sndPool.load(getResources().getAssets().openFd("sounds/heart_beep.ogg"), 1);
			soundPoolId[1] = sndPool.load(getResources().getAssets().openFd("sounds/lead_off.ogg"), 1);
			soundPoolId[2] = sndPool.load(getResources().getAssets().openFd("sounds/start_ad.ogg"), 1);
			soundPoolId[3] = sndPool.load(getResources().getAssets().openFd("sounds/stop_ad.ogg"), 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mTimer = new Timer();
		progressDialog = new ProgressDialog(this);
	    progressDialog.setMessage("正在搜索蓝牙设备");
	    WindowManager.LayoutParams params = progressDialog.getWindow().getAttributes();
	    progressDialog.getWindow().setGravity(Gravity.TOP);
	    params.y = 60;
	    progressDialog.getWindow().setAttributes(params);
	    progressDialog.setCancelable(false);
        setButtonEnabled(false);
		
	    mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBtAdapter==null) {
			ToastUtil.showShort(this, "该设备不支持蓝牙");
			return;
		}else if(!mBtAdapter.isEnabled()) {
			mBtAdapter.enable();
		}
		
		mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
		listUSDs = UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);
		waveDisplayType = PreferUtils.getIntance().getDisplayStyle();
		saveFile_AutoManual = PreferUtils.getIntance().getSaveFileStyle();
		timeStarTV.setVisibility(View.VISIBLE);
		if(saveFile_AutoManual==0){
			initSaveData();
		}
		
		paceVisible = PreferUtils.getIntance().getPace();
    	registerReceiver();
        if (!USB_Operation.isDetectionDevice(mUsbManager)) {
			connectBlueTooth();
        	return;
		}
        
        usbSerialDriver = listUSDs.get(0);
        if (usbSerialDriver==null) {
        	connectBlueTooth();
        	return;
		}
        
        usbConnection = mUsbManager.openDevice(usbSerialDriver.getDevice());
        if (usbConnection==null) {
        	connectBlueTooth();
        	return;
        }
        usbSerialPort = usbSerialDriver.getPorts().get(0);
        
        if (usbConnection==null || usbSerialPort==null || handler==null) {
			return;
		}
        usbSerialThread = new UsbSerialThread(usbConnection, usbSerialPort, handler, this);
        MyService_RunFlag = true;
        usbSerialThread.start();
	}
	
	private void initCanvas() {
		bodyImgW = 554;
        bodyImgH = 264;
		sfhWave = sfvWave.getHolder();
		/* 绘制前获取控件尺寸 */
		sfvWave.post(new Runnable() {
			
			@Override
			public void run() {
				screenWidth = sfvWave.getMeasuredWidth();
				screenHeight = sfvWave.getMeasuredHeight();
				backMap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
		     	waveMap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
		     	waveCanvas = new Canvas(waveMap);
		     	percentW = screenWidth / bodyImgW;
	            percentH = screenHeight / bodyImgH;
	            for (int i=0; i<10; i++){
	            	leadX[i] = leadX[i] * percentW;
	                leadY[i] = leadY[i] * percentH;
	            }
		     	waveCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
			}
		});
		bodyLeadMap = BitmapFactory.decodeResource(getResources(), R.drawable.body_bg);
     	mPaint = new Paint();
     	greenPaint = new Paint();
     	greenPaint.setColor(Color.GREEN);
		mPaint.setColor(Color.GREEN);
		mPaint.setAntiAlias(true);
     	pacePaint = new Paint();
     	pacePaint.setStrokeWidth(1.5f);
     	pacePaint.setColor(Color.RED);
     	subGridPaint = new Paint();
     	topGridPaint = new Paint();
     	topGridPaint.setColor(getResources().getColor(R.color.ecg_back_blue));
		topGridPaint.setStrokeWidth((float) 1.5);
		subGridPaint.setColor(getResources().getColor(R.color.ecg_back_little));
	}

	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.ecgbtn_display) {//显示按钮
			isDrawECGWave = true;
			currentX = 30;
			waveDisplayType = (waveDisplayType + 1) % 3;
			PreferUtils.getIntance().setDisplayStyle(waveDisplayType);
			if (waveDisplayType==2) {
				waveLeadSwitchTV.setVisibility(View.VISIBLE);
			}else {
				waveLeadSwitchTV.setVisibility(View.GONE);
			}
			isFirstDrawWave = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false};
			isRestartDraw = true;
			isDrawECGWave = false;
		}
		if (v.getId()==R.id.ecgbtn_filter) {//滤波按钮
			isDrawECGWave = true;
			if (saveFile_AutoManual==0) {
				isFileSave = false;
			}
			if (saveFile_AutoManual==1 && "停止".equals(startBtn.getText().toString())) {
				isFileSave = false;
			}
			showFilterDialog();
		}
		if (v.getId()==R.id.ecgbtn_gain) {//增益按钮
			waveGainChange();
		}
		if (v.getId()==R.id.ecgbtn_sound) {//声音按钮
			soundOpen = !soundOpen;
		}
		if (v.getId()==R.id.ecgbtn_speed) {//纸速按钮
			waveSpeedChange();
		}
		if (v.getId()==R.id.ecgbtn_start) {//开始采集按钮
			if (saveFile_AutoManual==2) {//24小时
				isFileSave = true;
				saveFileTotalCount = 60 * 500;
				saveCount = 0;
				baseLineState = 0;
				buffer24H.clear();
				paceRecBuffer = new int[60 * 500];
				setButtonEnabled(false);
				waveLeadSwitchTV.setEnabled(false);
				ecg2Device_time = ECGDataLong2DeviceDao.getInstance(idCardNo).saveECGDataByPid(idCardNo, saveFile_AutoManual, waveSingleDisplay_Switch);
				saveFile24HThread = new SaveFile24HThread(handler, ecg2Device_time);
				saveFile24HThread.start();
			}else if (saveFile_AutoManual==1) {//手动模式
				if ("采集".equals(startBtn.getText().toString())) {
					initSaveData();
					baseLineState = 0;
					isFileSave = true;
					initManualBuffer();
					ecg2Device_time = ECGDataLong2DeviceDao.getInstance(idCardNo).saveECGDataByPid(idCardNo, saveFile_AutoManual, 0);
					startBtn.setText("停止");
				}else if("停止".equals(startBtn.getText().toString())){
					timeStarTV.setText("");
					isFileSave = false;
					if (!isFinishing() && progressDialog!=null) {
						progressDialog.setMessage("正在分析心电数据...");
						progressDialog.show();
					}
					Integer[] d0 = new Integer[buf0.size()];
					buf0.toArray(d0);
					Integer[] d1 = new Integer[buf1.size()];
					buf1.toArray(d1);
					Integer[] d2 = new Integer[buf2.size()];
					buf2.toArray(d2);
					Integer[] d3 = new Integer[buf3.size()];
					buf3.toArray(d3);
					Integer[] d4 = new Integer[buf4.size()];
					buf4.toArray(d4);
					Integer[] d5 = new Integer[buf5.size()];
					buf5.toArray(d5);
					Integer[] d6 = new Integer[buf6.size()];
					buf6.toArray(d6);
					Integer[] d7 = new Integer[buf7.size()];
					buf7.toArray(d7);
					Integer[] d8 = new Integer[buf8.size()];
					buf8.toArray(d8);
					Integer[] d9 = new Integer[buf9.size()];
					buf9.toArray(d9);
					Integer[] d10 = new Integer[buf10.size()];
					buf10.toArray(d10);
					Integer[] d11 = new Integer[buf11.size()];
					buf11.toArray(d11);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("Gain", gainTV.getText().toString());
					map.put("Speed", speedTV.getText().toString());
					map.put("FilterBase", filter2TV.getText().toString());
					map.put("FilterMC", filter3TV.getText().toString());
					map.put("FilterAC", filter1TV.getText().toString());
					map.put("FilterLP", filter4TV.getText().toString());
					map.put("waveGain", waveGain);
					map.put("waveSpeed", waveSpeed);
					map.put("waveSingleDisplay_Switch", waveSingleDisplay_Switch);
					map.put("ecg2DeviceData_time", ecg2Device_time);
					map.put("ecgDataBuffer0", d0);
					map.put("ecgDataBuffer1", d1);
					map.put("ecgDataBuffer2", d2);
					map.put("ecgDataBuffer3", d3);
					map.put("ecgDataBuffer4", d4);
					map.put("ecgDataBuffer5", d5);
					map.put("ecgDataBuffer6", d6);
					map.put("ecgDataBuffer7", d7);
					map.put("ecgDataBuffer8", d8);
					map.put("ecgDataBuffer9", d9);
					map.put("ecgDataBuffer10", d10);
					map.put("ecgDataBuffer11", d11);
					map.put("paceBuffer", paceRecBuffer);
					map.put("saveFileManualCount", saveFileManualCount);
					map.put("timeSecondCount", timeSecondCount);
					updataTimeCount();
					eThreadUtils.startManulTask(map);
					startBtn.setText("采集");
				}
			}else if(saveFile_AutoManual==0){//自动模式
				baseLineState = 0;
				initSaveData();
			}
		}
		if (v.getId() == R.id.ecg_wavelead_switch) {
			if (waveDisplayType == 2) {
				waveSingleDisplay_Switch = (waveSingleDisplay_Switch + 1) % 12;
			}
		}
	}
	
	/**给设备发送停止命令**/
	private void stopDataSource() {
		if (btConnecThread!=null) {
			btConnecThread.stopBlueTooth();
		}
		if (usbSerialThread!=null) {
			usbSerialThread.stopSerial();
			listUSDs = null;
			usbSerialDriver = null;
			usbSerialPort = null;
			mUsbManager = null;
		}
		if (mTimerTask!=null) {
			mTimerTask.cancel();
		}
		bodyLeadState_GREEN_First = 0;
	}

	private void setButtonEnabled(boolean b) {
		startBtn.setEnabled(b);
		speedBtn.setEnabled(b);
		gainBtn.setEnabled(b);
		speedBtn.setEnabled(b);
		filterBtn.setEnabled(b);
		displayBtn.setEnabled(b);
		soundBtn.setEnabled(b);
	}

	private void waveGainChange(){
		waveGain *= 2;
		if(waveGain > 4)
			waveGain = 1;
		if(waveGain == 1){
			gainTV.setText("20");
		}else if(waveGain == 2){
			gainTV.setText("10");
		}else{// if(waveGain == 4)
			gainTV.setText("5");
		}//else {gainTV.setText("2.5");}
		isRestartDraw = true;
		currentX = 30;
	}
	
	private void waveSpeedChange(){
		isDrawECGWave = true;
		waveSpeed = (waveSpeed + 1) % 3;
		int ws = 125;
		if (fenbianlv==1) {
			ws = 125;
		}else if(fenbianlv==2){
			ws = 248;
		}
		if(waveSpeed == 0){
			speedTV.setText("25");
		}else if(waveSpeed==1){
			speedTV.setText("12.5");
			ws/=2 ;
		}else{
			speedTV.setText("50");
			ws*=2;
		}
		initDeSampleDot(ws);//126 / (waveSpeed + 1)
		currentX = 30;
		isRestartDraw = true;
		isDrawECGWave = false;
	}
	private void connectBlueTooth() {
		progressDialog.show();
		mBtAdapter.startDiscovery();
	}
	
	private void registerReceiver(){
		IntentFilter btFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		btFilter.addAction(BluetoothDevice.ACTION_FOUND);
		btFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(buleToothReceiver, btFilter);
		if (null!=usbConnection) {
			IntentFilter usbFilter = new IntentFilter();
			usbFilter.addAction(DETACHED);
			usbFilter.addAction(ATTACHED);
			this.registerReceiver(usbReceiver, usbFilter);
		}
	}
	
	private void unRegisterReceiver(){
		if (!isUnRegisterBR) {
			try {
				unregisterReceiver(buleToothReceiver);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (null!=usbConnection) {
			try {
				unregisterReceiver(usbReceiver);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private final BroadcastReceiver buleToothReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			String btMac = PreferUtils.getIntance().getECGDeviceBTMAC();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice currentDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (TextUtils.isEmpty(btMac)) {//没有使用过蓝牙
					if ("ALX SPP".equals(currentDevice.getName())) {
						mBtAdapter.cancelDiscovery();
						progressDialog.setMessage("正在连接");
						btConnecThread = new BTConnectStreamThread(currentDevice, handler, ECGMeasureActivity.this);
						btConnecThread.start();
						MyService_RunFlag = true;
						unregisterReceiver(this);
						isUnRegisterBR = true;
					}
				}else {//已经连接过，就用原来的蓝牙地址
					if (btMac.equals(currentDevice.getAddress())) {
						mBtAdapter.cancelDiscovery();
						progressDialog.setMessage("正在连接");
						btConnecThread = new BTConnectStreamThread(currentDevice, handler, ECGMeasureActivity.this);
						btConnecThread.start();
						MyService_RunFlag = true;
						unregisterReceiver(this);
						isUnRegisterBR = true;
					}
				}
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				progressDialog.dismiss();
				mBtAdapter.cancelDiscovery();
				finishDialog("没有搜索到蓝牙设备，请确保蓝牙是否打开");
				displayBtn.setEnabled(true);
				PreferUtils.getIntance().setECGDeviceBTMAC("");
			}
		}
	};
	
	private BroadcastReceiver usbReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (DETACHED.equals(action)) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						ToastUtil.showShort(ECGMeasureActivity.this, "USB设备已断开");
					}
				});
				ECGMeasureActivity.this.finish();
			}
			if (ATTACHED.equals(action)) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						ToastUtil.showShort(ECGMeasureActivity.this, "设备已接入");
					}
				});
			}
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			if (saveFile_AutoManual==2 && "停止".equals(startBtn.getText().toString())) {//手动模式正在测量
				updataTimeCount();
			}
			if (saveFile_AutoManual==1 && "停止".equals(startBtn.getText().toString())) {
				updataTimeCount();
			}
			stopDataSource();
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (MyService_RunFlag) {
			isDrawECGWave = false;
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (MyService_RunFlag) {
			isDrawECGWave = true;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unRegisterReceiver();
		sndPool.release();
	}

	/**画曲线图**/
	private void SimpleDraw(List<int[]> datas) {
		DrawEcgWave(datas);

		currentX += stepCount;
		
		if (waveDisplayType == 0) {
			if (currentX >= (screenWidth / 2)) {
				currentX = 30;
			}
		}else {
			if (currentX >= screenWidth ) {
				currentX = 30;
			}
		}
	}
	/**画波形图**/
	private void DrawEcgWave(List<int[]> datas) {
		List<int[]> leaData = StringUtil.getEcgData(datas);
		int lockStep = 0;
		if (waveDisplayType==2) {
			dot13 = sampleDot[12].SnapshotSample(leaData.get(waveSingleDisplay_Switch));
			if (dot13==null || dot13.isEmpty() ) {
				stepCount = 0;
				return;
			}
			lockStep = dot13.size();
		}else {
			dot0 = sampleDot[0].SnapshotSample(leaData.get(0));//
			dot1 = sampleDot[1].SnapshotSample(leaData.get(1));//
			dot2 = sampleDot[2].SnapshotSample(leaData.get(2));//
			dot3 = sampleDot[3].SnapshotSample(leaData.get(3));//
			dot4 = sampleDot[4].SnapshotSample(leaData.get(4));//
			dot5 = sampleDot[5].SnapshotSample(leaData.get(5));//
			dot6 = sampleDot[6].SnapshotSample(leaData.get(6));//
			dot7 = sampleDot[7].SnapshotSample(leaData.get(7));//
			dot8 = sampleDot[8].SnapshotSample(leaData.get(8));//
			dot9 = sampleDot[9].SnapshotSample(leaData.get(9));//
			dot10 = sampleDot[10].SnapshotSample(leaData.get(10));//
			dot11 = sampleDot[11].SnapshotSample(leaData.get(11));//
			if (dot0==null || dot0.isEmpty() ) {
				stepCount = 0;
				return;
			}
			lockStep = dot0.size();
		}
		
		stepCount = lockStep;
		if(stepCount > 0){
			Rect rect = new Rect();
			if (currentX == 30) {
				rect.set(currentX, 0, currentX + MARGIN + stepCount + 6 * MARGIN, screenHeight);
			}else {
				rect.set(currentX + MARGIN, 0, currentX + MARGIN + stepCount + 5 * MARGIN, screenHeight);
			}
			
			waveCanvas.drawBitmap(backMap, rect, rect,  null);
			if (waveDisplayType==0) {
				if (currentX == 30) {
					rect.set(currentX + baseX[6], 0, currentX + MARGIN + baseX[6] + stepCount + 6 * MARGIN, screenHeight);
				}else {
					rect.set(currentX + MARGIN + baseX[6], 0, currentX + MARGIN + baseX[6] + stepCount + 5 * MARGIN, screenHeight);
				}
				waveCanvas.drawBitmap(backMap, rect, rect,  null);
			}
		}
		if (waveDisplayType==2) {
			int step = 0;
			for (int k = 0; k < lockStep; k++) {
				DrawLeadWave(dot13.get(k), 5, currentX + step);
				step++;
			}
		}else {
			int step = 0;
			for (int k = 0; k < lockStep; k++) {
				DrawLeadWave(dot0.get(k), 0, currentX + step);
				DrawLeadWave(dot1.get(k), 1, currentX + step);
				DrawLeadWave(dot2.get(k), 2, currentX + step);
				DrawLeadWave(dot3.get(k), 3, currentX + step);
				DrawLeadWave(dot4.get(k), 4, currentX + step);
				DrawLeadWave(dot5.get(k), 5, currentX + step);
				DrawLeadWave(dot6.get(k), 6, currentX + step);
				DrawLeadWave(dot7.get(k), 7, currentX + step);
				DrawLeadWave(dot8.get(k), 8, currentX + step);
				DrawLeadWave(dot9.get(k), 9, currentX + step);
				DrawLeadWave(dot10.get(k), 10, currentX + step);
				DrawLeadWave(dot11.get(k), 11, currentX + step);
				step++;
			}
		}
		
		if(stepCount > 0){
			canvas = sfhWave.lockCanvas();
			if (canvas!=null) {
				canvas.drawColor(getResources().getColor(R.color.ecg_back_color));
				
				canvas.drawBitmap(waveMap, 0, 0, null);
				DrawWaveTag();
				sfhWave.unlockCanvasAndPost(canvas);
			}
		}
	}

	/**波形图的具体画法**/
	private void DrawLeadWave(int da,int leadNum, int offset) {
		int y;
		int oldX;
		int i;
		oldX = offset;
		if (baseLineState == -1) {
			mPaint.setColor(Color.BLUE);
		}else {
			mPaint.setColor(Color.GREEN);
		}
		i = offset + 1;
		if (isFirstDrawWave[leadNum]) {
			if (da==paceMaker) {
				y = oldY[leadNum];
				waveCanvas.drawLine(oldX + MARGIN + baseX[leadNum],
						oldY[leadNum]-15,// + baseY[leadNum]
						oldX + MARGIN + baseX[leadNum],
						oldY[leadNum]+15 ,// + baseY[leadNum]
						pacePaint);
			}else {
				y = (baseY[leadNum] + ( - da) / (105 * waveGain) * fenbianlv); //getYLead(leadNum, da); //192  192 * 2 
				waveCanvas.drawLine(oldX + MARGIN + baseX[leadNum],
						oldY[leadNum],// + baseY[leadNum]
						i + MARGIN + baseX[leadNum],
						y ,// + baseY[leadNum]
						mPaint);
			}
			oldY[leadNum] = y;
		}else {
			isFirstDrawWave[leadNum] = true;
			if (da == paceMaker) {
				y = oldY[leadNum];
			}else {
				y = (baseY[leadNum] + ( - da) / (105 * waveGain) * fenbianlv); //getYLead(leadNum, da); //192  192 * 2 
			}
			oldY[leadNum] = y;
		}
	}
	
	private void drawBodyLeadSpot(boolean[] bodyLead) {
		int circleRadius = 15;
		greenPaint.setAntiAlias(false);
		greenPaint.setColor(Color.GREEN);
		canvas = sfhWave.lockCanvas();
		if (canvas!=null) {
			canvas.drawColor(Color.BLACK);
			Rect rect = new Rect();
			rect.set(0, 0, screenWidth, screenHeight);
			canvas.drawBitmap(bodyLeadMap, null, rect, null);
			for (int i = 0; i < bodyLead.length; i++) {
				if (!bodyLead[i]) {
					canvas.drawCircle(leadX[i], leadY[i], circleRadius, greenPaint);
				}
				leadStateOld[i] = bodyLead[i];
			}
			canvas.drawCircle(leadX[9], leadY[9], circleRadius, greenPaint);
			sfhWave.unlockCanvasAndPost(canvas);
		}
	}
	
	/** 确定抽点 **/
	private void initSampleDot() {
		sampleDot = new SampleDotIntNew[LEADNUM+1];
		for (int i = 0; i < LEADNUM+1; i++) {
			sampleDot[i] = new SampleDotIntNew(500);//, 126 / (waveSpeed + 1)
		}
		if (fenbianlv==1) {
			initDeSampleDot(125 / (waveSpeed + 1));
		}else {
			initDeSampleDot(248 / (waveSpeed + 1));
		}
	}
	
	private void initDeSampleDot(int desDot){
		for (int i = 0; i < LEADNUM+1; i++) {
			sampleDot[i].setDesSampleDot(desDot);
		}
	}
	
	/**绘制背景**/
	private void DrawBackBmp(){
		if (waveDisplayType==0) {
			for (int i = 0; i < LEADNUM; i++) {
				baseY[i] = screenHeight / LEADNUM * 2 * (i % 6 + 1) - 50;
			}
			for (int i = 0; i < LEADNUM; i++) {
				if (i < 6) {
					baseX[i] = 0;
				} else {
					baseX[i] = screenWidth / 2 + MARGIN;
				}
			}
		}else{
			for (int i = 0; i < LEADNUM; i++) {
				baseY[i] = screenHeight / LEADNUM * (i % 12 + 1) - 20;
			}
			for (int i = 0; i < LEADNUM; i++) {
				baseX[i] = 0;
			}
		}

		canvas = new Canvas(backMap);
		canvas.drawColor(getResources().getColor(R.color.ecg_back_color));
		
		DrawVerticalLine(fenbianlv*5);
		DrawHorizontalLine(fenbianlv*5);
		if (waveDisplayType==0) {
			DrawWaveGain(0, baseY[2] + 20);
			DrawWaveGain(baseX[8], baseY[2] + 20);
		}else {
			DrawWaveGain(0, baseY[5] + 20);
		}
		canvas = new Canvas(waveMap);
		canvas.drawBitmap(backMap, 0, 0, null);
	}
	
	private void DrawWaveGain(int x, int y){
		greenPaint.setStrokeWidth(1);
		canvas.drawLine(x + MARGIN,
				y + 5,
				x + MARGIN + 5,
				y + 5,
				greenPaint);
		
		canvas.drawLine(x + MARGIN + 5,
				y + 5,
				x + MARGIN + 5,
				y - (20 / waveGain)*5*fenbianlv+5,
				greenPaint);
		
		canvas.drawLine(x + MARGIN + 5,
				y - (20 / waveGain)*5*fenbianlv+5,
				x + MARGIN + 5 + 10,
				y - (20 / waveGain)*5*fenbianlv+5,
				greenPaint);
		
		canvas.drawLine(x + MARGIN + 5 + 10,
				y + 5,
				x + MARGIN + 5 + 10,
				y - (20 / waveGain)*5*fenbianlv+5,
				greenPaint);
		
		canvas.drawLine(x + MARGIN + 5 + 10,
				y + 5,
				x + MARGIN + 5 + 10 + 5,
				y + 5,
				greenPaint);
	}
	/**画水平线条**/
	private void DrawVerticalLine(int step) {
		int j = 0;

		for (int i = 0; i <= screenHeight; i += step) {
			if (j == 0) {
				canvas.drawLine(MARGIN,
						i + MARGIN,
						screenWidth + step,
						i + MARGIN,
						topGridPaint);
			} else {
				canvas.drawLine(MARGIN,
						i + MARGIN,
						screenWidth + step,
						i + MARGIN,
						subGridPaint);
			}
			j++;
			if (j >= 5)
				j = 0;
		}
	}

	/**画垂直线条**/
	private void DrawHorizontalLine(int step) {
		int j = 0;

		for (int i = 0; i <= screenWidth; i += step) {
			if (j == 0) {
				canvas.drawLine(i + MARGIN,
						MARGIN,
						i + MARGIN,
						screenHeight + step,
						topGridPaint);
			} else {
				canvas.drawLine(i + MARGIN,
						MARGIN,
						i + MARGIN,
						screenWidth + step,
						subGridPaint);
			}
			j++;
			if (j >= 5)
				j = 0;
		}
	}

	/** 画波形名称 **/
	private void DrawWaveTag() {
		greenPaint.setTextSize(18f);
		int mY;
		if (waveDisplayType==0) {
			mY = 22;
		}else {
			mY = 5;
		}
		if (waveDisplayType == 2) {
			canvas.drawText(leadName[waveSingleDisplay_Switch], baseX[3] + 10, baseY[3] - mY, greenPaint);
		}else {
			for (int i = 0; i < LEADNUM; i++) {
				canvas.drawText(leadName[i], baseX[i] + 10, baseY[i] - mY, greenPaint);
			}
		}
	}

	@Override
	public void GetEcgData(int[] data, int len, boolean[] leadState, boolean pace) {
		if (!bodyLeadStateSkip) {
			for (int i = 0; i < leadState.length; i++) {
				if (leadState[i]) {//有导联脱落
					bodyLeadState = true;
					isRestartDraw = true;
					bodyLeadState_GREEN_First = 0;
					break;
				}
				bodyLeadState = false;
				bodyLeadState_GREEN_First += 1;
			}
			if (bodyLeadState) {//导联脱落，绘制导联图；
				if (!bodyLeadStateOff) {
					handler.sendEmptyMessage(BODY_LEAD_STATE_OFF);
					bodyLeadStateOff = true;
					Log.e(TAG, "首次导联脱落");
				}
				bodyLeaData = Arrays.copyOf(leadState, leadState.length);
				clearCache();
				clearSaveCache();
				return;
			}
			if(bodyLeadState_GREEN_First == 9){//首次连接成功后，显示导联连接成功两秒中
				bodyLeadStateOff = false;
				bodyLeadStateStart = System.currentTimeMillis();
				isDrawECGWave = true;
				bodyLeadState_GREEN = true;
				handler.sendEmptyMessage(DRAW_BODY_LEAD_START);
				bodyLeaData = Arrays.copyOf(leadState, leadState.length);
				handler.sendEmptyMessageDelayed(DRAW_BODY_LEAD_STATE, 2000);
				return;
			}
			if (bodyLeadState_GREEN_First > 50000) {//防止数据太大
				bodyLeadState_GREEN_First = 27;
			}
		}else {
			bodyLeadState = false;
			bodyLeadState_GREEN = false;
			isDrawECGWave = false;
		}
		ecgFilter.filter(data, 1, 12);
		if (baseLineState == -1) {
			baseLineState = ecgFilter.isBaseLineStable(data, 1, 12);
		}
		
		int heart = hrDetect.hrDetect(data, 1, 12);
		if (heart > 0) {
			Message msg = handler.obtainMessage(HEART_NUM);
			msg.obj = heart;
			handler.sendMessage(msg);
		}
		if (isFileSave && baseLineState==0) {//保存的数据需要滤波处理
			if (saveFile_AutoManual==2) {
				buffer24H.add(data[waveSingleDisplay_Switch]);
			}else {
				if (saveFile_AutoManual==0) {
					bufferAuto.add(data);
				}else {
					buf0.add(data[0]);
					buf1.add(data[1]);
					buf2.add(data[2]);
					buf3.add(data[3]);
					buf4.add(data[4]);
					buf5.add(data[5]);
					buf6.add(data[6]);
					buf7.add(data[7]);
					buf8.add(data[8]);
					buf9.add(data[9]);
					buf10.add(data[10]);
					buf11.add(data[11]);
				}
				if (pace) {
					paceRecBuffer[saveCount] = 1;
				}else {
					paceRecBuffer[saveCount] = 0;
				}
			}
			saveCount++;
			if (saveCount%500==0) {
				timeSecondCount = saveCount/500;
				handler.sendEmptyMessage(SAVE_PDFXML_TIMECOUNT);
			}
			if (saveCount==saveFileTotalCount) {
				if (saveFile_AutoManual==2) {
					Integer[] data24H = new Integer[buffer24H.size()];
					buffer24H.toArray(data24H);
					synchronized (saveFile24HThread.queue) {
						saveFile24HThread.addToQueue(data24H);
					}
					buffer24H.clear();
					saveCount = 0;
					saveFileManualCount+=1;
				}else if(saveFile_AutoManual==0){
					List<int[]> bufferLists = bufferAuto;
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("Gain", gainTV.getText().toString());
					map.put("Speed", speedTV.getText().toString());
					map.put("FilterBase", filter2TV.getText().toString());
					map.put("FilterMC", filter3TV.getText().toString());
					map.put("FilterAC", filter1TV.getText().toString());
					map.put("FilterLP", filter4TV.getText().toString());
					map.put("waveGain", waveGain);
					map.put("waveSpeed", waveSpeed);
					map.put("waveSingleDisplay_Switch", waveSingleDisplay_Switch);
					map.put("ecgDataBuffer", bufferLists);
					map.put("paceBuffer", paceRecBuffer);
					map.put("ecg2DeviceData_time", ecg2Device_time);
					map.put("saveFileManualCount", saveFileManualCount);
					map.put("timeSecondCount", timeSecondCount);
					stopDataSource();
					eThreadUtils.startAutoTask(map);
					isFileSave = false;
					isDrawECGWave = true;
				}else if (saveFile_AutoManual==1) {
					Integer[] d0 = new Integer[buf0.size()];
					buf0.toArray(d0);
					Integer[] d1 = new Integer[buf1.size()];
					buf1.toArray(d1);
					Integer[] d2 = new Integer[buf2.size()];
					buf2.toArray(d2);
					Integer[] d3 = new Integer[buf3.size()];
					buf3.toArray(d3);
					Integer[] d4 = new Integer[buf4.size()];
					buf4.toArray(d4);
					Integer[] d5 = new Integer[buf5.size()];
					buf5.toArray(d5);
					Integer[] d6 = new Integer[buf6.size()];
					buf6.toArray(d6);
					Integer[] d7 = new Integer[buf7.size()];
					buf7.toArray(d7);
					Integer[] d8 = new Integer[buf8.size()];
					buf8.toArray(d8);
					Integer[] d9 = new Integer[buf9.size()];
					buf9.toArray(d9);
					Integer[] d10 = new Integer[buf10.size()];
					buf10.toArray(d10);
					Integer[] d11 = new Integer[buf11.size()];
					buf11.toArray(d11);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("ecgDataBuffer0", d0);
					map.put("ecgDataBuffer1", d1);
					map.put("ecgDataBuffer2", d2);
					map.put("ecgDataBuffer3", d3);
					map.put("ecgDataBuffer4", d4);
					map.put("ecgDataBuffer5", d5);
					map.put("ecgDataBuffer6", d6);
					map.put("ecgDataBuffer7", d7);
					map.put("ecgDataBuffer8", d8);
					map.put("ecgDataBuffer9", d9);
					map.put("ecgDataBuffer10", d10);
					map.put("ecgDataBuffer11", d11);
					map.put("ecg2DeviceData_time", ecg2Device_time);
					map.put("saveFileManualCount", saveFileManualCount);
					map.put("timeSecondCount", timeSecondCount);
					updataTimeCount();
					eThreadUtils.startManulTask(map);
					clearManualBuffer();
					saveCount = 0;
					isFileSave = false;
				}
			}
		}
		if (pace && paceVisible==0) {//
			for (int i = 0; i < data.length; i++) {
				data[i] = paceMaker;
			}
		}
		
		if (isDrawECGWave) {//绘制开关
			return;
		}
		
		bufferList.add(data);
	
		callData_count++;
		if (callData_count == 8) {
			synchronized (ecgDataBuffer) {
				ecgDataBuffer.addAll(bufferList);
				bufferList.clear();
			}
			callData_count = 0;
		}
	}
	private void clearSaveCache(){
		baseLineState = -1;
		bufferAuto.clear();
		saveCount = 0;
	}
	
	/**清除缓存空间**/
	private void clearCache() {
		synchronized (ecgDataBuffer) {
			ecgDataBuffer.clear();
		}
		currentX = 30;
	}

	class MyTimeTask extends TimerTask {

		@Override
		public void run() {
			if (bodyLeadState || bodyLeadState_GREEN) {//导联脱落；首次导联状态全部连接时
				waveCanvas.drawColor(Color.BLACK);
				drawBodyLeadSpot(bodyLeaData);
				return;
			}
			if (isRestartDraw) {
				hrDetect.initHr(3495.2533333f * 3);
				DrawBackBmp();
				isRestartDraw = false;
			}
			
//			if (waveGainChangeFlag){
//				DrawBackBmp();
//				waveGainChangeFlag = false;
//				currentX = 30;
//			}
			if (isDrawECGWave) {
				mdlists.clear();
				return;
			}
			if(ecgDataBuffer.size()> (STEP)){
				synchronized(ecgDataBuffer) {
					int num= ecgDataBuffer.size();
					for (int i = num - 1; i >= 0; i--) {
						mdlists.add(ecgDataBuffer.removeFirst());
					}
				}
				SimpleDraw(mdlists);
				mdlists.clear();
			}
		}
	}
	
	private void showFilterDialog(){
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.current_filter_setting, (ViewGroup) findViewById(R.id.current_filter_dialog));
		hp_group = (RadioGroup) layout.findViewById(R.id.hp_group);
		mc_group = (RadioGroup) layout.findViewById(R.id.mc_group);
		ac_group = (RadioGroup) layout.findViewById(R.id.ac_group);
		lp_group = (RadioGroup) layout.findViewById(R.id.lp_group);
		
		if (FilterBase==2) {
			((RadioButton)hp_group.getChildAt(0)).setChecked(true);
		}else if (FilterBase==3) {
			((RadioButton)hp_group.getChildAt(1)).setChecked(true);
		}else if (FilterBase==4) {
			((RadioButton)hp_group.getChildAt(2)).setChecked(true);
		}else if (FilterBase==1) {
			((RadioButton)hp_group.getChildAt(3)).setChecked(true);
		}
		if (FilterMC==12) {
			((RadioButton)mc_group.getChildAt(0)).setChecked(true);
		}else if (FilterMC==13) {
			((RadioButton)mc_group.getChildAt(1)).setChecked(true);
		}else if (FilterMC==14) {
			((RadioButton)mc_group.getChildAt(2)).setChecked(true);
		}else if (FilterMC==11) {
			((RadioButton)mc_group.getChildAt(3)).setChecked(true);
		}
		if (FilterAC==22) {
			((RadioButton)ac_group.getChildAt(0)).setChecked(true);
		}else if (FilterAC==23) {
			((RadioButton)ac_group.getChildAt(1)).setChecked(true);
		}else if (FilterAC==21) {
			((RadioButton)ac_group.getChildAt(2)).setChecked(true);
		}
		if (FilterLP==37) {//25hz
			((RadioButton)lp_group.getChildAt(0)).setChecked(true);
		}else if (FilterLP==36) {//40hz
			((RadioButton)lp_group.getChildAt(1)).setChecked(true);
		}else if (FilterLP==32) {//75hz
			((RadioButton)lp_group.getChildAt(2)).setChecked(true);
		}else if (FilterLP==33) {//100hz
			((RadioButton)lp_group.getChildAt(3)).setChecked(true);
		}else if (FilterLP==35) {//150hz
			((RadioButton)lp_group.getChildAt(4)).setChecked(true);
		}else if (FilterLP==31) {//OFF
			((RadioButton)lp_group.getChildAt(5)).setChecked(true);
		}
		
		final AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
		imageDialog.setTitle("滤波设置").setIcon(android.R.drawable.btn_star).setView(layout);
		imageDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				//基线滤波选择
				if (R.id.hp_005hz==hp_group.getCheckedRadioButtonId()) {
					FilterBase = 2;	
					filter2TV.setText("0.05Hz");
				}else if (R.id.hp_025hz==hp_group.getCheckedRadioButtonId()) {
					FilterBase = 3;
					filter2TV.setText("0.25Hz");
				}else if (R.id.hp_050hz==hp_group.getCheckedRadioButtonId()) {
					FilterBase = 4;
					filter2TV.setText("0.50Hz");
				}else if (R.id.hp_off==hp_group.getCheckedRadioButtonId()) {
					FilterBase = 1;
					filter2TV.setText("OFF");
				}
				//肌电滤波选择
				if (R.id.mc_25hz==mc_group.getCheckedRadioButtonId()) {
					FilterMC = 12;
					filter3TV.setText("25Hz");
				}else if (R.id.mc_35hz==mc_group.getCheckedRadioButtonId()) {
					FilterMC = 13;	
					filter3TV.setText("35Hz");
				}else if (R.id.mc_45hz==mc_group.getCheckedRadioButtonId()) {
					FilterMC = 14;
					filter3TV.setText("45Hz");
				}else if (R.id.mc_off==mc_group.getCheckedRadioButtonId()) {
					FilterMC = 11;	
					filter3TV.setText("OFF");
				}
				//工作滤波选择
				if (R.id.ac_50hz==ac_group.getCheckedRadioButtonId()) {
					FilterAC = 22;
					filter1TV.setText("50Hz");
				}else if (R.id.ac_60hz==ac_group.getCheckedRadioButtonId()) {
					FilterAC = 23;
					filter1TV.setText("60Hz");
				}else if (R.id.ac_off==ac_group.getCheckedRadioButtonId()) {
					FilterAC = 21;
					filter1TV.setText("OFF");
				}
				//低通滤波
				if (R.id.lp_75hz==lp_group.getCheckedRadioButtonId()) {
					FilterLP = 32;
					filter4TV.setText("75Hz");
				}else if (R.id.lp_100hz==lp_group.getCheckedRadioButtonId()) {
					FilterLP = 33;
					filter4TV.setText("100Hz");
				}else if (R.id.lp_150hz==lp_group.getCheckedRadioButtonId()) {
					FilterLP = 34;
					filter4TV.setText("150Hz");
				}else if (R.id.lp_off==lp_group.getCheckedRadioButtonId()) {
					FilterLP = 31;
					filter4TV.setText("OFF");
				}else if (R.id.lp_40hz==lp_group.getCheckedRadioButtonId()) {
					FilterLP = 35;
					filter4TV.setText("40hz");
				}else if (R.id.lp_25hz==lp_group.getCheckedRadioButtonId()) {
					FilterLP = 37;
					filter4TV.setText("25Hz");
				}

				ecgFilter.initFilter(FilterBase, FilterMC, FilterAC, FilterLP);
				hrDetect.initHr(3495.2533333f * 3);
				isDrawECGWave = false;
				initSaveData();
				currentX = 30;
				arg0.dismiss();
			}
		});
		imageDialog.create();
		imageDialog.setCancelable(false);
		imageDialog.show();
	}

	private void updataTimeCount(){
		ECGDataLong2DeviceDao.getInstance(idCardNo).updateECGDataLong(ecg2Device_time, saveFileManualCount, hrTV.getText().toString().trim());
	}
	private void deleteLongTime(){
		ECGDataLong2DeviceDao.getInstance(idCardNo).deleteECGDataL(ecg2Device_time);
	}
	private void initSaveData(){
		timeStarTV.setText("");
		if (saveFile_AutoManual==0) {
			saveFileTotalCount = 5000;
			isFileSave = true;
			saveCount = 0;
			bufferAuto.clear();
			paceRecBuffer = new int[5000];
		}else if (saveFile_AutoManual==1) {
			paceRecBuffer = new int[60 * 500];
			saveFileTotalCount = 60 * 500;
			saveCount = 0;
			if ("停止".equals(startBtn.getText().toString())) {
				baseLineState = 0;
				isFileSave = true;
			}
		}
	}
	private void initManualBuffer(){
		buf0 = new ArrayList<Integer>();
		buf1 = new ArrayList<Integer>();
		buf2 = new ArrayList<Integer>();
		buf3 = new ArrayList<Integer>();
		buf4 = new ArrayList<Integer>();
		buf5 = new ArrayList<Integer>();
		buf6 = new ArrayList<Integer>();
		buf7 = new ArrayList<Integer>();
		buf8 = new ArrayList<Integer>();
		buf9 = new ArrayList<Integer>();
		buf10 = new ArrayList<Integer>();
		buf11 = new ArrayList<Integer>();
	}
	private void clearManualBuffer(){
		buf0.clear();
		buf1.clear();
		buf2.clear();
		buf3.clear();
		buf4.clear();
		buf5.clear();
		buf6.clear();
		buf7.clear();
		buf8.clear();
		buf9.clear();
		buf10.clear();
		buf11.clear();
	}
}
