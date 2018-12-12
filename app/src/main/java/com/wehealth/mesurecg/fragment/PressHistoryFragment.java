package com.wehealth.mesurecg.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.wehealth.mesurecg.R;
import com.wehealth.mesurecg.utils.CommUtils;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.mesurecg.view.LineChartViewHelper;
import com.wehealth.model.domain.model.AuthToken;
import com.wehealth.model.domain.model.BloodPressure;
import com.wehealth.model.interfaces.inter_register.WeHealthPatient;
import com.wehealth.model.util.NetWorkService;
import com.wehealth.model.util.StringUtil;

@SuppressLint("SimpleDateFormat")
public class PressHistoryFragment extends Fragment implements OnCheckedChangeListener, OnClickListener{

	private RadioGroup radioGroup;
//	private RadioButton dayBtn, weekBtn, monthBtn;
	ProgressDialog progressDialog;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfYM = new SimpleDateFormat("yyyy-MM");
	
	int maxOfMonth;
	private String date, idCardNo;
	DatePickerDialog mdialog;
	FrameLayout frameLayout;
	private TextView monthTV;
	private String[] titles = new String[] { "收缩压", "舒张压", "心率" };
	private Map<Integer, String> map;
	
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
				frameLayout.removeAllViews();
				List<BloodPressure> listBloodPress = (List<BloodPressure>) msg.obj;
				if (listBloodPress == null || listBloodPress.isEmpty()) {
					isNoticeDialog("在选中的时间段里，没有获取到血压数据");
					return;
				}
				Bundle bundle = msg.getData();
				int type = bundle.getInt("type", 1);
				initDrawLine(type, listBloodPress);
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.history_blood_press, container, false);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		radioGroup = (RadioGroup) getView().findViewById(R.id.history_blood_press_timetype);
		frameLayout = (FrameLayout) getView().findViewById(R.id.history_blood_press_layout);
		monthTV = (TextView) getView().findViewById(R.id.history_press_month);
		
		radioGroup.setOnCheckedChangeListener(this);
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setCancelable(false);
		frameLayout.removeAllViews();
		
		map = StringUtil.getWeekLabel();
		idCardNo = PreferUtils.getIntance().getIdCardNo();
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
		
		monthTV.setOnClickListener(this);
		if (!NetWorkService.isNetWorkConnected(getActivity())) {
			isNoticeDialog("网络不可用，请查看网络");
			return;
		}
		getBloodPress(startDate.getTime(), enDate.getTime(), 1);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.history_time_press_day:
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
			if (!NetWorkService.isNetWorkConnected(getActivity())) {
				isNoticeDialog("网络不可用，请查看网络");
				return;
			}
			getBloodPress(startDate.getTime(), enDate.getTime(), 1);
			break;
		case R.id.history_time_press_week:
			monthTV.setVisibility(View.GONE);
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
			getBloodPress(StringUtil.getLastWeek(), enDate.getTime(), 2);
			break;
		case R.id.history_time_press_month:
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
			if (!NetWorkService.isNetWorkConnected(getActivity())) {
				isNoticeDialog("网络不可用，请查看网络");
				return;
			}
			getBloodPress(startMonth.getTime(), enDate.getTime(), 3);
//			setDateTime();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 画线，type=1时为当天; type=2时为周; type=3时为月
	 * @param type
	 * @param listBloodPress 
	 */
	protected void initDrawLine(int type, List<BloodPressure> listBloodPress) {
		double[] x1 = new double[listBloodPress.size()];
		double[] y1 = new double[listBloodPress.size()];
		double[] y2 = new double[listBloodPress.size()];
		double[] y3 = new double[listBloodPress.size()];
		if (type==1) {
			LineChartViewHelper dayLineChart = new LineChartViewHelper(getActivity());
			dayLineChart.initBarDataSet(titles);
			dayLineChart.initRenderer("今天血压数据监测", 24, "时间(时)", null);
			frameLayout.addView(dayLineChart.getBarChartView());
			for (int i = 0; i < listBloodPress.size(); i++) {
				Date d = listBloodPress.get(i).getTestTime();
				x1[i] = StringUtil.strToDouble(d);
				y1[i] = listBloodPress.get(i).getHigh();
				y2[i] = listBloodPress.get(i).getLow();
				y3[i] = listBloodPress.get(i).getHeartRate();
				dayLineChart.updateChart(x1[i], y1[i], y2[i], y3[i]);
			}
			return;
		}
		if (type==2) {
			LineChartViewHelper weekLineChart = new LineChartViewHelper(getActivity());
			weekLineChart.initBarDataSet(titles);
			weekLineChart.initRenderer("一周血压数据监测", 7, "时间(周)", map);
			frameLayout.addView(weekLineChart.getBarChartView());
			for (int i = 0; i < listBloodPress.size(); i++) {
				Date d = listBloodPress.get(i).getTestTime();
				x1[i] = StringUtil.getPressDoubleByDate(map, d);
				y1[i] = listBloodPress.get(i).getHigh();
				y2[i] = listBloodPress.get(i).getLow();
				y3[i] = listBloodPress.get(i).getHeartRate();
				weekLineChart.updateChart(x1[i], y1[i], y2[i], y3[i]);
			}
			return;
		}
		if (type==3) {
			LineChartViewHelper monthLineChart = new LineChartViewHelper(getActivity());
			monthLineChart.initBarDataSet(titles);
			String currentMonth = monthTV.getText().toString();
			monthLineChart.initRenderer(currentMonth+"月血压数据监测", maxOfMonth, "时间(日)", null);
			frameLayout.addView(monthLineChart.getBarChartView());
			for (int i = 0; i < listBloodPress.size(); i++) {
				Date d = listBloodPress.get(i).getTestTime();
				x1[i] = StringUtil.dayOfMonthToDouble(d);
				y1[i] = listBloodPress.get(i).getHigh();
				y2[i] = listBloodPress.get(i).getLow();
				y3[i] = listBloodPress.get(i).getHeartRate();
				monthLineChart.updateChart(x1[i], y1[i], y2[i], y3[i]);
			}
			return;
		}
	}
	
	/**按照起止时间查询
	 * @param type **/
	private void getBloodPress(final long starTime, final long endTime, final int type) {
		progressDialog.setMessage("正在获取数据...");
		progressDialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				List<BloodPressure> list = null;
				try {
					AuthToken token = CommUtils.refreshToken();

					list = NetWorkService.createApi(WeHealthPatient.class, PreferUtils.getIntance().getServerUrl()).queryBloodPressure("Bearer " + token.getAccess_token(), starTime, endTime, idCardNo, starTime, endTime, null, null);
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
	
	/**月份选择**/
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
						
					sb.append(" ");
					sb.append(maxOfMonth);
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
					getBloodPress(startDate.getTime(), endDate.getTime(), 3);
					dialog.cancel();
					monthTV.setText(year_month);
				}
			});
		Dialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.history_press_month) {
			setDateTime();
		}
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
