package com.wehealth.model.interfaces.inter_other;

import com.wehealth.model.domain.model.CompanySetting;
import com.wehealth.model.domain.model.ResultPassHelper;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;
import retrofit.http.Query;

public interface WeHealthCompanySet {

	@GET("/companySetting/query/shortName/{shortName}")
	public CompanySetting getByShortName(@Header("Authorization") String authorization, @Path("shortName") String shortName);
	
	@GET("/companySetting/query/shortNames")
	public List<CompanySetting> getByShortNames(@Header("Authorization") String authorization, @Query("shortNames") List<String> shortNames);
	
	/**
	 * Name: serviceType, Value: serviceName=serviceFee. such as "紧急复核=20"
	 * serviceType复核类型: emergency, regular, free
	 * serivceName可能由两部分组成通过加号分隔， 例如 "首选医生复核+24小时复核"
	 * @return such as {name:regular,value:首选医生复核+24小时复核=20}
	 */
	@GET("/companySetting/feeService")
	public List<ResultPassHelper> getChargeService(@Header("Authorization") String authorization);
}
