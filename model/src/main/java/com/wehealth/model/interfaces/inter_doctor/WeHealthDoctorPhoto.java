package com.wehealth.model.interfaces.inter_doctor;

import com.wehealth.model.domain.model.DoctorPhoto;
import com.wehealth.model.domain.model.ResultPassHelper;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WeHealthDoctorPhoto {
    @GET("/doctorPhoto/idCard/{idCard}")
    DoctorPhoto getDoctorPhoto(@Header("Authorization") String authorization, @Path("idCard") String idCardNo);
    
    @GET("/doctorPhoto")
    DoctorPhotoList getAllDoctorPhoto(@Header("Authorization") String authorization);
    
    @POST("/doctorPhoto")
    DoctorPhoto createDoctor(@Header("Authorization") String authorization, @Body DoctorPhoto doctorPhoto);
    
    @PUT("/doctorPhoto")
    DoctorPhoto updateDoctorPhoto(@Header("Authorization") String authorization, @Body DoctorPhoto doctorPhoto);
    
    @GET("/doctorPhoto/versioned")
	DoctorPhoto getPhoto(@Header("Authorization") String authorization, @Query("id") String id, @Query("version") Long version);

    @POST("/doctorPhoto/upload")
    ResultPassHelper newCreateDoctorPhoto(@Header("Authorization") String authorization, @Body DoctorPhoto photo);
    
    @PUT("/doctorPhoto/upload")
	ResultPassHelper newUpdateDoctorPhoto(@Header("Authorization") String authorization, @Body DoctorPhoto photo);
    
}

