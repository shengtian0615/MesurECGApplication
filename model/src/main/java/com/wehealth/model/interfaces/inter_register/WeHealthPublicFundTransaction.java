package com.wehealth.model.interfaces.inter_register;

import com.wehealth.model.domain.model.PublicFundAccountTransaction;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WeHealthPublicFundTransaction {
    @GET("/publicFundTransaction/registeredUser/{idCardNo}")
    PublicFundAccountTransactionList getTransactioByRegisteredUserId(@Header("Authorization") String authorization,
                                                                     @Path("idCardNo") String idCardNo, @Query("start") Long start, @Query("end") Long end);
    
    @GET("/publicFundTransaction")
    PublicFundAccountTransactionList getAllTransaction(@Header("Authorization") String authorization, @Query("start") long start, @Query("end") long end);
    
    @POST("/publicFundTransaction")
 	public PublicFundAccountTransaction createTransaction(@Header("Authorization") String authorization, @Body PublicFundAccountTransaction trans);
    
    @GET("/publicFundTransaction/doctor/{idCardNo}")
    PublicFundAccountTransactionList getTransactioByDoctorId(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo);
 
    @POST("/publicFundTransaction/createItem")
	PublicFundAccountTransaction createItem(@Header("Authorization") String authorization, @Body PublicFundAccountTransaction transaction);

    @GET("/publicFundTransaction/registeredUser/{idCard}")
    List<PublicFundAccountTransaction> getTransactionsByRegisteredUserId(
            @Header("Authorization") String authorization,
            @Path("idCard") String idCard,
            @Query("page") Integer page,
            @Query("pageCount") Integer pageCount);
}

