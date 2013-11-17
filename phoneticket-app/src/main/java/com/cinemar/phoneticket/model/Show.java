package com.cinemar.phoneticket.model;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.cinemar.phoneticket.util.UIDateUtil;

public class Show {
	private String showId;
	private Date startTime;
	private String startTimeString;	
	private boolean isNumbered = true; //TODO change para tomarlo en el constructor

	public Show(String showId) {
		super();
		this.showId = showId;
	}
	
	public Show(JSONObject showObject) throws JSONException {
		super();
		
		this.showId = showObject.getString("id");
		this.isNumbered = showObject.getBoolean("numbered_seats");
		
		try {
			this.startTime = UIDateUtil.getDateFromString(showObject.getString("starts_at"));
			this.startTimeString = UIDateUtil.getStringFromDate(this.startTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//TODO Tambien bajar la sala/room donde se proyecta el show vienw bajo el showObject.getJSONObject("room")
	}	
		
	public String getShowId() {
		return showId;
	}

	public void setShowId(String showId) {
		this.showId = showId;
	}

	public Date getStartTime() {
		return startTime;
	}
		
	public String getStartTimeString() {
		return startTimeString;
	}
	
	public void setStartTimeString(String startTimeString) {
		this.startTimeString = startTimeString;
	}

	public boolean isNumbered() {
		return isNumbered;
	}
}
