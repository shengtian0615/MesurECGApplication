package com.wehealth.model.interfaces.inter_doctor;

import com.wehealth.model.domain.enumutil.CommonStatus;
import com.wehealth.model.domain.enumutil.DoctorTitle;
import com.wehealth.model.domain.model.DoctorBonusPointsTransaction;
import com.wehealth.model.domain.model.DoctorPill;
import com.wehealth.model.domain.model.HeartDoctor;
import com.wehealth.model.domain.model.HeartDoctorHelper;
import com.wehealth.model.domain.model.HeartDoctorPassHelper;
import com.wehealth.model.domain.model.Hospital;
import com.wehealth.model.domain.model.HospitalLicensePhoto;
import com.wehealth.model.domain.model.HospitalPhoto;
import com.wehealth.model.domain.model.ResultPassHelper;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WehealthHeartDoctor {
	
	/**
	 * 医生端登录后应该调用此接口，获取到了表示该医生具有“爱心医生”身份，才可以使用软件的功能
	 * @param docHid 是Doctor.id，不是身份证。
	 * @return
	 */
	@GET("/heartDoctor/{docHid}")
	HeartDoctor get(@Header("Authorization") String authorization, @Path("docHid") Long docHid);
	/**
	 * 随机获取n个医生前端展示
	 * @param size
	 * @param free
	 * @return
	 */
	@GET("/heartDoctor/random")
    List<HeartDoctorPassHelper> randomGet(@Header("Authorization") String authorization,
										  @Query("size") Integer size,
										  @Query("free") Boolean free);

	/**
	 * 查看更多医生时，采用分页搜索的形式
	 * @param name
	 * @param idCard
	 * @param cellPhone
	 * @param hospital
	 * @param title
	 * @param fields
	 * @param description
	 * @param hasFreePac
	 * @param page
	 * @param pageCount
	 * @return
	 */
	@GET("/heartDoctor/query")
    List<HeartDoctorPassHelper> query(@Header("Authorization") String authorization,
                                      @Query("name") String name,
                                      @Query("idCardNo") String idCard,
                                      @Query("cellPhone") String cellPhone,
                                      @Query("hospital") String hospital,
                                      @Query("title") DoctorTitle title,
                                      @Query("fields") String fields,
                                      @Query("description") String description,
                                      @Query("hasFreePac") Boolean hasFreePac,
                                      @Query("address") String address,
                                      @Query("nameOrHosp") String nameOrHosp,
                                      @Query("page") Integer page,
                                      @Query("pageCount") Integer pageCount);

	/**
	 * 医生给病人添加一条药物记录
	 * 新建/修改
	 */
	@POST("/patient/docPill")
	ResultPassHelper doctorCreatePill(@Header("Authorization") String authorization,
									  @Body DoctorPill dp);
	
	/**
	 *  删除开药记录
	 * @param hid
	 * @return
	 */
	@DELETE("/patient/docPill/{hid}")
	ResultPassHelper doctorDeletePill(@Header("Authorization") String authorization,
                                      @Path("hid") Long hid);
	
	/** 查询记录**/
	@GET("/patient/docPill/query")
    List<DoctorPill> queryDoctorPill(@Header("Authorization") String authorization,
									 @Query("patientId") String patientId,
									 @Query("doctorId") String doctorId,
									 @Query("expired") Boolean expired);
	
	/**
	 * 医生端注册接口
	 * @param helper
	 * @return返回值：
	注册成功：{name:success,value:hospitalId}，hospitalId是诊所的Long型ID，调用此接口成功后需要接着调用上传诊所照片的接口2.12.2，需要使用此hospitalId。
	失败：{name:error_occur,value:error message}
	 */
	@POST("/heartDoctor/register")
	ResultPassHelper register(@Header("Authorization") String authorization,
                              @Body HeartDoctorHelper helper);
	
	/**上传诊所照片**/
	@POST("/hospital/photo")
	ResultPassHelper updatePhoto(@Header("Authorization") String authorization,
                                 @Body HospitalPhoto photo);
	
	/**
	 * 1.2 获取某位医生的积分交易记录
	 * 参数:
		idCard --- 医生的身份证号,必填
	       返回值:
		指定时间段内所有交易记录.  如无记录,返回NULL.
	 */
	@GET("/doctorBonusPointsTransaction/doctor/{idCard}")
    List<DoctorBonusPointsTransaction> getTransactionsByDoctorId(
            @Header("Authorization") String authorization,
            @Path("idCard") String idCard,
            @Query("start") Long start,
            @Query("end") Long end,
            @Query("status") CommonStatus status,
            @Query("page") Integer page, @Query("pageCount") Integer pageCount);

	/**
	 * 1.3 获取某位医生的所有积分交易记录
	 * 参数:
		idCard --- 医生的身份证号,必填
	       返回值:
		所有交易记录.  如无记录,返回NULL.
	 */
	@GET("/doctorBonusPointsTransaction/allTransaction/{idCardNo}")
    List<DoctorBonusPointsTransaction> getTransactionsByDoctorId(
            @Header("Authorization") String authorization,
            @Path("idCardNo") String idCard);

	/**
	 * 2.1 根据执照号获取诊所照片
	 * 参数:
		idCard --- 诊所执照号,必填

	       返回值:
		诊所照片.  如无记录,返回NULL.
	 */
	@GET("/hospitalLicense/licenseNo/{licenseNo}")
	HospitalLicensePhoto getHospitalLicensePhotoById(
            @Header("Authorization") String authorization,
            @Path("licenseNo") String idCard);
	


	/**
	 * 2.2 上传诊所照片
	 * 返回值:
		ResultPassHelper {name:…,value:…}
		成功, 返回name= “success” 
		失败，返回 name= “failed” value = “ 出错原因”.
	 */
	@POST("/hospitalLicensePhoto")
	ResultPassHelper createHospitalLicensePhoto(
            @Header("Authorization") String authorization,
            @Body HospitalLicensePhoto photo);

	/**
	 * 2.3 更新诊所照片
	 * 返回值:
		ResultPassHelper {name:…,value:…}
		成功, 返回name= “success” 
		失败，返回 name= “failed” value = “ 出错原因”.
	 */
	@PUT("/hospitalLicensePhoto")
	ResultPassHelper updateHospitalLicensePhoto(
            @Header("Authorization") String authorization,
            @Body HospitalLicensePhoto photo);

	/**
	 * 2.4 删除诊所照片
	 * 	参数: idCard --- 诊所执照号,必填
		返回值: 成功返回“success”.  否则,返回“failed”.
	 */
	@DELETE("/hospitalLicensePhoto")
    String deleteHospitalLicensePhotoById(
            @Header("Authorization") String authorization,
            @Path("licenseNo") String idCard);
	/**
	 * 3.1 绑定设备
	 * @param idCardNo 医生ID号,必填
	 * @param deviceSN 设备序列号, 必填
	 * @return返回值: ResultPassHelper {name:status,value:1/2/3/4/5/6/7/8}
	1:  idCardNo是空
	2:  系统中该医生不存在
	3: 医生已绑定设备, 先解绑再绑定
	4: 无法获取设备绑定用户
	5：更新医生信息失败
	6: 更新注册用户信息失败
	7: 设备已绑定某个医生.
	8: 设备不是商业版.
	绑定成功返回 {name:userName, value: 真实用户ID} 
	 */
	@PUT("/doctor/bindDevice/{idCard}")
	ResultPassHelper bindDevice(
            @Header("Authorization") String authorization,
            @Path("idCard") String idCardNo,
            @Query("deviceSN") String deviceSN);
	
	/**
	 * 3.2 解绑设备
	 * @param doctorId 医生ID号,必填
	 * @return 返回值: ResultPassHelper {name:status,value:0/1/2}
	0: 解绑成功
	1: idCardNo是空
	2:系统中该医生不存在
	3:医生未绑定设备
	4. 无法获取设备绑定用户
	5. 更新用户环信Proxy信息失败
	6：更新医生信息失败
	 */
	@PUT("/doctor/unbindDevice/{idCard}")
	ResultPassHelper unbindDevice(
            @Header("Authorization") String authorization,
            @Path("idCard") String doctorId);
	/**
	 * 1.通过ID获取医院
	 * @param id:  医院或诊所id
	 * @return 成功返回Hospital实例 失败返回NULL
	 */
	@GET("/hospital/id/{id}")
	Hospital getHospital(
            @Header("Authorization") String authorization,
            @Path("id") Long id);
}
