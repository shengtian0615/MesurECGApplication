package com.wehealth.model.domain.enumutil;

import com.wehealth.model.domain.interfaceutil.NamedObject;

public enum BonusGiftType implements NamedObject {
	bonusCard("卡券"), merchandise("商品");

	private String text;

	private BonusGiftType(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}

	public static BonusGiftType getType(String type) {
		try {
			return BonusGiftType.valueOf(type);
		} catch (Exception ex) {
			return null;
		}
	}

	public String toString() {
		return getText();
	}
}
