package com.wehealth.mesurecg.activity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.wehealth.mesurecg.BaseActivity;
import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.adapter.BloodModelAdapter;
import com.wehealth.mesurecg.utils.CommUtils;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.mesurecg.utils.ToastUtil;
import com.wehealth.mesurecg.view.GraphicView;
import com.wehealth.model.domain.enumutil.BloodSugarType;
import com.wehealth.model.domain.enumutil.ViewStyle;
import com.wehealth.model.domain.model.AuthToken;
import com.wehealth.model.domain.model.BloodSugar;
import com.wehealth.model.domain.model.ResultPassHelper;
import com.wehealth.model.interfaces.inter_register.WeHealthPatient;
import com.wehealth.model.util.Constant;
import com.wehealth.model.util.DateUtils;
import com.wehealth.model.util.NetWorkService;
import com.wehealth.model.util.StringUtil;

public class BloodSugarActivity extends BaseActivity implements OnClickListener{

	private final int UPLOAD_SUCCESS = 10;
	private final int UPLOAD_FAILED = 9;
	private final int REFRESH_NO_DATA = 11;
	private final int REFRESH_SUCCESS = 12;

	private int type = -1;
	private View localView;
	private TextView typeBigTV, typeTimePNTV, typeTimeTV, tv1;
	private EditText sugarSumET;
	private BloodModelAdapter bloodAdapter;
	private List<BloodSugar> bSugarList;
	private BloodSugar bloodSugar;
	private boolean update;//是否是更新的数据
	private GraphicView scatView;
	private RelativeLayout titleLayout, backLayout;
	private Button save;//, okBtn, cancelBtn
	private String[] dataStrings;
	private String[] typeClass = new String[]{"早餐","午餐","晚餐", "睡前","其它"};
	private String[] typeTime = new String[]{"前","后一小时","后两小时"};
	private PopupWindow popupWindow;
	
	private List<String> listXLabels;
	private String idCardNo;

	private ProgressDialog pDialog;
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case UPLOAD_SUCCESS://上传成功
				if (!isFinishing() && pDialog!=null) {
					pDialog.dismiss();
					ToastUtil.showShort(BloodSugarActivity.this, "上传成功");
					bSugarList = (List<BloodSugar>) msg.obj;
					sugarSumET.setText("");
					bloodSugar = null;
					scatView.setVisibility(View.VISIBLE);
					initDrawLine(false);
				}
				break;
			case UPLOAD_FAILED://上传失败
				if (!isFinishing() && pDialog!=null) {
					pDialog.dismiss();
					isWait("上传失败，请查看网络环境是否良好");
				}
				break;
			case REFRESH_NO_DATA://首次进来后没有血糖数据
				if (!isFinishing() && pDialog!=null) {
					pDialog.dismiss();
					isWait("今天您还未记录血糖值，赶快记录吧！");
					scatView.setVisibility(View.GONE);
				}
				break;
			case REFRESH_SUCCESS:
				if (!isFinishing() && pDialog!=null) {
					pDialog.dismiss();
					bSugarList = (List<BloodSugar>) msg.obj;
					scatView.setVisibility(View.VISIBLE);
					initDrawLine(true);
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
		setContentView(R.layout.blood_sugar);
		
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);
		idCardNo = PreferUtils.getIntance().getIdCardNo();
		initView();
		
