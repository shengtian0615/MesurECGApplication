package com.wehealth.model.interfaces.inter_other;

import com.wehealth.model.domain.model.DoctorGroupMember;
import com.wehealth.model.domain.model.HeadPhoto;
import com.wehealth.model.domain.model.ResultPassHelper;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WeHealthEASEIM {

	/**
	 * 医生群组首次加载调用此方法获取所有医生昵称信息
	 * @return
	 */
	@GET("/doctorGroupChat/members/all/{groupId}")
    List<DoctorGroupMember> getAllDoctorMembers(
            @Header("Authorization") String authorization,
            @Path("groupId") String groupId,
            @Query("page") Integer page,
            @Query("pageCount") Integer pageCount);
	
	/**
	 * 批量获取医生基本信息
	 * @param ids
	 * @return
	 */
	@GET("/doctorGroupChat/members/lacked")
    List<DoctorGroupMember> getLackedMembers(
            @Header("Authorization") String authorization,
            @Query("ids") List<String> ids);
	
	/**
	 * 医生群组首次加载调用此方法获取所有医生小头像, 需要先获取总数量，然后再调用此方法分页获取
	 * @return
	 */
	@GET("/doctorGroupChat/headPhotos/{groupId}")
    List<HeadPhoto> getAllDoctorsHeadPhoto(
            @Header("Authorization") String authorization,
            @Path("groupId") String groupId,
            @Query("page") Integer page,
            @Query("pageCount") Integer pageCount);
	
	/**
	 * 医生群组首次加载调用此方法获取所有医生小头像总数量
	 * @return
	 */
	@GET("/doctorGroupChat/headPhotos/count/{groupId}")
	ResultPassHelper getDoctorsHeadPhotoCount(
            @Header("Authorization") String authorization,
            @Path("groupId") String groupId);
	
	/**
	 * 医生群组批量获取updateTime后的所有更新记录
	 * @return
	 */
	@GET("/doctorGroupChat/members/updated/{groupId}")
    List<DoctorGroupMember> getNewestRecord(
            @Header("Authorization") String authorization,
            @Path("groupId") String groupId,
            @Query("updateTime") Long updateTime);
	
	/**
	 * 批量获取医生小头像，如果医生数量太大，建议分页获取
	 * @return
	 */
	@GET("/doctorGroupChat/headPhotos/lacked")
    List<HeadPhoto> getLackedDoctorsHeadPhoto(
            @Header("Authorization") String authorization,
            @Query("ids") List<String> ids);
}
