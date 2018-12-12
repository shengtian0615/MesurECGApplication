package com.wehealth.model.interfaces.inter_other;

import com.wehealth.model.domain.model.CompanyServiceFee;
import com.wehealth.model.domain.model.ResultPassHelper;

import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;

public interface WeHealthCompany {
    @GET("/company/servicefee")
    CompanyServiceFee getCompanyServiceFee(@Header("Authorization") String authorization);
    
    @GET("/clientSoftwareInfo/upload")
	public ResultPassHelper uploadInfo(
            @Header("Authorization") String authorization,
            @Query("type") String type,
            @Query("clientId") String clientId,
            @Query("versionName") String versionName,
            @Query("deviceInfo") String deviceInfo);
}

