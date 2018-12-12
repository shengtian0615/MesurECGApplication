/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Apr 26, 2016
 */

package com.wehealth.model.domain.enumutil;

import com.wehealth.model.domain.interfaceutil.NamedObject;

/**
 *	the name() is CompanySetting's shortName
 */
public enum CompanySettingConstant implements NamedObject
{
	recommandDoctorNeedOnline("推荐医生需要在线", CompanySettingType.Boolean, "true"),
	exceptDoctorAccount("需要过滤的医生账号", CompanySettingType.String, "北京五维康医院"),
	
	emergencyCheckSwitch("紧急复核开关", CompanySettingType.Boolean, "false"),
	emergencyCheckName("紧急复核名称",CompanySettingType.String, "紧急复核"),
	emergencyCheckFee("紧急复核费用", CompanySettingType.Double, "50"),
	emergencyCheckFeeRate("紧急复核平台费率", CompanySettingType.Double, "0.5"),

	regularCheckSwitch("常规复核开关", CompanySettingType.Boolean, "true"),
	regularCheckName("常规复核名称", CompanySettingType.String, "首选医生复核"),
	regularCheckFee("常规复核费用", CompanySettingType.Double, "20"),
	regularCheckFeeRate("常规复核平台费率", CompanySettingType.Double, "0.5"),

	freeCheckSwitch("免费复核开关", CompanySettingType.Boolean, "true"),
	freeCheckName("免费复核名称", CompanySettingType.String, "专职医生复核"),
	freeCheckFee("免费复核费用", CompanySettingType.Double, "2"),
	freeCheckFeeRate("免费复核平台费率", CompanySettingType.Double, "0"),
	
//	emergencyCheckFee("紧急复核费用", CompanySettingType.Double, "50"),
//	emergencyCheckFeeRate("紧急复核平台费率", CompanySettingType.Double, "0.5"),
//	regularCheckFee("常规复核费用", CompanySettingType.Double, "20"),
//	regularCheckFeeRate("常规复核平台费率", CompanySettingType.Double, "0.5"),
	
	chatWithDoctorFee("医生咨询费", CompanySettingType.Double, "50"),
	chatWithDoctorFeeRate("医生咨询平台费率", CompanySettingType.Double, "0.4"),
	
	smsCardMonthFee("物联网卡月租费", CompanySettingType.Double, "15"),
	smsCardMonthTopLimit("物联网卡月流量上限", CompanySettingType.Integer, "100"),
	
	smsCardMonthFeeAutoCharge("物联网卡月费自动扣费", CompanySettingType.Boolean, "false"),
	
	//值为2，3，4表示验证的元素种类，如果enable=false表示不需要实名认证。
	bankCardRealnameCheck("银行卡实名认证", CompanySettingType.Integer, "0"),
	doctorAccountSettleFileBase("医生结算文件夹", CompanySettingType.String, ""),
	
	//紧急/常规，咨询等采用预扣费，事件正常结束则完成扣费，事件失败则退费
	preChargeMode("预扣费模式", CompanySettingType.Boolean, "false"),
	
	//运维系统中关闭常规复核，给注册用户发消息的内容
	closeRegularTitle("关闭常规复核通知标题", CompanySettingType.String, "常规复核关闭"),
	closeRegularContent("关闭常规复核通知内容", CompanySettingType.String, "用户您好，很抱歉，因您的医生长时间未回复您的常规复核请求，本次常规复核被后台关闭。"),
	
	chinapayEnable("银联支付开关", CompanySettingType.Boolean, "true"),
	wepayEnable("微信支付开关", CompanySettingType.Boolean, "true"),
	alipayEnable("支付宝支付开关", CompanySettingType.Boolean, "true"),
	
	newRegisteredUserAlertPhone("客服接收短信通知号码", CompanySettingType.String, "13001201476"),
	invoiceInfo("发票提示信息",CompanySettingType.String,"如果需要发票，请联系客服索取。<br>电话：010-85170997"),
	
	forcePosition("商业版出租设备强制定位", CompanySettingType.Boolean, "true"),
	rentDeviceNotWork("出租设备暂停提示", CompanySettingType.String, "您的设备已经暂停使用，请确认是否欠租。"),
	rechargeBonusForCR("商业出租设备充值赠送百分比", CompanySettingType.Double, "0", 
			"在商业版出租设备上充值有赠送，这里设置的是赠送的比率，如果设置为1，则充多少送多少。"),
	rentDeviceMonthFee("商业出租设备月租", CompanySettingType.Double, "300",
			"商业版出租设备的月租金。修改租金数字每月1号生效，1号结算上月租金。"),
	doctorCompletedRegistrationBonus("医生注册完善信息积分奖励",   CompanySettingType.Integer, "100"),
	;

	private String text;
	private CompanySettingType type;
	private String defaultVal;
	private String note;
	
	
	private CompanySettingConstant(String text, CompanySettingType type, String defaultVal)
	{
		this.text = text;
		this.type = type;
		this.defaultVal = defaultVal;
	}
	
	private CompanySettingConstant(String text, CompanySettingType type, String defVal, String note)
	{
		this(text, type,defVal);
		this.note = note;
	}
	
	@Override
	public String getText()
	{
		return text;
	}
	
	public CompanySettingType getType()
	{
		return type;
	}
	
	public String getDefaultVal()
	{
		return defaultVal;
	}
	
	public String getNote()
	{
		return note;
	}
}
