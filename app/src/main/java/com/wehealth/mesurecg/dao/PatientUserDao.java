package com.wehealth.mesurecg.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.wehealth.model.domain.model.PatientUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientUserDao {

	public static final String TABLE_NAME = "patient_user";
	public static final String COLUMN_NAME_ID = "patient_user_pid";
	public static final String COLUMN_NAME_IDCARDNO = "patient_user_idcardno";
	public static final String COLUMN_NAME_AGE = "patient_user_age";
	public static final String COLUMN_NAME_GENDER = "patient_user_gender";
	public static final String COLUMN_NAME_NAME = "patient_user_name";
	public static final String COLUMN_NAME_MSG = "patient_user_msg";
	public static final String COLUMN_NAME_STR = "patient_user_str";
//	public static final String COLUMN_NAME_SERIALNO = "patient_user_serialno";
	public static final String COLUMN_NAME_REGISTERID = "patient_user_registerid";
	public static final String COLUMN_NAME_TIME = "patient_user_time";
	public static final String COLUMN_NAME_UPDATA_TIME = "patient_user_updatatime";
	
	private DbOpenHelper dbHelper;
	private static PatientUserDao regIstance;

	public static PatientUserDao getInstance(Context ctx, String serialNo){
		if (regIstance==null) {
			regIstance = new PatientUserDao(ctx, serialNo);
		}
		return regIstance;
	}
	
	public PatientUserDao(Context ctx, String serialNo) {
		dbHelper = DbOpenHelper.getInstance(ctx, serialNo);
	}
	
	public synchronized void savePatientUser(PatientUser pu){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			db.beginTransaction();
			if(db.isOpen()){
				ContentValues values = new ContentValues();
				values.put(COLUMN_NAME_ID, pu.getPhoneId());
				values.put(COLUMN_NAME_IDCARDNO, pu.getInputIdCardNo());
				values.put(COLUMN_NAME_NAME, pu.getName());
				values.put(COLUMN_NAME_AGE, pu.getAge());
				values.put(COLUMN_NAME_GENDER, pu.getGender());
				values.put(COLUMN_NAME_MSG, pu.getMsg());
//				values.put(COLUMN_NAME_SERIALNO, pu.getSerialNo());
				values.put(COLUMN_NAME_REGISTERID, pu.getRegisterId());
				values.put(COLUMN_NAME_STR, pu.getStr());
				values.put(COLUMN_NAME_TIME, pu.getTime());
				values.put(COLUMN_NAME_UPDATA_TIME, pu.getUpdataTime());
				if (checkIdExists(pu.getPhoneId())) {
					updatePatientUser(pu);
				}else {
					long id = db.insert(TABLE_NAME, null, values);
					System.out.println("insert ："+id);
				}
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (db!=null) {
				db.endTransaction();
				System.out.println("结束事务");
			}
		}
	}
	
	public PatientUser getPatientUser(long time){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		PatientUser rd = null;
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "+ COLUMN_NAME_TIME+" = "+time, null);
			if (cursor.moveToFirst()) {
				rd = new PatientUser();
				String puid = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
				String puIdCardNo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_IDCARDNO));
				String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME));
				int age = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_AGE));
				int gender = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_GENDER));
				String msg = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MSG));
//				serialNo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SERIALNO));
				String registerid = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_REGISTERID));
				String str = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_STR));
				time = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_TIME));
				long updataTime = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_UPDATA_TIME));
				
				rd.setPhoneId(puid);
				rd.setInputIdCardNo(puIdCardNo);
				rd.setName(name);
				rd.setAge(age);
				rd.setGender(gender);
				rd.setName(name);
//				rd.setSerialNo(serialNo);
				rd.setRegisterId(registerid);
				rd.setStr(str);
				rd.setMsg(msg);
				rd.setTime(time);
				rd.setUpdataTime(updataTime);
			}
			if (cursor != null) {
				cursor.close();
			}
		}
		return rd;
	}
	
	public PatientUser getPatientUserBytpId(String tpId){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		PatientUser rd = null;
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "+ COLUMN_NAME_ID+" = ?", new String[]{tpId});
			if (cursor.moveToFirst()) {
				rd = new PatientUser();
				String puid = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
				String puIdCardNo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_IDCARDNO));
				String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME));
				int age = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_AGE));
				int gender = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_GENDER));
				String msg = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MSG));
//				serialNo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SERIALNO));
				String registerid = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_REGISTERID));
				String str = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_STR));
				long time = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_TIME));
				long updataTime = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_UPDATA_TIME));
				
				rd.setPhoneId(puid);
				rd.setInputIdCardNo(puIdCardNo);
				rd.setName(name);
				rd.setAge(age);
				rd.setGender(gender);
				rd.setName(name);
