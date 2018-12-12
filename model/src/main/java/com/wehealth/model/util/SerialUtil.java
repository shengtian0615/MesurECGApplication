/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Nov 9, 2016
 */

package com.wehealth.model.util;

import android.annotation.SuppressLint;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class SerialUtil {
	
	static private String phoneReg = "1[34578]\\d{9}";
	private static int[] power = new int[] { 5, 3, 7, 1, 9 };
	private static SimpleDateFormat dayForSerial = new SimpleDateFormat("yyyyMMdd");

	/**
	 * 检查pid是否是字母或者数字
	 * 如果是，则返回true；否则返回false
	 * @param pid
	 * @return
	 */
	public static boolean checkPatientID(String pid){
		String m = "^[0-9a-zA-Z]{3,11}$";
		return pid.matches(m);
	}
	
	/**
	 * 验证手机号码
	 * @param phoneStr 手机号码
	 * @return 如果是有效的返回true
	 */
	public static boolean phone(String phoneStr) {
		
		if (phoneStr==null) {
			return false;
		}else if ("".equals(phoneStr)) {
			return false;
		}else if (!phoneStr.matches(phoneReg)) {
			return false;
		}else {
			return true;
		}
	}
	
	/**判断id是否是编号还是身份证号**/
	public static boolean checkID(String id){
		if (phone(id)) {
			return true;
		}
		if (IDCardValidator.isValidateIDCard(id)) {
			return true;
		}
		if (checkPatientID(id)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Make serial
	 * 
	 * @param fiveString
	 * @return
	 */
	public static String makeSerial(String fiveString) {
		String lastLetter = cal6Code(fiveString);
		return fiveString + lastLetter;
	}

	/**
	 * Is the serial valid serail?
	 * 
	 * @param serial
	 *            6 number string.
	 * @return
	 */
	public static boolean isValSerial(String serial) {
		if (serial == null || !DataUtil.isPositiveNumber(serial, 6)) {
			return false;
		}

		String fiveString = serial.substring(0, 5);
		String lastLetter = cal6Code(fiveString);
		return serial.endsWith(lastLetter);
	}

	public static String encodeId(Long id) {
		String tmp = "";
		String code = String.valueOf(id);
		int sum = 0;
		for (int i = 0; i < code.length(); i++) {
			char c = code.charAt(i);
			int number = toNumber(c);
			number = 9 - number;
			tmp += String.valueOf(number);

			sum += number * (i * 2 + 1);
		}
		int mode = sum % 10;

		tmp += mode;

		return Long.toOctalString(Long.parseLong(tmp));
	}

	public static Long decodeId(String serial) {
		long number = Long.parseLong(serial, 8);
		String code = String.valueOf(number);
		String sub = code.substring(0, code.length() - 1);
		String tmp = "";
		int sum = 0;
		for (int i = 0; i < sub.length(); i++) {
			char c = sub.charAt(i);
			int n = toNumber(c);
			sum += n * (i * 2 + 1);
			tmp += String.valueOf(9 - n);
		}
		int mode = sum % 10;
		if (!code.endsWith(mode + ""))
			return null;

		return Long.parseLong(tmp);
	}

	/**
	 * turn number char to number
	 * 
	 * @param numberChar
	 * @return
	 */
	private static int toNumber(char numberChar) {
		return numberChar - '0';
	}

	/**
	 * Cal the 6 letter
	 * 
	 * @param fiveString
	 *            , 5 number string.
	 * @return
	 */
	private static String cal6Code(String fiveString) {
		if (fiveString == null || !DataUtil.isPositiveNumber(fiveString, 5)) {
			return null;
		}

		int sum = 0;
		for (int i = 0; i < 5; i++) {
			char c = fiveString.charAt(i);
			int number = toNumber(c);
			sum += power[i] * number;
		}

		int result = sum % 10;

		return String.valueOf(result);
	}

	public static String formatNumber(long number, String pattern) {
		DecimalFormat formatter = new DecimalFormat(pattern);

		return formatter.format(number);
	}

	public static String dayForSerial(Date date) {
		return dayForSerial.format(date);
	}

	public static void main(String[] args) {
		System.out.println(formatNumber(34, "000"));

	}
}
