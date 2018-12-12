package com.wehealth.model.interfaces.inter_other;

import com.wehealth.model.domain.model.Bonus;
import com.wehealth.model.domain.model.OrderAddress;
import com.wehealth.model.domain.model.ResultPassHelper;
import com.wehealth.model.domain.model.ServiceItem;
import com.wehealth.model.domain.model.ServicePackage;
import com.wehealth.model.domain.model.UserServicePackage;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WeHealthBonus {

	/**
	 * 查找在有效期内的，还有剩余次数的卡券
	 * @param forUserId, regUser idCardNo
	 * @param bonusType
	 * 
	 * @return
	 */
	@GET("/bonus/query/available/{forUserId}")
    List<Bonus> queryAvailableBonus(
            @Header("Authorization") String authorization,
            @Path("forUserId") String forUserId,
            @Query("bonusType") String bonusType);
	
	/**
	 * 1. Query "can use": forUserId, expired=false, usedUp=false;
	 * 2. Query "usedUp": forUserId, usedUp=true;
	 * 3. Query "expired": forUserId, expired= true, usedUp=false;
	 * 
	 * @param forUserId, RegisteredUser idCardNo
	 * @param type, BonusType
	 * @param bonusReason, BonusReason reasonId
	 * @param expireBefore, expired before this time
	 * @param expired, true means currentTime > expireTime
	 * @param usedUp, true means times = 0
	 * @param page
	 * @param pageCount
	 * @return
	 */
	@GET("/bonus/query")
    List<Bonus> queryBonus(
            @Header("Authorization") String authorization,
            @Query("forUserId") String forUserId,
            @Query("type") String type,
            @Query("bonusReason") String bonusReason,
            @Query("expireBefore") Long expireBefore,
            @Query("expired") Boolean expired,
            @Query("usedUp") Boolean usedUp,
            @Query("page") Integer page,
            @Query("pageCount") Integer pageCount);
	
	@GET("/servicePackage/pac/available")
    List<ServicePackage> queryAvailableServicePackage(@Header("Authorization") String authorization);
	
	@GET("/servicePackage/items/{packageId}")
    List<ServiceItem> queryPackageItems(
            @Header("Authorization") String authorization,
            @Path("packageId") String packageId);
	
	@GET("/servicePackage/up/{regUserIdCardNo}")
    List<UserServicePackage> getPackage(
            @Header("Authorization") String authorization,
            @Path("regUserIdCardNo") String forUserId,
            @Query("expired") Boolean expired);
	
	@POST("/servicePackage/package/buy")
	ResultPassHelper buyPackage(
            @Header("Authorization") String authorization,
            @Body UserServicePackage userPac);
	
	/**
	 * @param address
	 * @return success:new id, error_occur/failed:error_msg/failed_msg
	 */
	@POST("/orderRelated/address")
	ResultPassHelper createOrderAddress(
            @Header("Authorization") String authorization,
            @Body OrderAddress address);
	
	@PUT("/orderRelated/address")
	ResultPassHelper updateOrderAddress(
            @Header("Authorization") String authorization,
            @Body OrderAddress address);
	
	@GET("/orderRelated/address/{userId}")
    List<OrderAddress> getUserAddresses(
            @Header("Authorization") String authorization,
            @Path("userId") String userId);
	
	@GET("/orderRelated/address/def/{userId}")
	OrderAddress getUserDefaultAddress(
            @Header("Authorization") String authorization,
            @Path("userId") String userId);
	@DELETE("/orderRelated/address/{id}")
	public ResultPassHelper deleteUserAddress(
            @Header("Authorization") String authorization,
            @Path("id") Long id);
}
