package com.wehealth.model.interfaces.inter_register;

import com.wehealth.model.domain.model.ECGPhoto;
import com.wehealth.model.domain.model.ResultPassHelper;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WeHealthECGPhoto {
	@GET("/ecgPhoto/list/{patientId}")
    List<ECGPhoto> queryList(@Header("Authorization") String authorization, @Path("patientId") Long patientId, @Query("reportId") String reportId, @Query("diagnoseTime") Long diagnoseTime);
	
	@GET("/ecgPhoto/query/{id}")
	ECGPhoto getById(@Header("Authorization") String authorization, @Path("id") Long id);
	
	@POST("/ecgPhoto")
	ResultPassHelper createECGPhoto(@Header("Authorization") String authorization, @Body ECGPhoto photo);
	
	@PUT("/ecgPhoto")
	ResultPassHelper updatePhoto(@Header("Authorization") String authorization, @Body ECGPhoto photo);
	
	@GET("/ecgPhoto/delete")
	ResultPassHelper deletePhoto(@Header("Authorization") String authorization, @Query("id") Long id, @Query("patientId") Long patientId, @Query("reportId") Long reportId);
	
	@GET("/ecgPhoto/versioned")
	ECGPhoto getPhoto(@Header("Authorization") String authorization, @Query("id") String id, @Query("version") Long version);
	
}
