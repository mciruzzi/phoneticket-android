package com.cinemar.phoneticket.model;

import java.util.Date;

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
