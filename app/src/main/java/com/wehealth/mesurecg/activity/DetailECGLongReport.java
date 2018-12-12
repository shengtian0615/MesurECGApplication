//package com.wehealth.mesurecg.activity;
//
//import java.text.SimpleDateFormat;
//import java.util.Map;
//
//import com.wehealth.ecgequipment.BaseFragmentActivity;
//import com.wehealth.ecgequipment.R;
//import com.wehealth.ecgequipment.fragment.ECGWaveFragment;
//import com.wehealth.ecgequipment.model.ECGDataLong;
//import com.wehealth.ecgequipment.util.FileUtils;
//import com.wehealth.ecgequipment.util.PreferenceUtils;
//import com.wehealth.shared.datamodel.ECGData;
//import com.wehealth.shared.datamodel.util.DataUtil;
//import com.wehealth.shared.datamodel.util.ZipUtil;
//
//import android.annotation.SuppressLint;
//import android.app.ProgressDialog;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class DetailECGLongReport extends BaseFragmentActivity implements OnClickListener{
//
//	private TextView pageNum;
//	private Button beforeBtn, afterBtn;
//	private String path;
//	private RelativeLayout backLayout, titleLayout;
//	private ProgressDialog pDialog;
////	private StringBuffer ch0SB;
////	private StringBuffer ch1SB;
////	private StringBuffer ch2SB;
////	private StringBuffer ch3SB;
////	private StringBuffer ch4SB;
////	private StringBuffer ch5SB;
////	private StringBuffer ch6SB;
////	private StringBuffer ch7SB;
////	private StringBuffer ch8SB;
////	private StringBuffer ch9SB;
////	private StringBuffer ch10SB;
////	private StringBuffer ch11SB;
////	private ECGData ecgData;
//	private Map<String, short[]> map;
//	/**获取的页数**/
//	private int type = -1;
////	private ECGReadFragment readFragment;
//	/**每页的数据量**/
//	private int pageOfNum;
//	/**总得页数**/
//	private int pageAllSum = 13;
//	private String pageTV;
//
//	private final int PARSE_ECGDATA_OVER = 100;
//	private final int PARSE_ECGDATA_ISEMPTY = 101;
//	private final int PARSE_ECGDATA_ISNULL = 102;
//
//	@SuppressLint("SimpleDateFormat")
//	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//	private String sdk = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ECGDATA/XML/";
//	private ECGDataLong ecgDataLong;
//
//
//	@SuppressLint("HandlerLeak")
//	Handler handler = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case PARSE_ECGDATA_OVER:
//				if (pDialog.isShowing()) {
//					pDialog.dismiss();
//				}
//				ECGData data = (ECGData) msg.obj;
//				drawECGView(data);
//				break;
//			case PARSE_ECGDATA_ISEMPTY:
//				if (pDialog.isShowing()) {
//					pDialog.dismiss();
//				}
//				Toast.makeText(DetailECGLongReport.this, "本地心电数据文件已经删除", Toast.LENGTH_SHORT).show();
//				DetailECGLongReport.this.finish();
//				break;
//			case PARSE_ECGDATA_ISNULL:
//				if (pDialog.isShowing()) {
//					pDialog.dismiss();
//				}
//				Toast.makeText(DetailECGLongReport.this, "没有获取到心电数据", Toast.LENGTH_SHORT).show();
//				break;
//
//			default:
//				break;
//			}
//		}
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.ecg_long_show);
//		TextView titleView = (TextView) findViewById(R.id.page_title);
//		titleView.setText(R.string.ecg_long_history_report);
//		backLayout = (RelativeLayout) findViewById(R.id.backlayout);
//		titleLayout = (RelativeLayout) findViewById(R.id.title_bar);
//
//		initView();
//		reflushBackgroud();
//
//		ecgDataLong = (ECGDataLong) getIntent().getSerializableExtra("ecg_long");
//		path = sdk+sdf.format(ecgDataLong.getTesTime())+".xml";
//		if (path==null) {
//			Toast.makeText(this, "本地心电数据文件已经删除", Toast.LENGTH_SHORT).show();
//			this.finish();
//			return;
//		}
//		pDialog.show();
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				map = FileUtils.parseListSB(path);
//				type = 1;
//				if (map==null || map.isEmpty()) {
//					handler.sendEmptyMessage(PARSE_ECGDATA_ISEMPTY);
//					return;
//				}
//				ECGData ecgData = getECGDataByMap(type, map);
//				if (ecgData!=null) {
//					Message msg = handler.obtainMessage(PARSE_ECGDATA_OVER);
//					msg.obj = ecgData;
//					handler.sendMessageDelayed(msg, 600);
//				}else {
//					handler.sendEmptyMessage(PARSE_ECGDATA_ISNULL);
//				}
//			}
//		}).start();
//	}
//
//	private void initView() {
//		pageNum = (TextView) findViewById(R.id.long_show_page);
//		beforeBtn = (Button) findViewById(R.id.long_show_before);
//		afterBtn = (Button) findViewById(R.id.long_show_after);
//
//		pDialog = new ProgressDialog(this);
//		String message = getString(R.string.loading_data);
//		pDialog.setMessage(message);
//		pDialog.setCancelable(false);
//		pageTV = getString(R.string.page);
//		beforeBtn.setOnClickListener(this);
//		afterBtn.setOnClickListener(this);
//	}
//
//	private void reflushBackgroud() {
//		if (PreferenceUtils.getInstance(this).getChangeStyle()) {
//			backLayout.setBackgroundColor(getResources().getColor(R.color.page_background_color));
//			titleLayout.setBackgroundColor(getResources().getColor(R.color.page_title_bar_color));
//		} else {
//			backLayout.setBackgroundColor(getResources().getColor(R.color.text_white));
//			titleLayout.setBackgroundColor(getResources().getColor(R.color.background_yellow));
//		}
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.long_show_after:
//			if (type>pageAllSum-1) {
//				Toast.makeText(this, "已经是心电数据最后一页了", Toast.LENGTH_SHORT).show();
//				return;
//			}
//			type++;
//			if (map==null || map.isEmpty()) {
//				return;
//			}
//			pDialog.show();
//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					ECGData ecgData = getECGDataByMap(type, map);
//					if (ecgData!=null) {
////						handler.sendEmptyMessageDelayed(PARSE_ECGDATA_OVER, 600);
//						Message msg = handler.obtainMessage(PARSE_ECGDATA_OVER);
//						msg.obj = ecgData;
//						handler.sendMessageDelayed(msg, 600);
//					}else {
//						handler.sendEmptyMessageDelayed(PARSE_ECGDATA_ISNULL,600);
//					}
//				}
//			}).start();
//			pageNum.setText(type+""+pageTV);
//			break;
//		case R.id.long_show_before:
//
//			if (type<=1) {
//				Toast.makeText(this, "已经是心电数据第一页了", Toast.LENGTH_SHORT).show();
//				return;
//			}
//			if (map==null || map.isEmpty()) {
//				return;
//			}
//			type--;
//			pDialog.show();
//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					ECGData ecgData = getECGDataByMap(type, map);
//					if (ecgData!=null) {
//						Message msg = handler.obtainMessage(PARSE_ECGDATA_OVER);
//						msg.obj = ecgData;
//						handler.sendMessageDelayed(msg, 600);
////						handler.sendEmptyMessage(PARSE_ECGDATA_OVER);
//					}else {
//						handler.sendEmptyMessageDelayed(PARSE_ECGDATA_ISNULL, 600);
//					}
//				}
//			}).start();
//			pageNum.setText(type+""+pageTV);
//			break;
//
//		default:
//			break;
//		}
//	}
//
//	protected void drawECGView(ECGData ecgData) {
//		ECGWaveFragment waveFragment = new ECGWaveFragment();
//		Bundle bundle = new Bundle();
//		bundle.putByteArray("avr", ecgData.getaVr());
//		bundle.putByteArray("avf", ecgData.getaVf());
//		bundle.putByteArray("avl", ecgData.getaVl());
//		bundle.putByteArray("avr", ecgData.getaVr());
//		bundle.putByteArray("v1", ecgData.getV1());
//		bundle.putByteArray("v2", ecgData.getV2());
//		bundle.putByteArray("v3", ecgData.getV3());
//		bundle.putByteArray("v4", ecgData.getV4());
//		bundle.putByteArray("v5", ecgData.getV5());
//		bundle.putByteArray("v6", ecgData.getV6());
//		bundle.putByteArray("vi", ecgData.getvI());
//		bundle.putByteArray("vii", ecgData.getvII());
//		bundle.putByteArray("viii", ecgData.getvIII());
//		bundle.putBoolean("style", !PreferenceUtils.getInstance(this).getChangeStyle());
//		waveFragment.setArguments(bundle);
//		getSupportFragmentManager().beginTransaction()
//		.replace(R.id.long_ecg_view, waveFragment).show(waveFragment)
//		.commit();
//		pageNum.setText(type+""+pageTV);
//	}
//
//	/****
//	 * 根据t获取StringBuffer中的数据
//	 * @param t
//	 * @param mapSB
//	 * @return
//	 */
//	private ECGData getECGDataByMap(int t, Map<String, short[]> mapSB){
//		ECGData ed = null;
//		if (t>=0 && t<=pageAllSum) {
//			ed = new ECGData();
//			short[] ch0 = mapSB.get("I_Ch0");//获取数据
//			short[] ch1 = mapSB.get("II_Ch1");
//			short[] ch2 = mapSB.get("III_Ch2");
//			short[] ch3 = mapSB.get("avR_Ch3");
//			short[] ch4 = mapSB.get("avL_Ch4");
//			short[] ch5 = mapSB.get("avF_Ch5");
//			short[] ch6 = mapSB.get("v1_Ch6");
//			short[] ch7 = mapSB.get("v2_Ch7");
//			short[] ch8 = mapSB.get("v3_Ch8");
//			short[] ch9 = mapSB.get("v4_Ch9");
//			short[] ch10 = mapSB.get("v5_Ch10");
//			short[] ch11 = mapSB.get("v6_Ch11");
//
//			pageOfNum = ch0.length/pageAllSum;//算出每页的数据量
//
//			short[] ch0Page = new short[pageOfNum];//每页每导的数据量
//			short[] ch1Page = new short[pageOfNum];//每页每导的数据量
//			short[] ch2Page = new short[pageOfNum];//每页每导的数据量
//			short[] ch3Page = new short[pageOfNum];//每页每导的数据量
//			short[] ch4Page = new short[pageOfNum];//每页每导的数据量
//			short[] ch5Page = new short[pageOfNum];//每页每导的数据量
//			short[] ch6Page = new short[pageOfNum];//每页每导的数据量
//			short[] ch7Page = new short[pageOfNum];//每页每导的数据量
//			short[] ch8Page = new short[pageOfNum];//每页每导的数据量
//			short[] ch9Page = new short[pageOfNum];//每页每导的数据量
//			short[] ch10Page = new short[pageOfNum];//每页每导的数据量
//			short[] ch11Page = new short[pageOfNum];//每页每导的数据量
//
//			int initValues = (t-1)*pageOfNum;
//			for (int i = (t-1)*pageOfNum; i < t*pageOfNum; i++) {
//				ch0Page[i-initValues] = ch0[i];
//				ch1Page[i-initValues] = ch1[i];
//				ch2Page[i-initValues] = ch2[i];
//				ch3Page[i-initValues] = ch3[i];
//				ch4Page[i-initValues] = ch4[i];
//				ch5Page[i-initValues] = ch5[i];
//				ch6Page[i-initValues] = ch6[i];
//				ch7Page[i-initValues] = ch7[i];
//				ch8Page[i-initValues] = ch8[i];
//				ch9Page[i-initValues] = ch9[i];
//				ch10Page[i-initValues] = ch10[i];
//				ch11Page[i-initValues] = ch11[i];
//			}
//			ed.setvI(ZipUtil.gZip(DataUtil.toByteArray(ch0Page)));
//			ed.setvII(ZipUtil.gZip(DataUtil.toByteArray(ch1Page)));
//			ed.setvIII(ZipUtil.gZip(DataUtil.toByteArray(ch2Page)));
//			ed.setaVr(ZipUtil.gZip(DataUtil.toByteArray(ch3Page)));
//			ed.setaVl(ZipUtil.gZip(DataUtil.toByteArray(ch4Page)));
//			ed.setaVf(ZipUtil.gZip(DataUtil.toByteArray(ch5Page)));
//			ed.setV1(ZipUtil.gZip(DataUtil.toByteArray(ch6Page)));
//			ed.setV2(ZipUtil.gZip(DataUtil.toByteArray(ch7Page)));
//			ed.setV3(ZipUtil.gZip(DataUtil.toByteArray(ch8Page)));
//			ed.setV4(ZipUtil.gZip(DataUtil.toByteArray(ch9Page)));
//			ed.setV5(ZipUtil.gZip(DataUtil.toByteArray(ch10Page)));
//			ed.setV6(ZipUtil.gZip(DataUtil.toByteArray(ch11Page)));
//		}
//		return ed;
//	}
//
//	public void onBackBottonClick(View view) {
//		finish();
//	}
//}
