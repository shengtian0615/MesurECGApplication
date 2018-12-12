package com.wehealth.mesurecg.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.adapter.BloodModelAdapter;
import com.wehealth.mesurecg.utils.CommUtils;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.mesurecg.view.GraphicView;
import com.wehealth.model.domain.enumutil.BloodSugarType;
import com.wehealth.model.domain.enumutil.ViewStyle;
import com.wehealth.model.domain.model.AuthToken;
import com.wehealth.model.domain.model.BloodSugar;
import com.wehealth.model.interfaces.inter_register.WeHealthPatient;
import com.wehealth.model.util.NetWorkService;
import com.wehealth.model.util.StringUtil;

@SuppressLint("SimpleDateFormat")
public class SugarHistoryFragment extends Fragment implements OnCheckedChangeListener, OnClickListener{

	private RadioGroup radioGroup;
//	private RadioButton dayBtn;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdfYM = new SimpleDateFormat("yyyy-MM");
	
	private View localView;
	private PopupWindow popupWindow;
	private BloodModelAdapter bloodAdapter;
	
	private GraphicView graphicView;
	
	private int maxOfMonth;
	private TextView sugarTypeTV, monthTV, scatTitle;
//	private FrameLayout frameLayout;
	private ProgressDialog progressDialog;
	private List<BloodSugar> listBloodSugar;
	private Map<Integer, String> map;
//	private Map<Integer, String> mapDay;
	private List<String> listXLabels;
	private String[] typeClass = new String[]{"早餐前","早餐后一小时","早餐后两小时","午餐前","午餐后一小时","午餐后两小时",
			"晚餐前","晚餐后一小时","晚餐后两小时","睡前","其它"};
	private int type_week_month=0;//type=1时，表示最近一周；type=2，表示按月查询。
//	private int sugarTypeClass = -1;
	private String idCardNo;
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 11:
				if (progressDialog!=null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
//				frameLayout.removeAllViews();
				listBloodSugar = (List<BloodSugar>) msg.obj;
				if (listBloodSugar == null || listBloodSugar.isEmpty()) {
					graphicView.setVisibility(View.GONE);
					isNoticeDialog("在选中的时间段里，没有获取到血糖数据");
					return;
				}
				graphicView.setVisibility(View.VISIBLE);
				Bundle b = msg.getData();
				int type = b.getInt("type");
				drawScatterView(type);
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.history_blood_sugar, container, false);
	}


	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		radioGroup = (RadioGroup) getView().findViewById(R.id.history_blood_sugar_timetype);
//		frameLayout = (FrameLayout) getView().findViewById(R.id.history_blood_sugar_layout);
		sugarTypeTV = (TextView) getView().findViewById(R.id.history_sugar_type);
//		dayBtn = (RadioButton) getView().findViewById(R.id.history_time_sugar_day);
		monthTV = (TextView) getView().findViewById(R.id.history_sugar_month);
		scatTitle = (TextView) getView().findViewById(R.id.history_sugar_chart_title);
		graphicView = (GraphicView) getView().findViewById(R.id.history_blood_sugar_scat);
		graphicView.setVisibility(View.INVISIBLE);
		
		idCardNo = PreferUtils.getIntance().getIdCardNo();
		radioGroup.setOnCheckedChangeListener(this);
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setCancelable(false);
		bloodAdapter = new BloodModelAdapter(getActivity());
		
		map = StringUtil.getWeekLabel();
