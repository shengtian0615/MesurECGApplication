package com.wehealth.model.interfaces.inter_register;

import com.wehealth.model.domain.model.PatientPayTransaction;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WeHealthPatientPayTransaction {
    @GET("/patientPayTransaction/registeredUser/{idCardNo}")
    PatientPayTransactionList getTransactioByRegisteredUserId(@Header("Authorization") String authorization,
                                                              @Path("idCardNo") String idCardNo, @Query("start") long start, @Query("end") long end);
    
    @GET("/patientPayTransaction")
    PatientPayTransactionList getAllTransaction(@Header("Authorization") String authorization, @Query("start") long start, @Query("end") long end);
    
    @POST("/patientPayTransaction")
 	public PatientPayTransaction createTransaction(@Header("Authorization") String authorization, @Body PatientPayTransaction trans);
    
    @GET("/patientPayTransaction/doctor/{idCardNo}")
    PatientPayTransactionList getTransactioByDoctorId(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo,
                                                      @Query("start") long start, @Query("end") long end);
    
    @GET("/patientPayTransaction/cost/{idCard}")
    List<PatientPayTransaction> getTransactionsByRegisteredUserId(
            @Header("Authorization") String authorization,
            @Path("idCard") String idCard,
            @Query("page") Integer page,
            @Query("pageCount") Integer pageCount);
}

