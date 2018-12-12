package com.wehealth.mesurecg.dao;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.wehealth.mesurecg.MeasurECGApplication;
import com.wehealth.model.domain.model.ECGDevice;
import com.wehealth.model.domain.model.RegisteredUser;

public class RegisterUserDao {

	public static final String TABLE_NAME = "registeruser";
	public static final String COLUMN_NAME_ID = "REGISTERUSER_ID";
	public static final String COLUMN_NAME_NAME = "REGISTERUSER_NAME";
	public static final String COLUMN_NAME_USERNAME = "REGISTERUSER_USERNAME";
	public static final String COLUMN_NAME_IDCARDNO = "REGISTERUSER_IDCARDNO";
	public static final String COLUMN_NAME_ADDRESS = "REGISTERUSER_ADDRESS";
	public static final String COLUMN_NAME_BALANCE = "REGISTERUSER_BALANCE";
	public static final String COLUMN_NAME_PHONE = "REGISTERUSER_PHONE";
	public static final String COLUMN_NAME_DATEBRITH = "REGISTERUSER_DATEBRITH";
	public static final String COLUMN_NAME_EDID = "PATIENT_ECGDEVICE_ID";
	public static final String COLUMN_NAME_EDIMEI = "PATIENT_ECGDEVICE_IMEI";
	public static final String COLUMN_NAME_EDMODEL = "PATIENT_ECGDEVICE_MODEL";
	public static final String COLUMN_NAME_EDSERIALNO = "PATIENT_ECGDEVICE_SERIALNO";
	public static final String COLUMN_NAME_EDSIMCARDNO = "PATIENT_ECGDEVICE_SIMCARDNO";
	public static final String COLUMN_NAME_EDWSP = "PATIENT_ECGDEVICE_WIRESERVICEPROVIDER";
	
	private DbOpenHelper dbHelper;
	private static RegisterUserDao appIntance;

	private RegisterUserDao(String idCardNo) {
		dbHelper = DbOpenHelper.getInstance(MeasurECGApplication.getInstance(), idCardNo);
	}

	public static RegisterUserDao getInstance(String idCardNo){
		if (appIntance==null) {
			appIntance = new RegisterUserDao(idCardNo);
		}
		return appIntance;
	}
	
