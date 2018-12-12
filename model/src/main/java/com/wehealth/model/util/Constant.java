/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Dec 19, 2014
 */

package com.wehealth.model.util;

public class Constant {



	public final static int LEADNUM = 12;

	public static final String LOCAL_MESSAGE = "local_message";
	public static final String C21 = "C21";
	
	public static final String DoctorAssist = "DoctorAssistant";
	public static final String AppUserRegister = "AppUserRegister";
	public static final String RegisteredUser = "RegisteredUser";
	public static final String Doctor = "Doctor";
	
	public static final String MESSAGE_ATTR_ROBOT_MSGTYPE = "msgtype";
	public static final String APP_ADMIN = "AppAdmin";
	public static final String APPADMIN = "appadmin";
	public static final String MSG_PLACE_ORDER = "PLACE_ORDER";
	public static final String MSG_ORDER_ACCEPTED = "ORDER_ACCEPTED";
	public static final String MSG_ORDER_REJECTED = "ORDER_REJECTED";
	public static final String MSG_ORDER_COMPLETED = "ORDER_COMPLETED";
	public static final String MSG_ORDER_CANCEL = "ORDER_CANCELED";
	
	public static final String MSG_CHAT_GROUP = "chat_group";

	public static final String ACCOUNT_REMOVED = "account_removed";
	
	public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
	public static final String GROUP_USERNAME = "item_groups";
	public static final String CHAT_ROOM = "item_chatroom";
	public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
	public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";
	public static final String MESSAGE_ATTL_IS_CMD = "is_cmd";
	public static final String CHAT_ROBOT = "item_robots";
	public static final String CONNECT_OAUTH_TOKEN_ERROE = "oauth_token_error";
	public static final String INTERNAL_SERVER_ERROR= "Internal_Server_Error";
	public static final String ERROR = "error";
	
	public static final String SUCCESS = "success";
	public static final String FAILED = "failed";
	public static final String NO_TOKEN = "no_auth";
	public static final String NOT_AVAILABLE = "not_available";
	public static final String ERROR_OCCUR = "error_occur";
	
	public static final String MSG_ECG_NEW_TESTED_DATA = "NEW_TESTED_DATA";
	public static final String MSG_ECG_Manual_Diagnosis_Result = "Ecg__Manual_Diagnosis_Result";
	public static final String MSG_ECG_Manual_Diagnosis_Request = "Ecg__Manual_Diagnosis_Request";
	public static final String MSG_ECG_Regular_Check_Request = "Ecg__Regular_Check_Request";
	public static final String MSG_ECG_Free_Check_Request = "Ecg__Free_Check_Request";
	
	public static final String MSG_CHARGE_FEE = "Charge_Fee_Msg";
	public static final String MSG_DOCTOR_INCOME = "Doctor_Income_Msg";
	public static final String MSG_STATUS_CHANGE = "Status_Change";
	
	public static final String MSG_STATUS_CHANGE_ENABLE= "enabled";
	public static final String MSG_STATUS_CHANGE_PROVE= "proved";
	public static final String MSG_STATUS_CHANGE_SEARCHABLE= "searchAble";
	
	public static final String Manual_Diagnosis = "Manual_Diagnosis";
	public static final String Order_Diagnosis = "Order_Diagnosis";
	
	public static final String PREFER_DOCTOR_REQUEST = "Prefer_Doctor_Request";
	public static final String DOCTOR_ACCEPT_PREFER_REQUEST = "accept";
	public static final String DOCTOR_DENY_PREFER_REQUEST = "deny";
	
	public static final String VERIFY_PASSED = "verify_passed";
	public static final String VERIFY_FAILED = "verify_failed";
	public static final String VERIFY_TIME_OUT = "verify_time_out";
	
	public static final String ID_EXIST = "exist";
	public static final String ID_USEABLE = "useable";
	public static final String ID_FORMAT_ERROR = "format_error";
	
	public static final String CONNECT_WORK_TIMEOUT="timeout";
	public static final String CONNECT_WORK_400BAD_REQUEST="400bad_request";
	public static final String CONNECT_WORK_404NOT_FOUND = "404not_found";
	
	public static final String CP_REG_REGI = "reg_reg";//用户头像缓存
	public static final String CP_REG_DOCT = "reg_doc";//用户端医生头像的缓存
	public static final String CP_DOC_DOCT = "doc_doc";//医生端头像缓存
	public static final String CP_DOC_REGI = "doc_reg";//医生端患者的缓存
	public static final String CP_DOC_LENCE = "doc_lence";//医生端执照的缓存
	
	public static final String CP_REG_PATI_NOMAL = "reg_pati_nomal";//用户端缓存普通图片
	public static final String CP_REG_PATI_ECG = "reg_pati_ecg";//用户端缓存心电图片
	public static final String CP_DOC_PATI_NOMAL = "doc_pati_nomal";//用户端缓存普通图片
	public static final String CP_DOC_PATI_ECG = "doc_pati_ecg";//用户端缓存心电图片
	
	public static final String OBSERVER_STRING = "observer_string";//观察者的String

	public static final String ECG_PATIENT_INFO = "ECG_PATIENT_INFO";
	public static final String ECG_ANALYSE_PARAM = "ECG_ANALYSE_PARAM";
	
//	public static final String PATIENT_PORTRAIT_P = "/wehealth/patient/p_portrait/";
//	public static final String PATIENT_PORTRAIT_D = "/wehealth/patient/d_portrait/";
//	public static final String PATIENT_UPLOADIMG = "/wehealth/patient/uploadimg";
//	
//	public static final String DOCTOR_PORTRAIT_D = "/wehealth/doctor/d_portrait/";
//	public static final String DOCTOR_PORTRAIT_P = "/wehealth/doctor/p_portrait/";
//	public static final String DOCTOR_LICENSE_PHOTO = "/wehealth/doctor/licenseimg/";
	
}
