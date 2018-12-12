package com.wehealth.model.domain.enumutil;

import com.wehealth.model.domain.interfaceutil.NamedObject;

public enum RentStatusEnum implements NamedObject
{
	pending("新租"),//出租首月，未交租
	running("出租"),//交过一次租金了
	pause("暂停"),//欠费停机
	sellout("售出"),//出租24个月后转为售出
	cancel("退租"),//退租
	change("更换"),//用户更换设备
	;
	private String text;
	
	private RentStatusEnum(String text)
	{
		this.text = text;
	}

	@Override
	public String getText()
	{
		return text;
	}
	
	public static boolean canWork(RentStatusEnum rs)
	{
		switch(rs)
		{
		case pending:
		case running:
		case sellout:
			return true;

		case pause:
		case cancel:
		case change:
		default:
			return false;
		}
	}
	
}
