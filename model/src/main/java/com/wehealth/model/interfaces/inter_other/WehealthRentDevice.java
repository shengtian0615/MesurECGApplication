package com.wehealth.model.interfaces.inter_other;

import com.wehealth.model.domain.model.RentAccountRecharge;
import com.wehealth.model.domain.model.RentDevice;
import com.wehealth.model.domain.model.RentDeviceTrace;
import com.wehealth.model.domain.model.ResultPassHelper;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WehealthRentDevice {

	@GET("/rentDevice/{serialNo}")
	RentDevice getRentDevice(@Header("Authorization") String authorization, @Path("serialNo") String serialNo);

	@POST("/rentDevice/trace")
	ResultPassHelper canRentDeviceWork(@Header("Authorization") String authorization, @Body RentDeviceTrace trace);
	
	/**
	 * Recharge for rent account by unionpay.
	 * @param idCardNo 用户的身份编号
	 * @param rentId 租用记录ID
	 * @param amount 充值金额：单位 分
	 * @return
	 */
	@GET("/rentAccountRecharge/unionpay/{idCardNo}")
	ResultPassHelper unionPayRent(@Header("Authorization") String authorization,
                                  @Path("idCardNo") String idCardNo,
                                  @Query("rentId") Long rentId,
                                  @Query("ammount") Integer amount);
	
	/**
	 * Recharge for rent account: Get wepay prepay Id.
	 * 
	 * @param idCardNo
	 * @param amount 单位为分
	 * @return
	 */
	@GET("/rentAccountRecharge/wepay/{idCardNo}")
	ResultPassHelper wepayRent(@Header("Authorization") String authorization,
                               @Path("idCardNo") String idCardNo,
                               @Query("amount") Integer amount,
                               @Query("rentId") Long rentId);
	
	/**
	 * Recharge for rent account by alipay.
	 * 
	 * @param idCardNo
	 * @param amount 单位为分
	 * @return {success:order info}, {error_occur:errorMsg}
	 */
	@GET("/rentAccountRecharge/alipay/{idCardNo}")
	ResultPassHelper alipayRent(@Header("Authorization") String authorization,
                                @Path("idCardNo") String idCardNo,
                                @Query("amount") Integer amount,
                                @Query("rentId") Long rentId);
	
	/**
	 * 查询充值明细
	 * @param rentId 出租登记ID
	 * @param page
	 * @param pageCount
	 * @return
	 */
	@GET("/rentAccountRecharge/detail/recharge/{rentId}")
    List<RentAccountRecharge> queryRecharge(@Header("Authorization") String authorization,
                                            @Path("rentId") Long rentId,
                                            @Query("page") Integer page,
                                            @Query("pageCount") Integer pageCount);
	
	/**
	 * 查询消费明细
	 * @param rentId 出租登记ID
	 * @param page
	 * @param pageCount
	 * @return
	 */
	@GET("/rentAccountRecharge/detail/cost/{rentId}")
    List<RentAccountRecharge> queryCharge(@Header("Authorization") String authorization,
										  @Path("rentId") Long rentId,
										  @Query("page") Integer page,
										  @Query("pageCount") Integer pageCount);
}
