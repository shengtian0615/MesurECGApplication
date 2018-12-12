/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *
 */
package com.wehealth.mesurecg.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wehealth.mesurecg.MeasurECGApplication;
import com.wehealth.model.domain.enumutil.NotificationMesageStatus;
import com.wehealth.model.domain.enumutil.NotifyDoctorAskStatus;
import com.wehealth.model.domain.model.AppNotificationMessage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class AppNotificationMessageDao {
	static final String TABLE_NAME = "app_notification_msgs";
//	public static final String COLUMN_NAME_ID = "id";
	static final String COLUMN_NAME_TIME = "time";
	static final String COLUMN_NAME_TESTTIME = "test_time";
	static final String COLUMN_NAME_MESSAGE = "message";
	static final String COLUMN_NAME_SUBJECT = "subject";
	static final String COLUMN_NAME_STATUS = "status";
	static final String COLUMN_NAME_MSG_ID = "messageID";
	static final String COLUMN_NAME_ASK_STATUS = "ask_status";
	static final String COLUMN_NAME_MSG_LEVEL = "msg_level";
	static final String COLUMN_NAME_DOCTOR_IDCARDNO = "doctor_idcardno";
	static final String COLUMN_NAME_REGISTER_IDCARDNO = "register_idcardno";
	static final String COLUMN_NAME_PATIENT_IDCARDNO = "patient_idcardno";
	static final String COLUMN_NAME_HOSPITAL= "msg_hospital";
	static final String COLUMN_NAME_COMMENT = "comment";
	static final String COLUMN_NAME_DOCTOR_NAME = "doctor_name";
	static final String COLUMN_NAME_MSG_OTHER= "msg_other";
	static final String COLUMN_NAME_MSG_EASEMOB_STRING = "easemob_string";
	
	private DbOpenHelper dbHelper;
	private static AppNotificationMessageDao appInstance;
	
	public static AppNotificationMessageDao getAppInstance(String idCardNo){
		if (appInstance==null) {
			appInstance = new AppNotificationMessageDao(idCardNo);
		}
		return appInstance;
	}
	
	private AppNotificationMessageDao(String idCardNo){
		dbHelper = DbOpenHelper.getInstance(MeasurECGApplication.getInstance(), idCardNo);
	}
	
	/**
	 * 保存message
	 * @param message
	 * @return  返回这条messaged在db中的id
	 */
	public synchronized Integer saveMessage(AppNotificationMessage message){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int id = -1;
		if(db.isOpen()){
			ContentValues values = new ContentValues();
			values.put(COLUMN_NAME_MESSAGE, message.getMessage());
			values.put(COLUMN_NAME_SUBJECT, message.getSubject());
			values.put(COLUMN_NAME_MSG_ID, message.getMsgId());
			values.put(COLUMN_NAME_TIME, message.getTime());
			values.put(COLUMN_NAME_TESTTIME, message.getTestTime());
			values.put(COLUMN_NAME_ASK_STATUS, message.getAskStatus().ordinal());
			values.put(COLUMN_NAME_STATUS, message.getStatus().ordinal());
			values.put(COLUMN_NAME_DOCTOR_IDCARDNO, message.getMsgDoctorIdCardNo());
			values.put(COLUMN_NAME_REGISTER_IDCARDNO, message.getMsgRegisterIdCardNo());
			values.put(COLUMN_NAME_PATIENT_IDCARDNO, message.getMsgPatientIdCardNo());
			values.put(COLUMN_NAME_MSG_LEVEL, message.getMsgLevel());
			values.put(COLUMN_NAME_COMMENT, message.getMsgComment());
			values.put(COLUMN_NAME_DOCTOR_NAME, message.getDoctorName());
			values.put(COLUMN_NAME_HOSPITAL, message.getMsgHospital());
			values.put(COLUMN_NAME_MSG_OTHER, message.getMsgOther());
			values.put(COLUMN_NAME_MSG_EASEMOB_STRING, message.getMsgEaseMobString());
			db.insert(TABLE_NAME, null, values);
			
			Cursor cursor = db.rawQuery("select last_insert_rowid() from " + TABLE_NAME,null); 
            if(cursor.moveToFirst()){
                id = cursor.getInt(0);
            }

			if (cursor!=null) {
				cursor.close();
			}
		}
		return id;
	}
	
	/**
	 * 更新message
	 * @param msgId
	 * @param values
	 */
	public void updateMessage(String msgId, ContentValues values){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.update(TABLE_NAME, values, COLUMN_NAME_MSG_ID + " = ?", new String[]{msgId});
		}
	}

	public void updateMessage(AppNotificationMessage message){
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_MESSAGE, message.getMessage());
		values.put(COLUMN_NAME_SUBJECT, message.getSubject());
		values.put(COLUMN_NAME_MSG_ID, message.getMsgId());
		values.put(COLUMN_NAME_TIME, message.getTime());
		values.put(COLUMN_NAME_TESTTIME, message.getTestTime());
		values.put(COLUMN_NAME_STATUS, message.getStatus().ordinal());
		values.put(COLUMN_NAME_ASK_STATUS, message.getAskStatus().ordinal());
		values.put(COLUMN_NAME_DOCTOR_IDCARDNO, message.getMsgDoctorIdCardNo());
		values.put(COLUMN_NAME_REGISTER_IDCARDNO, message.getMsgRegisterIdCardNo());
		values.put(COLUMN_NAME_PATIENT_IDCARDNO, message.getMsgPatientIdCardNo());
		values.put(COLUMN_NAME_MSG_LEVEL, message.getMsgLevel());
		values.put(COLUMN_NAME_COMMENT, message.getMsgComment());
		values.put(COLUMN_NAME_DOCTOR_NAME, message.getDoctorName());
		values.put(COLUMN_NAME_HOSPITAL, message.getMsgHospital());
		values.put(COLUMN_NAME_MSG_OTHER, message.getMsgOther());
		values.put(COLUMN_NAME_MSG_EASEMOB_STRING, message.getMsgEaseMobString());
		updateMessage(message.getMsgId(), values);
	}
	
	/**
	 * 获取messges
	 * @return
	 */
	public List<AppNotificationMessage> getAllMessageList(){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<AppNotificationMessage> msgs = new ArrayList<AppNotificationMessage>();
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME  ,null);//+ " where "+COLUMN_NAME_ASK_STATUS+" = "+ NotifyDoctorAskStatus.ASK.ordinal()
			while(cursor.moveToNext()){
				AppNotificationMessage msg = new AppNotificationMessage();
//				int id = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID));
				String subject = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SUBJECT));
				String message = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MESSAGE));
				long time = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_TIME));
				long testTime = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_TESTTIME));
				int status = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_STATUS));
				String msgId = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MSG_ID));
				int askStatus = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ASK_STATUS));
				String doctorIdCard = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DOCTOR_IDCARDNO));
				String registerIdCard = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_REGISTER_IDCARDNO));
				String patientIdCard = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PATIENT_IDCARDNO));
				String level = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MSG_LEVEL));
				String comment = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_COMMENT));
				String doctorName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DOCTOR_NAME));
				String hospital = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_HOSPITAL));
				String msgOther = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MSG_OTHER));
				
