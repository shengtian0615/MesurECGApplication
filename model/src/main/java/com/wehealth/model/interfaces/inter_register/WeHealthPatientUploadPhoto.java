package com.wehealth.model.interfaces.inter_register;

import com.wehealth.model.domain.model.PatientUploadPhoto;
import com.wehealth.model.domain.model.ResultPassHelper;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WeHealthPatientUploadPhoto {

	@GET("/patientUploadPhoto/list/{patientId}")
	PatientUploadPhotoList queryListPUP(
            @Header("Authorization") String authorization,
            @Path("patientId") Long patientId,
            @Query("createTime") Long createTime,
            @Query("description") String description);
	
	@GET("/patientUploadPhoto/query/{id}")
	PatientUploadPhoto getById(@Header("Authorization") String authorization, @Path("id") Long id);
	
	@POST("/patientUploadPhoto")
	ResultPassHelper createPhoto(@Header("Authorization") String authorization, @Body PatientUploadPhoto photo);
	
	@PUT("/patientUploadPhoto")
	ResultPassHelper updatePhoto(@Header("Authorization") String authorization, @Body PatientUploadPhoto photo);
	
	@DELETE("/patientUploadPhoto")
	ResultPassHelper deletePhoto(@Header("Authorization") String authorization, @Query("id") Long id, @Query("patientId") Long patientId);

	@GET("/patientUploadPhoto/versioned")
	PatientUploadPhoto getPhoto(@Header("Authorization") String authorization, @Query("id") String id, @Query("version") Long version);
	
}
