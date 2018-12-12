package com.wehealth.mesurecg.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wehealth.mesurecg.MeasurECGApplication;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.model.domain.model.AuthToken;


public class OAuthTokenDao {
	
	public static final String TABLE_NAME = "oauth_token";
	public static final String COLUMN_NAME_USERID = "user_id";
 	public static final String COLUMN_NAME_ACCESS_TOKEN = "access_token";
	public static final String COLUMN_NAME_EXPIRES_IN = "expires_in";
	public static final String COLUMN_NAME_REFRESH_TOKEN = "refresh_token";
	
	private static OAuthTokenDao intance;
	private static DbOpenHelper dbHelper;

	public OAuthTokenDao(Context context, String serialNo) {
		dbHelper = DbOpenHelper.getInstance(context, serialNo);
	}
	
	public static OAuthTokenDao getIntance(String serialNo){
		if (intance == null) {
			intance = new OAuthTokenDao(MeasurECGApplication.getInstance(), serialNo);
		}
		return intance;
	}

	/**
	 * 保存当前登录用户的Token
	 */
	public void saveAuthToken(AuthToken token) {
		if (token==null){
			return;
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put(COLUMN_NAME_USERID, PreferUtils.getIntance().getIdCardNo());
			values.put(COLUMN_NAME_ACCESS_TOKEN, token.getAccess_token());
			values.put(COLUMN_NAME_EXPIRES_IN, token.getExpires_in());
			values.put(COLUMN_NAME_REFRESH_TOKEN, token.getRefresh_token());
			long i =  db.replace(TABLE_NAME, null, values);
			System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaa"+i);
		}
	}

	/**获取登录当前用户的token**/
	public AuthToken getAuthToken(){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		AuthToken token = null;
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME+ " where "+ COLUMN_NAME_USERID+" =? ", new String[]{PreferUtils.getIntance().getIdCardNo()});
			if (cursor.moveToFirst()) {
				token = new AuthToken();
				String assessToken = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ACCESS_TOKEN));
				Long expires_in = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_EXPIRES_IN));
				String refreshToken = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_REFRESH_TOKEN));
				token.setAccess_token(assessToken);
				token.setExpires_in(expires_in);
				token.setRefresh_token(refreshToken);
			}
			if (cursor!=null) {
				cursor.close();
			}
		}
		return token;
	}
	
	/**
	 * 删除AuthToken
	 * @param userId
	 */
	public void deleteAuthToken(String userId){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete(TABLE_NAME, COLUMN_NAME_USERID + " = ?", new String[]{userId});
		}
	}
	
	/**
	 * 更新AuthToken
	 */
	public void updateAuthToken(String userId, AuthToken token){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_USERID, userId);
		values.put(COLUMN_NAME_ACCESS_TOKEN, token.getAccess_token());
		values.put(COLUMN_NAME_EXPIRES_IN, token.getExpires_in());
		values.put(COLUMN_NAME_REFRESH_TOKEN, token.getRefresh_token());
		if(db.isOpen()){
			long u = db.replace(TABLE_NAME, null, values);
			System.out.println("AuthToken 更新结果" + u);
		}
	}
	public void reset(){
		if (intance!=null) {
			intance = null;
		}
	}
}
