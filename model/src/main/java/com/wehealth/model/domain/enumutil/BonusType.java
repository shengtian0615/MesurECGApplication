/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Sep 1, 2016
 */

package com.wehealth.model.domain.enumutil;


import com.wehealth.model.domain.interfaceutil.NamedObject;

public enum BonusType implements NamedObject {
	
	free_emg_check("免费紧急复核(次)", 0), 
	free_regular_check("免费常规复核(次)", 0),
	free_chat("免费咨询(次)", 0),
	bonus_money("代金券(元)", 1), ;

	private String text;
	// 0: 次 1: 元
	private int unit;

	private BonusType(String text, int unit) {
		this.text = text;
		this.unit = unit;
	}

	@Override
	public String getText() {
		return text;
	}

	/**
	 * 0: 次 1: 元
	 */
	public int getUnit() {
		return unit;
	}

	public static BonusType getType(String type) {
		try {
			return BonusType.valueOf(type);
		} catch (Exception ex) {
			return null;
		}
	}

	public String getUnitString() {
		return getUnit() == 0 ? "次" : "元";
	}
}
