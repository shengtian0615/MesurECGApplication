package com.wehealth.mesurecg.service;

import com.wehealth.mesurecg.dao.AppNotificationMessageDao;
import com.wehealth.mesurecg.utils.PreferUtils;
import com.wehealth.model.domain.model.AppNotificationMessage;

import java.util.Observable;

public class BaseNotifyObserver extends Observable{

	private AppNotificationMessage appNotifyMessage;
//	private Order order;
	private String UserInfo;
	
	private static BaseNotifyObserver instance;
	public static BaseNotifyObserver getInstance(){
		if (instance == null) {
			instance = new BaseNotifyObserver();
		}
		return instance;
	}
	/**
	 * 当有新消息来的时候  先保存然后根据是否是离线来设置Observer
	 * @param msg
	 */
	public void setAppNotifyMsg(AppNotificationMessage msg){
		this.appNotifyMessage = msg;
		AppNotificationMessageDao.getAppInstance(PreferUtils.getIntance().getIdCardNo()).saveMessage(appNotifyMessage);
		this.setChanged();
		this.notifyObservers(appNotifyMessage);
	}
	
	public AppNotificationMessage getAppNotifyMsg(){
		return appNotifyMessage;
	}
	
	/**
	 * 当有新消息来的时候  先保存然后根据是否是离线来设置Observer
	 * @param info 设为true时为即时消息，否则为离线消息处理
	 */
	public void setAppUserInfoString(String info){
		this.UserInfo = info;
		this.setChanged();
		this.notifyObservers(UserInfo);
	}
	
	public String getAppUserInfoString(){
		return UserInfo;
	}
	
//	public void setOrderNotify(Order o){
//		this.order = o;
//		this.setChanged();
//		this.notifyObservers(order);
//	}
//	
//	public Order getOrderNotify(){
//		return order;
//	}
}
