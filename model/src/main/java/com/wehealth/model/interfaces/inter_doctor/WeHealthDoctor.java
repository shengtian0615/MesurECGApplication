package com.wehealth.model.interfaces.inter_doctor;

import com.wehealth.model.domain.enumutil.DoctorTitle;
import com.wehealth.model.domain.model.Doctor;
import com.wehealth.model.domain.model.ResultPassHelper;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WeHealthDoctor {
	
	/**在App端注册**/
	@POST("/doctor")
    Doctor registerDoctor(@Header("Authorization") String authorization, @Body Doctor doctor);
	/**
	 * 根据身份证号找医生
	 * @param idCardNo
	 * @return
	 */
    @GET("/doctor/idCard/{idCardNo}")
    Doctor getDoctor(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo);
    
    /**
     * 根据用户名找医生
     * @param authorization
     * @param username
     * @return
     */
    @GET("/doctor/username/{username}")
    Doctor getByUsername(@Header("Authorization") String authorization, @Path("username") String username);
    /**
     * 获取所有的医生
     * @return
     */
    @GET("/doctor")
    DoctorList getAllDoctor(@Header("Authorization") String authorization);
    
    /**
     * 根据身份证号集合找医生
     * @param ids
     * @return
     */
    @GET("/doctor/list")
    DoctorList findDoctorsById(@Header("Authorization") String authorization, @Query("ids") List<String> ids);
    
    /**
     * 更新医生
     * @param doctor
     * @return
     */
    @PUT("/doctor")
    Doctor updateDoctor(@Header("Authorization") String authorization, @Body Doctor doctor);
    
    /**
     * 根据地点查询医生
     * @param address
     * @param page
     * @param pageCount
     * @return
     */
    @GET("/doctor/query/address")
    DoctorList findDoctorByAddress(@Header("Authorization") String authorization, @Query("address") String address, @Query("page") int page, @Query("pageCount") int pageCount);
    
    /**
     * 根据职称获取医生
     * @param page
     * @param pageCount
     * @return
     */
    @GET("/doctor/query/title")
    DoctorList findDoctorbyTitle(@Header("Authorization") String authorization, @Query("page") int page, @Query("pageCount") int pageCount, @Query("title") DoctorTitle title);
    
    /**
     * 根据锦旗数获取医生
     * @param page
     * @param pageCount
     * @return
     */
    @GET("/doctor/query/satisfiedCount")
    DoctorList findDoctorOrderbySatisfiedCount(@Header("Authorization") String authorization, @Query("page") int page, @Query("pageCount") int pageCount);
    
    /**忘记密码**/
    @GET("/doctor/changepw/{phoneNumber}")
    ResultPassHelper changePassword(@Path("phoneNumber") String phoneNumber, @Query("smsId") String smsId, @Query("password") String password, @Query("operator") String operator);

    /**修改密码**/
    @GET("/doctor/changeOldPw/{idCardNo}")
    ResultPassHelper changeOldPassword(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo, @Query("oldPassword") String oldPassword, @Query("newPassword") String newPassword);
    
    @GET("/doctor/check/phone/{phone}")
    ResultPassHelper checkPhoneExists(@Header("Authorization") String authorization, @Path("phone") String phone);
    
    @GET("/doctor/checkUsername/{username}")
    ResultPassHelper checkUserExist(@Path("username") String username);
    
    @GET("/doctor/checkIdCardNo/{idCardNo}")
    ResultPassHelper checkIdCardExists(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo);
    
    @POST("/doctor/recommand/online")
    ResultPassHelper addToCache(@Header("Authorization") String authorization, @Body Doctor doctor);
    
    @POST("/doctor/recommand/offline")
    ResultPassHelper removeFromCache(@Header("Authorization") String authorization, @Body Doctor doctor);
    
    @GET("/doctor/recommand/list")
    DoctorList getRecommandDoctors(@Header("Authorization") String authorization, @Query("page") int page, @Query("pageCount") int pageCount);
    
    @GET("/doctor/onduty/{idCardNo}")
    ResultPassHelper becomeOnDutyDoctor(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo, @Query("onDutyStartTime") Long startTime, @Query("onDutyEndTime") Long endTime);
    
    @GET("/doctor/onduty/cancel/{idCardNo}")
    ResultPassHelper cancelOnDuty(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo);
    
    @GET("/doctor/deal/prefer/{idCardNo}")
    ResultPassHelper dealPerferRequest(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo, @Query("regUserIdCardNo") String regIdCardNo, @Query("agree") boolean agree);
    
    @GET("/doctor/easemob/{username}")
    Doctor getByEasemobUsername(@Header("Authorization") String authorization, @Path("username") String username);
    
    @GET("/doctor/refresh/balance/{idCardNo}")
    ResultPassHelper getDoctorInCome(@Header("Authorization") String authorization, @Path("idCardNo") String doctorId);
    
    @GET("/doctor/checkOnline/{idCardNo}")
	public ResultPassHelper isDoctorOnline(@Header("Authorization") String authorization, @Path("idCardNo") String idCard);
}

