package com.wehealth.model.interfaces.inter_doctor;

import com.wehealth.model.domain.model.DoctorLicensePhoto;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WeHealthDoctorLicense {

	/**
	 * 根据身份证号找医生执照
	 * @param idCardNo
	 * @return
	 */
    @GET("/docLicense/licenseNo/{licenseNo}")
    DoctorLicensePhoto getDoctorLicensePhoto(@Header("Authorization") String authorization, @Path("licenseNo") String idCardNo);
   
    /**
     * 创建医生执照
     * @return
     */
    @POST("/docLicense")
    DoctorLicensePhoto createDoctorLicensePhoto(@Header("Authorization") String authorization, @Body DoctorLicensePhoto doctorLicensePhoto);
    
    /**
     * 更新医生执照
     * @param doctorLicensePhoto
     * @return
     */
    @PUT("/docLicense")
    DoctorLicensePhoto updateDoctorLicensePhoto(@Header("Authorization") String authorization, @Body DoctorLicensePhoto doctorLicensePhoto);
    
    /**
     * 删除执照
     * @param idCard
     * @return
     */
    @DELETE("/docLicense/licenseNo/{licenseNo}")
    String deleteDoctorLicensePhotoById(@Header("Authorization") String authorization, @Path("licenseNo") String idCard);
    
    @GET("/docLicense/versioned")
	DoctorLicensePhoto getPhoto(@Header("Authorization") String authorization, @Query("id") String id, @Query("version") Long version);
}
