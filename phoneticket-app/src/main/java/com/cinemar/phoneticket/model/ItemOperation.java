package com.cinemar.phoneticket.model;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cinemar.phoneticket.util.UIDateUtil;

public class ItemOperation {

	private String title;
	private String cinema;
	private Date date;
	private String[] seating;
	private String ticketsType;
	private String code;
	
//	public ItemOperation(String title, String cinema) {
//		
//		setTitle(title);
//		setCinema(cinema);
//		setDate(new Date());
//	}
	
	public ItemOperation(JSONObject operation) throws JSONException {
		
		JSONObject show = operation.getJSONObject("show");
		JSONArray seats = operation.getJSONArray("seats");
		JSONObject room = show.getJSONObject("room");
		JSONObject cinema = show.getJSONObject("theatre");
		JSONObject film = show.getJSONObject("movie");

		setCode(operation.getString("id"));
		setTitle(film.getString("title"));
		setCinema(cinema.getString("name") + " " + room.getString("name"));
		setDate(show.getString("starts_at"));
		setSeating(seats);
	}
	
	public String getTitle() {
		return title;
	}
	private void setTitle(String title) {
		this.title = title;
	}
	public String getCinema() {
		return cinema;
	}
	private void setCinema(String cinema) {
		this.cinema = cinema;
	}
	public Date getDate() {
		return date;
	}
	
	public String getDateToString() {
		
		if (date == null)
			return "No se encuentra la fecha y el horario disponible";
		
		return UIDateUtil.getStringFromDate(date);	
	}
	
	private void setDate(String date) {
		try {
			this.date = UIDateUtil.getDateFromString(date);
		
		} catch (ParseException e) {
			e.printStackTrace();
			this.date = null;
		}
	}
	
	private void setDate(Date date) {
		this.date = date;
	}
	
	public String[] getSeating() {
		return seating;
	}
	
	public String getSeatingToString() {
		
		String seatings = "";
		
		for (int i = 0; i < seating.length; i++)
			seatings += seating[i] + "; ";
		
		return seatings;
	}

	private void setSeating(JSONArray seats) throws JSONException {
		
		String[] seating = new String[seats.length()];
		
		for (int i = 0; i < seats.length(); i++)
			seating[i] = seats.getString(i);
		
		setSeating(seating);
	}
	
	private void setSeating(String[] seating) {
		this.seating = seating;
	}
	
	public String getTicketsType() {
		return ticketsType;
	}
	
	public void setTicketsType(String ticketsType) {
		this.ticketsType = ticketsType;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
}