//				msg.setId(id);
				msg.setMessage(message);
				msg.setMsgId(msgId);
				msg.setSubject(subject);
				msg.setTime(time);
				msg.setTestTime(testTime);
				msg.setMsgDoctorIdCardNo(doctorIdCard);
				msg.setMsgRegisterIdCardNo(registerIdCard);
				msg.setMsgPatientIdCardNo(patientIdCard);
				msg.setMsgLevel(level);
				msg.setDoctorName(doctorName);
				msg.setMsgComment(comment);
				msg.setMsgHospital(hospital);
				msg.setMsgOther(msgOther);
				if (askStatus == NotifyDoctorAskStatus.ASK.ordinal()) {
					msg.setAskStatus(NotifyDoctorAskStatus.ASK);
				}else if (askStatus == NotifyDoctorAskStatus.UNASK.ordinal()) {
					msg.setAskStatus(NotifyDoctorAskStatus.UNASK);
				}
				if(status == NotificationMesageStatus.READ.ordinal()) {
					msg.setStatus(NotificationMesageStatus.READ);
				}
				else if(status == NotificationMesageStatus.UNREAD.ordinal()) {
					msg.setStatus(NotificationMesageStatus.UNREAD);
				}
				msgs.add(msg);
			}
			if (cursor!=null) {
				cursor.close();
			}
		}
		Collections.sort(msgs);
		return msgs;
	}
	
	/**查询是否有未读消息
	 * 有返回true  否则为false
	 * **/
	public boolean getUnreadMsg(){
		boolean unread = false;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "+ COLUMN_NAME_STATUS +" = 1", null);
			if (cursor.getCount()!=0) {
				unread = true;
			}
			if (cursor!=null) {
				cursor.close();
			}
		}
		return unread;
	}
	
	/**获取未读消息**/
	public int getUnreadMsgCount(){
		int count = 0;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "+ COLUMN_NAME_STATUS +" = 1", null);
			if (cursor!=null) {
				count = cursor.getCount();
			}
			if (cursor!=null) {
				cursor.close();
			}
		}
		return count;
	}
	
	/**获取系统消息 by time**/
	public AppNotificationMessage getMessageByTime(long tt){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		AppNotificationMessage msg = null;
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select * from "+TABLE_NAME+" where "+COLUMN_NAME_TESTTIME+" = "+tt, null);
			if(cursor.moveToFirst()){
				msg = new AppNotificationMessage();
//				int id = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID));
				String subject = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SUBJECT));
				String message = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MESSAGE));
				long time = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_TIME));
				long testTime = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_TESTTIME));
				int status = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_STATUS));
				String msgId = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MSG_ID));
				int askStatus = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ASK_STATUS));
				String doctorIdCard = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DOCTOR_IDCARDNO));
				String registerIdCard = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_REGISTER_IDCARDNO));
				String patientIdCard = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PATIENT_IDCARDNO));
				String level = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MSG_LEVEL));
				String comment = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_COMMENT));
				String doctorName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DOCTOR_NAME));
				String hospital = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_HOSPITAL));
				String msgOther = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MSG_OTHER));
				
