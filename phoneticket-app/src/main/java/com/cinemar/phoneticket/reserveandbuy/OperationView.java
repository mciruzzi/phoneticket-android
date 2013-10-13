package com.cinemar.phoneticket.reserveandbuy;

import android.content.Intent;

import com.cinemar.phoneticket.model.ItemOperation;

public class OperationView {

	private ItemOperation item;
	private Class<?> activityClass;
	private int idOperation;
	
	public OperationView(ItemOperation item, Class<?> activity, int idOperation) {
		
		setItem(item);
		setActivityClass(activity);
		setIdOperation(idOperation);
	}

	public void setInformationToIntent(Intent intent) {

		intent.putExtra(OperationConstants.TITLE, item.getTitle());
		intent.putExtra(OperationConstants.CINEMA, item.getCinema());
		intent.putExtra(OperationConstants.DATE, item.getDateToString());
		intent.putExtra(OperationConstants.SEATING, item.getSeatingToString());
		intent.putExtra(OperationConstants.TICKETS_TYPE, item.getTicketsType());
		intent.putExtra(OperationConstants.CODE, item.getCode());
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

	public int getIdOperation() {
		return idOperation;
	}

	private void setIdOperation(int idOperation) {
		this.idOperation = idOperation;
	}
} 