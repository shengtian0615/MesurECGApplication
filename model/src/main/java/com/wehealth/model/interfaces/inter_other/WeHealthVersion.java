package com.wehealth.model.interfaces.inter_other;

import com.wehealth.model.domain.enumutil.AppType;
import com.wehealth.model.domain.model.AppVersion;

import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;

public interface WeHealthVersion {

	@GET("/appVersion/query/new")
	AppVersion getNewVersion(@Header("Authorization") String authorization, @Query("appType") AppType appType, @Query("versionCode") int versionCode);
}
