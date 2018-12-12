package com.wehealth.model.interfaces.inter_other;


import com.wehealth.model.domain.model.ResultPassHelper;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WeHealthSms {

	@GET("/sms/register/doctor/{phoneNumber}")
	ResultPassHelper doctorAppRegisterVerify(@Path("phoneNumber") String phoneNumber, @Query("appType") String appType);
	
	@GET("/sms/register/patient/{phoneNumber}")
	ResultPassHelper patientAppRegisterVerify(@Path("phoneNumber") String phoneNumber, @Query("appType") String appType);
	
	@GET("/sms/changepd/doctor/{phoneNumber}")
	ResultPassHelper doctorAppChangePasswordVerify(@Path("phoneNumber") String phoneNumber,
                                                   @Query("operator") String operator, @Query("appType") String appType);
	
	@GET("/sms/changepd/patient/{phoneNumber}")
	ResultPassHelper patientAppChangePasswordVerify(@Path("phoneNumber") String phoneNumber,
                                                    @Query("operator") String operator, @Query("appType") String appType);
	
	@GET("/sms/verify/{smsId}")
	ResultPassHelper verifyCode(@Path("smsId") String smsId, @Query("code") String code);
	
	@GET("/sms/commonPhoneCheck/{phoneNumber}")
	ResultPassHelper commonPhoneCheck(@Path("phoneNumber") String phoneNumber,
                                      @Query("operator") String operator, @Query("appType") String appType);
}
