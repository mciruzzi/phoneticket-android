package com.cinemar.phoneticket.theaters;

import com.cinemar.phoneticket.external.APIClient;
import com.cinemar.phoneticket.external.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TheatresClientAPI {
	
	private RestClient client;

	public TheatresClientAPI() {
		client = new APIClient();
	}
	
	public void getTheatres(JsonHttpResponseHandler responseHandler){			
		client.get("theatres.json", responseHandler);
	}
	
	public void getCarteleraComplejo(String theatreId, JsonHttpResponseHandler responseHandler){
		client.get("theatres/" + theatreId + ".json", responseHandler);
	}

	public void getShowSeats(String showId, JsonHttpResponseHandler responseHandler){
		client.get("shows/" + showId + ".json", responseHandler);
	}
}
