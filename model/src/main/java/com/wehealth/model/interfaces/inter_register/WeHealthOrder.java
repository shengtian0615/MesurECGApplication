package com.wehealth.model.interfaces.inter_register;

import com.wehealth.model.domain.model.Order;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WeHealthOrder {
    @GET("/order/registeredUser/{idCardNo}")
    OrderList getOrdersByRegisteredUserId(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo);
    
    @GET("/order")
    OrderList getAllOrder(@Header("Authorization") String authorization);
    
    @POST("/order")
 	public Order createOrder(@Header("Authorization") String authorization, @Body Order patient);
    
    @PUT("/order")
    Order updateOrder(@Header("Authorization") String authorization, @Body Order order);
 
    @GET("/order/doctor/open/{idCardNo}")
    OrderList getOpenOrdersByDorctorId(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo);
    
    @GET("/order/doctor/closed/{idCardNo}")
    OrderList getClosedOrdersByDorctorId(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo);
    
    @GET("/order/history/comments/{idCardNo}")
    OrderCommentList getMyOrderCommentHelper(@Header("Authorization") String authorization, @Path("idCardNo") String idCardNo, @Query("page") int page, @Query("count") int count);
    
    /**
	 * status和completed条件不要同时存在。
	 */
	@GET("/order/query")
	OrderList query(@Header("Authorization") String authorization,
                    @Query("registeredUserId") String regId,
                    @Query("patientId") String patientId,
                    @Query("doctorId") String doctorId,
                    @Query("statisfy") Boolean satisfy,
                    @Query("status") Integer status,
                    @Query("completed") Boolean completed,
                    @Query("startDay") Long startDay, @Query("endDay") Long endDay,
                    @Query("page") Integer page, @Query("pageCount") Integer pageCount);
}