		reflushBackgroud();
	}
	
	private void initView() {
		backLayout = (RelativeLayout) findViewById(R.id.backgroud);
		titleLayout = (RelativeLayout) findViewById(R.id.title_layout);
		save = (Button) findViewById(R.id.btn_save);
		typeBigTV = (TextView) findViewById(R.id.blood_sugar_type_class);
		typeTimePNTV = (TextView) findViewById(R.id.blood_sugar_type_classtime);
		typeTimeTV = (TextView) findViewById(R.id.blood_sugar_type_time);
		sugarSumET = (EditText) findViewById(R.id.blood_sugar_num);
		tv1 = (TextView) findViewById(R.id.blood_s_t1);
		scatView = (GraphicView) findViewById(R.id.blood_sugar_scat);
		scatView.setVisibility(View.GONE);
		
		save.setOnClickListener(this);
		typeBigTV.setOnClickListener(this);
		typeTimePNTV.setOnClickListener(this);
		typeTimeTV.setOnClickListener(this);

		initData();
	}
	
	private void initData() {
		bSugarList = new ArrayList<BloodSugar>();
		
		StringUtil.getSugarType();
		listXLabels = StringUtil.getListSugarType();
		
		bloodAdapter = new BloodModelAdapter(this);
		
		int hourOfDay = StringUtil.getHourIntByDate(new Date());
		if (hourOfDay<8) {//早餐前
			typeBigTV.setText("早餐");
			typeTimePNTV.setText("前");
		}
		if (hourOfDay>=8 && hourOfDay<11) {//早餐后一小时
			typeBigTV.setText("早餐");
			typeTimePNTV.setText("后一小时");
		}
		if (hourOfDay>=11 && hourOfDay<=12) {//午餐前
			typeBigTV.setText("午餐");
			typeTimePNTV.setText("前");
		}
		if (hourOfDay>12 && hourOfDay<16) {//午餐后
			typeBigTV.setText("午餐");
			typeTimePNTV.setText("后一小时");
		}
		if (hourOfDay>=16&& hourOfDay<19) {//晚餐前
			typeBigTV.setText("晚餐");
			typeTimePNTV.setText("前");
		}
		if (hourOfDay>=19&& hourOfDay<21) {//晚餐后
			typeBigTV.setText("晚餐");
			typeTimePNTV.setText("后一小时");
		}
		if (hourOfDay>=21) {//睡前
			typeBigTV.setText("睡前");
			typeTimePNTV.setText("前");
		}
		
		getSugarData();
	}
	/**将血糖数据以散列点显示**/
	protected void initDrawLine(boolean updata) {
		
		if (bSugarList==null || bSugarList.isEmpty()) {
			return;
		}
		Double[] x1 = new Double[bSugarList.size()];
		Double[] y1 = new Double[bSugarList.size()];
		for (int i = 0; i < bSugarList.size(); i++) {
			if (bSugarList.get(i).getType()!=BloodSugarType.other) {
				x1[i] = bSugarList.get(i).getType().ordinal()+0.8;
			}else {
				x1[i] = bSugarList.get(i).getType().ordinal()+1.0;
			}
			y1[i] = bSugarList.get(i).getNumber();
		}
		scatView.setData(y1, x1, listXLabels, 50, 10, 12, ViewStyle.Scatter, " ");
		scatView.invalidate();
	}
	/**从服务器获取血糖数据**/
	private void getSugarData() {
		if (!NetWorkService.isNetWorkConnected(this)) {
			Toast.makeText(this, "网络不可用，请检查", Toast.LENGTH_LONG).show();
			return;
		}
		if (TextUtils.isEmpty(idCardNo)) {
			return;
		}
		
		pDialog.setMessage("正在获取数据...");
		pDialog.show();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				List<BloodSugar> sugars = getTodaySugar();
				
				Message msg = handler.obtainMessage();
				if (sugars==null ||sugars.isEmpty()) {
					msg.what =REFRESH_NO_DATA;
					msg.sendToTarget();
				}else {
					msg.what = REFRESH_SUCCESS;
					msg.obj = sugars;
					msg.sendToTarget();
				}
			}
		}).start();
	}
	/**获取今天的血糖数据**/
	protected List<BloodSugar> getTodaySugar() {
		String currentDate = DateUtils.sdf_yyyy_MM_dd.format(new Date());
		Date startDate = null;
		Date enDate = null;
		try {
			startDate = DateUtils.sdf_yyyy_MM_dd_HHmm.parse(currentDate+" 00:00");
			enDate = DateUtils.sdf_yyyy_MM_dd_HHmm.parse(currentDate + " 23:59");
		} catch (ParseException e) {
			e.printStackTrace();
			startDate = new Date();
			enDate = new Date();
		}
		AuthToken token = CommUtils.refreshToken();
		List<BloodSugar> sugars = NetWorkService.createApi(WeHealthPatient.class, PreferUtils.getIntance().getServerUrl()).queryBloodSugar("Bearer " + token.getAccess_token(), startDate.getTime(), enDate.getTime(), idCardNo, null, startDate.getTime(), enDate.getTime(),null, null);
		
		return sugars;
	}

	private void reflushBackgroud() {
			backLayout.setBackgroundColor(getResources().getColor(R.color.page_background_color));
			titleLayout.setBackgroundColor(getResources().getColor(R.color.page_title_bar_color));
			sugarSumET.setBackground(getResources().getDrawable(R.drawable.blood_bs_bg0));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_save:
			if (!NetWorkService.isNetWorkConnected(this)) {
				Toast.makeText(this, "网络不可用，请检查网络", Toast.LENGTH_LONG).show();
				return;
			}
			String sugarNumStr = sugarSumET.getText().toString();
			if (TextUtils.isEmpty(sugarNumStr)) {
				Toast.makeText(this, "请输入血糖值！", Toast.LENGTH_LONG).show();
				return;
			}
			double sugarNum = 0;
			try {
				sugarNum = Double.valueOf(sugarNumStr);
			} catch (Exception e) {
				e.printStackTrace();
				sugarNum = 0;
			}
			if (sugarNum > 50 || sugarNum<=0) {
				Toast.makeText(this, "请输入正确的血糖值", Toast.LENGTH_SHORT).show();
				return;
			}
			BigDecimal bd = new BigDecimal(sugarNum);   
			bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP); 
			String strType = typeBigTV.getText().toString();
			String strTypePN = typeTimePNTV.getText().toString();
			bloodSugar = new BloodSugar();
			if (strType.equals("早餐")) {
				if (strTypePN.equals("前")) {
					bloodSugar.setType(BloodSugarType.beforeBreakfast);
				}else if (strTypePN.equals("后一小时")) {
					bloodSugar.setType(BloodSugarType.afterBreakfastOneHour);
				}else if (strTypePN.equals("后两小时")) {
					bloodSugar.setType(BloodSugarType.afterBreakfastTwoHour);
				}
			}else if (strType.equals("午餐")) {
				if (strTypePN.equals("前")) {
					bloodSugar.setType(BloodSugarType.beforeLunch);
				}else if (strTypePN.equals("后一小时")) {
					bloodSugar.setType(BloodSugarType.afterLunchOneHour);
				}else if (strTypePN.equals("后两小时")) {
					bloodSugar.setType(BloodSugarType.afterLunchTwoHour);
				}
			}else if (strType.equals("晚餐")) {
				if (strTypePN.equals("前")) {
					bloodSugar.setType(BloodSugarType.beforeSupper);
				}else if (strTypePN.equals("后一小时")) {
					bloodSugar.setType(BloodSugarType.afterSupperOneHour);
				}else if (strTypePN.equals("后两小时")) {
					bloodSugar.setType(BloodSugarType.afterSupperTwoHour);
				}
			}else if (strType.equals("睡前")) {
				if (strTypePN.equals("前")) {
					bloodSugar.setType(BloodSugarType.beforeSleep);
				}
			}else if (strType.equals("其它")) {
				bloodSugar.setType(BloodSugarType.other);
				Date date = new Date();
				String d1 = DateUtils.sdf_yyyy_MM_dd.format(date);
				String d2 = typeTimeTV.getText().toString();
				bloodSugar.setNote(d1+d2);
			}
			update = false;//是否是更新
			if (bSugarList!=null && !bSugarList.isEmpty()) {
				for (BloodSugar bs : bSugarList) {
					if (bs.getType().ordinal()==bloodSugar.getType().ordinal()) {
						bloodSugar = bs;
						update = true;
						break;
					}
				}
			}
			bloodSugar.setCreateTime(new Date());
			bloodSugar.setUpdateTime(new Date());
			bloodSugar.setTestTime(new Date());
			bloodSugar.setNumber(bd.doubleValue());
			bloodSugar.setPatientId(idCardNo);
			
			pDialog.setMessage("正在上传数据...");
			pDialog.show();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					ResultPassHelper rph = null;
					try{
						AuthToken token = CommUtils.refreshToken();
						if (update) {//更新这一时间段的血糖值
							rph = NetWorkService.createApi(WeHealthPatient.class, PreferUtils.getIntance().getServerUrl()).updateBloodSugar("Bearer " + token.getAccess_token(), bloodSugar);
						}else {
							rph = NetWorkService.createApi(WeHealthPatient.class, PreferUtils.getIntance().getServerUrl()).createBloodSugar("Bearer " + token.getAccess_token(), bloodSugar);
						}
					}catch (Exception e){
						e.printStackTrace();
					}