//				rd.setSerialNo(serialNo);
				rd.setRegisterId(registerid);
				rd.setStr(str);
				rd.setMsg(msg);
				rd.setTime(time);
				rd.setUpdataTime(updataTime);
			}
			if (cursor != null) {
				cursor.close();
			}
		}
		return rd;
	}
	
	public boolean checkIdExists(String id){
		boolean isExists = false;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select count(*) from "+TABLE_NAME+" where "+COLUMN_NAME_ID+" = ? ", new String[]{id});
			cursor.moveToFirst();  
			long count = cursor.getLong(0);
			if (count>0) {
				isExists = true;
			}
			if (cursor != null) {
				cursor.close();
			}
		}
		
		return isExists;
	}
	
	/**获取最近十条的用户信息**/
	public List<String> get10UserIDs(){
		List<String> list = new ArrayList<String>();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NAME_ID}, null, null, null, null, COLUMN_NAME_UPDATA_TIME+" desc", "10");
//			Cursor cursor = db.rawQuery("select " + COLUMN_NAME_ID + " from " + TABLE_NAME + " where "+COLUMN_NAME_SERIALNO+ " = ? " , new String[]{serialNo});
			while (cursor.moveToNext()) {
				String puid = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
				list.add(puid);
			}
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}
	
	/**获取用户信息**/
	public PatientUser getUserByPhone(String phone){
		if (TextUtils.isEmpty(phone)) {
			return null;
		}
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		PatientUser rd = null;
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "+ COLUMN_NAME_ID+" = ?", new String[]{phone});
			if (cursor.moveToFirst()) {
				rd = new PatientUser();
				String puid = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
				String puIdCardNo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_IDCARDNO));
				String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME));
				int age = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_AGE));
				int gender = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_GENDER));
				String msg = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MSG));
//				serialNo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SERIALNO));
				String registerid = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_REGISTERID));
				String str = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_STR));
				long time = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_TIME));
				long updataTime = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_UPDATA_TIME));
				
				rd.setPhoneId(puid);
				rd.setInputIdCardNo(puIdCardNo);
				rd.setName(name);
				rd.setAge(age);
				rd.setGender(gender);
				rd.setName(name);
//				rd.setSerialNo(serialNo);
				rd.setRegisterId(registerid);
				rd.setStr(str);
				rd.setMsg(msg);
				rd.setTime(time);
				rd.setUpdataTime(updataTime);
			}
			if (cursor != null) {
				cursor.close();
			}
		}
		return rd;
	}
	
	/**获取用户信息**/
	public PatientUser getUserByIdCardno(String idCardNo){//String serialNo,
//		if (TextUtils.isEmpty(serialNo)) {
//			return null;
//		}
		if (TextUtils.isEmpty(idCardNo)) {
			return null;
		}
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		PatientUser rd = null;
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "+ COLUMN_NAME_IDCARDNO+" = ?", new String[]{idCardNo});
			if (cursor.moveToFirst()) {
				rd = new PatientUser();
				String puid = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
				String puIdCardNo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_IDCARDNO));
				String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME));
				int age = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_AGE));
				int gender = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_GENDER));
				String msg = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MSG));
//				serialNo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SERIALNO));
				String registerid = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_REGISTERID));
				String str = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_STR));
				long time = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_TIME));
				long updataTime = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_UPDATA_TIME));
				
				rd.setPhoneId(puid);
				rd.setInputIdCardNo(puIdCardNo);
				rd.setName(name);
				rd.setAge(age);
				rd.setGender(gender);
				rd.setName(name);
//				rd.setSerialNo(serialNo);
				rd.setRegisterId(registerid);
				rd.setStr(str);
				rd.setMsg(msg);
				rd.setTime(time);
				rd.setUpdataTime(updataTime);
			}
			if (cursor != null) {
				cursor.close();
			}
		}
		return rd;
	}
	
	/**
	 * 更新Device
	 * @param id 
	 * @param values
	 */
	private void updatePatientUser(String id, ContentValues values){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			db.beginTransaction();
			if(db.isOpen()){
				int a = db.update(TABLE_NAME, values, COLUMN_NAME_ID + " = ?", new String[]{id});
				System.out.println("update : "+a);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (db!=null) {
				db.endTransaction();
			}
		}
	}
	
	public void updatePatientUser(PatientUser pu){
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_NAME, pu.getName());
		values.put(COLUMN_NAME_AGE, pu.getAge());
		values.put(COLUMN_NAME_IDCARDNO, pu.getInputIdCardNo());
		values.put(COLUMN_NAME_GENDER, pu.getGender());
		values.put(COLUMN_NAME_UPDATA_TIME, pu.getUpdataTime());
		updatePatientUser(pu.getPhoneId(), values);
	}

	public void deletePatientUserById(String id){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete(TABLE_NAME, COLUMN_NAME_ID + " = ?", new String[]{id});
		}
	}

	public void reset() {
		if (regIstance!=null) {
			regIstance = null;
		}
	}

	public Map<String, String> getUserNameByPhone() {
		Map<String, String> map = new HashMap<String, String>();
//		if (TextUtils.isEmpty(serialNo)) {
//			return map;
//		}
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if(db.isOpen()){
			Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NAME_ID, COLUMN_NAME_NAME}, null, null, null, null, null);
			while (cursor.moveToNext()) {
				String puId = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
				String puName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME));
				map.put(puId, puName);
			}
			if (cursor != null) {
				cursor.close();
			}
			
		}
		return map;
	}
}
