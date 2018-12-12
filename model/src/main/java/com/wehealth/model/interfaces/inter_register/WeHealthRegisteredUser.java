package com.wehealth.model.interfaces.inter_register;

import com.wehealth.model.domain.model.RegisteredUser;
import com.wehealth.model.domain.model.ResultPassHelper;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WeHealthRegisteredUser {
    @GET("/registeredUser/idCard/{idCardNo}")
    RegisteredUser getRegisteredUser(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo);
    
    @GET("/registeredUser/username/{username}")
    RegisteredUser getByUsername(@Header("Authorization") String authorization, @Path("username") String username);
    
    @GET("/registeredUser/equipment/{equipmentSerialNo}")
    RegisteredUser getRegisteredUserByEquipmentSerialNo(@Header("Authorization") String authorization, @Path("equipmentSerialNo") String equipmentSerialNo);
    
    @PUT("/registeredUser")
    RegisteredUser updateRegisteredUser(@Header("Authorization") String authorization, @Body RegisteredUser user, @Query("bindDevice") Boolean bindDevice, @Query("operator") String operator) ;
    
    @POST("/registeredUser")
    RegisteredUser registerRegisteredUser(@Header("Authorization") String authorization, @Body RegisteredUser registeredUser);
    
    @GET("/registeredUser/query/users/{idCard}")
    RegisteredUserList userPreferedTheDoctor(@Header("Authorization") String authorization, @Path("idCard") String idCard);
    
    @GET("/registeredUser/query/patient/{patientId}")
    RegisteredUser getUsersBindThePatient(@Header("Authorization") String authorization, @Path("patientId") String idCard);
    
    @GET("/registeredUser/bind/{idCardNo}")
    RegisteredUser bindPatient(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo, @Query("serialNo") String equipSerial, @Query("patientId") String patientId);
    
    @GET("/registeredUser/bindPatients/{idCardNo}")
    ResultPassHelper bindPatient(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo, @Query("patientId") String patientId);
    
    /**忘记密码**/
    @GET("/registeredUser/changepw/{phoneNumber}")
	ResultPassHelper changePassword(@Path("phoneNumber") String phoneNumber, @Query("smsId") String smsId, @Query("password") String password, @Query("operator") String operator);

    /**修改密码**/
    @GET("/registeredUser/changeOldPw/{idCardNo}")
    ResultPassHelper changeOldPassword(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo, @Query("oldPassword") String oldPassword, @Query("newPassword") String newPassword);

    @GET("/registeredUser/prefer/{idCardNo}")
    ResultPassHelper sendPreferAskToDoctor(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo, @Query("doctorIdCardNo") String doctorIdCardNo);
    
    @GET("/registeredUser/unprefer/{idCardNo}")
    ResultPassHelper cancelPreferDoctor(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo, @Query("doctorIdCardNo") String doctorIdCardNo);
    
    @GET("/registeredUser/check/phone/{phone}")
    ResultPassHelper checkIfPhoneExists(@Header("Authorization") String authorization, @Path("phone") String phone);
    
    @GET("/registeredUser/checkUsername/{username}")
    ResultPassHelper checkUserExist(@Path("username") String username);
    
    @GET("/registeredUser/check/idCard/{idCardNo}")
    ResultPassHelper checkIDCardExist(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo);
    
    @GET("/registeredUser/checkIdCardNo/{idCardNo}")
    ResultPassHelper checkIDCardExists(@Path("idCardNo") String idCardNo);
    
    @GET("/registeredUser/easemob/{username}")
    RegisteredUser getByEasemobUsername(@Header("Authorization") String authorization, @Path("username") String username);
    
    @GET("/registeredUser/device/{deviceNo}")
	RegisteredUser getByDevice(@Header("Authorization") String authorization, @Path("deviceNo") String deviceNo);
	
    @GET("/registeredUser/device/serialNo/{serialNo}")
	RegisteredUser getByDeviceSerial(@Header("Authorization") String authorization, @Path("serialNo") String serialNo);
    
    @POST("/registeredUser")
	RegisteredUser registerRegisteredUser(@Header("Authorization") String authorization, @Body RegisteredUser user, @Query("createPatient") Boolean cp, @Query("bindDevice") Boolean bindDevice);
    
    @GET("/registeredUser/personal/has")
    ResultPassHelper hasPersonelECGDoctor(@Header("Authorization") String authorization, @Query("regId") String regId, @Query("patientId") String patientId);
}

