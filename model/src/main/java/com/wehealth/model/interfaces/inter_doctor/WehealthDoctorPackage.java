package com.wehealth.model.interfaces.inter_doctor;

import com.wehealth.model.domain.enumutil.DoctorPackageStatus;
import com.wehealth.model.domain.model.BounsGift;
import com.wehealth.model.domain.model.DoctorPacItem;
import com.wehealth.model.domain.model.DoctorPackage;
import com.wehealth.model.domain.model.DoctorPackagePassHelper;
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

public interface WehealthDoctorPackage {

	/**
	 * 医生添加套餐
	 * @param authorization
	 * @param hp
	 * @return{success:套餐记录的ID} {error_occur:错误信息}
	 * 注意：套餐与医生身份证相关联，不应该为空。
	 */
	@POST("/doctorPac")
	ResultPassHelper createPackage(@Header("Authorization") String authorization, @Body DoctorPackagePassHelper hp);
	
	/**
	 * 医生更新套餐
	 * @param authorization
	 * @param hp
	 * @return{success:更新后套餐的ID} {error_occur:错误信息}
	 * 注意：更新会生成新的记录，与老记录的ID不同。具体查看Model解释。 用户购买套餐使用的是ID。
	 */
	@PUT("/doctorPac")
	ResultPassHelper updatePackage(@Header("Authorization") String authorization, @Body DoctorPackagePassHelper hp);
	
	/**
	 * 获取一位医生的所有套餐
	 * @param authorization
	 * @param idCardNo
	 *  status详见Model解释。如果不传此参数，默认值是run。
	run: 只获取已经上线的套餐，适合用户查看医生套餐。
	edit: 获取上线、和编辑中的套餐，适合医生自己获取套餐。
	history: 获取全部状态的套餐，适合医生自己获取套餐，以及展示套餐历史。 （目前不需要医生端展示历史记录）
	增加了parentId参数。当医生需要获取某个套餐的历史记录时，最好是传此参数，同时status传history。
	增加了role参数：0医生，1用户。必须有此参数。
	增加了agencyId参数：用户属于机构时传此参数，查看后面的DoctorPackage解释。用途，查询普通套餐以及agencyId的专属套餐。
	 * @return
	 */
	@GET("/doctorPac/pac/{idCardNo}")
    List<DoctorPackage> queryDoctorPackage(@Header("Authorization") String authorization,
										   @Path("idCardNo") String idCardNo,//必须
										   @Query("status") DoctorPackageStatus status,
										   @Query("parentId") Long parentId,
										   @Query("role") Integer role,//必须
										   @Query("agencyId") String agencyId);

	/**
	 * 获取某一套餐的项目列表
	 * @param pacId
	 * @return
	 */
	@GET("/doctorPac/items/{pacId}")
    List<DoctorPacItem> queryDoctorPacItems(@Header("Authorization") String authorization,
											@Path("pacId") Long pacId);

	/**
	 * 获取某一套餐：适合查看当前用户的套餐
	 * @param authorization
	 * @param pacId
	 * @return
	 */
	@GET("/doctorPac/get/{pacId}")
	DoctorPackage getDoctorPackage(@Header("Authorization") String authorization,
                                   @Path("pacId") Long pacId);


	/**
	 * 获取某一套餐的全部信息（helper: 套餐及项目列表）：适合查看当前用户的套餐
	 */
	@GET("/doctorPac/helper/{pacId}")
	DoctorPackagePassHelper getPassHelper(@Header("Authorization") String authorization,
                                          @Path("pacId") Long pacId);

	/**
	 * 获取某位医生的全部套餐(helper: 套餐及项目列表):
	 */
	@GET("/doctorPac/helper/query/{idCardNo}")
    List<DoctorPackagePassHelper> queryPassHelper(@Header("Authorization") String authorization,
                                                  @Path("idCardNo") String idCardNo,
                                                  @Query("status") DoctorPackageStatus status,
                                                  @Query("parentId") Long parentId,
                                                  @Query("role") Integer role,//必须
                                                  @Query("agencyId") String agencyId);

