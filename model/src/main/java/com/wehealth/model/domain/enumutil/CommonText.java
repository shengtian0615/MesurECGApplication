package com.wehealth.model.domain.enumutil;


import com.wehealth.model.domain.interfaceutil.LocalizedNamedObject;

public enum CommonText implements LocalizedNamedObject
{
	LAST_MODIFY("Last Modified", "CommonText.LAST_MODIFY"),
	MALE("Male", "CommonText.MALE"),
	FEMALE("FeMale", "CommonText.FEMALE"),
	PATIENT("Patient", "CommonText.PATIENT"),
	DOCTOR("Doctor", "CommonText.DOCTOR"),
	Prefer_Doctor("Prefer Doctor", "CommonText.Prefer_doctor"),
	MINUTE("Minute", "CommonText.MINUTE"),
	HOUR("Hour", "CommonText.Hour"),
	DAY("Day", "CommonText.DAY"),
	MONTH("Month", "CommonText.MONTH"),
	YEAR("Year", "CommonText.YEAR"),
	WEEK("Week", "CommonText.WEEK"),
	REGISTERED_USER("Registered User", "CommonText.REGISTERED_USER"),
	COMMON_ROLE("Common Role", "CommonText.COMMON_ROLE"),
	
	NAME("Name", "CommonText.NAME"),
	AGE("Age", "CommonText.AGE"),
	GENDER("Gender", "CommonText.GENDER"),
	IDCARD("ID", "CommonText.ID"),
	PHONE("Phone", "CommonText.PHONE"),
	STATUS("Status", "CommonText.STATUS"),
	BIRTHDAY("Birthday", "CommonText.BIRTHDAY"),
	HEIGHT("Height", "CommonText.HEIGHT"),
	WEIGHT("Weight", "CommonText.WEIGHT"),
	BLOOD_TYPE("Blood Type", "CommonText.BLOOD_TYPE"),
	NICKNAME("Nickname", "CommonText.NICKNAME"),
	HEART_RATE("Heart Rate", "CommonText.HEART_RATE"),
	BLOOD_HIGH("Blood Pressure High", "CommonText.BLOOD_HIGH"),
	BLOOD_LOW("Blood Pressure Low", "CommonText.BLOOD_LOW"),
	
	ENABLE("Enable", "CommonText.ENABLE"),
	USERNAME("Username", "CommonText.USERNAME"),
	PASSWORD("Password", "CommonText.PASSWORD"),
	CONFIRM_PASSWORD("Confirm Password", "CommonText.CONFIRM_PASSWORD"),
	CHANGE_PASSWORD("Change Password", "CommonText.CHANGE_PASSWORD"),
	
	EMAIL("Email", "CommonText.EMAIL"),
	ADDRESS("Address", "CommonText.ADDRESS"),
	ICE("ICE", "CommonText.ICE"),
	RELATION("Relationship", "CommonText.RELATION"),
	DESCRIPTION("Description", "CommonText.DESCRIPTION"),
	
	CREATE_TIME("Create Time", "CommonText.CREATE_TIME"),
	UPDATE_TIME("Update_Time", "CommonText.UPDATE_TIME"),
	DEVICE("Device", "CommonText.DEVICE"),
	
	ACTION("Action", "CommonText.ACTION"),
	FILL_ALL("Please fill in all the required field", "CommonText.FILL_ALL"),
	ARG0_FORMAT_ERROR("{0} Format Error", "CommonText.ARG0_FORMAT_ERROR"),
	PASS_NOT_EQUAL("Two passwords are not equal", "CommonText.PASS_NOT_EQUAL"),
	ARG0_SHOULD_NUMBER("{0} should be integer", "CommonText.ARG0_SHOULD_NUMBER"),
	ARG0_EXISTS("{0} exists", "CommonText.ARG0_EXISTS"),
	
	ERROR_OCCUR("Error Occur", "CommonText.Error_Occur"),
	YES("Yes", "CommonText.YES"),
	NO("No", "CommonText.NO"),
	
	CHECK("Check", "CommonText.CHECK"),
	HOSPITAL("Hospital", "CommonText.HOSPITAL"),
	ETHNICITY("Ethnicity", "CommonText.ETHNICITY"),
	CAREER("Career", "CommonText.CAREER"),
	
	DELETE_ALERT("Are you sure", "CommonText.DELETE_ALERT"),
	
	SYSTEM("System", "CommonText.SYSTEM"),
	
	DayStart("Start Day", "CommonText.DayStart"),
	DayEnd("End Day", "CommonText.DayEnd"),
	
	InfoTimeOut("This information has time out.", "CommonText.InfoTimeOut"),
	InputNumber("Input Number", "CommonText.InputNumber"), 
	
	;
	
	private String _text;
	private String _key;
	
	
	private CommonText(String text, String key)
	{
		_text = text;
		_key = key;
	}
	
	@Override
	public String getText()
	{
		return _text;
	}

	@Override
	public String getBundleName()
	{
		return "com.wehealth.shared.datamodel.util.messages";
	}

	@Override
	public String getKey()
	{
		return _key;
	}

}
