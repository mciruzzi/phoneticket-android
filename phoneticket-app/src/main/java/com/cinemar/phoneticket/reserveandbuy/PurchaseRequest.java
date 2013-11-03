package com.cinemar.phoneticket.reserveandbuy;

import java.util.ArrayList;
import java.util.Collection;

public class PurchaseRequest {
	
	String email;
	String showId;
	Collection<String> seatsIds; 
	int seatsCount;
	String reservationId = null; //if coming from reservation
	int kidsCount;
	String promotionId;
	String promotionCode = null;
	int cardNumber,cardVerification;
	String cardOwner;
	boolean isNumbered;


	public boolean isNumbered() {
		return isNumbered;
	}

	public void setNumbered(boolean isNumbered) {
		this.isNumbered = isNumbered;
	}

	public Collection<String> getSeatsIds() {
		return seatsIds;
	}

	public String getReservationId() {
		return reservationId;
	}
	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}
	public int getKidsCount() {
		return kidsCount;
	}
	public void setKidsCount(int kidsCount) {
		this.kidsCount = kidsCount;
	}
	public String getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}
	public String getPromotionCode() {
		return promotionCode;
	}
	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}
	public int getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(int cardNumber) {
		this.cardNumber = cardNumber;
	}
	public int getCardVerification() {
		return cardVerification;
	}
	public void setCardVerification(int cardVerification) {
		this.cardVerification = cardVerification;
	}
	public String getCardOwner() {
		return cardOwner;
	}
	public void setCardOwner(String cardOwner) {
		this.cardOwner = cardOwner;
	}
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
		seatsCount = seatsIds.size();
	}	
	public int getSeatsCount() {
		return seatsCount;
	}
	public void setSeatsCount(int seatsAmount) {
		this.seatsCount = seatsAmount;
	}
	
	

}
