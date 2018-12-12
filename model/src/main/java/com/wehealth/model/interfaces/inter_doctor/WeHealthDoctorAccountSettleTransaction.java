package com.wehealth.model.interfaces.inter_doctor;

import com.wehealth.model.domain.model.DoctorAccountSettleTransaction;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WeHealthDoctorAccountSettleTransaction {
    
    @GET("/doctorAccountSettleTransaction")
    DoctorAccountSettleTransactionList getAllTransaction(@Header("Authorization") String authorization, @Query("start") long start, @Query("end") long end);
    
    @POST("/doctorAccountSettleTransaction")
 	public DoctorAccountSettleTransaction createTransaction(@Header("Authorization") String authorization, @Body DoctorAccountSettleTransaction trans);
    
    @GET("/doctorAccountSettleTransaction/doctor/{idCardNo}")
    DoctorAccountSettleTransactionList getTransactioByDoctorId(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo,
                                                               @Query("start") long start, @Query("end") long end);
}