//		mapDay = StringUtils.getSugarType();
		
		String currentDate = sdf1.format(new Date());
		Date startDate = null;
		Date enDate = null;
		try {
			startDate = sdf.parse(currentDate + " 00:00");
			enDate = sdf.parse(currentDate + " 23:59");
		} catch (ParseException e) {
			e.printStackTrace();
			startDate = new Date();
		}
		
		String y_m = sdfYM.format(new Date());
		monthTV.setText(y_m);
		sugarTypeTV.setOnClickListener(this);
		monthTV.setOnClickListener(this);
		scatTitle.setText("今天血糖数据监测");
		if (!NetWorkService.isNetWorkConnected(getActivity())) {
			isNoticeDialog("网络不可用，请查看网络");
			return;
		}
		getBloodSugar(startDate.getTime(), enDate.getTime(), 1, null);
	}
	
	private void getBloodSugar(final long starTime, final long endTime, final int type, final BloodSugarType sugarType) {
		progressDialog.setMessage("正在获取数据...");
		progressDialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				List<BloodSugar> list = null;
				try{
					AuthToken token = CommUtils.refreshToken();
					list = NetWorkService.createApi(WeHealthPatient.class, PreferUtils.getIntance().getServerUrl()).queryBloodSugar(NetWorkService.bear+token.getAccess_token(),starTime, endTime,idCardNo, sugarType, starTime, endTime,null,null);
				}catch (Exception e){
					e.printStackTrace();
				}
				Message msg = handler.obtainMessage(11);
				Bundle bundle = new Bundle();
				bundle.putInt("type", type);
				msg.setData(bundle);
				msg.obj = list;
				msg.sendToTarget();
			}
		}).start();

	}
	
	/**画出散列点**/
	protected void drawScatterView(int type) {
		Double[] x = new Double[listBloodSugar.size()];
		Double[] y = new Double[listBloodSugar.size()];
		if (type==1) {//当天的血糖
			listXLabels = StringUtil.getListSugarType();
//			ScatterChartDayMonth dayScatterView = new ScatterChartDayMonth(getActivity());
//			dayScatterView.setSeriesDataset("血糖");
//			dayScatterView.initRenderer("当天血糖数据监测", 11, "", mapDay);
//			frameLayout.addView(dayScatterView.getBarChartView());
			for (int i = 0; i < listBloodSugar.size(); i++) {
				if (listBloodSugar.get(i).getType()!=BloodSugarType.other) {
					x[i] = listBloodSugar.get(i).getType().ordinal()+0.8;
				}else {
					x[i] = listBloodSugar.get(i).getType().ordinal()+1.0;
				}
				y[i] = listBloodSugar.get(i).getNumber();
//				dayScatterView.updateChart(x[i], y[i]);
			}
			graphicView.setData(y, x, listXLabels, 50, 10, 12, ViewStyle.Scatter, "");
			graphicView.invalidate();
			return;
		}
		if (type==2) {//一周内的血糖
			listXLabels = StringUtil.getListWeekLabel();
//			ScatterChartViewHelper weekScatterView = new ScatterChartViewHelper(getActivity());
//			weekScatterView.setSeriesDataset("血糖");
//			weekScatterView.initRenderer("一周血糖数据监测", 7, "时间(天)", map);
//			frameLayout.addView(weekScatterView.getBarChartView());
			for (int i = 0; i < listBloodSugar.size(); i++) {
				Date d = listBloodSugar.get(i).getCreateTime();
//				BloodSugarType sugarType = listBloodSugar.get(i).getType();
//				if (sugarTypeClass==1) {//全部时间
//					x[i] = StringUtils.getSugarDoubleByDate(map, d, sugarType);
//				}else{//按时间类别
				x[i] = StringUtil.getDoubleByDate2(map, d);
//				}
				y[i] = listBloodSugar.get(i).getNumber();
//				weekScatterView.updateChart(x[i], y[i]);
			}
			graphicView.setData(y, x, listXLabels, 50, 10, 7, ViewStyle.Scatter, " ");
			graphicView.invalidate();
			return;
		}
		if (type==3) {//按月查询的血糖
			listXLabels = StringUtil.getListMonthSugarType(maxOfMonth);
//			ScatterChartViewHelper monthScatterView = new ScatterChartViewHelper(getActivity());
//			monthScatterView.setSeriesDataset("血糖");
//			monthScatterView.initRenderer(currentMonth+"月血糖数据监测", maxOfMonth, "时间(日)", null);
//			frameLayout.addView(monthScatterView.getBarChartView());
			for (int i = 0; i < listBloodSugar.size(); i++) {
				Date d = listBloodSugar.get(i).getCreateTime();
//				BloodSugarType sugarType = listBloodSugar.get(i).getType();
//				if (sugarTypeClass==1) {//全部时间
//					x[i] = StringUtils.dayOfMonthToDoublePress(d, sugarType);
//				}else{//按时间类别
					x[i] = StringUtil.dayOfMonthToDouble2(d);
//				}
				y[i] = listBloodSugar.get(i).getNumber();
//				monthScatterView.updateChart(x[i], y[i]);
			}
			graphicView.setData(y, x, listXLabels, 50, 10, maxOfMonth+1, ViewStyle.Scatter, "日");
			graphicView.invalidate();
			return;
		}
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.history_time_sugar_day:
//			frameLayout.removeAllViews();
			sugarTypeTV.setVisibility(View.GONE);
			monthTV.setVisibility(View.GONE);
			String currentDate = sdf1.format(new Date());
			Date startDate = null;
			Date enDate = null;
			try {
				startDate = sdf.parse(currentDate + " 00:00");
				enDate = sdf.parse(currentDate + " 23:59");
			} catch (ParseException e) {
				e.printStackTrace();
				startDate = new Date();
				enDate = new Date();
			}
			scatTitle.setText("今天血糖数据监测");
			if (!NetWorkService.isNetWorkConnected(getActivity())) {
				isNoticeDialog("网络不可用，请查看网络");
				return;
			}
			getBloodSugar(startDate.getTime(), enDate.getTime(), 1, null);
			break;
		case R.id.history_time_sugar_week:
			type_week_month = 1;
//			frameLayout.removeAllViews();
			sugarTypeTV.setVisibility(View.VISIBLE);
			sugarTypeTV.setText("早餐前");
			monthTV.setVisibility(View.GONE);
			scatTitle.setText("一周血糖数据监测");
			currentDate = sdf1.format(new Date());
			enDate = null;
			try {
				enDate = sdf.parse(currentDate + " 23:59");
			} catch (ParseException e) {
				e.printStackTrace();
				enDate = new Date();
			}
			if (!NetWorkService.isNetWorkConnected(getActivity())) {
				isNoticeDialog("网络不可用，请查看网络");
				return;
			}
			getBloodSugar(StringUtil.getLastWeek(), enDate.getTime(), 2, BloodSugarType.beforeBreakfast);
			break;
		case R.id.history_time_sugar_month:
			type_week_month = 2;
//			frameLayout.removeAllViews();
			sugarTypeTV.setVisibility(View.VISIBLE);
			sugarTypeTV.setText("早餐前");
			monthTV.setVisibility(View.VISIBLE);
			Date startMonth = null;
			currentDate = sdf1.format(new Date());
			enDate = null;
			try {
				String y_m = sdfYM.format(new Date());
				maxOfMonth = StringUtil.getMaxDayOfMonth(y_m);
				monthTV.setText(y_m);
				startMonth = sdf.parse(y_m+"-01 00:00");
				enDate = sdf.parse(currentDate + " 23:59");
			} catch (ParseException e) {
				e.printStackTrace();
				startMonth = new Date();
				enDate = new Date();
			}
			String currentMonth = monthTV.getText().toString();
			scatTitle.setText(currentMonth+"月血糖数据监测");
			if (!NetWorkService.isNetWorkConnected(getActivity())) {
				isNoticeDialog("网络不可用，请查看网络");
				return;
			}
			getBloodSugar(startMonth.getTime(), enDate.getTime(), 3, BloodSugarType.beforeBreakfast);
			break;

		default:
			break;
		}
	}

	private void setDateTime() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = View.inflate(getActivity(), R.layout.date_time_dialog, null);
		final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
		final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
		builder.setView(view);

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH), null);

		if (datePicker != null) {
			datePicker.setCalendarViewShown(false);
			//只显示年月
			((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);//.getChildAt(0)
		}
		timePicker.setVisibility(View.GONE);

		builder.setTitle("选择月份");
		builder.setPositiveButton("确  定",
			new DialogInterface.OnClickListener() {
			
				@SuppressLint("DefaultLocale")
				@Override
				public void onClick(DialogInterface dialog, int which) {
					StringBuffer sb = new StringBuffer();
					String year_month = String.format("%d-%02d", datePicker.getYear(), datePicker.getMonth() + 1);
					sb.append(year_month);
					maxOfMonth = StringUtil.getDayOfMonth(datePicker.getYear(), datePicker.getMonth());
					
					monthTV.setText(year_month);
					Date startDate = null;
					Date endDate = null;
					try {
						startDate = sdf.parse(year_month+"-01 00:00");
						endDate = sdf.parse(year_month+"-"+maxOfMonth+" 23:59");
					} catch (ParseException e) {
						e.printStackTrace();
						startDate = new Date();
						endDate = new Date();
					}
					String currentMonth = monthTV.getText().toString();
					scatTitle.setText(currentMonth+"月血糖数据监测");
					String sugarStr = sugarTypeTV.getText().toString();
					BloodSugarType sugarType = getSugarTypeBySelect(sugarStr);
					getBloodSugar(startDate.getTime(), endDate.getTime(), 3, sugarType);
					dialog.cancel();
				}
			});
		Dialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.history_sugar_type:
			if (popupWindow!=null && popupWindow.isShowing()) {
				popupWindow.dismiss();
			}
			showPopuWindow();
			break;
		case R.id.history_sugar_month:
			setDateTime();
			break;

		default:
			break;
		}
	}
	
	private void showPopuWindow(){
		showDateSelection(sugarTypeTV, typeClass);
		sugarTypeTV.setCompoundDrawablesWithIntrinsicBounds(0, 0,
				R.drawable.green_arrow_up, 0);
	}
	
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	private void showDateSelection(final TextView dateFilter, final String[] type) {
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		localView = getActivity().getLayoutInflater().inflate(R.layout.blood_model_type, null);
		popupWindow = new PopupWindow(localView, dateFilter.getWidth(), dm.heightPixels/2);
		ListView localListView = (ListView) localView.findViewById(R.id.filter_spinner_list);
		bloodAdapter.setNameList(type);
		localListView.setAdapter(bloodAdapter);
		localListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				popupWindow.dismiss();
				String timeStr = type[position];
				dateFilter.setText(timeStr);
//				if (timeStr.equals("全部")) {
//					sugarTypeClass = 1;//选择全部时按照时间显示
//				}else {
//					sugarTypeClass = 2;
//				}
				BloodSugarType sugarType = getSugarTypeBySelect(timeStr);
				if (type_week_month==1) {//按周查询
					getBloodSugar(StringUtil.getLastWeek(), new Date().getTime(), 2, sugarType);
				}else if (type_week_month==2) {//按月查询
					String yearMonth = monthTV.getText().toString();
					long[] lse =  StringUtil.getMonthStartEndByMonth(yearMonth);
					getBloodSugar(lse[0], lse[1], 3, sugarType);
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

	protected BloodSugarType getSugarTypeBySelect(String type) {
		if (type.equals(BloodSugarType.beforeBreakfast.getText())) {//早餐前
			return BloodSugarType.beforeBreakfast;
		}
		if (type.equals(BloodSugarType.afterBreakfastOneHour.getText())) {//早餐后一小时
			return BloodSugarType.afterBreakfastOneHour;
		}
		if (type.equals(BloodSugarType.afterBreakfastTwoHour.getText())) {//早餐后两小时
			return BloodSugarType.afterBreakfastTwoHour;
		}
		if (type.equals(BloodSugarType.beforeLunch.getText())) {//午餐前
			return BloodSugarType.beforeLunch;
		}
		if (type.equals(BloodSugarType.afterLunchOneHour.getText())) {//午餐后一小时
			return BloodSugarType.afterLunchOneHour;
		}
		if (type.equals(BloodSugarType.afterLunchTwoHour.getText())) {//午餐后两小时
			return BloodSugarType.afterLunchTwoHour;
		}
		if (type.equals(BloodSugarType.beforeSupper.getText())) {//晚餐前
			return BloodSugarType.beforeSupper;
		}
		if (type.equals(BloodSugarType.afterSupperOneHour.getText())) {//晚餐后一小时
			return BloodSugarType.afterSupperOneHour;
		}
		if (type.equals(BloodSugarType.afterSupperTwoHour.getText())) {//晚餐后两小时
			return BloodSugarType.afterSupperTwoHour;
		}
		if (type.equals(BloodSugarType.beforeSleep.getText())) {//睡前
			return BloodSugarType.beforeSleep;
		}
		if (type.equals(BloodSugarType.other.getText())) {//其它
			return BloodSugarType.other;
		}
		return null;//全部
	}
	
	private void isNoticeDialog(String message){
		FragmentActivity context = getActivity();
		if (context==null) {
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("友情提示");
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}
}
