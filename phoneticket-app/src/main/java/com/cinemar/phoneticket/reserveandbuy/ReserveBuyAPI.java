package com.cinemar.phoneticket.reserveandbuy;

import com.cinemar.phoneticket.external.APIClient;
import com.cinemar.phoneticket.external.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ReserveBuyAPI {
	
	private RestClient client;

	public ReserveBuyAPI() {
		client = new APIClient();
	}
	
	public void sendCancelation(String reservationId, JsonHttpResponseHandler responseHandler){
		client.delete("reservations/" + reservationId + ".json", responseHandler);
	}
	
	public void performNumberedReserve( ReserveRequest reserve , JsonHttpResponseHandler responseHandler){
		RequestParams params =new RequestParams();
		params.put("email", reserve.getEmail());
		params.put("show_id", reserve.getShowId());
		params.put("seats", reserve.getSeatsAsArray());	
		
		client.post("reservations.json", params, responseHandler);
	}
	
	
	public void performUnNumberedReserve( ReserveRequest reserve , JsonHttpResponseHandler responseHandler){
		RequestParams params =new RequestParams();
		params.put("email", reserve.getEmail());
		params.put("show_id", reserve.getShowId());
		params.put("seats_count", Integer.toString(reserve.getSeatsCount()));	
		
		client.post("reservations.json", params, responseHandler);
	}

}
