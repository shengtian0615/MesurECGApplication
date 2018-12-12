package com.wehealth.urion;

import android.os.Parcel;
import android.os.Parcelable;


public class Error extends IBean {

	/**
	 * 发送指令失败
	 */
	public static final int ERROR_FIND_NODEVICE_FAILED = 7;
	/**
	 * 给血压计发送消息失败
	 */
	public static final int ERROR_SEND_INSTRUCT_FAILED = 8;
	/**
	 * 连接指定设备失败
	 */
	public static final int ERROR_CONNECTION_FAILED = 0;
	/**
	 * 连接丢失
	 */
	public static final int ERROR_CONNECTION_LOST = 9;

	// 血压仪错误信息常量
	/**
	 * E-E EEPROM异常
	 */
	public static final int ERROR_EEPROM = 0x0E;
	/**
	 * E-1 人体心跳信号太小或压力突降
	 */
	public static final int ERROR_HEART = 0x01;
	/**
	 * E-2 杂讯干扰
	 */
	public static final int ERROR_DISTURB = 0x02;
	/**
	 * E-3 充气时间过长
	 */
	public static final int ERROR_GASING = 0x03;
	/**
	 * E-4 测得的结果异常
	 */
	public static final int ERROR_TEST = 0x05;
	/**
	 * E-C 校正异常
	 */
	public static final int ERROR_REVISE = 0x0C;
	/**
	 * E-B 电源低电压
	 */
	public static final int ERROR_POWER = 0x0B;

	/**
	 * 错误代码，该错误代码分为连接时的错误(int类型)和连接后血压仪发送的错误(float类型)
	 */
	private int error_code;

	private int error;

	public Error() {
		super();
	}

	public Error(int errorCode) {
		super();
		error_code = errorCode;
	}

	public int getError_code() {
		return error_code;
	}

	public void setError_code(int errorCode) {
		error_code = errorCode;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(error_code);
		dest.writeInt(error);
	}

	public static final Creator<Error> CREATOR = new Creator<Error>() {
		public Error createFromParcel(Parcel in) {
			return new Error(in);
		}

		public Error[] newArray(int size) {
			return new Error[size];
		}
	};

	private Error(Parcel in) {
		error_code = in.readInt();
		error = in.readInt();
	}

	public void analysis(int[] f) {
		error = f[3];
	}
}
