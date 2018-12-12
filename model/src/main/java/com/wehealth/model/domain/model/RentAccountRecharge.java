package com.wehealth.model.domain.model;

import com.wehealth.model.domain.enumutil.CommonStatus;
import com.wehealth.model.domain.interfaceutil.Entity;
import com.wehealth.model.domain.interfaceutil.TimeAuditable;

import java.util.Date;

public class RentAccountRecharge implements TimeAuditable, Entity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private int type=0;//0, 充值，1，消费，2：退费
	
	private double amount;
	private double balance;

	private String fromUser;
	private Long rentId;
	
	private String orderId;
	private UserChargePath chargePath;

	/** Use: pending, completed, error */
	private CommonStatus status = CommonStatus.pending;
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
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	public double getAmount()
	{
		return amount;
	}
	public void setAmount(double amount)
	{
		this.amount = amount;
	}
	public double getBalance()
	{
		return balance;
	}
	public void setBalance(double balance)
	{
		this.balance = balance;
	}
	public String getFromUser()
	{
		return fromUser;
	}
	public void setFromUser(String fromUser)
	{
		this.fromUser = fromUser;
	}
	public Long getRentId()
	{
		return rentId;
	}
	public void setRentId(Long rentId)
	{
		this.rentId = rentId;
	}
	public String getOrderId()
	{
		return orderId;
	}
	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}
	public UserChargePath getChargePath()
	{
		return chargePath;
	}
	public void setChargePath(UserChargePath chargePath)
	{
		this.chargePath = chargePath;
	}
	public CommonStatus getStatus()
	{
		return status;
	}
	public void setStatus(CommonStatus status)
	{
		this.status = status;
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
	
}
