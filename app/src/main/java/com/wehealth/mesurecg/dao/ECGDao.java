/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *
 */
package com.wehealth.mesurecg.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wehealth.mesurecg.MeasurECGApplication;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.model.domain.enumutil.ECGDataDiagnosisType;
import com.wehealth.model.domain.model.ECGData;
import com.wehealth.model.domain.model.RegisteredUser;
import com.wehealth.model.util.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ECGDao {
	static final String TABLE_NAME = "ecgdata";
	static final String COLUMN_NAME_ID = "ECGDATA_ID";
	static final String COLUMN_NAME_TIME = "ECGDATA_TIME";
	static final String COLUMN_NAME_AUTO_DIAGNOSIS_RESULT = "ECGDATA_AUTO_DIAGNOSIS_RESULT";
	static final String COLUMN_NAME_EQUIPMENT_SERIAL_NO = "ECGDATA_EQUIPMENT_SERIAL_NO";
	static final String COLUMN_NAME_PATIENT_ID_CARD = "ECGDATA_PATIENT_ID_CARD";
	static final String COLUMN_NAME_SYMPTOMS = "ECGDATA_SYMPTOMS";
	static final String COLUMN_NAME_VERSION = "ECGDATA_VERSION";
	static final String COLUMN_NAME_REG_USR_ID = "ECGDATA_REG_USR_ID";
	static final String COLUMN_NAME_DIAGNOSIS_TYPE = "ECGDATA_DIAGNOSIS_TYPE";
	static final String COLUMN_NAME_HEART_RATE = "ECGDATA_HEART";
	static final String COLUMN_NAME_DOCTOR_ID = "ECGDATA_DOCTOR_ID";
	static final String COLUMN_NAME_MANUL_DIAGNOSIS_RESULT = "ECGDATA_MANUL_DIAGNOSIS_RESULT";
	static final String COLUMN_NAME_LEVEL = "ECGDATA_LEVEL";//用ECGDATA对象里的score表示
	static final String COLUMN_NAME_USER_PHONE = "ECGDATA_USER_PHONE";
	static final String COLUMN_NAME_USER_NAME = "ECGDATA_USER_NAME";

	private DbOpenHelper dbHelper;

	private static ECGDao ecgInstance;
	
	public static ECGDao getECGIntance(String idCardNo){
		if (ecgInstance==null) {
			ecgInstance = new ECGDao(idCardNo);
		}
		return ecgInstance;
	}
	
	private ECGDao(String idCardNo){
		dbHelper = DbOpenHelper.getInstance(MeasurECGApplication.getInstance(), idCardNo);
	}

	public synchronized long saveData(int eCGData_Level, ECGData data) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// int id = -1;
		long db_id = -1;
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put(COLUMN_NAME_AUTO_DIAGNOSIS_RESULT, data.getAutoDiagnosisResult());
			values.put(COLUMN_NAME_DIAGNOSIS_TYPE, data.getRequestedDiagnosisType());
			values.put(COLUMN_NAME_DOCTOR_ID, data.getDoctorId());
			values.put(COLUMN_NAME_EQUIPMENT_SERIAL_NO, data.getEquipmentSerialNo());
			values.put(COLUMN_NAME_MANUL_DIAGNOSIS_RESULT, data.getManulDiagnosisResult());
			values.put(COLUMN_NAME_PATIENT_ID_CARD, data.getPatientId());
			values.put(COLUMN_NAME_REG_USR_ID, data.getRegisteredUserId());
			values.put(COLUMN_NAME_HEART_RATE, data.getHeartRate());
			values.put(COLUMN_NAME_TIME, data.getTime().getTime());
			values.put(COLUMN_NAME_VERSION, data.getVersion());
			values.put(COLUMN_NAME_SYMPTOMS, data.getSymptoms());
			values.put(COLUMN_NAME_LEVEL, eCGData_Level);
			values.put(COLUMN_NAME_USER_PHONE, data.getCellphone());
			values.put(COLUMN_NAME_USER_NAME, data.getPatiName());
			db_id = db.insert(TABLE_NAME, null, values);

		}
		return db_id;
	}

	public List<ECGData> getECGDataByPatId(String patientId) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<ECGData> msgs = new ArrayList<ECGData>();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select * from " + TABLE_NAME
							+ " desc where ECGDATA_PATIENT_ID_CARD='"
							+ patientId + "'", null);
			while (cursor.moveToNext()) {
				ECGData msg = constructECGData(cursor);
				msgs.add(msg);
			}
			if (cursor != null) {
				cursor.close();
			}
		}
		return msgs;
	}
	
	public List<ECGData> queryECGDataByPid(String pId){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<ECGData> msgs = new ArrayList<ECGData>();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_NAME_PATIENT_ID_CARD +" like '%"+ pId +"%' or " + COLUMN_NAME_USER_PHONE+" like '%"+ pId +"%' or " +COLUMN_NAME_USER_NAME+ " like '%"+pId+"%' ", null);
			while (cursor.moveToNext()) {
				ECGData msg = constructECGData(cursor);
				msgs.add(msg);
			}
			if (cursor != null) {
				cursor.close();
			}
		}
		return msgs;
	}

	public List<ECGData> getAllECGData() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<ECGData> msgs = new ArrayList<ECGData>();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
			while (cursor.moveToNext()) {
				ECGData msg = constructECGData(cursor);
				msgs.add(msg);
			}
			if (cursor != null) {
				cursor.close();
			}
		}
		Collections.sort(msgs);
		return msgs;
	}
	
	public void updataECGDataByTime(ECGData data){
		updatECGDataByTime(data.getTime().getTime(), constructValues(data));
	}
	
	public void updatECGDataByTime(long time, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.update(TABLE_NAME, values, COLUMN_NAME_TIME + " = ?",
					new String[] { String.valueOf(time) });
		}
	}

	public void deleteECGData(String id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(TABLE_NAME, COLUMN_NAME_TIME + " = ?", new String[] { id });
		}
	}
	
	public void updatEcgData(long ecgData_id, ContentValues values) {//int level,
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
//			values.put(COLUMN_NAME_LEVEL, level);
//			db.update(TABLE_NAME, values, COLUMN_NAME_ID + " = ?",
//					new String[] { String.valueOf(ecgData_id) });

			db.update(TABLE_NAME, values, COLUMN_NAME_ID + " = ?",
					new String[] { String.valueOf(ecgData_id) });
		}
	}

	public void updatEcgData(long ecgData_id, ECGData data) {
		updatEcgData(ecgData_id, constructValues(data));
	}
	

	public ECGData getDataByTime(long time){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		ECGData ecgData = null;
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_NAME_TIME + " = " + time, null);
			if (cursor == null) {
				return null;
			}
			if (cursor.moveToFirst()) {
				ecgData = constructECGData(cursor);
			}
			if (cursor != null) {
				cursor.close();
			}
		}
		return ecgData;
	}

	public ContentValues constructValues(ECGData data) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_AUTO_DIAGNOSIS_RESULT,
				data.getAutoDiagnosisResult());
		values.put(COLUMN_NAME_DIAGNOSIS_TYPE, data.getRequestedDiagnosisType());
		values.put(COLUMN_NAME_DOCTOR_ID, data.getDoctorId());
		values.put(COLUMN_NAME_EQUIPMENT_SERIAL_NO, data.getEquipmentSerialNo());
		values.put(COLUMN_NAME_MANUL_DIAGNOSIS_RESULT,
				data.getManulDiagnosisResult());
		values.put(COLUMN_NAME_PATIENT_ID_CARD, data.getPatientId());
		values.put(COLUMN_NAME_REG_USR_ID, data.getRegisteredUserId());
		values.put(COLUMN_NAME_HEART_RATE, data.getHeartRate());
		values.put(COLUMN_NAME_TIME, data.getTime().getTime());
		values.put(COLUMN_NAME_VERSION, data.getVersion());
		values.put(COLUMN_NAME_SYMPTOMS, data.getSymptoms());
		values.put(COLUMN_NAME_LEVEL, data.getScore());
		values.put(COLUMN_NAME_USER_PHONE, data.getCellphone());
		values.put(COLUMN_NAME_USER_NAME, data.getPatiName());

		return values;
	}

	public ECGData constructECGData(Cursor paramCursor) {
		ECGData ret = new ECGData();
		int i1 = paramCursor.getInt(paramCursor.getColumnIndex(COLUMN_NAME_ID));
		ret.setId((long) i1);
		String str1 = paramCursor.getString(paramCursor
				.getColumnIndex(COLUMN_NAME_AUTO_DIAGNOSIS_RESULT));
		ret.setAutoDiagnosisResult(str1);

		i1 = paramCursor.getInt(paramCursor.getColumnIndex(COLUMN_NAME_DIAGNOSIS_TYPE));
		ret.setRequestedDiagnosisType(i1);

		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_DOCTOR_ID));
		ret.setDoctorId(str1);

		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_EQUIPMENT_SERIAL_NO));
		ret.setEquipmentSerialNo(str1);

		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_MANUL_DIAGNOSIS_RESULT));
		ret.setManulDiagnosisResult(str1);

		i1 = paramCursor.getInt(paramCursor.getColumnIndex(COLUMN_NAME_HEART_RATE));
		ret.setHeartRate(i1);
		
		i1 = paramCursor.getInt(paramCursor.getColumnIndex(COLUMN_NAME_VERSION));
		ret.setVersion(i1);
		
		i1 = paramCursor.getInt(paramCursor.getColumnIndex(COLUMN_NAME_LEVEL));
		ret.setScore(i1);
		
		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_PATIENT_ID_CARD));
		ret.setPatientId(str1);

		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_REG_USR_ID));
		ret.setRegisteredUserId(str1);

		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_SYMPTOMS));
		ret.setSymptoms(str1);
		
		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_USER_PHONE));
		ret.setCellphone(str1);
		
		str1 = paramCursor.getString(paramCursor.getColumnIndex(COLUMN_NAME_USER_NAME));
		ret.setPatiName(str1);
		
		long l1 = paramCursor.getLong(paramCursor.getColumnIndex(COLUMN_NAME_TIME));
		ret.setTime(new Date(l1));

		return ret;
	}

	public long saveECGDataByMap(Map<String, Map<String, String>> listMaps) {
		Map<String, String> pInfo = listMaps.get(Constant.ECG_PATIENT_INFO);
		Map<String, String> ecgAttr = listMaps.get(Constant.ECG_ANALYSE_PARAM);
		long timeLong = Long.valueOf(pInfo.get("ecg_checktime"));
		String p_ID = pInfo.get("ID");
		String Auto_Result = ecgAttr.get("Auto_Result");
		String hr = ecgAttr.get("HeartRate");
		ECGData ecgData = new ECGData();
		ecgData.setVersion(1);
		ecgData.setAutoDiagnosisResult(Auto_Result);
		ecgData.setHeartRate(Integer.valueOf(hr));
		ecgData.setPatientId(p_ID);
		ecgData.setTime(new Date(timeLong));
		RegisteredUser user = MeasurECGApplication.getInstance().getRegisterUser();
		ecgData.setEquipmentSerialNo(PreferUtils.getIntance().getSerialNo());
		ecgData.setRegisteredUserId(user.getIdCardNo());
		ecgData.setRequestedDiagnosisType(ECGDataDiagnosisType.auto.ordinal());
		return saveData(1, ecgData);
	}

	public void reset(){
		if (ecgInstance!=null) {
			ecgInstance = null;
		}
	}
}
/**
 	
 	public Date getLastUsedTime(String patientId) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select MAX(ECGDATA_TIME) as abc, ECGDATA_ID from "
							+ TABLE_NAME + " where ECGDATA_PATIENT_ID_CARD='"
							+ patientId + "'", null);
			if (cursor.getCount() == 0) {
				cursor.close();
				return null;
			}
			cursor.moveToNext();
			long l1 = cursor.getLong(cursor.getColumnIndex("abc"));
			if (l1 == 0) {
				if (cursor != null) {
					cursor.close();
				}
				return null;
			}
			Date ret = new Date(l1);
			if (cursor != null) {
				cursor.close();
			}
			return ret;
		}
		return null;
	}
 	
 	public long getNumberOfRecord(String patientId) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select count(*) from " + TABLE_NAME
							+ " desc where ECGDATA_PATIENT_ID_CARD='"
							+ patientId + "'", null);
			if (cursor.getCount() == 0) {
				cursor.close();
				return 0;
			}
			cursor.moveToNext();
			long ret = cursor.getLong(0);
			if (cursor != null) {
				cursor.close();
			}
			return ret;
		}
		return 0;
	}
 	
    public List<ECGData> getECGDataList(String patientId, long start, long end) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<ECGData> msgs = new ArrayList<ECGData>();
		if (db.isOpen()) {
			Cursor cursor = db
					.rawQuery("select * from " + TABLE_NAME
							+ " desc where ECGDATA_TIME >= '" + start
							+ "' and ECGDATA_TIME <= '" + end
							+ "' and ECGDATA_PATIENT_ID_CARD='" + patientId
							+ "'", null);
			while (cursor.moveToNext()) {
				ECGData msg = constructECGData(cursor);
				msgs.add(msg);
			}
			if (cursor != null) {
				cursor.close();
			}
		}
		return msgs;
	}
	**/
