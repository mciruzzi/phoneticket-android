package com.cinemar.phoneticket.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cinemar.phoneticket.model.Room.Seat;
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
	private boolean isNumered;
	
	public static final String SEPARADOR = ";";
	
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
		setNumered(show.getBoolean("numbered_seats"));

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
			seatings += seating[i] + SEPARADOR + " ";
		
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
	
	static public int getCountSeats(String seatings) {
		return seatings.split(SEPARADOR).length - 1;
	}

	public boolean isNumered() {
		return isNumered;
	}

	private void setNumered(boolean isNumered) {
		this.isNumered = isNumered;
	}
	
	static public ArrayList<String> getSeatSortList(LinkedList<Seat> selectedSeats) {
		
		ArrayList<String> seatsIds = new ArrayList<String>();
		
		for (Seat seat : selectedSeats) {
			seatsIds.add(seat.getId());
		}
		
		sortSeats(seatsIds);
		
		return seatsIds;
	}
	
	static public void sortSeats(ArrayList<String> seatsIds ) {
		Collections.sort(seatsIds, new Comparator<String>()
	    {
	        public int compare(String seat1, String seat2)
	        {
	        	String[] seat1Split = seat1.split("-");
	        	String[] seat2Split = seat2.split("-");
	        		        	
	        	if (seat1Split[0].compareTo(seat2Split[0]) == 0) {
		        	
	        		Integer number1 = Integer.valueOf(seat1Split[1]);
	        		Integer number2 = Integer.valueOf(seat2Split[1]);
	        		
	        		return number2.compareTo(number1);
	        	}
	        	
	        	return seat2.compareTo(seat1);
	        }        
	    });
//		Log.i("ASIENTO STATOS", seatsIds.toString());
	}
	
	static public void sortInverseSeats(ArrayList<String> seatsIds ) {
		Collections.sort(seatsIds, new Comparator<String>()
	    {
	        public int compare(String seat1, String seat2)
	        {
	        	String[] seat1Split = seat1.split("-");
	        	String[] seat2Split = seat2.split("-");
	        		        	
	        	if (seat1Split[0].compareTo(seat2Split[0]) == 0) {
		        	
	        		Integer number1 = Integer.valueOf(seat1Split[1]);
	        		Integer number2 = Integer.valueOf(seat2Split[1]);
	        		
	        		return number1.compareTo(number2);
	        	}
	        	
	        	return seat1.compareTo(seat2);
	        }        
	    });
//		Log.i("ASIENTO STATOS IN", seatsIds.toString());
	}
}
