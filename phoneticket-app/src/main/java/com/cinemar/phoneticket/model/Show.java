package com.cinemar.phoneticket.model;

import java.util.Calendar;

import org.json.JSONObject;

public class Show {
	String showId;
	Calendar startTime;
	
	
	
	public Show(String showId, Calendar startTime) {
		super();
		this.showId = showId;
		this.startTime = startTime;		
	}
	
	public Show(JSONObject showObject) {
		super();
		
		//TODO
		
		
	}
	
		
	public String getShowId() {
		return showId;
	}

	public void setShowId(String showId) {
		this.showId = showId;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}



}
