package com.wehealth.model.util;

import android.annotation.SuppressLint;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

@SuppressLint("SimpleDateFormat")
public class DateUtils {

    private static final long INTERVAL_IN_MILLISECONDS = 30 * 1000;
    public static SimpleDateFormat sdf_yyyy_MM_dd_EEE = new SimpleDateFormat("yyyy-MM-dd EEE");// EEE代表星期几
    public static SimpleDateFormat sdf_m = new SimpleDateFormat("mm");
    public static SimpleDateFormat sdf_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat sdf_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
    public static SimpleDateFormat sdf_yyyy = new SimpleDateFormat("yyyy");
    public static SimpleDateFormat sdf_yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
    public static SimpleDateFormat sdf_yyyy_MM_dd_HHmm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat sdf_yyyy_MM_dd_HHmm_EEE = new SimpleDateFormat("yyyy-MM-dd HH:mm EEE");
    public static SimpleDateFormat sdf_yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat sdf_s = new SimpleDateFormat("ss");
    public static SimpleDateFormat sdf_H = new SimpleDateFormat("HH");
	public static SimpleDateFormat sdf_D = new SimpleDateFormat("dd");
    public static SimpleDateFormat sdf_EEE = new SimpleDateFormat("EEE");// EEE代表星期几
    public static SimpleDateFormat sdf_HHmm = new SimpleDateFormat("HHmm");
    public static SimpleDateFormat sdf_HH_mm = new SimpleDateFormat("HH:mm");
    
//    public static String getTimestampString(Date messageDate) {
//        Locale curLocale = HXSDKHelper.getInstance().getAppContext().getResources().getConfiguration().locale;
//        
//        String languageCode = curLocale.getLanguage();
//        
//        boolean isChinese = languageCode.contains("zh");
//        
//        String format = null;
//        
//        long messageTime = messageDate.getTime();
//        if (isSameDay(messageTime)) {
//            Calendar calendar = GregorianCalendar.getInstance();
//            calendar.setTime(messageDate);
//            int hour = calendar.get(Calendar.HOUR_OF_DAY);
//            
//            format = "HH:mm";
//            
//            if (hour > 17) {                
//                if(isChinese){
//                    format = "晚上 hh:mm";
//                }
//                
//            }else if(hour >= 0 && hour <= 6){                
//                if(isChinese){
//                    format = "凌晨 hh:mm";
//                }
//            } else if (hour > 11 && hour <= 17) {
//                if(isChinese){
//                    format = "下午 hh:mm";
//                }
//                
//            } else {
//                if(isChinese){
//                    format = "上午 hh:mm";
//                }
//            }
//        } else if (isYesterday(messageTime)) {
//            if(isChinese){
//                format = "昨天 HH:mm";
//            }else{
//                format = "MM-dd HH:mm";
//            }
//            
//        } else {
//            if(isChinese){
//                format = "M月d日 HH:mm";
//            }else{
//                format = "MM-dd HH:mm";
//            }
//        }
//        
//        if(isChinese){
//            return new SimpleDateFormat(format, Locale.CHINA).format(messageDate);
//        }else{
//            return new SimpleDateFormat(format, Locale.US).format(messageDate); 
//        }
//    }

    public static boolean isCloseEnough(long time1, long time2) {
        // long time1 = date1.getTime();
        // long time2 = date2.getTime();
        long delta = time1 - time2;
        if (delta < 0) {
            delta = -delta;
        }
        return delta < INTERVAL_IN_MILLISECONDS;
    }


