package com.wehealth.model.domain.model;

import java.io.Serializable;

public class AdvertisementInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private byte[] coverPic;
	private String htmlCode;
	private float price;
	private String customer;
	
	public AdvertisementInfo() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getCoverPic() {
		return coverPic;
	}

	public void setCoverPic(byte[] coverPic) {
		this.coverPic = coverPic;
	}

	public String getHtmlCode() {
		return htmlCode;
	}

	public void setHtmlCode(String htmlCode) {
		this.htmlCode = htmlCode;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
}
