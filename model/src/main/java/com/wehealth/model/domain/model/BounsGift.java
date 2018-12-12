package com.wehealth.model.domain.model;

import com.wehealth.model.domain.enumutil.BonusGiftType;

import java.io.Serializable;

/**
 * BonusGift用来建模积分兑换的礼物. 其中BonusGiftType表述礼物类型,可以是卡券或者是实物商品.
 * 如果礼物是卡券merchandiseId 保存卡券项目ID, 否则是商品ID. 这期只能选卡券. Expired表明该礼物是否已过期.
 */
public class BounsGift implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private byte[] photo;
	private String name;
	private String descr;
	private Integer priceByPoints;
	private BonusGiftType type;
	// if gift is bonus card, it will save BonusReason's id
	// if gift is merchandised, it will save merchandise's id
	private String merchandiseId;
	private boolean expired = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Integer getPriceByPoints() {
		return priceByPoints;
	}

	public void setPriceByPoints(Integer priceByPoints) {
		this.priceByPoints = priceByPoints;
	}

	public BonusGiftType getType() {
		return type;
	}

	public void setType(BonusGiftType type) {
		this.type = type;
	}

	public String getMerchandiseId() {
		return merchandiseId;
	}

	public void setMerchandiseId(String merchandiseId) {
		this.merchandiseId = merchandiseId;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}
}