    public static Date StringToDate(String dateStr, String formatStr) {
        DateFormat format = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /**
     * 
     * @param timeLength Millisecond
     * @return
     */
    public static String toTime(int timeLength) {
        timeLength /= 1000;
        int minute = timeLength / 60;
        int hour = 0;
        if (minute >= 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        int second = timeLength % 60;
        // return String.format("%02d:%02d:%02d", hour, minute, second);
        return String.format("%02d:%02d", minute, second);
    }
    /**
     * 
     * @param timeLength second
     * @return
     */
    public static String toTimeBySecond(int timeLength) {
//      timeLength /= 1000;
        int minute = timeLength / 60;
        int hour = 0;
        if (minute >= 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        int second = timeLength % 60;
        // return String.format("%02d:%02d:%02d", hour, minute, second);
        return String.format("%02d:%02d", minute, second);
    }
     
    

//    public static TimeInfo getYesterdayStartAndEndTime() {
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.add(Calendar.DATE, -1);
//        calendar1.set(Calendar.HOUR_OF_DAY, 0);
//        calendar1.set(Calendar.MINUTE, 0);
//        calendar1.set(Calendar.SECOND, 0);
//        calendar1.set(Calendar.MILLISECOND, 0);
//
//        Date startDate = calendar1.getTime();
//        long startTime = startDate.getTime();
//
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.add(Calendar.DATE, -1);
//        calendar2.set(Calendar.HOUR_OF_DAY, 23);
//        calendar2.set(Calendar.MINUTE, 59);
//        calendar2.set(Calendar.SECOND, 59);
//        calendar2.set(Calendar.MILLISECOND, 999);
//        Date endDate = calendar2.getTime();
//        long endTime = endDate.getTime();
//        TimeInfo info = new TimeInfo();
//        info.setStartTime(startTime);
//        info.setEndTime(endTime);
//        return info;
//    }
//
//    public static TimeInfo getTodayStartAndEndTime() {
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.set(Calendar.HOUR_OF_DAY, 0);
//        calendar1.set(Calendar.MINUTE, 0);
//        calendar1.set(Calendar.SECOND, 0);
//        calendar1.set(Calendar.MILLISECOND, 0);
//        Date startDate = calendar1.getTime();
//        long startTime = startDate.getTime();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");
//
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.set(Calendar.HOUR_OF_DAY, 23);
//        calendar2.set(Calendar.MINUTE, 59);
//        calendar2.set(Calendar.SECOND, 59);
//        calendar2.set(Calendar.MILLISECOND, 999);
//        Date endDate = calendar2.getTime();
//        long endTime = endDate.getTime();
//        TimeInfo info = new TimeInfo();
//        info.setStartTime(startTime);
//        info.setEndTime(endTime);
//        return info;
//    }
//
//    public static TimeInfo getBeforeYesterdayStartAndEndTime() {
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.add(Calendar.DATE, -2);
//        calendar1.set(Calendar.HOUR_OF_DAY, 0);
//        calendar1.set(Calendar.MINUTE, 0);
//        calendar1.set(Calendar.SECOND, 0);
//        calendar1.set(Calendar.MILLISECOND, 0);
//        Date startDate = calendar1.getTime();
//        long startTime = startDate.getTime();
//
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.add(Calendar.DATE, -2);
//        calendar2.set(Calendar.HOUR_OF_DAY, 23);
//        calendar2.set(Calendar.MINUTE, 59);
//        calendar2.set(Calendar.SECOND, 59);
//        calendar2.set(Calendar.MILLISECOND, 999);
//        Date endDate = calendar2.getTime();
//        long endTime = endDate.getTime();
//        TimeInfo info = new TimeInfo();
//        info.setStartTime(startTime);
//        info.setEndTime(endTime);
//        return info;
//    }
//    
//    /**
//     * endtime为今天
//     * @return
//     */
//    public static TimeInfo getCurrentMonthStartAndEndTime(){
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.set(Calendar.DATE, 1);
//        calendar1.set(Calendar.HOUR_OF_DAY, 0);
//        calendar1.set(Calendar.MINUTE, 0);
//        calendar1.set(Calendar.SECOND, 0);
//        calendar1.set(Calendar.MILLISECOND, 0);
//        Date startDate = calendar1.getTime();
//        long startTime = startDate.getTime();
//
//        Calendar calendar2 = Calendar.getInstance();
////      calendar2.set(Calendar.HOUR_OF_DAY, 23);
////      calendar2.set(Calendar.MINUTE, 59);
////      calendar2.set(Calendar.SECOND, 59);
////      calendar2.set(Calendar.MILLISECOND, 999);
//        Date endDate = calendar2.getTime();
//        long endTime = endDate.getTime();
//        TimeInfo info = new TimeInfo();
//        info.setStartTime(startTime);
//        info.setEndTime(endTime);
//        return info;
//    }
//    
//    public static TimeInfo getLastMonthStartAndEndTime(){
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.add(Calendar.MONTH, -1);
//        calendar1.set(Calendar.DATE, 1);
//        calendar1.set(Calendar.HOUR_OF_DAY, 0);
//        calendar1.set(Calendar.MINUTE, 0);
//        calendar1.set(Calendar.SECOND, 0);
//        calendar1.set(Calendar.MILLISECOND, 0);
//        Date startDate = calendar1.getTime();
//        long startTime = startDate.getTime();
//
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.add(Calendar.MONTH, -1);
//        calendar2.set(Calendar.DATE, 1);
//        calendar2.set(Calendar.HOUR_OF_DAY, 23);
//        calendar2.set(Calendar.MINUTE, 59);
//        calendar2.set(Calendar.SECOND, 59);
//        calendar2.set(Calendar.MILLISECOND, 999);
//        calendar2.roll(Calendar.DATE,  - 1 );
//        Date endDate = calendar2.getTime();
//        long endTime = endDate.getTime();
//        TimeInfo info = new TimeInfo();
//        info.setStartTime(startTime);
//        info.setEndTime(endTime);
//        return info;
//    }
    
    public static String getTimestampStr() {
        return Long.toString(System.currentTimeMillis());
    }
    
    /**最近一周**/
   	public static Date getLastWeek(){
   		Calendar calendar1 = Calendar.getInstance();
   		calendar1.add(GregorianCalendar.DATE, -6);
   		Date date = calendar1.getTime();
		String s = sdf_yyyy_MM_dd.format(date);
		String start = s+" 00:00";
		Date startDate = null;
		try {
			startDate = sdf_yyyy_MM_dd_HHmm.parse(start);
		} catch (ParseException e) {
			e.printStackTrace();
			startDate = calendar1.getTime();
		}
   		return startDate;
   	}
   	
   	/**获取最近一周,**/
   	@SuppressLint("UseSparseArrays")
   	public static Map<Integer, String> getWeekLabel(){
   		Map<Integer, String> map = new HashMap<Integer, String>();
   		map.clear();
   		for (int i = 0; i < 7; i++) {
   			Calendar calendar = Calendar.getInstance(Locale.CHINA);
   			calendar.add(GregorianCalendar.DATE, i-6);
   			Date dd = new Date(calendar.getTimeInMillis());
   			String dd1 = sdf_yyyy_MM_dd_EEE.format(dd);
   			map.put(i, dd1);
   		}
   		return map;
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
		list.add(11, "其它");//自定义
		return list;
	}
	
	/**获取最近一周**/
	@SuppressLint("UseSparseArrays")
	public static List<String> getListWeekLabel(){
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 7; i++) {
			Calendar calendar = Calendar.getInstance(Locale.CHINA);
			calendar.add(GregorianCalendar.DATE, i-6);
			Date dd = new Date(calendar.getTimeInMillis());
			String dd1 = sdf_EEE.format(dd);
			list.add(i, dd1);
		}
		return list;
	}
   	
   	/**
   	 * 将小时转换成天（周）的小数
   	 * @return
   	 */
   	public static double dayOfWeekToDouble(Date d){
   		String H = sdf_H.format(d);
   		String m = sdf_m.format(d);
   		String s = sdf_s.format(d);
   		double sd = Double.valueOf(s);
   		double md = Double.valueOf(m);
   		double hd = Double.valueOf(H);
   		BigDecimal mb = new BigDecimal(md/60);
   		BigDecimal sb = new BigDecimal(sd/(60*60));
   		double df1 = mb.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
   		double df2 = sb.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
   		BigDecimal hb = new BigDecimal((hd+df1+df2)/48);
   		double f2 = hb.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
   		return f2;
   	}
   	
   	/**
	 * 将分变换成小时的小数
	 * @return
	 */
	public static double strToDouble(Date d){
		String h = sdf_H.format(d);
		String m = sdf_m.format(d);
   		String s = sdf_s.format(d);
		double hd = Double.valueOf(h);
		double md = Double.valueOf(m);
   		double sd = Double.valueOf(s);
		BigDecimal b = new BigDecimal(md/60);
   		BigDecimal sb = new BigDecimal(sd/(60*60));
		double f1 = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
   		double df2 = sb.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
   		BigDecimal hb = new BigDecimal((hd+f1+df2)/2);
   		double f2 = hb.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f2;
	}
   	
   	/**获取周对应的时间小数 **/
	public static double getPressDoubleByDate(Map<Integer, String> map, Date date){
		Iterator<Entry<Integer, String>> it = map.entrySet().iterator();
		double dayDouble = 0;
		while (it.hasNext()) {
			Entry<Integer, String> m = it.next();
			if (m.getValue().contains(sdf_yyyy_MM_dd_EEE.format(date))) {
				double i = m.getKey();
				dayDouble = i + dayOfWeekToDouble(date);
				break;
			}
		}
		return dayDouble;
	}
	
//	/**获取周对应的时间小数
//	 * @param sugarType **/
//	public static double getSugarDoubleByDate(Map<Integer, String> map, Date date, BloodSugarType sugarType){
//		Iterator<Entry<Integer, String>> it = map.entrySet().iterator();
//		double dayDouble = 0;
//		while (it.hasNext()) {
//			Map.Entry<Integer, String> m = it.next();
//			if (m.getValue().contains(sdf_yyyy_MM_dd_EEE.format(date))) {
//				dayDouble = m.getKey();
//				break;
//			}
//		}
//		double type = sugarType.ordinal();
//		BigDecimal mb = new BigDecimal(type/12);
//		double f1 = mb.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
//		return dayDouble+f1;
//	}
   	
   	/**获取周对应的时间小数**/
   	public static double getDoubleByDate2(Map<Integer, String> map, Date date){
   		Iterator<Entry<Integer, String>> it = map.entrySet().iterator();
   		double dayDouble = 0;
   		while (it.hasNext()) {
   			Entry<Integer, String> m = it.next();
   			if (m.getValue().contains(sdf_yyyy_MM_dd_EEE.format(date))) {
   				double i = m.getKey();
   				dayDouble = i;
   				break;
   			}
   		}
   		return dayDouble;
   	}
   	
   	public static String parseTime(String times) {
		String timeStr = "";
		if (times.contains(",")) {
			String[] tims = times.split(",");
			if (tims.length==2) {
				timeStr = sdf_HH_mm.format(new Date(Long.valueOf(tims[0])));
				timeStr += ","+sdf_HH_mm.format(new Date(Long.valueOf(tims[1])));
			}
			if (tims.length==3) {
				timeStr = sdf_HH_mm.format(new Date(Long.valueOf(tims[0])));
				timeStr += ","+sdf_HH_mm.format(new Date(Long.valueOf(tims[1])));
				timeStr += ","+sdf_HH_mm.format(new Date(Long.valueOf(tims[2])));
			}
			if (tims.length==4) {
				timeStr = sdf_HH_mm.format(new Date(Long.valueOf(tims[0])));
				timeStr += ","+sdf_HH_mm.format(new Date(Long.valueOf(tims[1])));
				timeStr += ","+sdf_HH_mm.format(new Date(Long.valueOf(tims[2])));
				timeStr += ","+sdf_HH_mm.format(new Date(Long.valueOf(tims[3])));
			}
		}else {
			timeStr = sdf_HH_mm.format(new Date(Long.valueOf(times)));
		}
		return timeStr;
	}
}