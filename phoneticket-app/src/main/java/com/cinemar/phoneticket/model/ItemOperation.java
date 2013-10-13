package com.cinemar.phoneticket.model;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cinemar.phoneticket.util.UIDateUtil;

import android.util.Log;

public class ItemOperation {

	private String title;
	private String cinema;
	private Date date;
	private String[] seating;
	private String ticketsType;
	private String code;
	
	public ItemOperation(String title, String cinema) {
		
		setTitle(title);
		setCinema(cinema);
		setDate(new Date());
	}
	
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
		
		Log.i("la", "***************************************************Reserva " + getDate());
		
//		String seating = "(" + Integer.toString(seats.length()) + ") ";
		
		String[] seating = new String[seats.length()];
		
		for (int i = 0; i < seats.length(); i++)
		{
//			seating += seats.getString(i) + " ";
			seating[i] = seats.getString(i);
		}
		setSeating(seating);
		
//		private Date date;
//		private String[] seating;
		
//		this.title = .getString("title");
//		this.name = cinema.getString("name");
//		this.latitude = cinema.getInt("latitude");
//		this.longitude = cinema.getInt("longitude");
//		this.address = cinema.getString("address");
//		this.PhotoUrl = cinema.getString("photo_url");
//		shows = new ArrayList<Show>();
//
//		if (cinema.has("shows")) {
//			JSONArray showsArray = cinema.getJSONArray("shows");
//
//			for (int j = 0; j < showsArray.length(); j++) {
//				JSONObject showObject = showsArray.optJSONObject(j);
//				shows.add(new Show(showObject));
//			}
//		}
		
	


	}

	public String toString() {
		return getTitle() + " " + getCinema() + " Lunes";
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCinema() {
		return cinema;
	}
	public void setCinema(String cinema) {
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
	
	public void setDate(String date) {
		try {
			this.date = UIDateUtil.getDateFromString(date);
		
		} catch (ParseException e) {
			e.printStackTrace();
			this.date = null;
		}
	}
	
	public void setDate(Date date) {
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
	
	public void setSeating(String[] seating) {
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
