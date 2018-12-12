package com.wehealth.mesurecg.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static DbOpenHelper instance;


	private static final String NOTIFICATION_MESSAGE_TABLE_CREATE = "CREATE TABLE "
			+ AppNotificationMessageDao.TABLE_NAME + " ("
//			+ AppNotificationMessageDao.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ AppNotificationMessageDao.COLUMN_NAME_MESSAGE + " TEXT, "
			+ AppNotificationMessageDao.COLUMN_NAME_MSG_ID + " TEXT PRIMARY KEY, "
			+ AppNotificationMessageDao.COLUMN_NAME_STATUS + " INTEGER, "
			+ AppNotificationMessageDao.COLUMN_NAME_ASK_STATUS + " INTEGER, "
			+ AppNotificationMessageDao.COLUMN_NAME_SUBJECT + " TEXT, "
			+ AppNotificationMessageDao.COLUMN_NAME_DOCTOR_IDCARDNO + " TEXT, "
			+ AppNotificationMessageDao.COLUMN_NAME_REGISTER_IDCARDNO + " TEXT, "
			+ AppNotificationMessageDao.COLUMN_NAME_PATIENT_IDCARDNO + " TEXT, "
			+ AppNotificationMessageDao.COLUMN_NAME_MSG_EASEMOB_STRING + " TEXT, "
			+ AppNotificationMessageDao.COLUMN_NAME_MSG_LEVEL+" TEXT, "
			+ AppNotificationMessageDao.COLUMN_NAME_MSG_OTHER+" TEXT, "
			+ AppNotificationMessageDao.COLUMN_NAME_COMMENT + " TEXT, "
			+ AppNotificationMessageDao.COLUMN_NAME_DOCTOR_NAME+" TEXT, "
			+ AppNotificationMessageDao.COLUMN_NAME_HOSPITAL+" TEXT, "
			+ AppNotificationMessageDao.COLUMN_NAME_TESTTIME +" TEXT, "
			+ AppNotificationMessageDao.COLUMN_NAME_TIME + " TEXT); ";
	
//	private static final String DGM_TABLE_CREATE = "CREATE TABLE "
//			+ DoctorGroupDao.TABLE_NAME + "("
//			+ DoctorGroupDao.COLUMN_NAME_EASEID +" TEXT PRIMARY KEY, "
//			+ DoctorGroupDao.COLUMN_NAME_GROUPID + " TEXT, "
//			+ DoctorGroupDao.COLUMN_NAME_NICK + " TEXT, "
//			+ DoctorGroupDao.COLUMN_NAME_UPDATA_TIME +" TEXT, "
//			+ DoctorGroupDao.COLUMN_NAME_TYPE + " INTEGER); "; 
	
//	private static final String HEADPHOTO_TABLE_CREATE = "CREATE TABLE "
//			+ DoctorHeadPhotoDao.TABLE_NAME + "("
//			+ DoctorHeadPhotoDao.COLUMN_NAME_EASEID +" TEXT PRIMARY KEY, "
//			+ DoctorHeadPhotoDao.COLUMN_NAME_VERSION + " INTEGER, "
//			+ DoctorHeadPhotoDao.COLUMN_NAME_CREATETIME + " INTEGER, "
//			+ DoctorHeadPhotoDao.COLUMN_NAME_PHOTO + " BLOB); ";
	
	private static final String OAUTHTOKEN_TABLE_CREATE = "CREATE TABLE "
			+ OAuthTokenDao.TABLE_NAME + "("
			+ OAuthTokenDao.COLUMN_NAME_USERID+" TEXT PRIMARY KEY, "
			+ OAuthTokenDao.COLUMN_NAME_ACCESS_TOKEN+" TEXT, "
			+ OAuthTokenDao.COLUMN_NAME_EXPIRES_IN+" TEXT, "
			+ OAuthTokenDao.COLUMN_NAME_REFRESH_TOKEN+" TEXT);";
	
	private static final String PATIENT_USER_TABLE_CREATE = "CREATE TABLE "
			+ PatientUserDao.TABLE_NAME+" ("
			+ PatientUserDao.COLUMN_NAME_ID+" TEXT, "
			+ PatientUserDao.COLUMN_NAME_IDCARDNO+" TEXT, "
			+ PatientUserDao.COLUMN_NAME_NAME+" TEXT, "
			+ PatientUserDao.COLUMN_NAME_AGE+" INTEGER, "
			+ PatientUserDao.COLUMN_NAME_GENDER+" INTEGER, "
			+ PatientUserDao.COLUMN_NAME_MSG+" TEXT, "
			+ PatientUserDao.COLUMN_NAME_REGISTERID+" TEXT, "
