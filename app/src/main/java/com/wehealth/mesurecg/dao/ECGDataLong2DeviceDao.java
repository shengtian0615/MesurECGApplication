package com.wehealth.mesurecg.dao;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.wehealth.mesurecg.MeasurECGApplication;
import com.wehealth.model.domain.model.ECGDataLong2Device;
import com.wehealth.model.domain.model.RegisteredUser;

@SuppressLint("SimpleDateFormat")
public class ECGDataLong2DeviceDao {
	
	public static final String TABLE_NAME = "ecgdata_longdetail";
	public static final String COLUMN_NAME_TIME = "ECGDATALONGDETAIL_TIME";
	public static final String COLUMN_NAME_ECGDATA_ID = "ECGDATALONGDETAIL_ECGDATA_ID";
	public static final String COLUMN_NAME_PATIENT_ID_CARD = "ECGDATAL_PATIENT_ID_CARD";
	public static final String COLUMN_NAME_REG_USR_ID = "ECGDATAL_REG_USR_ID";
	public static final String COLUMN_NAME_ANALYSE_RESULT = "ECGDATAL_MANUL_DIAGNOSIS_RESULT";
	public static final String COLUMN_NAME_TOTAL_TIME = "ECGDATAL_TOTAL_TIME";
	public static final String COLUMN_NAME_HEART_RATE = "ECGDATAL_PATIENT_HEART";
	public static final String COLUMN_NAME_SAVEFILETYPE = "ECGDATAL_SAVEFILETYPE";
	public static final String COLUMN_NAME_24H_LEADNAME = "ECGDATAL_24H_LEADNAME";
	public static final String COLUMN_NAME_V1_PATH = "ECGDATAL_V1_PATH";
	public static final String COLUMN_NAME_V2_PATH = "ECGDATAL_V2_PATH";
	public static final String COLUMN_NAME_V3_PATH = "ECGDATAL_V3_PATH";
	public static final String COLUMN_NAME_V4_PATH = "ECGDATAL_V4_PATH";
	public static final String COLUMN_NAME_V5_PATH = "ECGDATAL_V5_PATH";
	public static final String COLUMN_NAME_V6_PATH = "ECGDATAL_V6_PATH";
	public static final String COLUMN_NAME_I_PATH = "ECGDATAL_I_PATH";
	public static final String COLUMN_NAME_II_PATH = "ECGDATAL_II_PATH";
	public static final String COLUMN_NAME_III_PATH = "ECGDATAL_III_PATH";
	public static final String COLUMN_NAME_AVR_PATH = "ECGDATAL_AVR_PATH";
	public static final String COLUMN_NAME_AVL_PATH = "ECGDATAL_AVL_PATH";
	public static final String COLUMN_NAME_AVF_PATH = "ECGDATAL_AVF_PATH";
	public static final String COLUMN_NAME_24H_PATH = "ECGDATAL_24H_PATH";

	private DbOpenHelper dbHelper;
	private static ECGDataLong2DeviceDao instance;
	
	public static ECGDataLong2DeviceDao getInstance(String idCardNo){
		if (instance==null) {
			instance = new ECGDataLong2DeviceDao(MeasurECGApplication.getInstance(), idCardNo);
		}
		return instance;
	}

	private ECGDataLong2DeviceDao(Context context, String idCardNo) {
		dbHelper = DbOpenHelper.getInstance(context, idCardNo);
	}
	
