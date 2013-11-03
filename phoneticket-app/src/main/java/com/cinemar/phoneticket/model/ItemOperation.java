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
	private String shareUrl;
	private String cover;
	private String id;
	private String idShow;
	
	public ItemOperation(JSONObject operation) throws JSONException {
		
		JSONObject show = operation.getJSONObject("show");
		JSONArray seats = operation.getJSONArray("seats");
		JSONObject room = show.getJSONObject("room");
		JSONObject cinema = show.getJSONObject("theatre");
		JSONObject film = show.getJSONObject("movie");
		
		setShareUrl(operation.getString("share_url"));
		setTitle(film.getString("title"));
		setCinema(cinema.getString("name") + " " + room.getString("name"));
		setDate(show.getString("starts_at"));
		setSeating(seats);
		setCode(getTitle() + "|"+ getDate() + "|" + getCinema());
		setId(operation.getString("id"));
		setCover(film.getString("cover_url"));
		setIdShow(show.getString("id"));
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

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public String getCover() {
		return cover;
	}

	private void setCover(String cover) {
		this.cover = cover;
	}

	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}

	public String getIdShow() {
		return idShow;
	}

	private void setIdShow(String idShow) {
		this.idShow = idShow;
	}
}
