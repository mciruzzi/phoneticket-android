package com.cinemar.phoneticket.reserveandbuy;

import android.content.Intent;

import com.cinemar.phoneticket.model.ItemOperation;

public class OperationView {

	private ItemOperation item;
	private Class<?> activityClass;
	
	public OperationView(ItemOperation item, Class<?> activity) {
		
		setItem(item);
		setActivityClass(activity);
	}

	public void setInformationToIntent(Intent intent) {

		intent.putExtra(OperationConstants.TITLE, item.getTitle());
		intent.putExtra(OperationConstants.CINEMA, item.getCinema());
		intent.putExtra(OperationConstants.DATE, item.getDateToString());
		intent.putExtra(OperationConstants.SEATING, item.getSeatingToString());
		intent.putExtra(OperationConstants.TICKETS_TYPE, item.getTicketsType());
		intent.putExtra(OperationConstants.CODE, item.getCode());
		intent.putExtra(OperationConstants.SHARE_URL, item.getShareUrl());
		intent.putExtra(OperationConstants.SCHEDULABLE_DATE, item.getDate().getTime());
		intent.putExtra(OperationConstants.)
	}
	
	public ItemOperation getItem() {
		return item;
	}

	private void setItem(ItemOperation item) {
		this.item = item;
	}

	public Class<?> getActivityClass() {
		return activityClass;
	}

	private void setActivityClass(Class<?> activityClass) {
		this.activityClass = activityClass;
	}
} 