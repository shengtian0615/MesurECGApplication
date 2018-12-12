package com.wehealth.model.domain.model;

import com.wehealth.model.domain.enumutil.RentStatusEnum;
import com.wehealth.model.domain.interfaceutil.Entity;
import com.wehealth.model.domain.interfaceutil.TimeAuditable;

import java.util.Date;

public class RentDevice implements TimeAuditable, Entity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private RentStatusEnum status = RentStatusEnum.pending;
	private double balance;//租金账户余额
	private double unSettle;//未结算金额，正表示欠费金额
	
	private Date rentDate;//起租日
	private String serialNo;//设备序列号
	
	private String idCard;
	private String name;
	private String tel;
	
	private String note;
	private Date createTime;
	private Date updateTime;
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public RentStatusEnum getStatus()
	{
		return status;
	}
	public void setStatus(RentStatusEnum status)
	{
		this.status = status;
	}
	public double getBalance()
	{
		return balance;
	}
	public void setBalance(double balance)
	{
		this.balance = balance;
	}
	public double getUnSettle()
	{
		return unSettle;
	}
	public void setUnSettle(double unSettle)
	{
		this.unSettle = unSettle;
	}
	public Date getRentDate()
	{
		return rentDate;
	}
	public void setRentDate(Date rentDate)
	{
		this.rentDate = rentDate;
	}
	public String getSerialNo()
	{
		return serialNo;
	}
	public void setSerialNo(String serialNo)
	{
		this.serialNo = serialNo;
	}
	public String getIdCard()
	{
		return idCard;
	}
	public void setIdCard(String idCard)
	{
		this.idCard = idCard;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getTel()
	{
		return tel;
	}
	public void setTel(String tel)
	{
		this.tel = tel;
	}
	public String getNote()
	{
		return note;
	}
	public void setNote(String note)
	{
		this.note = note;
	}
	public Date getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
	public Date getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}
	
	public String describe()
	{
		return "RentDevice [status=" + status + ", balance=" + balance + ", unSettle=" + unSettle + ", rentDate="
				+ rentDate + ", serialNo=" + serialNo + ", idCard=" + idCard + ", name=" + name + ", tel=" + tel
				+ ", note=" + note + "]";
	}
}
