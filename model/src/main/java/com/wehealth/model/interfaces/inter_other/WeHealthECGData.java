package com.wehealth.model.interfaces.inter_other;

import com.wehealth.model.domain.model.ECGData;
import com.wehealth.model.domain.model.ECGDataPassHelper;
import com.wehealth.model.domain.model.ECGDevice;
import com.wehealth.model.domain.model.ResultPassHelper;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WeHealthECGData {
	/**
	 * 获取最近一个心电数据数
	 * 说明：idCard必传，其他4项如果不需要可以不传，或者传-1。
		获取具有身份证号idCard的病人的最近一次心电数据，结果可能来自商业版
	 * @return
	 */
	@GET("/ecgData/lastest/idCard/{idCard}")
    ECGData getLastestECGDataByIdCard(@Header("Authorization") String authorization,
                                      @Path("idCard") String idCard,
                                      @Query("start") long start,//@DefaultValue("-1")
                                      @Query("end") long end,//@DefaultValue("-1")
                                      @Query("page") int page,//@DefaultValue("-1")
                                      @Query("count") int count);//@DefaultValue("-1")
	
	/**获取某病人的心电档案
	 * @param idCard 参数说明：idCard必传，其他4项如果不需要可以不传，或者传-1。
	 * @param start
	 * @param end
	 * @param page
	 * @param count
	 * @return 获取具有身份证号idCard的病人的心电数据，默认获取已经关联了真实身份证的病人的心电数据。两种身份的数据一起按时间降序排列。
	 */
	@GET("/ecgData/idCard/{idCard}")
    List<ECGDataPassHelper> getECGDataByIdCard(@Header("Authorization") String authorization,
                                               @Path("idCard") String idCard,
                                               @Query("start") long start,
                                               @Query("end") long end,
                                               @Query("page") int page,
                                               @Query("count") int count);

	
	@GET("/ecgData/helper/{id}")
	ECGDataPassHelper getHelperById(@Header("Authorization") String authorization, @Path("id") Long id);
    
	@GET("/ecgData/idCard/{idCardNo}")
    ECGDataList getECGData(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo, @Query("start") long start, @Query("end") long end, @Query("page") int page, @Query("count") int count);
   
    @GET("/ecgData/id/{id}")
    ECGData getECGDataById(@Header("Authorization") String authorization, @Path("id") long id);
    
    @POST("/ecgData")
    ResultPassHelper createECGData(@Header("Authorization") String authorization, @Body ECGData data);
    
    @FormUrlEncoded
    @POST("/ecgData/manualResult/accept/{idCardNo}")
    String acceptManualDiagnosisTask(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo,
                                     @Field("ecgDataId") long ecgDataId, @Field("accept") boolean accept, @Field("level") String docLevel);

    @POST("/ecgData/manualResult/upload/{idCardNo}")
    ResultPassHelper uploadManualDiagnosisResult(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo, @Query("ecgDataId") long ecgDataId, @Query("code") String code, @Query("comment") String comment);

    @GET("/ecgData/manualResult/regularCheck/{dataId}")
    ResultPassHelper requestRegularCheck(@Header("Authorization") String authorization, @Path("dataId") String id);
    
    @GET("/ecgData/manualResult/regularCheck/{dataId}")
	ResultPassHelper requestRegularCheck(@Header("Authorization") String authorization, @Path("dataId") String id, @Query("bonusId") Long bonusId);
    
    @GET("/ecgData/manualResult/freeCheck/{dataId}")
    ResultPassHelper requestFreeCheck(@Header("Authorization") String authorization, @Path("dataId") String id, @Query("bonusId") Long bonusId, @Query("symptoms") String symptoms);//
  
    @GET("/ecgData/manualResult/freeCheck/{dataId}")
    ResultPassHelper requestFreeCheck(@Header("Authorization") String authorization, @Path("dataId") String id, @Query("bonusId") Long bonusId, @Query("symptoms") String symptoms, @Query("idEncoded") Boolean isEncoded);//最后一个参数  true表示商业版的id
  
    @FormUrlEncoded
    @POST("/ecgData/manualResult/freeCheck/accept/{idCardNo}")
    String acceptFreeDiagnosisTask(@Header("Authorization") String authorization, @Path("idCardNo") String idCard, @Field("ecgDataId") long ecgDataId);
    
    @POST("/ecgData/manualResult/freeCheck/upload/{idCardNo}")
	ResultPassHelper uploadFreeCheckResult(@Header("Authorization") String authorization, @Path("idCardNo") String idCard, @Query("ecgDataId") Long ecgDataId, @Query("code") String code, @Query("comment") String comment);
    
    @POST("/ecgData/manualResult/regularCheck/upload/{idCardNo}")
    ResultPassHelper uploadRegularCheckResult(@Header("Authorization") String authorization, @Path("idCardNo") String idCard, @Query("ecgDataId") long ecgDataId, @Query("code") String code, @Query("comment") String comment);
    
    @GET("/ecgData/idCard/{idCard}")
    ECGDataPassHelperList getECGDataHelperByIdCard(@Header("Authorization") String authorization, @Path("idCard") String idCard, @Query("start") long start, @Query("end") long end, @Query("page") int page, @Query("count") int count);

    @GET("/ecgDevice/get")
    ECGDevice getECGDevice(@Header("Authorization") String authorization, @Query("serialNo") String serialNo, @Query("imei") String imei);
    
    @GET("/ecgDevice/checkSerial/{serialNo}")
	ResultPassHelper checkSerial(@Header("Authorization") String authorization, @Path("serialNo") String serialNo);
    
    @GET("/ecgData/personal/check/{dataId}")
    ResultPassHelper personalEcgCheck(@Header("Authorization") String authorization, @Path("dataId") String id, @Query("symptoms") String symptoms);
}