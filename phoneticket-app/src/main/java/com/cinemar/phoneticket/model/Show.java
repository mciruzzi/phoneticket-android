package com.cinemar.phoneticket.model;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

public class Show {
	String showId;
	Calendar startTime;
	String startTimeString;	

	public Show(String showId, Calendar startTime) {
		super();
		this.showId = showId;
		this.startTime = startTime;		
	}
	
	public Show(JSONObject showObject) throws JSONException {
		super();
		
		this.showId = showObject.getString("id");
		this.startTimeString = showObject.getString("starts_at");
		//TODO How to parse string into a date Calendar		
		//TODO Tambien bajar la sala/room donde se proyecta el show vienw bajo el showObject.getJSONObject("room")
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
		
	public String getStartTimeString() {
		return startTimeString;
	}

	public void setStartTimeString(String startTimeString) {
		this.startTimeString = startTimeString;
	}




}
