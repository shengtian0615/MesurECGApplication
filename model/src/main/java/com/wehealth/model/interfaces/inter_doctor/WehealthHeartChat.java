package com.wehealth.model.interfaces.inter_doctor;

import com.wehealth.model.domain.model.HeartChatGroup;
import com.wehealth.model.domain.model.ResultPassHelper;
import com.wehealth.model.domain.model.ScannedUser;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WehealthHeartChat {
//	/**
//	 * 用户注册
//	 * @param idCardNo
//	 * @param deviceSerial
//	 * @return 返回值：{success:注册成功},{error_occur:失败消息}
//	 * 失败原因：a. 需要先在测心电软件注册  b. 用户未绑定设备  c. 序列号错误，或用户绑定了其他设备   d. 已经注册过，可以直接登录
//	 * 注意：用户需要先注册测心电家用版，并绑定设备。测心电是一定要注册的，所以采取这样的流程。用户也不需要在此软件重新填一遍详细信息。
//	 */
//	@GET("/heartChatGroup/reg")
//	ResultPassHelper register(@Header("Authorization") String authorization, 
//			@Query("idCardNo") String idCardNo,
//			@Query("deviceSerial") String deviceSerial);

	/**
	 * 接口前要调用一下本接口。根据软件变更需求20180206-4.1的要求，RegisteredUser如果没绑定设备，没有错误。
	 * @param idCardNo
	 * @return 返回值：{success:null}，可以登录，{error_occur: error msg},不能登录
	 */
	@GET("/heartChatGroup/checkLogin/{idCardNo}")
	ResultPassHelper loginCheck(@Header("Authorization") String authorization,
								@Path("idCardNo") String idCardNo);

	/**
	 * 参数解释：helper.name存放userId, helper.value存放docNote
		docNote保存在HeartChatGroup里，是一个新的属性。
	 * @return
	 */
	@PUT("/heartChatGroup/docNote")
	ResultPassHelper setDocNote(@Header("Authorization") String authorization,
                                @Body ResultPassHelper helper);

	/**
	 * 用户登录
	 * @param idCardNo
	 * @return
	 * 患者端登录流程：
		a. 用户名密码获取Token
		b. 调用本接口获取群组，如果获取不到，表示没注册，获取到了表示登录成功。
		c. 根据获取到的群组记录，判断当前状态，查看HeartChatGroup的解释。
	 */
	@GET("/heartChatGroup/userLogin/{idCardNo}")
	HeartChatGroup groupLogin(@Header("Authorization") String authorization,
							  @Path("idCardNo") String idCardNo);

	/**
	 * 查询List<HeartChatGroup>
	 * @param authorization
	 * @param userId
	 * @param seatingId
	 * @param doctorId
	 * @param groupName
	 * @param userEase
	 * @param seatingEase
	 * @param docEase
	 * @param groupId
	 * @param pacId
	 * @param timeOver
	 * @param page
	 * @param pageCount
	 * @return 
	 * 医助查询患者列表：seatingId
	 * 医生查询患者列表：doctorId
		分页：page, pageCount
		其他参数是运维系统查询条件。
	 */
	@GET("/heartChatGroup/query")
    List<HeartChatGroup> query(@Header("Authorization") String authorization,
                               @Query("userId") String userId,
                               @Query("seatingId") Long seatingId,
                               @Query("doctorId") String doctorId,
                               @Query("groupName") String groupName,
                               @Query("userEase") String userEase,
                               @Query("seatingEase") String seatingEase,
                               @Query("docEase") String docEase,
                               @Query("groupId") String groupId,
                               @Query("pacId") Long pacId,
                               @Query("timeOver") Boolean timeOver,
                               @Query("page") Integer page,
                               @Query("pageCount") Integer pageCount);
	/**
	 * create user by doctor
	 * @param su
	 * @return返回值：
		成功则返回｛name:success, value:groupId(HeartChatGroup.groupId)｝
		可以使用groupId到后台获取新的HeartChatGroup, 调用4.6接口。
		环信消息：成功同时后台也会发4.1接口同样的消息，医生端也可以从此环信消息中提取groupId，到后台获取。

		错误返回{name:error_occur, value:error message}
	 */
	@POST("/heartChatGroup/cubd")
	ResultPassHelper doctorCreateUser(@Header("Authorization") String authorization, @Body ScannedUser su);
	
	/**
	 * 使用环信groupId获取HeartChatGroup
		适用于医生、医助根据消息中的groupId，获取新创建的group。
	 */
	@GET("/heartChatGroup/group/{groupId}")
	HeartChatGroup getGroup(@Header("Authorization") String authorization, @Path("groupId") String groupId);
}