//				msg.setId(id);
				msg.setMessage(message);
				msg.setMsgId(msgId);
				msg.setSubject(subject);
				msg.setTime(time);
				msg.setTestTime(testTime);
				msg.setMsgDoctorIdCardNo(doctorIdCard);
				msg.setMsgRegisterIdCardNo(registerIdCard);
				msg.setMsgPatientIdCardNo(patientIdCard);
				msg.setMsgLevel(level);
				msg.setDoctorName(doctorName);
				msg.setMsgComment(comment);
				msg.setMsgHospital(hospital);
				msg.setMsgOther(msgOther);
				
				if (askStatus == NotifyDoctorAskStatus.ASK.ordinal()) {
					msg.setAskStatus(NotifyDoctorAskStatus.ASK);
				}else if (askStatus == NotifyDoctorAskStatus.UNASK.ordinal()) {
					msg.setAskStatus(NotifyDoctorAskStatus.UNASK);
				}
				if(status == NotificationMesageStatus.READ.ordinal()) {
					msg.setStatus(NotificationMesageStatus.READ);
				}
				else if(status == NotificationMesageStatus.UNREAD.ordinal()) {
					msg.setStatus(NotificationMesageStatus.UNREAD);
				}
			}
			if (cursor!=null) {
				cursor.close();
			}
		}
		return msg;
	}

	public void deleteMessage(String msgId){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete(TABLE_NAME, COLUMN_NAME_MSG_ID + " = " + msgId, null);
		}
	}
	
	public AppNotificationMessage getMsgById(String messageId){
		if (messageId == null) {
			return null;
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		AppNotificationMessage msg = null;
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "+COLUMN_NAME_MSG_ID + " = ?", new String[]{messageId});
			if (cursor.moveToFirst()) {
				msg = new AppNotificationMessage();
//				int id = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID));
				String subject = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SUBJECT));
				String message = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MESSAGE));
				long time = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_TIME));
				long testTime = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_TESTTIME));
				int status = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_STATUS));
				String msgId = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MSG_ID));
				int askStatus = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ASK_STATUS));
				String doctorIdCard = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DOCTOR_IDCARDNO));
				String registerIdCard = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_REGISTER_IDCARDNO));
				String patientIdCard = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PATIENT_IDCARDNO));
				String level = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MSG_LEVEL));
				String comment = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_COMMENT));
				String doctorName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DOCTOR_NAME));
				String hospital = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_HOSPITAL));
				String msgOther = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MSG_OTHER));
				
//				msg.setId(id);
				msg.setMessage(message);
				msg.setMsgId(msgId);
				msg.setSubject(subject);
				msg.setTime(time);
				msg.setTestTime(testTime);
				msg.setMsgDoctorIdCardNo(doctorIdCard);
				msg.setMsgRegisterIdCardNo(registerIdCard);
				msg.setMsgPatientIdCardNo(patientIdCard);
				msg.setMsgLevel(level);
				msg.setDoctorName(doctorName);
				msg.setMsgComment(comment);
				msg.setMsgHospital(hospital);
				msg.setMsgOther(msgOther);
				
				if (askStatus == NotifyDoctorAskStatus.ASK.ordinal()) {
					msg.setAskStatus(NotifyDoctorAskStatus.ASK);
				}else if (askStatus == NotifyDoctorAskStatus.UNASK.ordinal()) {
					msg.setAskStatus(NotifyDoctorAskStatus.UNASK);
				}
				if(status == NotificationMesageStatus.READ.ordinal()) {
					msg.setStatus(NotificationMesageStatus.READ);
				}
				else if(status == NotificationMesageStatus.UNREAD.ordinal()) {
					msg.setStatus(NotificationMesageStatus.UNREAD);
				}
			}
			if (cursor!=null) {
				cursor.close();
			}
		}
		return msg;
	}
}
