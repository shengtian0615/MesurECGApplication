package com.wehealth.model.interfaces.inter_other;

import com.wehealth.model.domain.enumutil.AppType;
import com.wehealth.model.domain.model.Addr;
import com.wehealth.model.domain.model.AppVersion;
import com.wehealth.model.domain.model.Hospital;
import com.wehealth.model.domain.model.ResultPassHelper;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WehealthTokenFree {

	/**
	 * Checking new AppVersion doesn't need login.
	 * @param appType
	 * @param versionCode
	 */
	@GET("/tokenFree/appVersion/checkNew")
	AppVersion getNewVersion(@Query("appType") AppType appType, @Query("versionCode") int versionCode);
	
	/**
	 * Uploading Client Software Info doesn't need login.
	 * @param type
	 * @param clientId
	 * @param versionName
	 * @param deviceInfo
	 * @return
	 */
	@GET("/tokenFree/clientSoftwareInfo/upload")
	ResultPassHelper uploadInfo(
            @Query("type") String type,
            @Query("clientId") String clientId,
            @Query("versionName") String versionName,
            @Query("deviceInfo") String deviceInfo);
	
	/**
	 * Doctor
	 * @param username
	 * @return
	 */
	@GET("/tokenFree/doctor/checkUsername/{username}")
	ResultPassHelper checkUserExist(@Path("username") String username);
	
	/**
	 * Doctor
	 * @param idCardNo
	 * @return
	 */
	@GET("/tokenFree/doctor/checkIdCardNo/{idCardNo}")
	ResultPassHelper checkIdCardExists(@Path("idCardNo") String idCardNo);
	
	@GET("/tokenFree/doctor/changepw/{phoneNumber}")
	ResultPassHelper changePassword(@Path("phoneNumber") String phoneNumber, @Query("smsId") String smsId,
                                    @Query("password") String password, @Query("operator") String operator);
	
	/**
	 * 返回的结果：
	 * exist: 用户名存在
	 * usable: 可以使用
	 * format_error: 格式错误，格式详见DataUtil.checkUserAvailable(String)
	 */
	@GET("/tokenFree/registeredUser/checkUsername/{username}")
	ResultPassHelper regUserCheckUserExist(@Path("username") String username);
	
	/**
	 * RegisterUser
	 * @param idCardNo
	 * @return
	 */
	@GET("/tokenFree/registeredUser/checkIdCardNo/{idCardNo}")
	ResultPassHelper checkIDCardExists(@Path("idCardNo") String idCardNo);
	
	@GET("/tokenFree/registeredUser/changepw/{phoneNumber}")
	ResultPassHelper regUserChangePassword(
            @Path("phoneNumber") String phoneNumber,
            @Query("smsId") String smsId,
            @Query("password") String password,
            @Query("operator") String operator);
	
	/**
	 * 1. search by province + city
	 * 2. search by name/address
	 */
	@GET("/tokenFree/hospital/query/names")
    List<Hospital> listNames(
            @Query("province") String province,
            @Query("city") String city,
            @Query("name") String name,
            @Query("address") String address,
            @Query("page") Integer page, @Query("pageCount") Integer pageCount);
	
	@GET("/tokenFree/ecgDevice/thirdHost/{imei}")
	ResultPassHelper getThirdAgencyHost(@Path("imei") String imei);
	
	/**
	 * 获取地名：四个条件至少传一个。否则获取不到地名<br>
	 * 若需要按级别列出地址，只传parent参数，第一级别parent传0。
	 * @param parent，父地名编码
	 * @param name, 模糊查找地名中包含name的地名
	 * @param level, 查找某级别的地名
	 * @param code, 模糊查找编码中包含code的地名
	 * @return
	 */
	@GET("/tokenFree/addr/query")
    List<Addr> queryAddr(
            @Query("parent") String parent,
            @Query("name") String name,
            @Query("level") Integer level,
            @Query("code") String code);
	
	/**
	 * 查询天奇诊所
	 * @param name 诊所名称，模糊查找
	 * @param address 诊所地址，模糊查找
	 * @param serial 诊所编码，精确查找
	 * @param page 页码，从0开始
	 * @param pageCount 第页记录数，例如10
	 * @return
	 */
	@GET("/tokenFree/tianqi/query")
    List<Hospital> query(
            @Query("name") String name,
            @Query("address") String address,
            @Query("serial") String serial,
            @Query("page") Integer page,
            @Query("pageCount") Integer pageCount);
	
	
	/**
	 *   4.1 获取下一个可用的诊所编号，前台创建Hospital时需先调用该接口，然后设置诊所编号
	 * @param addressCode 诊所地址代码---必填
	 * @return返回值: 其中value 里保存的是可用诊所编号. 如果小于0,说明获取诊所编号有错, 意味着传入的地址编码小于六位.
	 */
	@GET("/tokenFree/v1/query/nextAvalClinicCode")
	ResultPassHelper getNextAvailableClinicCode(@Query("addressCode") String addressCode);

}
