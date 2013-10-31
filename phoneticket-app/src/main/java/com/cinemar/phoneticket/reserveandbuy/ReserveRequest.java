package com.cinemar.phoneticket.reserveandbuy;

import java.util.ArrayList;
import java.util.Collection;

import com.cinemar.phoneticket.model.Room.Seat;

public class ReserveRequest {
	
	String email;
	String showId;
	Collection<String> seatsIds; 
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getShowId() {
		return showId;
	}
	public void setShowId(String showId) {
		this.showId = showId;
	}
	public Collection<String> getSeats() {
		return seatsIds;
	}
	public ArrayList<String> getSeatsAsArray() {
		ArrayList<String> result = new ArrayList<String>(seatsIds);
		return result;
	}
	public void setSeats(Collection<String> seatsIds) {
		this.seatsIds = seatsIds;		
	}
	
	

}
