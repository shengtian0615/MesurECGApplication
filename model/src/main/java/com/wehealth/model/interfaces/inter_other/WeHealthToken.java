package com.wehealth.model.interfaces.inter_other;

import com.wehealth.model.domain.model.AuthToken;

import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;


public interface WeHealthToken {

	@POST("/token")
	AuthToken authorize(@Query("grant_type") String grantType, @Query("scope") String scope,
						@Query("client_id") String username, @Query("client_secret") String password);
	
	@POST("/token")
	AuthToken refreshToken(@Header("Authorization") String authorization, @Query("grant_type") String grantType,
                           @Query("scope") String scope, @Query("refresh_token") String refreshToken);
}
