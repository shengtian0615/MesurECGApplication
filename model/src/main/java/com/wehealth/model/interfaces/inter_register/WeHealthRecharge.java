package com.wehealth.model.interfaces.inter_register;


import com.wehealth.model.domain.model.ResultPassHelper;

import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;
import retrofit.http.Query;


public interface WeHealthRecharge {
    @GET("/recharge/unionpay/registeredUser/{idCardNo}")
	ResultPassHelper getUnionTransactionNo(@Header("Authorization") String authorization,
										   @Path("idCardNo") String idCardNo, @Query("ammount") int amount);
    
    @GET("/recharge/unionpay/new1/registeredUser/{idCardNo}")
	ResultPassHelper getUnionTransactionNo_new1(@Header("Authorization") String authorization,
                                                @Path("idCardNo") String idCardNo, @Query("ammount") int amount);
    
    /**
     * 银联充值接口
	 * @param idCardNo
	 * @param amount 单位 分
	 * @param forCR，商业出租设备充值余额
	 * @return
	 */
    @GET("/recharge/unionpay/new1/registeredUser/{idCardNo}")
	ResultPassHelper getUnionTransactionNo_new1(
            @Header("Authorization") String authorization,
            @Path("idCardNo") String idCardNo,
            @Query("ammount") int amount,
            @Query("forCR") Boolean forCR);
    
    /**
	 * 客户端可以根据公司设置CompanySettingConstant.bankCardRealnameCheck 来变化实名认证type.
	 * 
	 * 
	 * @param type 2：姓名&银行卡号，3：姓名&银行卡号&身份证号，4：姓名&银行卡号&身份证号&预留手机号
	 * @param name 户名, required
	 * @param bankCardNo 银行卡号, required
	 * @param idCardNo 身份证号, if type >=3, required
	 * @param cellPhone 手机号，如果调用4要素接口，可以先向此手机号发验证码，验证通过后再调用此接口。, if type == 4, required
	 * @return 当passer.name是success时，表示验证成功，value里的值，pass为信息一致，fail为信息不一致；
	 * 			passer.name为其他时验证不成功，value里反映了失败原因。
	 */
    @GET("/bankCard/check")
	ResultPassHelper checkBankCard(@Header("Authorization") String authorization,
                                   @Query("type") Integer type,
                                   @Query("name") String name,
                                   @Query("bankCardNo") String bankCardNo,
                                   @Query("idCardNo") String idCardNo,
                                   @Query("cellPhone") String cellPhone);
    
    /**
   	 * 微信充值接口
   	 * @param idCardNo
   	 * @param amount 单位为分
   	 * @param desc 充值类型 0：找心电专家-服务帐户充值，1：测心电-服务帐户充值，2测心电-账户充值
   	 * @return
   	 */
       @GET("/recharge/wepay/registeredUser/{idCardNo}")
   	ResultPassHelper getWepayPrepayId(
               @Header("Authorization") String authorization,
               @Path("idCardNo") String idCardNo,
               @Query("amount") Integer amount,
               @Query("desc") Integer desc);
       
       /**
   	 * 支付宝充值接口
   	 * @param idCardNo
   	 * @param amount 单位为分
   	 * @param desc 充值类型 0：找心电专家-服务帐户充值，1：测心电-服务帐户充值，2测心电-账户充值
   	 * @return {success:order info}, {error_occur:errorMsg}
   	 */
   	@GET("/recharge/alipay/registeredUser/{idCardNo}")
   	ResultPassHelper getAlipayOrder(
            @Header("Authorization") String authorization,
            @Path("idCardNo") String idCardNo,
            @Query("amount") Integer amount,
            @Query("desc") Integer desc);
   	
   	/**
	 * 微信充值接口
	 * @param idCardNo
	 * @param amount 单位为分
	 * @param forCR 商业出租设备充值余额
	 * @return
	 */
    @GET("/recharge/wepay/registeredUser/{idCardNo}")
	ResultPassHelper getWepayPrepayId(
            @Header("Authorization") String authorization,
            @Path("idCardNo") String idCardNo,
            @Query("amount") Integer amount,
            @Query("forCR") Boolean forCR,
            @Query("desc") Integer desc);
    
    /**
	 * 支付宝充值接口
	 * @param idCardNo
	 * @param amount 单位为分
	 * @param forCR 商业出租设备充值余额
	 * @return {success:order info}, {error_occur:errorMsg}
	 */
	@GET("/recharge/alipay/registeredUser/{idCardNo}")
	public ResultPassHelper getAlipayOrder(
            @Header("Authorization") String authorization,
            @Path("idCardNo") String idCardNo,
            @Query("amount") Integer amount,
            @Query("forCR") Boolean forCR,
            @Query("desc") Integer desc);
}

