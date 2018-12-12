package com.wehealth.model.interfaces.inter_doctor;

import com.wehealth.model.domain.model.DoctorAccountTransaction;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WeHealthDoctorAccountTransaction {
    @GET("/doctorAccountTransaction/registeredUser/{idCardNo}")
    DoctorAccountTransactionList getTransactionByRegisteredUserId(@Header("Authorization") String authorization,
                                                                  @Path("idCardNo") String idCardNo, @Query("start") long start, @Query("end") long end);
    
    @GET("/doctorAccountTransaction")
    DoctorAccountTransactionList getAllTransaction(@Header("Authorization") String authorization, @Query("start") long start, @Query("end") long end);
    
    @POST("/doctorAccountTransaction")
 	public DoctorAccountTransaction createTransaction(@Header("Authorization") String authorization, @Body DoctorAccountTransaction trans);
    
    @GET("/doctorAccountTransaction/doctor/{idCardNo}")
    DoctorAccountTransactionList getTransactioByDoctorId(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo,
                                                         @Query("start") long start, @Query("end") long end);
    
    @GET("/doctorAccountTransaction/list")
    DoctorAccountTransactionList getTransactionsBySettleId(@Header("Authorization") String authorization, @Query("settleId") Long settleId, @Query("idCard") String idCard);
}

