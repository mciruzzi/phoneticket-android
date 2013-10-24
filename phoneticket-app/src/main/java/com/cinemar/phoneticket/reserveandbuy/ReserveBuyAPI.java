package com.cinemar.phoneticket.reserveandbuy;

import com.cinemar.phoneticket.external.APIClient;
import com.cinemar.phoneticket.external.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ReserveBuyAPI {
	
	private RestClient client;

	public ReserveBuyAPI() {
		client = new APIClient();
	}
	
	public void sendCancelation(String reservationId, JsonHttpResponseHandler responseHandler){
		client.delete("reservations/" + reservationId + ".json", responseHandler);
	}

}