	public synchronized void saveRegisterUser(RegisteredUser registerUser){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			ContentValues values = new ContentValues();
			values.put(COLUMN_NAME_ID, registerUser.getId());
			values.put(COLUMN_NAME_NAME, registerUser.getName());
			values.put(COLUMN_NAME_USERNAME, registerUser.getUsername());
			values.put(COLUMN_NAME_IDCARDNO, registerUser.getIdCardNo());
			values.put(COLUMN_NAME_ADDRESS, registerUser.getAddress());
			values.put(COLUMN_NAME_BALANCE, registerUser.getBalance());
			values.put(COLUMN_NAME_PHONE, registerUser.getCellPhone());
			if (registerUser.getDateOfBirth() != null) {
				values.put(COLUMN_NAME_DATEBRITH, registerUser.getDateOfBirth().getTime());
			}
			if (registerUser.getDevice() != null) {
				values.put(COLUMN_NAME_EDID, registerUser.getDevice().getId());
				values.put(COLUMN_NAME_EDIMEI, registerUser.getDevice().getImei());
				values.put(COLUMN_NAME_EDMODEL, registerUser.getDevice().getModel());
				values.put(COLUMN_NAME_EDSERIALNO, registerUser.getDevice().getSerialNo());
				values.put(COLUMN_NAME_EDSIMCARDNO, registerUser.getDevice().getSimCardNo());
				values.put(COLUMN_NAME_EDWSP, registerUser.getDevice().getWirelesserviceProvider());
			}
			db.replace(TABLE_NAME, null, values);
		}
		if (db != null) {
			db.close();
		}
	}
	
	/**
	 * 更新message
	 * @param idCardNo
	 * @param values
	 */
	public void updateRegisterUser(String idCardNo, ContentValues values){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.update(TABLE_NAME, values, COLUMN_NAME_IDCARDNO + " = ?", new String[]{idCardNo});
		}
	}
	
	public void updateRegisterUser(RegisteredUser user){
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_ID, user.getId());
		values.put(COLUMN_NAME_NAME, user.getName());
		values.put(COLUMN_NAME_USERNAME, user.getUsername());
		values.put(COLUMN_NAME_IDCARDNO, user.getIdCardNo());
		values.put(COLUMN_NAME_ADDRESS, user.getAddress());
		values.put(COLUMN_NAME_BALANCE, user.getBalance());
		values.put(COLUMN_NAME_PHONE, user.getCellPhone());
		if (user.getDateOfBirth()!=null) {
			values.put(COLUMN_NAME_DATEBRITH, user.getDateOfBirth().getTime());
		}
		if (user.getDevice() != null) {
			values.put(COLUMN_NAME_EDID, user.getDevice().getId());
			values.put(COLUMN_NAME_EDIMEI, user.getDevice().getImei());
			values.put(COLUMN_NAME_EDMODEL, user.getDevice().getModel());
			values.put(COLUMN_NAME_EDSERIALNO, user.getDevice().getSerialNo());
			values.put(COLUMN_NAME_EDSIMCARDNO, user.getDevice().getSimCardNo());
			values.put(COLUMN_NAME_EDWSP, user.getDevice().getWirelesserviceProvider());
		}
		updateRegisterUser(user.getIdCardNo(), values);
	}
	
	public RegisteredUser getIdCardNo(String idCardNo){
		if (TextUtils.isEmpty(idCardNo)) {
			return null;
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		RegisteredUser msg = null;
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "+COLUMN_NAME_IDCARDNO + " = ?", new String[]{idCardNo});
			if (cursor.moveToNext()) {
				msg = new RegisteredUser();
				long id = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_ID));
				String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME));
				String userName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USERNAME));
				String idcard = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_IDCARDNO));
				String address = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ADDRESS));
				double balance = cursor.getDouble(cursor.getColumnIndex(COLUMN_NAME_BALANCE));
				String phone = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PHONE));
				long dateBirth = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DATEBRITH));
				
				long deviceId = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_EDID));
				String deviceIMEI = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EDIMEI));
				String deviceModel = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EDMODEL));
				String deviceSerialNo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EDSERIALNO));
				String deviceSimCardNo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EDSIMCARDNO));
				String deviceWSP = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EDWSP));
				
				msg.setId(id);
				msg.setName(name);
				msg.setUsername(userName);
				msg.setIdCardNo(idcard);
				msg.setDateOfBirth(new Date(dateBirth));
				msg.setAddress(address);
				msg.setBalance(balance);
				msg.setCellPhone(phone);
				
				ECGDevice device = new ECGDevice();
				device.setId(deviceId);
				device.setImei(deviceIMEI);
				device.setModel(deviceModel);
				device.setSerialNo(deviceSerialNo);
				device.setSimCardNo(deviceSimCardNo);
				device.setWirelesserviceProvider(deviceWSP);
				msg.setDevice(device);
			}
			if (cursor != null) {
				cursor.close();
			}
		}
		return msg;
	}
	
	public RegisteredUser getIMEI(String imei){
		if (TextUtils.isEmpty(imei)) {
			return null;
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		RegisteredUser msg = null;
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "+COLUMN_NAME_EDIMEI + " = ?", new String[]{imei});
			if (cursor.moveToFirst()) {
				msg = new RegisteredUser();
				long id = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_ID));
				String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME));
				String userName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USERNAME));
				String idcard = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_IDCARDNO));
				String address = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ADDRESS));
				double balance = cursor.getDouble(cursor.getColumnIndex(COLUMN_NAME_BALANCE));
				String phone = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PHONE));
				long dateBirth = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DATEBRITH));
				
				long deviceId = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_EDID));
				String deviceIMEI = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EDIMEI));
				String deviceModel = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EDMODEL));
				String deviceSerialNo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EDSERIALNO));
				String deviceSimCardNo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EDSIMCARDNO));
				String deviceWSP = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EDWSP));
				
				msg.setId(id);
				msg.setName(name);
				msg.setUsername(userName);
				msg.setIdCardNo(idcard);
				msg.setDateOfBirth(new Date(dateBirth));
				msg.setAddress(address);
				msg.setBalance(balance);
				msg.setCellPhone(phone);
				
				ECGDevice device = new ECGDevice();
				device.setId(deviceId);
				device.setImei(deviceIMEI);
				device.setModel(deviceModel);
				device.setSerialNo(deviceSerialNo);
				device.setSimCardNo(deviceSimCardNo);
				device.setWirelesserviceProvider(deviceWSP);
				msg.setDevice(device);
			}
			if (cursor != null) {
				cursor.close();
			}
		}
		return msg;
	}
	
	public RegisteredUser getUserBySerialNo(String serialNo){
		if (TextUtils.isEmpty(serialNo)) {
			return null;
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		RegisteredUser msg = null;
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "+COLUMN_NAME_EDSERIALNO + " = ?", new String[]{serialNo});
			if (cursor.moveToFirst()) {
				msg = new RegisteredUser();
				long id = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_ID));
				String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME));
				String userName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USERNAME));
				String idcard = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_IDCARDNO));
				String address = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ADDRESS));
				double balance = cursor.getDouble(cursor.getColumnIndex(COLUMN_NAME_BALANCE));
				String phone = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PHONE));
				long dateBirth = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DATEBRITH));
				
				long deviceId = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_EDID));
				String deviceIMEI = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EDIMEI));
				String deviceModel = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EDMODEL));
				String deviceSerialNo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EDSERIALNO));
				String deviceSimCardNo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EDSIMCARDNO));
				String deviceWSP = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EDWSP));
				
				msg.setId(id);
				msg.setName(name);
				msg.setUsername(userName);
				msg.setIdCardNo(idcard);
				msg.setDateOfBirth(new Date(dateBirth));
				msg.setAddress(address);
				msg.setBalance(balance);
				msg.setCellPhone(phone);
				
				ECGDevice device = new ECGDevice();
				device.setId(deviceId);
				device.setImei(deviceIMEI);
				device.setModel(deviceModel);
				device.setSerialNo(deviceSerialNo);
				device.setSimCardNo(deviceSimCardNo);
				device.setWirelesserviceProvider(deviceWSP);
				msg.setDevice(device);
			}
			if (cursor != null) {
				cursor.close();
			}
		}
		return msg;
	}
	
	public void deleteRegisterUser(String idCardNo){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete(TABLE_NAME, COLUMN_NAME_IDCARDNO + " = ?", new String[]{idCardNo});
		}
	}
	
	public void deleteRegisterUser(){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete(TABLE_NAME, null, null);
		}
	}
}
