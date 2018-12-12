package com.wehealth.model.interfaces.inter_register;

import com.wehealth.model.domain.model.RegisteredUserPhoto;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WehealthRegisterUserPhoto {

	/**根据身份证号获取用户头像**/
	@GET("/regUserPhoto/idCard/{idCard}")
	RegisteredUserPhoto getRegisteredUserPhotoById(@Header("Authorization") String authorization, @Path("idCard") String idCardNo);
	
	/**上传用户头像**/
	@POST("/regUserPhoto")
	RegisteredUserPhoto createRegisteredUserPhoto(@Header("Authorization") String authorization, @Body RegisteredUserPhoto user);
	
	/**更新用户头像**/
	@PUT("/regUserPhoto")
	RegisteredUserPhoto updateRegisteredUserPhoto(@Header("Authorization") String authorization, @Body RegisteredUserPhoto user);
	
	@GET("/regUserPhoto/versioned")
	RegisteredUserPhoto getPhoto(@Header("Authorization") String authorization, @Query("id") String id, @Query("version") Long version);
	
}