	/**保存心电数据**/
	public long saveECGDataByPid(String p_Id, int saveType, int leadName){
		long timeLong = System.currentTimeMillis();
		ECGDataLong2Device ecgData = new ECGDataLong2Device();
		ecgData.setPatientId(p_Id);
		ecgData.setTime(timeLong);
		
//		File fileDir = new File(android.os.Environment.getExternalStorageDirectory()+File.separator
//				+"ECGDATA"+File.separator+"Data2Device");
//		if (!fileDir.exists()) {
//			fileDir.mkdirs();
//		}
		if (saveType==1) { // 手动模式
			ecgData.setAVF_path(timeLong+"_avf.txt");//fileDir.getAbsolutePath()+File.separator+
			ecgData.setAVL_path(timeLong+"_avl.txt");
			ecgData.setAVR_path(timeLong+"_avr.txt");
			ecgData.setI_path(timeLong+"_i.txt");
			ecgData.setII_path(timeLong+"_ii.txt");
			ecgData.setIII_path(timeLong+"_iii.txt");
			ecgData.setV1_path(timeLong+"_v1.txt");
			ecgData.setV2_path(timeLong+"_v2.txt");
			ecgData.setV3_path(timeLong+"_v3.txt");
			ecgData.setV4_path(timeLong+"_v4.txt");
			ecgData.setV5_path(timeLong+"_v5.txt");
			ecgData.setV6_path(timeLong+"_v6.txt");
		}else if (saveType==2) { // 24小时模式
			ecgData.setH24_path(String.valueOf(timeLong));
			ecgData.setH24_leadName(leadName);
		}
		ecgData.setSaveFileType(saveType);
		RegisteredUser user = MeasurECGApplication.getInstance().getRegisterUser();
		ecgData.setRegisteredUserId(user.getIdCardNo());
		return saveData(ecgData);
	}
	
	public synchronized long saveData(ECGDataLong2Device data){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long db_id = -1;
		if(db.isOpen()){
			ContentValues values = new ContentValues();
			values.put(COLUMN_NAME_ANALYSE_RESULT, data.getAutoDiagnosisResult());
			values.put(COLUMN_NAME_PATIENT_ID_CARD, data.getPatientId());
			values.put(COLUMN_NAME_ECGDATA_ID, data.getEcgdata_id());
			values.put(COLUMN_NAME_REG_USR_ID, data.getRegisteredUserId());
			values.put(COLUMN_NAME_TIME, data.getTime());
			values.put(COLUMN_NAME_HEART_RATE, data.getHeartRate());
			values.put(COLUMN_NAME_TOTAL_TIME, data.getTotalTime());
			values.put(COLUMN_NAME_SAVEFILETYPE, data.getSaveFileType());
			values.put(COLUMN_NAME_24H_LEADNAME, data.getH24_leadName());
			values.put(COLUMN_NAME_24H_PATH, data.getH24_path());
			values.put(COLUMN_NAME_V1_PATH, data.getV1_path());
			values.put(COLUMN_NAME_V2_PATH, data.getV2_path());
			values.put(COLUMN_NAME_V3_PATH, data.getV3_path());
			values.put(COLUMN_NAME_V4_PATH, data.getV4_path());
			values.put(COLUMN_NAME_V5_PATH, data.getV5_path());
			values.put(COLUMN_NAME_V6_PATH, data.getV6_path());
			values.put(COLUMN_NAME_AVF_PATH, data.getAVF_path());
			values.put(COLUMN_NAME_AVL_PATH, data.getAVL_path());
			values.put(COLUMN_NAME_AVR_PATH, data.getAVR_path());
			values.put(COLUMN_NAME_I_PATH, data.getI_path());
			values.put(COLUMN_NAME_II_PATH, data.getII_path());
			values.put(COLUMN_NAME_III_PATH, data.getIII_path());
			long id = db.insert(TABLE_NAME, null, values);
			if (id==-1) {
				db_id = 0;
			}else {
				db_id = data.getTime();
			}
		}
		return db_id;
	}
	
