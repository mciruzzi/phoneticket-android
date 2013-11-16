package com.cinemar.phoneticket.reserveandbuy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReserveRequest {
	
	String email;
	String showId;
	List<String> seatsIds; 
	int seatsCount;

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
	public List<String> getSeats() {
		return seatsIds;
	}
	public ArrayList<String> getSeatsAsArray() {
		ArrayList<String> result = new ArrayList<String>(seatsIds);
		return result;
	}
	public void setSeats(List<String> seatsIds) {
		Collections.sort(seatsIds, new Comparator<String>()
	    {
	        public int compare(String f1, String f2)
	        {
	            return f2.toString().compareTo(f1.toString());
	        }        
	    });
		this.seatsIds = seatsIds;
		seatsCount = seatsIds.size();
	}	
	public int getSeatsCount() {
		return seatsCount;
	}
	public void setSeatsCount(int seatsAmount) {
		this.seatsCount = seatsAmount;
	}
	
	

}
