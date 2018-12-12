package com.wehealth.model.interfaces.inter_doctor;

import com.wehealth.model.domain.model.DoctorAssistant;
import com.wehealth.model.domain.model.ResultPassHelper;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WeHealthDoctorAssist {

	/**
	 * 医助注册接口
	 * @param authorization
	 * @param ass
	 * @param inviteCode
	 * @return {success:null}, {error_occur:error_message}
	 * 
	 * 医助注册流程：
		1. 调用通用验证码接口
		2. 调用验证码验证接口
		3. 使用手机号、短信ID获取Token
		4. 调用医助注册接口
	 */
	@POST("/doctorAssistant/register")
	ResultPassHelper register(@Header("Authorization") String authorization, @Body DoctorAssistant ass, @Query("inviteCode") String inviteCode);
	
	/**
	 * 医助更新个人资料      身份证、手机号不可更改
	 * @param authorization
	 * @param ass
	 * @return {success:null}, {error_occur:error_message}
	 */
	@PUT("/doctorAssistant")
	ResultPassHelper update(@Header("Authorization") String authorization, @Body DoctorAssistant ass);
	
	/**
	 * 获取医助信息
	 * @param idCardNo
	 * @return
	 */
	@GET("/doctorAssistant/{idCardNo}")
	DoctorAssistant getAssistant(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo);
}