	public List<ECGDataLong2Device> getECGDataLsByECGData_ID(int ecgId){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<ECGDataLong2Device> msgs = new ArrayList<ECGDataLong2Device>();
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "+COLUMN_NAME_ECGDATA_ID+" = " + ecgId ,null);
			while(cursor.moveToNext()){
				ECGDataLong2Device msg = constructECGData(cursor);
				msgs.add(msg);
			}
			cursor.close();
		}
		return msgs;
	}
	
	public List<ECGDataLong2Device> getECGDataLsByPatient_ID(String patientID){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<ECGDataLong2Device> msgs = new ArrayList<ECGDataLong2Device>();
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "+COLUMN_NAME_PATIENT_ID_CARD+" = " + patientID ,null);
			while(cursor.moveToNext()){
				ECGDataLong2Device msg = constructECGData(cursor);
				msgs.add(msg);
			}
			cursor.close();
		}
		return msgs;
	}
	
	
	public void deleteECGDataL(long ecgdata_Time){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete(TABLE_NAME, COLUMN_NAME_TIME + " = "+ecgdata_Time, null);
		}
	}
	
	public void updateECGDataLong(long ecg2Device_time, int totalTime, String heartRate){
		if (ecg2Device_time < 0) {
			return ;
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			ContentValues values = new ContentValues();
			values.put(COLUMN_NAME_TOTAL_TIME, totalTime);
			if (!TextUtils.isEmpty(heartRate) && !"---".equals(heartRate)) {
				values.put(COLUMN_NAME_HEART_RATE, heartRate);
			}
//			values.put(COLUMN_NAME_TIME, ecg2Device_time);
			int updateResult = db.update(TABLE_NAME, values, COLUMN_NAME_TIME+" = ?", new String[]{String.valueOf(ecg2Device_time)});
			System.out.println("update totaltime = "+updateResult);
		}
	}
	
	private ECGDataLong2Device constructECGData(Cursor paramCursor)
	  {
		ECGDataLong2Device ret = new ECGDataLong2Device();
		String str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_ANALYSE_RESULT));
		ret.setAutoDiagnosisResult(str1);

		int ecgdata_id = paramCursor.getInt(paramCursor.getColumnIndex(COLUMN_NAME_ECGDATA_ID));
		ret.setEcgdata_id(ecgdata_id);
		
		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_PATIENT_ID_CARD));
		ret.setPatientId(str1);

		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_REG_USR_ID));
		ret.setRegisteredUserId(str1);
		
		long time = paramCursor.getLong(paramCursor.getColumnIndex(COLUMN_NAME_TIME));
		ret.setTime(time);
		
		int hr = paramCursor.getInt(paramCursor.getColumnIndex(COLUMN_NAME_HEART_RATE));
		ret.setHeartRate(hr);
		
		hr = paramCursor.getInt(paramCursor.getColumnIndex(COLUMN_NAME_TOTAL_TIME));
		ret.setTotalTime(hr);
		
		hr = paramCursor.getInt(paramCursor.getColumnIndex(COLUMN_NAME_SAVEFILETYPE));
		ret.setSaveFileType(hr);
		
		hr = paramCursor.getInt(paramCursor.getColumnIndex(COLUMN_NAME_24H_LEADNAME));
		ret.setH24_leadName(hr);
		
		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_24H_PATH));
		ret.setH24_path(str1);
		
		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_AVF_PATH));
		ret.setAVF_path(str1);
		
		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_AVL_PATH));
		ret.setAVL_path(str1);
		
		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_AVR_PATH));
		ret.setAVR_path(str1);
		
		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_I_PATH));
		ret.setI_path(str1);
		
		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_II_PATH));
		ret.setII_path(str1);
		
		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_III_PATH));
		ret.setIII_path(str1);
		
		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_V1_PATH));
		ret.setV1_path(str1);
		
		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_V2_PATH));
		ret.setV2_path(str1);
		
		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_V3_PATH));
		ret.setV3_path(str1);
		
		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_V4_PATH));
		ret.setV4_path(str1);
		
		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_V5_PATH));
		ret.setV5_path(str1);
		
		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_V6_PATH));
		ret.setV6_path(str1);
		
	    return ret;
	  }
}