//			+ PatientUserDao.COLUMN_NAME_SERIALNO+" TEXT, "
			+ PatientUserDao.COLUMN_NAME_TIME+" TEXT PRIMARY KEY , "
			+ PatientUserDao.COLUMN_NAME_UPDATA_TIME+" TEXT, "
			+ PatientUserDao.COLUMN_NAME_STR+" TEXT);";
	
//	private static final String HEARTCHATGROUP_TABLE_CREATE ="CREATE TABLE "
//			+HeartChatGroupDao.TABLE_NAME+"("
//			+HeartChatGroupDao.COLUMN_NAME_ID +" INTEGER PRIMARY KEY,"
//			+HeartChatGroupDao.COLUMN_NAME_DOCTOR_EASEID +" TEXT, "
//			+HeartChatGroupDao.COLUMN_NAME_DOCTOR_MENU_ID +" TEXT, "
//			+HeartChatGroupDao.COLUMN_NAME_DOCTOR_ID +" TEXT, "
//			+HeartChatGroupDao.COLUMN_NAME_END_TIME +" TEXT, "
//			+HeartChatGroupDao.COLUMN_NAME_GROUP_DESC +" TEXT, "
//			+HeartChatGroupDao.COLUMN_NAME_GROUPID +" TEXT, "
//			+HeartChatGroupDao.COLUMN_NAME_GROUPNAME +" TEXT, "
//			+HeartChatGroupDao.COLUMN_NAME_MENU_ITEMS +" TEXT, "
//			+HeartChatGroupDao.COLUMN_NAME_NOTE +" TEXT, "
//			+HeartChatGroupDao.COLUMN_NAME_SEATING_EASEID +" TEXT, "
//			+HeartChatGroupDao.COLUMN_NAME_SEATING_ID +" TEXT, "
//			+HeartChatGroupDao.COLUMN_NAME_START_TIME +" TEXT, "
//			+HeartChatGroupDao.COLUMN_NAME_USER_EASEID +" TEXT, "
//			+HeartChatGroupDao.COLUMN_NAME_USER_PHONE+" TEXT, "
//			+HeartChatGroupDao.COLUMN_NAME_DOCTOR_NOTE +" TEXT, "
//			+HeartChatGroupDao.COLUMN_NAME_USERID +" TEXT);";
//	
//	private static final String DOCTOR_TABLE_CREATE ="CREATE TABLE "
//			+DoctorDao.TABLE_NAME+"("
//			+DoctorDao.COLUMN_NAME_ID +" INTEGER, "
//			+DoctorDao.COLUMN_NAME_ADDRESS +" TEXT, "
//			+DoctorDao.COLUMN_NAME_BALANCE +" TEXT, "
//			+DoctorDao.COLUMN_NAME_BANKACCOUNTID +" TEXT, "
//			+DoctorDao.COLUMN_NAME_BANKACCOUNTNAME +" TEXT, "
//			+DoctorDao.COLUMN_NAME_BANKNAME +" TEXT, "
//			+DoctorDao.COLUMN_NAME_BIOGRAPHY +" TEXT, "
//			+DoctorDao.COLUMN_NAME_DEGREE +" TEXT, "
//			+DoctorDao.COLUMN_NAME_DEPARTMENT +" TEXT, "
//			+DoctorDao.COLUMN_NAME_EASEID +" TEXT, "
//			+DoctorDao.COLUMN_NAME_EASEPSD +" TEXT, "
//			+DoctorDao.COLUMN_NAME_FIELDS +" TEXT, "
//			+DoctorDao.COLUMN_NAME_HOSPITAL +" TEXT, "
//			+DoctorDao.COLUMN_NAME_HOSPITALID +" TEXT, "
//			+DoctorDao.COLUMN_NAME_IDCARDNO +" TEXT PRIMARY KEY, "
//			+DoctorDao.COLUMN_NAME_NAME +" TEXT, "
//			+DoctorDao.COLUMN_NAME_PHONE +" TEXT, "
//			+DoctorDao.COLUMN_NAME_POSITION +" TEXT, "
//			+DoctorDao.COLUMN_NAME_TITLE +" TEXT, "
//			+DoctorDao.COLUMN_NAME_USERNAME +" TEXT, "
//			+DoctorDao.COLUMN_NAME_SERIALNO +" TEXT, "
//			+DoctorDao.COLUMN_NAME_BONUS_POINTS +" TEXT, "
//			+DoctorDao.COLUMN_NAME_LICENSENO +" TEXT);";
//	
//	private static final String USERNAME_TABLE_CREATE = "CREATE TABLE "
//			+ UserDao.TABLE_NAME + " ("
//			+ UserDao.COLUMN_NAME_NICK +" TEXT, "
//			+ UserDao.COLUMN_NAME_TYPE +" INTEGER, "
//			+ UserDao.COLUMN_NAME_GENDER +" INTEGER, "
//			+ UserDao.COLUMN_NAME_DOC_PATIENTNOTE + " TEXT, "
//			+ UserDao.COLUMN_NAME_ID + " TEXT PRIMARY KEY);";
	
	private static final String REGISTERUSER_TABLE_CREATE="CREATE TABLE "
			+ RegisterUserDao.TABLE_NAME+" ("
			+ RegisterUserDao.COLUMN_NAME_ID+" INTEGER PRIMARY KEY, "
			+ RegisterUserDao.COLUMN_NAME_NAME+" TEXT, "
			+ RegisterUserDao.COLUMN_NAME_USERNAME+" TEXT, "
