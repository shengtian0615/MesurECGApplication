package com.wehealth.model.domain.model;

import com.wehealth.model.domain.enumutil.FrequencyUnit;
import com.wehealth.model.domain.interfaceutil.Entity;
import com.wehealth.model.domain.interfaceutil.TimeAuditable;

import java.util.Date;

public class ServiceItem implements Entity, TimeAuditable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String packageId;
	
	private String name;
	private String itemId;
	//0: assign once, 1: assign periodically
	private int assignType;
	//0: times, 1: during package time
	private int useType;
	//0: other Type, 1: Bonus Type
	private int itemType;
	//if itemType is 1
	private String bonusReason;
	
	//cost per time
	private double perCost;
	//assign period: 1 week, 1 month ...
	private int period;
	// week, month ...
	private FrequencyUnit unit;
	// times assigned in a period
	private int count;
	// calculated in servicePackage
	private int totalCount;
	
	private String note;
	
	private Date createTime;
	private Date updateTime;
	
	private boolean showInPublic;
	private int itemOrder = -1;
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getPackageId()
	{
		return packageId;
	}
	public void setPackageId(String packageId)
	{
		this.packageId = packageId;
	}
	public int getAssignType()
	{
		return assignType;
	}
	public void setAssignType(int assignType)
	{
		this.assignType = assignType;
	}
	public int getUseType()
	{
		return useType;
	}
	public void setUseType(int useType)
	{
		this.useType = useType;
	}
	public int getItemType()
	{
		return itemType;
	}
	public void setItemType(int itemType)
	{
		this.itemType = itemType;
	}
	public double getPerCost()
	{
		return perCost;
	}
	public void setPerCost(double perCost)
	{
		this.perCost = perCost;
	}
	public int getPeriod()
	{
		return period;
	}
	public void setPeriod(int period)
	{
		this.period = period;
	}
	public FrequencyUnit getUnit()
	{
		return unit;
	}
	public void setUnit(FrequencyUnit unit)
	{
		this.unit = unit;
	}
	public int getCount()
	{
		return count;
	}
	public void setCount(int count)
	{
		this.count = count;
	}
	public int getTotalCount()
	{
		return totalCount;
	}
	public void setTotalCount(int totalCount)
	{
		this.totalCount = totalCount;
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
	public String getBonusReason()
	{
		return bonusReason;
	}
	public void setBonusReason(String bonusReason)
	{
		this.bonusReason = bonusReason;
	}
	public String getNote()
	{
		return note;
	}
	public void setNote(String note)
	{
		this.note = note;
	}
	public boolean isShowInPublic()
	{
		return showInPublic;
	}
	public void setShowInPublic(boolean showInPublic)
	{
		this.showInPublic = showInPublic;
	}
	public String getItemId()
	{
		return itemId;
	}
	public void setItemId(String itemId)
	{
		this.itemId = itemId;
	}
	public int getItemOrder()
	{
		return itemOrder;
	}
	public void setItemOrder(int itemOrder)
	{
		this.itemOrder = itemOrder;
	}
	
	
	/**
	 * Set packageId, itemOrder, showInPublic outSide, <br>
	 * 
	 * period, unit, count, totalCount Should be set outside<br>

	 * @param old
	 * @return
	 */
	public static ServiceItem copyFrom(ServiceItem old)
	{
		ServiceItem item = new ServiceItem();
		item.setName(old.getName());
		item.setItemId(old.getItemId());
		item.setAssignType(old.getAssignType());
		item.setUseType(old.getUseType());
		item.setItemType(old.getItemType());
		item.setPerCost(old.getPerCost());
		item.setNote(old.getNote());
		
		return item;
	}
	
}