	/**
	 * 医生发布套餐：医生编辑完成后，调用此接口发布，用户就可以看到了。
	 * 返回值：
	{success: 发布成功}
	{error_occur: 错误信息}
	 */
	@GET("/doctorPac/run/{pacId}")
	ResultPassHelper runPac(@Header("Authorization") String authorization,
                            @Path("pacId") Long pacId);

	/**
	 * 删除套餐
	返回值：
	{success: 删除成功}
	{error_occur: 错误信息}
	历史记录不能删除； 后台不会真的删除，会把edit/run状态的记录变为history。因为用户可能已经购买过某个套餐，变成history记录后用户还可以查看。
	 */
	@DELETE("/doctorPac/delete/{hid}")
	ResultPassHelper delete(@Header("Authorization") String authorization,
                            @Path("hid") Long hid);

	/**
	 * 选医生及套餐(免费的)，提交建群。
	 * @param userId 用户身份证
	 * @param docId 医生身份证
	 * @param pacId 被选中的医生套餐ID，（不是parentId）
	 * @return {success：“成功”} {error_occur: “错误原因”}
	 */
	@GET("/heartChatGroup/complete")
	ResultPassHelper completeGroup(@Header("Authorization") String authorization,
                                   @Query("userId") String userId,
                                   @Query("docId") String docId,
                                   @Query("pacId") Long pacId);

	/**
	 * 查询积分兑换礼物
	 * @param name --- 礼物名称按名字尽可能匹配
	 * @param type --- 礼物类型，卡券或实物
	 * @param expired --- 查过期的或没过期的
	 * @param page --- 从第page页开始
	 * @param pageCount --- 每页多少个数据
	 * @return
	 */
	@GET("/bonusGift/query")
    List<BounsGift> queryBonus(@Header("Authorization") String authorization,
							   @Query("name") String name,
							   @Query("type") String type,
							   @Query("expired") Boolean expired,
							   @Query("page") Integer page, @Query("pageCount") Integer pageCount);
	
	/**
	 *  积分兑换礼品
	 * @param doctorId --- 医生ID号,必填
	 * @param giftId --- 礼品卡的id, 必填
	 * @return ResultPassHelper
		{name:status,value:0/1/2/3/4/5/6}
		0: 兑换成功
		1: idCardNo， 或者giftId是空
		2: 系统中该医生不存在
		3: 系统中关于所兑换的礼品信息不存在
		4: 用户当前积分不足以兑换该礼品
		5： 更新医生信息失败
		6: 更新交易信息失败
	 */
	@PUT("/doctor/cashBonus/{idCardNo}")
	ResultPassHelper cashBonus(@Header("Authorization") String authorization,
                               @Path("idCardNo") String doctorId,
                               @Query("giftId") Long giftId);
	
	/**
	 * 获取医生积分
	 * @param doctorId --- 医生ID号,必填
	 * @return
	 */
	@GET("/doctor/refresh/bonusPoints/{idCardNo}")
	ResultPassHelper getDoctorBonus(@Header("Authorization") String authorization,
                                    @Path("idCardNo") String doctorId);
	
	/**
	 * 3.5 获取医生积分和设备帐户余额
	 * @param authorization
	 * @param idCard --- 医生ID号,必填
	 * @return List<ResultPassHelper>
	其中第一个元素表示状态:
	List[0]:  {name:status,value:  }
	 0: 表示成功获取积分，余额。
	 1：表示输入idCard为空
	 2：表示无法获取医生信息
	List [1]: {name: bonusPoints,value: 积分}
	List[2]: {name: userAccountBalance,value: 设备帐户余额}  注：如果获取失败，该元素在返回值中不存在
	 */
	@GET("/doctor/bonusAndDeviceBalance/{idCard}")
    List<ResultPassHelper> getBonusAndDeviceBalance(
            @Header("Authorization") String authorization,
            @Path("idCard") String idCard);

}