//					handler.sendEmptyMessage();

					Message msg = handler.obtainMessage();
					if (rph!=null && rph.getName().equals(Constant.SUCCESS)) {
						List<BloodSugar> bss = getTodaySugar();
						msg.obj = bss;
						msg.what=UPLOAD_SUCCESS;
					}else {
						msg.what=UPLOAD_FAILED;
					}
					msg.sendToTarget();
				}
			}).start();
			break;
		case R.id.blood_sugar_type_class:
			if (popupWindow!=null && popupWindow.isShowing()) {
				popupWindow.dismiss();
			}
			showDateSelection(typeBigTV, typeClass, 1);
			typeBigTV.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.green_arrow_up, 0);
			break;
		case R.id.blood_sugar_type_classtime:
			if (popupWindow!=null && popupWindow.isShowing()) {
				popupWindow.dismiss();
			}
			showDateSelection(typeTimePNTV, typeTime, 2);
			typeTimePNTV.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.green_arrow_up, 0);
			break;
		case R.id.blood_sugar_type_time:
			setDateTime("自定义的时间", typeTimeTV, 2);
			break;
//		case R.id.blood_sugar_num:
//			wheeLayout.setVisibility(View.VISIBLE);
//			wheelView.setCurrentItem(60);
//			break;
//		case R.id.blood_sugar_numcancel:
//			wheeLayout.setVisibility(View.GONE);
//			break;
//		case R.id.blood_sugar_numok:
//			BloodModel bm = adapter.getBM(wheelView.getCurrentItem());
//			if (bm==null) {
//				UIHelper.showToast(this, "您没有选中值", 1);
//				return;
//			}
//			sugarSumTV.setText(bm.getDoubleValue()+"");
//			wheeLayout.setVisibility(View.GONE);
//			break;

		default:
			break;
		}
	}
	
	public void onBackBottonClick(View view) {
		finish();
	}
	
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	private void showDateSelection(final TextView dateFilter, final String[] type, final int i) {
		localView = this.getLayoutInflater().inflate(R.layout.blood_model_type, null);
		popupWindow = new PopupWindow(localView, dateFilter.getWidth(), 320);
		ListView localListView = (ListView) localView.findViewById(R.id.filter_spinner_list);
		bloodAdapter.setNameList(type);
		localListView.setAdapter(bloodAdapter);
		localListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				popupWindow.dismiss();
				String timeStr = type[position];
				dateFilter.setText(timeStr);
				if (i==1 && timeStr.equals("其它")) {
					typeTimePNTV.setVisibility(View.GONE);
					typeTimeTV.setVisibility(View.VISIBLE);
					typeTimeTV.setText(DateUtils.sdf_HH_mm.format(new Date()));
				}else if(i==1 && timeStr.equals("睡前")){
					typeTimePNTV.setEnabled(false);
					typeTimePNTV.setText("前");
					typeTimePNTV.setVisibility(View.VISIBLE);
					typeTimeTV.setVisibility(View.GONE);
				}else {
					typeTimePNTV.setEnabled(true);
					typeTimePNTV.setVisibility(View.VISIBLE);
					typeTimeTV.setVisibility(View.GONE);
				}
			}
		});
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				dateFilter.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						R.drawable.black_arrow_down, 0);
			}

		});
		popupWindow.showAsDropDown(dateFilter, 0, 1);
	}

	private void setDateTime(String string, final TextView tv, final int i) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.date_time_dialog, null);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        if (i==2) {
        	datePicker.setVisibility(View.GONE);
		}else if(i==1){
			datePicker.setVisibility(View.VISIBLE);
		}
        builder.setView(view);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);

        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(Calendar.MINUTE);
        final int inType = tv.getInputType();
        tv.setInputType(InputType.TYPE_NULL);
        tv.setInputType(inType);
        
        builder.setTitle(string);
        builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
       	 
        	@Override
       	 	public void onClick(DialogInterface dialog, int which) {
       		 
        		StringBuffer sb = new StringBuffer();
        		if (i==2) {
					
				}else if (i==1) {
					sb.append(String.format("%d-%02d-%02d",
	        				datePicker.getYear(),
	        				datePicker.getMonth() + 1,
	        				datePicker.getDayOfMonth()));
				}
        		sb.append(" ");
       		 	sb.append(timePicker.getCurrentHour()).append(":").append(timePicker.getCurrentMinute());
       		 	tv.setText(sb);
       		 	dialog.cancel();
        	}
        });
        Dialog dialog = builder.create();
        dialog.show();
	}

}
