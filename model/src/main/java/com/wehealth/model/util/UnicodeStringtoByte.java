package com.wehealth.model.util;

import android.text.TextUtils;

public class UnicodeStringtoByte {
	private static final byte ASCII_SPACE = ' ';
	private static final byte ASCII_0 = '0';
	private static final byte ASCII_9 = '9';
	private static final byte ASCII_MINUS = '-';
	private static final byte ASCII_NUMBER_VALUE = 48; /* ASCII码到数字值的转换 */

	private static int getStringByteLength(String str) {
		int len = 1;
		byte[] byteArray;

		if (str == null)
			return 0;

		byteArray = str.getBytes();
		for (int i = 0; i < byteArray.length; i++) {
			if (byteArray[i] == ASCII_SPACE) {
				len++;
			}
		}

		return len;
	}

	public static int[] string2Int(String source){
		String[] str = source.split(" ");
		int[] des = new int[str.length];
		for (int i = 0; i < str.length; i++) {
			if (!TextUtils.isEmpty(str[i])) {
				des[i] = Integer.valueOf(str[i].trim());
			}
		}
		return des;
	}

	public static short[] StringToByte(String str) {
		short[] byteArray;
		byte[] srcArray;
		short tmp = 0;
		int len = 0;
		boolean SymbolFlag = false;
		int count = 0;

		len = getStringByteLength(str);
		srcArray = str.getBytes();
		byteArray = new short[len];

		for (int i = 0; i < srcArray.length; i++) {
			if (srcArray[i] == ASCII_SPACE) {
				if (SymbolFlag)
					tmp = (short) -tmp;
				byteArray[count++] = tmp;

				SymbolFlag = false;
				tmp = 0;
			} else if (srcArray[i] == ASCII_MINUS) {
				SymbolFlag = true;
			} else if (srcArray[i] >= ASCII_0 && srcArray[i] <= ASCII_9) {
				tmp = (short) (tmp * 10 + (srcArray[i] - ASCII_NUMBER_VALUE));
			} else {
				return null;
			}
		}

		if (SymbolFlag)
			tmp = (short) -tmp;

		byteArray[count] = tmp;

		return byteArray;
	}
	
}