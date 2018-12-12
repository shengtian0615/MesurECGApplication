package com.wehealth.model.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.wehealth.model.domain.enumutil.BloodSugarType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import static com.wehealth.model.util.DateUtils.dayOfWeekToDouble;

public class StringUtil {

	public static String WXapp_id = "wxe43807da0648eda2";
	public static final String VERIFY_PASSED = "verify_passed";
	public static final String VERIFY_FAILED = "verify_failed";
	public static final String VERIFY_TIME_OUT = "verify_time_out";
	private static String regEx = "[\\u4e00-\\u9fa5]";

	public static PackageInfo getPackInfo(Context context){
		
		PackageManager pm = context.getPackageManager();
		PackageInfo info = null;
		try {
			info = pm.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return info;
	}

	/**获取身份证号后六位**/
	public static String getIdCardPsd(String idCardNo){
		int start = idCardNo.length()-6;
		int end = idCardNo.length();
		return idCardNo.substring(start, end);
	}

	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();
	}
	/**获取最近一周**/
	@SuppressLint("UseSparseArrays")
	public static List<String> getListWeekLabel(){
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 7; i++) {
			Calendar calendar = Calendar.getInstance(Locale.CHINA);
			calendar.add(GregorianCalendar.DATE, i-6);
			Date dd = new Date(calendar.getTimeInMillis());
			String dd1 = DateUtils.sdf_EEE.format(dd);
			list.add(i, dd1);
		}
		return list;
	}

	public static double dayOfMonthToDouble2(Date d){
		String D = DateUtils.sdf_D.format(d);
		double dd = Double.valueOf(D);
		return dd;
	}

	public static long[] getMonthStartEndByMonth(String yearMonth) {
		String[] ym = yearMonth.split("-");
		int year = Integer.valueOf(ym[0]);
		int m = Integer.valueOf(ym[1]);
		int maxOfMonth = getDayOfMonth(year, m);
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = DateUtils.sdf_yyyy_MM_dd_HHmm.parse(yearMonth+"-01 00:00");
			endDate = DateUtils.sdf_yyyy_MM_dd_HHmm.parse(yearMonth+"-"+maxOfMonth+" 23:59");
		} catch (ParseException e) {
			e.printStackTrace();
			startDate = new Date();
			endDate = new Date();
		}

		return new long[]{startDate.getTime(), endDate.getTime()};
	}

	/**获取血糖月数的X轴**/
	public static List<String> getListMonthSugarType(int currentMonth) {
		List<String> list = new ArrayList<String>();
		if (currentMonth%2==0) {//偶数天的月
			for (int i = 0; i <= currentMonth; i++) {
				if (i%2==0) {
					if (i<10) {
						list.add(" "+i);
					}else {
						list.add(""+i);
					}
				}else {
					list.add(" ");
				}
			}
			return list;
		}
		if (currentMonth%2==1) {//奇数月
			for (int i = 0; i <= currentMonth; i++) {
				if (i%2==0) {
					list.add(" ");
				}else {
					if (i<10) {
						list.add(" "+i);
					}else {
						list.add(""+i);
					}
				}
			}
			return list;
		}
		return null;
	}

	public static double getDoubleByDate2(Map<Integer, String> map, Date date){
		Iterator<Map.Entry<Integer, String>> it = map.entrySet().iterator();
		double dayDouble = 0;
		while (it.hasNext()) {
			Map.Entry<Integer, String> m = it.next();
			if (m.getValue().contains(DateUtils.sdf_yyyy_MM_dd_EEE.format(date))) {
				double i = m.getKey();
				dayDouble = i;
				break;
			}
		}
		return dayDouble;
	}

	@SuppressLint("UseSparseArrays")
	public static Map<Integer, String> getSugarType(){
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(0, "早餐");//早餐前
		map.put(1, "");//早餐后1
		map.put(2, "");//早餐后2
		map.put(3, "午餐");//午餐前
		map.put(4, "");//午餐后1
		map.put(5, "");//午餐后2
		map.put(6, "晚餐");//晚餐前
		map.put(7, "");//晚餐后1
		map.put(8, "");//晚餐后2
		map.put(9, "睡前");//睡前
		map.put(10, "自定义");//自定义
		return map;
	}
	@SuppressLint("UseSparseArrays")
	public static List<String> getListSugarType(){
		List<String> list = new ArrayList<String>();
		list.add(0, "");
		list.add(1, "早餐");//早餐前
		list.add(2, "");//早餐后1
		list.add(3, "");//早餐后2
		list.add(4, "午餐");//午餐前
		list.add(5, "");//午餐后1
		list.add(6, "");//午餐后2
		list.add(7, "晚餐");//晚餐前
		list.add(8, "");//晚餐后1
		list.add(9, "");//晚餐后2
		list.add(10, "睡前");//睡前
		list.add(11, "其它");//其它
		return list;
	}

	public static int getHourIntByDate(Date d){
		int hour = 8;
		try {
			String hourStr = DateUtils.sdf_H.format(d);
			hour = Integer.valueOf(hourStr);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return hour;
	}

	/**将点的集合改为数组**/
	public static int[] getEcgDataINTs(List<int[]> d){
		long start = System.currentTimeMillis();
		int[] data = new int[d.size()*12];
		for (int i = 0; i < d.size(); i++) {
			for (int j = 0; j < 12; j++) {
				data[i*12+j] = d.get(i)[j];
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("重新赋值时间："+(end-start));
		return data;
	}

	/**将点的集合改为导的集合**/
	public static List<int[]> getEcgData(List<int[]> data) {
		List<int[]> des = new ArrayList<int[]>();
		int[] d1 = new int[data.size()];
		int[] d2 = new int[data.size()];
		int[] d3 = new int[data.size()];
		int[] d4 = new int[data.size()];
		int[] d5 = new int[data.size()];
		int[] d6 = new int[data.size()];
		int[] d7 = new int[data.size()];
		int[] d8 = new int[data.size()];
		int[] d9 = new int[data.size()];
		int[] d10 = new int[data.size()];
		int[] d11 = new int[data.size()];
		int[] d12 = new int[data.size()];
		for (int j = 0; j<data.size(); j++) {
			for (int i = 0; i < Constant.LEADNUM; i++) {
				if (i==0) {
					d1[j] = data.get(j)[i];
				}
				if (i==1) {
					d2[j] = data.get(j)[i];
				}
				if (i==2) {
					d3[j] = data.get(j)[i];
				}
				if (i==3) {
					d4[j] = data.get(j)[i];
				}
				if (i==4) {
					d5[j] = data.get(j)[i];
				}
				if (i==5) {
					d6[j] = data.get(j)[i];
				}
				if (i==6) {
					d7[j] = data.get(j)[i];
				}
				if (i==7) {
					d8[j] = data.get(j)[i];
				}
				if (i==8) {
					d9[j] = data.get(j)[i];
				}
				if (i==9) {
					d10[j] = data.get(j)[i];
				}
				if (i==10) {
					d11[j] = data.get(j)[i];
				}
				if (i==11) {
					d12[j] = data.get(j)[i];
				}
			}
		}
		des.add(d1);
		des.add(d2);
		des.add(d3);
		des.add(d4);
		des.add(d5);
		des.add(d6);
		des.add(d7);
		des.add(d8);
		des.add(d9);
		des.add(d10);
		des.add(d11);
		des.add(d12);
		return des;
	}

	@SuppressLint("UseSparseArrays")
	public static Map<Integer, Integer[]> praseIntegerData(Map<String, Object> map) {
		Integer[] d0 = (Integer[]) map.get("ecgDataBuffer0");
		Integer[] d1 = (Integer[]) map.get("ecgDataBuffer1");
		Integer[] d2 = (Integer[]) map.get("ecgDataBuffer2");
		Integer[] d3 = (Integer[]) map.get("ecgDataBuffer3");
		Integer[] d4 = (Integer[]) map.get("ecgDataBuffer4");
		Integer[] d5 = (Integer[]) map.get("ecgDataBuffer5");
		Integer[] d6 = (Integer[]) map.get("ecgDataBuffer6");
		Integer[] d7 = (Integer[]) map.get("ecgDataBuffer7");
		Integer[] d8 = (Integer[]) map.get("ecgDataBuffer8");
		Integer[] d9 = (Integer[]) map.get("ecgDataBuffer9");
		Integer[] d10 = (Integer[]) map.get("ecgDataBuffer10");
		Integer[] d11 = (Integer[]) map.get("ecgDataBuffer11");

		Map<Integer, Integer[]> mapInt = new HashMap<Integer, Integer[]>();
		mapInt.put(0, d0);
		mapInt.put(1, d1);
		mapInt.put(2, d2);
		mapInt.put(3, d3);
		mapInt.put(4, d4);
		mapInt.put(5, d5);
		mapInt.put(6, d6);
		mapInt.put(7, d7);
		mapInt.put(8, d8);
		mapInt.put(9, d9);
		mapInt.put(10, d10);
		mapInt.put(11, d11);

		return mapInt;
	}

	public static int[] getMapEcgDataINTs(Map<String, Object> map) {
		Integer[] d0 = (Integer[]) map.get("ecgDataBuffer0");
		Integer[] d1 = (Integer[]) map.get("ecgDataBuffer1");
		Integer[] d2 = (Integer[]) map.get("ecgDataBuffer2");
		Integer[] d3 = (Integer[]) map.get("ecgDataBuffer3");
		Integer[] d4 = (Integer[]) map.get("ecgDataBuffer4");
		Integer[] d5 = (Integer[]) map.get("ecgDataBuffer5");
		Integer[] d6 = (Integer[]) map.get("ecgDataBuffer6");
		Integer[] d7 = (Integer[]) map.get("ecgDataBuffer7");
		Integer[] d8 = (Integer[]) map.get("ecgDataBuffer8");
		Integer[] d9 = (Integer[]) map.get("ecgDataBuffer9");
		Integer[] d10 = (Integer[]) map.get("ecgDataBuffer10");
		Integer[] d11 = (Integer[]) map.get("ecgDataBuffer11");

		int[] data = new int[d0.length * 12];

		for (int i = 0; i < d0.length; i++) {//d0
			data[i] = d0[i];
		}
		for (int i = 0; i < d0.length; i++) {//d1
			data[d0.length+i] = d1[i];
		}
		for (int i = 0; i < d0.length; i++) {//d2
			data[d0.length*2+i] = d2[i];
		}
		for (int i = 0; i < d0.length; i++) {//d3
			data[d0.length*3+i] = d3[i];
		}
		for (int i = 0; i < d0.length; i++) {//d4
			data[d0.length*4+i] = d4[i];
		}
		for (int i = 0; i < d0.length; i++) {//d5
			data[d0.length*5+i] = d5[i];
		}
		for (int i = 0; i < d0.length; i++) {//d6
			data[d0.length*6+i] = d6[i];
		}
		for (int i = 0; i < d0.length; i++) {//d7
			data[d0.length*7+i] = d7[i];
		}
		for (int i = 0; i < d0.length; i++) {//d8
			data[d0.length*8+i] = d8[i];
		}
		for (int i = 0; i < d0.length; i++) {//d9
			data[d0.length*9+i] = d9[i];
		}
		for (int i = 0; i < d0.length; i++) {//d10
			data[d0.length*10+i] = d10[i];
		}
		for (int i = 0; i < d0.length; i++) {//d11
			data[d0.length*11+i] = d11[i];
		}
		return data;
	}

	@SuppressLint("UseSparseArrays")
	public static Map<Integer, int[]> praseIntData(List<int[]> bufferDrawSnap) {
		Map<Integer, int[]> map = new HashMap<Integer, int[]>();
		int[] d1 = new int[bufferDrawSnap.size()];
		int[] d2 = new int[bufferDrawSnap.size()];
		int[] d3 = new int[bufferDrawSnap.size()];
		int[] d4 = new int[bufferDrawSnap.size()];
		int[] d5 = new int[bufferDrawSnap.size()];
		int[] d6 = new int[bufferDrawSnap.size()];
		int[] d7 = new int[bufferDrawSnap.size()];
		int[] d8 = new int[bufferDrawSnap.size()];
		int[] d9 = new int[bufferDrawSnap.size()];
		int[] d10 = new int[bufferDrawSnap.size()];
		int[] d11 = new int[bufferDrawSnap.size()];
		int[] d12 = new int[bufferDrawSnap.size()];
		for (int j = 0; j<bufferDrawSnap.size(); j++) {
			for (int i = 0; i < 12; i++) {
				if (i==0) {
					d1[j] = bufferDrawSnap.get(j)[i];
				}
				if (i==1) {
					d2[j] = bufferDrawSnap.get(j)[i];
				}
				if (i==2) {
					d3[j] = bufferDrawSnap.get(j)[i];
				}
				if (i==3) {
					d4[j] = bufferDrawSnap.get(j)[i];
				}
				if (i==4) {
					d5[j] = bufferDrawSnap.get(j)[i];
				}
				if (i==5) {
					d6[j] = bufferDrawSnap.get(j)[i];
				}
				if (i==6) {
					d7[j] = bufferDrawSnap.get(j)[i];
				}
				if (i==7) {
					d8[j] = bufferDrawSnap.get(j)[i];
				}
				if (i==8) {
					d9[j] = bufferDrawSnap.get(j)[i];
				}
				if (i==9) {
					d10[j] = bufferDrawSnap.get(j)[i];
				}
				if (i==10) {
					d11[j] = bufferDrawSnap.get(j)[i];
				}
				if (i==11) {
					d12[j] = bufferDrawSnap.get(j)[i];
				}
			}
		}
		map.put(0, d1);
		map.put(1, d2);
		map.put(2, d3);
		map.put(3, d4);
		map.put(4, d5);
		map.put(5, d6);
		map.put(6, d7);
		map.put(7, d8);
		map.put(8, d9);
		map.put(9, d10);
		map.put(10, d11);
		map.put(11, d12);
		return map;
	}

	public static Map<String, StringBuffer> praseIntDataToString(List<int[]> bufferDrawSnap) {
		Map<String, StringBuffer> map = new HashMap<String, StringBuffer>();
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		StringBuffer sb3 = new StringBuffer();
		StringBuffer sb4 = new StringBuffer();
		StringBuffer sb5 = new StringBuffer();
		StringBuffer sb6 = new StringBuffer();
		StringBuffer sb7 = new StringBuffer();
		StringBuffer sb8 = new StringBuffer();
		StringBuffer sb9 = new StringBuffer();
		StringBuffer sb10 = new StringBuffer();
		StringBuffer sb11 = new StringBuffer();
		StringBuffer sb12 = new StringBuffer();
		for (int j = 0; j<bufferDrawSnap.size(); j++) {
			for (int i = 0; i < 12; i++) {
				if (i==0) {
					sb1.append(bufferDrawSnap.get(j)[i]+" ");
				}
				if (i==1) {
					sb2.append(bufferDrawSnap.get(j)[i]+" ");
				}
				if (i==2) {
					sb3.append(bufferDrawSnap.get(j)[i]+" ");
				}
				if (i==3) {
					sb4.append(bufferDrawSnap.get(j)[i]+" ");
				}
				if (i==4) {
					sb5.append(bufferDrawSnap.get(j)[i]+" ");
				}
				if (i==5) {
					sb6.append(bufferDrawSnap.get(j)[i]+" ");
				}
				if (i==6) {
					sb7.append(bufferDrawSnap.get(j)[i]+" ");
				}
				if (i==7) {
					sb8.append(bufferDrawSnap.get(j)[i]+" ");
				}
				if (i==8) {
					sb9.append(bufferDrawSnap.get(j)[i]+" ");
				}
				if (i==9) {
					sb10.append(bufferDrawSnap.get(j)[i]+" ");
				}
				if (i==10) {
					sb11.append(bufferDrawSnap.get(j)[i]+" ");
				}
				if (i==11) {
					sb12.append(bufferDrawSnap.get(j)[i]+" ");
				}
			}
		}
		map.put("Ch0", sb1);
		map.put("Ch1", sb2);
		map.put("Ch2", sb3);
		map.put("Ch3", sb4);
		map.put("Ch4", sb5);
		map.put("Ch5", sb6);
		map.put("Ch6", sb7);
		map.put("Ch7", sb8);
		map.put("Ch8", sb9);
		map.put("Ch9", sb10);
		map.put("Ch10", sb11);
		map.put("Ch11", sb12);
		return map;
	}

	public static String parseJson(String string){
		String result = "";
		if (TextUtils.isEmpty(string)) {
			return result;
		}
		try {
			JSONObject jsonObject = new JSONObject(string);
			result = jsonObject.getString("userName");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String parseNewGroupJson(String json){
		String result = "";
		if (TextUtils.isEmpty(json)) {
			return result;
		}
		try {
			JSONObject jsonObject = new JSONObject(json);
			String new_group = jsonObject.getString("action");
			if (new_group.equals("new_group")) {
				result = jsonObject.getString("groupId");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String parseAction(String json){
		String action = "";
		if (TextUtils.isEmpty(json)) {
			return action;
		}
		try {
			JSONObject jsonObject = new JSONObject(json);
			action = jsonObject.getString("action");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return action;
	}
	
	public static String[] parseReCheckJson(String json){
		String[] result = new String[2];
		if (TextUtils.isEmpty(json)) {
			return result;
		}
		try {
			JSONObject jsonObject = new JSONObject(json);
			result[0] = jsonObject.getString("action");
			result[1] = jsonObject.getString("groupId");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/** 计算时间 **/
	public static long getTimeLong(int index) {
		long timeInMillis = 0;
//		if (index == 0) {//最近一个星期
//			Calendar calendar = Calendar.getInstance();
//			calendar.add(GregorianCalendar.DATE, -7);
//			timeInMillis = calendar.getTimeInMillis();
//			Log.i("ChargeRecord", "最近一个星期："+sdf.format(calendar.getTime()));
//			Log.i("ChargeRecord", "最近一个星期的Long型："+timeInMillis);
//		}
//		if (index == 1) {//最近二个星期
//			Calendar calendar = Calendar.getInstance();
//			calendar.add(GregorianCalendar.DATE, -14);
//			timeInMillis = calendar.getTimeInMillis();
//			Log.i("ChargeRecord", "最近二个星期："+sdf.format(calendar.getTime()));
//			Log.i("ChargeRecord", "最近二个星期的Long型："+timeInMillis);
//		}
		if (index == 0) {//最近一个月
			Calendar calendar = Calendar.getInstance();
			calendar.add(GregorianCalendar.MONTH, -1);
			timeInMillis = calendar.getTimeInMillis();
			Log.i("ChargeRecord", "最近一个月："+DateUtils.sdf_yyyy_MM_dd.format(calendar.getTime()));
			Log.i("ChargeRecord", "最近一个月的Long型："+timeInMillis);
		}
		if (index == 1) {//最近二个月
			Calendar calendar = Calendar.getInstance();
			calendar.add(GregorianCalendar.MONTH, -2);
			timeInMillis = calendar.getTimeInMillis();
			Log.i("ChargeRecord", "最近二个月："+DateUtils.sdf_yyyy_MM_dd.format(calendar.getTime()));
			Log.i("ChargeRecord", "最近二个月的Long型："+timeInMillis);
		}
		if (index == 2) {//最近三个月
			Calendar calendar = Calendar.getInstance();
			calendar.add(GregorianCalendar.MONTH, -3);
			timeInMillis = calendar.getTimeInMillis();
			Log.i("ChargeRecord", "最近三个月："+DateUtils.sdf_yyyy_MM_dd.format(calendar.getTime()));
			Log.i("ChargeRecord", "最近三个月的Long型："+timeInMillis);
		}
		if (index == 3) {//最近半年
			Calendar calendar = Calendar.getInstance();
			calendar.add(GregorianCalendar.MONTH, -6);
			timeInMillis = calendar.getTimeInMillis();
			Log.i("ChargeRecord", "最近半年："+DateUtils.sdf_yyyy_MM_dd.format(calendar.getTime()));
			Log.i("ChargeRecord", "最近半年的Long型："+timeInMillis);
		}
		if (index == 4) {//最近一年
			Calendar calendar = Calendar.getInstance();
			calendar.add(GregorianCalendar.YEAR, -1);
			timeInMillis = calendar.getTimeInMillis();
			Log.i("ChargeRecord", "最近一年："+DateUtils.sdf_yyyy_MM_dd.format(calendar.getTime()));
			Log.i("ChargeRecord", "最近一年的Long型："+timeInMillis);
		}
		if (index==5 || index==-1) {//全部
			timeInMillis = -1;
		}
		return timeInMillis;
	}

	/**最近一周**/
	public static long getLastWeek(){
		Calendar calendar1 = Calendar.getInstance();
		calendar1.add(GregorianCalendar.DATE, -6);
		Date date = calendar1.getTime();
		String s = DateUtils.sdf_yyyy_MM_dd.format(date);
		String start = s+" 00:00";
		Date startDate = null;
		try {
			startDate = DateUtils.sdf_yyyy_MM_dd_HHmm.parse(start);
		} catch (ParseException e) {
			e.printStackTrace();
			startDate = calendar1.getTime();
		}
		return startDate.getTime();
	}

	/**
	 * 将分变换成小时的小数
	 * @param d
	 * @return
	 */
	public static double strToDouble(Date d){
		String h = DateUtils.sdf_H.format(d);
		String m = DateUtils.sdf_m.format(d);
		double hd = Double.valueOf(h);
		double md = Double.valueOf(m);
		BigDecimal b = new BigDecimal(md/60);
		double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return hd+f1;
	}

	/**
	 * 将小时转换成天（月）的小数
	 * @param d
	 * @return
	 */
	public static double dayOfMonthToDouble(Date d){
		String D = DateUtils.sdf_D.format(d);
		String H = DateUtils.sdf_H.format(d);
		String m = DateUtils.sdf_m.format(d);
		double md = Double.valueOf(m);
		double dd = Double.valueOf(D);
		double hd = Double.valueOf(H);
		BigDecimal mb = new BigDecimal(md/60);
		double f1 = mb.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		BigDecimal hb = new BigDecimal((hd+f1)/24);
		double f2 = hb.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return dd+f2;
	}

	/**月的最大天数**/
	public static int getMaxDayOfMonth(String d){
		String[] ym = d.split("-");
		int y = Integer.valueOf(ym[0]);
		int m = Integer.valueOf(ym[1]);
		int max = getDayOfMonth(y, m-1);
		return max;
	}

	/**获取当前月的天数**/
	public static int getDayOfMonth(int year, int month){
		Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
		aCalendar.set(Calendar.YEAR, year);
		aCalendar.set(Calendar.MONTH, month);
		aCalendar.set(Calendar.DATE, 1);
		aCalendar.roll(Calendar.DATE, -1);
		int day = aCalendar.get(Calendar.DATE);
//		int day = aCalendar.getActualMaximum(Calendar.DATE);
		return day;
	}

	/**获取最近一周**/
	@SuppressLint("UseSparseArrays")
	public static Map<Integer, String> getWeekLabel(){
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (int i = 0; i < 7; i++) {
			Calendar calendar = Calendar.getInstance(Locale.CHINA);
			calendar.add(GregorianCalendar.DATE, i-6);
			Date dd = new Date(calendar.getTimeInMillis());
			String dd1 = DateUtils.sdf_yyyy_MM_dd_EEE.format(dd);
			map.put(i, dd1);
		}
		return map;
	}
	
	/**如果是英文或者汉字返回true**/
	public static boolean verifyName(String userNameStr) {
		int result = -1;
		char[] ch = userNameStr.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			if (Pattern.matches(regEx, String.valueOf(ch[i]))) {//是汉字
				
			}else if(((ch[i]>='a' && ch[i]<='z') || (ch[i]>='A' && ch[i]<='Z'))){//英文字母
				
			}else {
				result = 1;//包含不是汉字或者英文的字符时，跳出for循环。
				break;
			}
		}
		return result==1 ? false : true;
	}

	/**血压获取周对应的时间小数
	 * @param map **/
	public static double getPressDoubleByDate(Map<Integer, String> map, Date date){
		Iterator<Map.Entry<Integer, String>> it = map.entrySet().iterator();
		double dayDouble = 0;
		while (it.hasNext()) {
			Map.Entry<Integer, String> m = it.next();
			if (m.getValue().contains(DateUtils.sdf_yyyy_MM_dd_EEE.format(date))) {
				double i = m.getKey();
				dayDouble = i + dayOfWeekToDouble(date);
				break;
			}
		}
		return dayDouble;
	}

	/**血糖获取周对应的时间小数
	 * @param sugarType **/
	public static double getSugarDoubleByDate(Map<Integer, String> map, Date date, BloodSugarType sugarType){
		Iterator<Map.Entry<Integer, String>> it = map.entrySet().iterator();
		double dayDouble = 0;
		while (it.hasNext()) {
			Map.Entry<Integer, String> m = it.next();
			if (m.getValue().contains(DateUtils.sdf_yyyy_MM_dd_EEE.format(date))) {
				double i = m.getKey();
				dayDouble = i;
				break;
			}
		}
		double type = sugarType.ordinal();
		BigDecimal mb = new BigDecimal(type/12);
		double f1 = mb.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
		return dayDouble+f1;
	}

	public static int getAgeByBirthDay(String idCardNo) {
		int age = 60;
		Date date = new Date();
		try {
			String subStr;
			subStr = idCardNo.substring(6, 10);
			String current = DateUtils.sdf_yyyy.format(date);
			int bd = Integer.valueOf(subStr);
			int crt = Integer.valueOf(current);
			age = crt - bd;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return age;
	}

	/**从身份证号中获取性别**/
	@SuppressLint("DefaultLocale")
	public static int getIntGender(String idCardNo) {//保证
		int sex = 0;
		if (idCardNo == null || (idCardNo.length() != 15 && idCardNo.length() != 18)) {
			return sex;
		}
		if (idCardNo.length() != 15 && idCardNo.length() != 18) {
			return sex;
		}

		String lastValue = idCardNo.substring(idCardNo.length() - 2, idCardNo.length() - 1);
		sex = Integer.valueOf(lastValue) % 2;
		if (sex == 0) {
			sex = 1;
			return sex;
		}
		if (sex == 1) {
			sex = 0;
			return sex;
		}
		return sex;
	}
	
	public static String getBirthDay(String idCardNo) {
		Date date = null;
		String subStr;
		subStr = idCardNo.substring(6, 14);
		try {
			date = DateUtils.sdf_yyyyMMdd.parse(subStr);
			return DateUtils.sdf_yyyy_MM_dd.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**从身份证号中获取性别**/
	@SuppressLint("DefaultLocale")
	public static String getGender(String idCardNo) {
		if (idCardNo == null || (idCardNo.length() != 15 && idCardNo.length() != 18)) {
			return "男";
		}
		if (idCardNo.length() == 15 || idCardNo.length() == 18) {
			String lastValue = idCardNo.substring(idCardNo.length()-2, idCardNo.length()-1);
			int sex;
			sex = Integer.parseInt(lastValue) % 2;
			return sex == 0 ? "女" : "男";
		} else {
			return "男";
		}
	}

	/**
	 * s1等于s2返回0 s1小于s2返回负值 s1大于s2返回正值
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static int compareDate(String s1, String s2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(DateUtils.sdf_yyyy_MM_dd_HHmm.parse(s1));
			c2.setTime(DateUtils.sdf_yyyy_MM_dd_HHmm.parse(s2));
		} catch (ParseException e) {
			System.err.println("格式不正确");
		}
		int result = c1.compareTo(c2);
		if (result == 0)
			System.out.println("c1相等c2");
		else if (result < 0)
			System.out.println("c1小于c2");
		else
			System.out.println("c1大于c2");
		return result;
	}

	/**
	 * 计算年龄
	 * 
	 * @param date
	 * @return String类型
	 */
	public static String getAge(Date date) {
		if (date == null) {
			return "0";
		} else {
			String birthDay = DateUtils.sdf_yyyy_MM_dd.format(date);
			// 得到当前的年份
			String cYear = DateUtils.sdf_yyyy_MM_dd.format(new Date()).substring(0, 4);
			// 得到生日年份
			String birthYear = birthDay.substring(0, 4);

			int age = Integer.parseInt(cYear) - Integer.parseInt(birthYear);
			return age + "";
		}
	}

	/** 计算年龄  */
	public static int getAge(String idCardNo) {
		int age = 0;
		if (TextUtils.isEmpty(idCardNo)) {
			return age;
		}
		try {
			String birthDay = getBirthDay(idCardNo);
			// 得到当前的年份
			String cYear = DateUtils.sdf_yyyy_MM_dd.format(new Date()).substring(0, 4);
			// 得到生日年份
			String birthYear = birthDay.substring(0, 4);

			age = Integer.parseInt(cYear) - Integer.parseInt(birthYear);
		} catch (Exception e) {
			e.printStackTrace();
			age = 0;
		}
		return age;
	}
	
	public static int getAgeByIdCard(String idCardNo){
		String subStr = idCardNo.substring(6, 10);
		int age;
		try {
			age = Integer.valueOf(subStr);
		} catch (Exception e) {
			age = 60;
		}
		Date date = new Date();
		try {
			String current = DateUtils.sdf_yyyy.format(date);
			age = Integer.valueOf(current)-Integer.valueOf(subStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return age;
	}
	
	/**
	 * 对象转整数
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try {
			return Long.parseLong(obj);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;
	}

	/**
	 * 改变字符编码
	 * 
	 * @param str
	 *            目标字符
	 * @return 转换字符
	 */
	public static String changeEncoding(String str, String encoding) {
		try {
			String returnStr = new String(str.getBytes(), encoding);
			return returnStr;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}
	
	/**隐藏中间字符
	 * 必须大于8个的字符**/
	public static String inVisiable(String allString) {
		String qian;
		String hou;
		qian = allString.substring(0, 4);
		hou = allString.substring(allString.length()-4, allString.length());
		String inVis = qian +"**********" + hou;
		return inVis;
	}

	/**银行卡号隐藏
	 * 必须大于8个的字符**/;
	public static String bankIdVisiable(String bankAccountId) {
		String qian;
		String hou;
		qian = bankAccountId.substring(0, 4);
		hou = bankAccountId.substring(bankAccountId.length()-4, bankAccountId.length());
		String inVis = qian +"******" + hou;
		return inVis;
	}

	/**从身份证号中获取性别
	 * @param idCardNo
	 * @return 返回1为男；否则为女
	 */
	@SuppressLint("DefaultLocale")
	public static int getGenderInt(String idCardNo) {
		if (idCardNo == null || (idCardNo.length() != 15 && idCardNo.length() != 18)) {
			return 1;
		}
		try {
			if (idCardNo.length() == 15 || idCardNo.length() == 18) {
				String lastValue = idCardNo.substring(idCardNo.length()-2, idCardNo.length()-1);
				int sex;
				sex = Integer.parseInt(lastValue) % 2;
				return sex;
			} else {
				return 1;
			}
		} catch (Exception e) {
			return 1;
		}
	}

	public static void deleteECGFile(String pid, Date date){
		try {
			String xml = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ECGDATA/XML/";
			File file  = new File(xml+DateUtils.sdf_yyyyMMddHHmmss.format(date)+".xml");
			if (file.exists()) {
				file.delete();
			}
			String pdf = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ECGDATA/PDF/";
			File file1  = new File(pdf+pid+"_"+DateUtils.sdf_yyyyMMddHHmmss.format(date)+".pdf");
			if (file1.exists()) {
				file1.delete();
			}
			file1.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteECGLongFile(long time){
		String path = getSDPath()+"/ECGDATA/Data2Device/";
		File file = new File(path+time+"_h24.txt");
		if (file.exists()) {
			file.delete();
		}
		File[] files = new File[12];
		files[0] = new File(path+time+"_v1.txt");
		files[1] = new File(path+time+"_v2.txt");
		files[2] = new File(path+time+"_v3.txt");
		files[3] = new File(path+time+"_v4.txt");
		files[4] = new File(path+time+"_v5.txt");
		files[5] = new File(path+time+"_v6.txt");
		files[6] = new File(path+time+"_avf.txt");
		files[7] = new File(path+time+"_avl.txt");
		files[8] = new File(path+time+"_avr.txt");
		files[9] = new File(path+time+"_i.txt");
		files[10] = new File(path+time+"_ii.txt");
		files[11] = new File(path+time+"_iii.txt");
		for (int i = 0; i < files.length; i++) {
			if (files[i].exists()) {
				files[i].delete();
			}
		}
	}

	public static void writeException(Exception e, String type) {
		StringBuffer sb = new StringBuffer();
		if (e!=null) {
			String LocalizedMessage = e.getLocalizedMessage();
			sb.append("LocalizedMessage == " + LocalizedMessage + "\n");
			String message = e.getMessage();
			sb.append("message == " + message + "\n");
			sb.append(DateUtils.sdf_yyyy_MM_dd_HH_mm_ss.format(new Date())+"\n");
		}
		String fileName = getSDPath() + "/" + "exception21.txt";
		writException2File(fileName, "\n \n" + type +"+++++"+sb.toString()+"\n \n");
	}

	/**
	 * 追加文件：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true
	 * @param fileName
	 * @param content
	 */
	public static void writException2File(String fileName, String content) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName, true)));
			out.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out!=null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
