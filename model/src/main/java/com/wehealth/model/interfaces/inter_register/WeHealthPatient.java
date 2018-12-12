package com.wehealth.model.interfaces.inter_register;

import com.wehealth.model.domain.enumutil.BloodSugarType;
import com.wehealth.model.domain.model.BloodPressure;
import com.wehealth.model.domain.model.BloodSugar;
import com.wehealth.model.domain.model.Patient;
import com.wehealth.model.domain.model.ResultPassHelper;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WeHealthPatient {
    @GET("/patient/idCard/{idCardNo}")
    Patient getPatient(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo);
    
    @POST("/patient")
	public Patient registerPatient(@Header("Authorization") String authorization, @Body Patient patient);
    
    @PUT("/patient")
    Patient updatePatient(@Header("Authorization") String authorization, @Body Patient patient);
    
    @GET("/patient/equipment/{equipmentSerialNo}")
    Patient getPatientByEquipmentSerialNo(@Header("Authorization") String authorization, @Path("equipmentSerialNo") String equipmentSerialNo);
    
//    @GET("/query/")
//	 public List<Patient> findPatients(@Query("idCard") String idCard, 
//			@Query("name") String name, @Query("cellPhone") String cellPhone,
//			@Query("registeredUserIdCardNo") String registeredUserIdCardNo);
    
    @GET("/patient/check/phone/{phone}")
    ResultPassHelper checkPhoneExists(@Path("phone") String phone, @Query("checkReg") boolean checkReg);
    
    @POST("/patient/sugar")
    public ResultPassHelper createBloodSugar(@Header("Authorization") String authorization, @Body BloodSugar sugar);
    
    @PUT("/patient/sugar")
    public ResultPassHelper updateBloodSugar(@Header("Authorization") String authorization, @Body BloodSugar sugar);
    
    @GET("/patient/sugar/query")
    public List<BloodSugar> queryBloodSugar(@Header("Authorization") String authorization,
                                            @Query("createTime") Long createTime,
                                            @Query("updateTime") Long updateTime,
                                            @Query("patientId") String patientId,
                                            @Query("type") BloodSugarType type,
                                            @Query("testStartTime") Long testStart,
                                            @Query("testEndTime") Long testEnd,
                                            @Query("page") Integer page, @Query("pageCount") Integer pageCount);
    
    @GET("/patient/sugar/delete/{hid}")
    public ResultPassHelper deleteBloodSugar(@Header("Authorization") String authorization, @Path("hid") Long hid);
    
    @POST("/patient/pressure")
    public ResultPassHelper createBloodPressure(@Header("Authorization") String authorization, @Body BloodPressure pressure);
    
    @PUT("/patient/pressure")
    public ResultPassHelper updateBloodPressure(@Header("Authorization") String authorization, @Body BloodPressure pressure);
    
    @GET("/patient/pressure/query")
    public List<BloodPressure> queryBloodPressure(@Header("Authorization") String authorization,
                                                  @Query("createTime") Long createTime,
                                                  @Query("updateTime") Long updateTime,
                                                  @Query("patientId") String patientId,
                                                  @Query("testStartTime") Long testStart,
                                                  @Query("testEndTime") Long testEnd,
                                                  @Query("page") Integer page, @Query("pageCount") Integer pageCount);
    
    @GET("/patient/pressure/delete/{hid}")
    public ResultPassHelper deleteBloodPressure(@Header("Authorization") String authorization, @Path("hid") Long hid);
}