//			+ RegisterUserDao.COLUMN_NAME_PASSWORD+" TEXT, "
			+ RegisterUserDao.COLUMN_NAME_IDCARDNO+" TEXT, "
//			+ RegisterUserDao.COLUMN_NAME_EASE_ID+" TEXT, "
//			+ RegisterUserDao.COLUMN_NAME_EASE_PSD+" TEXT, "
			+ RegisterUserDao.COLUMN_NAME_ADDRESS+" TEXT, "
			+ RegisterUserDao.COLUMN_NAME_BALANCE+" TEXT, "
			+ RegisterUserDao.COLUMN_NAME_PHONE+" TEXT, "
			+ RegisterUserDao.COLUMN_NAME_DATEBRITH+" TEXT, "
			+ RegisterUserDao.COLUMN_NAME_EDID+" INTEGER, "
			+ RegisterUserDao.COLUMN_NAME_EDIMEI+" TEXT, "
			+ RegisterUserDao.COLUMN_NAME_EDMODEL+" TEXT, "
			+ RegisterUserDao.COLUMN_NAME_EDSERIALNO+" TEXT, "
			+ RegisterUserDao.COLUMN_NAME_EDSIMCARDNO+" TEXT, "
//			+ RegisterUserDao.COLUMN_NAME_EDSTATUS+" INTEGER, "
			+ RegisterUserDao.COLUMN_NAME_EDWSP+" TEXT);";
	private static final String ECGDATALONG_2_DEVICE_TABLE_CREATE = "CREATE TABLE "
			+ ECGDataLong2DeviceDao.TABLE_NAME+" ("
			+ ECGDataLong2DeviceDao.COLUMN_NAME_HEART_RATE + " INTEGER , "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_PATIENT_ID_CARD+" TEXT, "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_REG_USR_ID+" TEXT, "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_ANALYSE_RESULT+" TEXT, "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_AVF_PATH+" TEXT, "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_AVL_PATH+" TEXT, "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_AVR_PATH+" TEXT, "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_I_PATH+" TEXT, "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_II_PATH+" TEXT, "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_III_PATH+" TEXT, "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_V1_PATH+" TEXT, "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_V2_PATH+" TEXT, "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_V3_PATH+" TEXT, "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_V4_PATH+" TEXT, "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_V5_PATH+" TEXT, "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_V6_PATH+" TEXT, "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_ECGDATA_ID+" INTEGER, "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_24H_PATH+" TEXT, "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_TOTAL_TIME+" INTEGER, "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_24H_LEADNAME+" INTEGER, "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_SAVEFILETYPE+" INTEGER, "
			+ ECGDataLong2DeviceDao.COLUMN_NAME_TIME+" INTEGER);";

	private static final String ECG_TABLE_CREATE = "CREATE TABLE "
			+ ECGDao.TABLE_NAME + " ("
			+ ECGDao.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ ECGDao.COLUMN_NAME_AUTO_DIAGNOSIS_RESULT +" TEXT, "
			+ ECGDao.COLUMN_NAME_DIAGNOSIS_TYPE +" INTEGER, "
			+ ECGDao.COLUMN_NAME_DOCTOR_ID +" TEXT, "
			+ ECGDao.COLUMN_NAME_EQUIPMENT_SERIAL_NO +" TEXT, "
			+ ECGDao.COLUMN_NAME_MANUL_DIAGNOSIS_RESULT +" TEXT, "
			+ ECGDao.COLUMN_NAME_PATIENT_ID_CARD +" TEXT, "
			+ ECGDao.COLUMN_NAME_REG_USR_ID +" TEXT, "
			+ ECGDao.COLUMN_NAME_SYMPTOMS +" TEXT, "
			+ ECGDao.COLUMN_NAME_USER_PHONE +" TEXT, "
			+ ECGDao.COLUMN_NAME_USER_NAME +" TEXT, "
			+ ECGDao.COLUMN_NAME_TIME +" INTEGER, "
			+ ECGDao.COLUMN_NAME_HEART_RATE + " INTEGER, "
			+ ECGDao.COLUMN_NAME_VERSION + " INTEGER, "
			+ ECGDao.COLUMN_NAME_LEVEL + " INTEGER);";
	
	
	private DbOpenHelper(Context context, String serialNo) {//, String dbfile
		super(context, getUserDatabaseName(serialNo), null, DATABASE_VERSION);
	}
	
	public static DbOpenHelper getInstance(Context context, String serialNo) {//, String userName
		if (instance == null) {
			instance = new DbOpenHelper(context.getApplicationContext(), serialNo);//, userName
		}
		return instance;
	}
	
	private static String getUserDatabaseName(String serialNo) {//String userName
        return  serialNo+"auto_family.db";
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(NOTIFICATION_MESSAGE_TABLE_CREATE);
		db.execSQL(OAUTHTOKEN_TABLE_CREATE);
//		db.execSQL(HEARTCHATGROUP_TABLE_CREATE);
//		db.execSQL(DOCTOR_TABLE_CREATE);
//		db.execSQL(USERNAME_TABLE_CREATE);
		db.execSQL(ECG_TABLE_CREATE);
		db.execSQL(REGISTERUSER_TABLE_CREATE);
		db.execSQL(PATIENT_USER_TABLE_CREATE);
		db.execSQL(ECGDATALONG_2_DEVICE_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	public void closeDB() {
	    if (instance != null) {
	        try {
	            SQLiteDatabase db = instance.getWritableDatabase();
	            db.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        instance = null;
	    }
	}
	
}
