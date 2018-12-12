package com.wehealth.model.interfaces.inter_other;

import com.wehealth.model.domain.model.ECGData;
import com.wehealth.model.domain.model.RegisteredUser;
import com.wehealth.model.domain.model.ResultPassHelper;
import com.wehealth.model.domain.model.ThirdAgency;
import com.wehealth.model.domain.model.ThirdAgencyPatient;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WeHealthThirdAgency {

	/**
	 * 输入项： 名称、用户名、密码、地址、描述
		提供一个负责人的联系方式，放在紧急联系人里：姓名，手机号，地址
	 * 
	 * keep tuid in idCardNo.
	 * @param cid, agency id.
	 * @param user
	 * @return "error_occur":"error msg", "failed":"fail error", "success":null
	 */
	@POST("/agencyApi/user")
	ResultPassHelper createAgencyUser(
            @Header("Authorization") String authorization,
            @Body RegisteredUser user,
            @Query("cid") String cid,
            @Query("equipSerialNo") String equipSerialNo);
	
	/**
	 * 商业版If pass pid, check whether create new AgencyPatient.
	 * @param ecgData
	 * @param pid
	 * @return "error_occur":"errorMsg", "failed":"reason","success":"data id"
	 */
	@POST("/agencyApi/ecg")
	ResultPassHelper agencyCreateECGData(
            @Header("Authorization") String authorization,
            @Body ECGData ecgData,
            @Query("pid") String pid,
            @Query("pName") String pName,
            @Query("pAge") Integer pAge,
            @Query("isMale") Boolean isMal);
	
	/**
	 * 诊所版 创建ECG数据
	 * @param ecgData --- ECG数据
	 * @param pid--- 病人身份证
	 * @param cellPhone --- 病人电话
	 * @param pName--- 病人姓名
	 * @param pAge---- 病人年龄
	 * @param isMale---病人性别
	 * @return  返回值:ResultPassHelper {name:…,value:…}
		成功, 返回name= “success” 
		失败，返回 name= “failed” value = “ 出错原因”.
	 */
	@POST("/agencyApi/clinic/ecg")
	ResultPassHelper clinicCreateECGData(
            @Header("Authorization") String authorization,
            @Body ECGData ecgData,
            @Query("pid") String pid,
            @Query("cellPhone") String cellPhone,
            @Query("pName") String pName,
            @Query("pAge") Integer pAge,
            @Query("isMale") Boolean isMale);
	
	@POST("/agencyApi/patient")
	ResultPassHelper createPatient(
            @Header("Authorization") String authorization,
            @Body ThirdAgencyPatient p);
	
	@GET("/thirdAgency/find/{clientId}")
	ThirdAgency find(
            @Header("Authorization") String authorization,
            @Path("clientId") String clientId);
	
	/**
	 * @param user
	 * @param realIdCardNo, nullable, if have means add realIdCardNo
	 * @return
	 */
	@PUT("/agencyApi/user")
	ResultPassHelper updateAgencyRegisteredUser(@Header("Authorization") String authorization, @Body RegisteredUser user, @Query("realIdCardNo") String realIdCardNo, @Query("bindDevice") Boolean bindDevice);
	
	/**
	 * ThirdAgencyUser tries to login with username and deviceSerial
	 * 
	 * @param username
	 * @param deviceSerial
	 * @return success:null or error_occur:error_msg, success can login
	 */
	@GET("/agencyApi/login")
	ResultPassHelper login(@Header("Authorization") String authorization, @Query("username") String username, @Query("deviceSerial") String deviceSerial);

}
