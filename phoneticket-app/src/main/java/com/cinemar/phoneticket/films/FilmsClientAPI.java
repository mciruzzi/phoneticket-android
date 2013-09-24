package com.cinemar.phoneticket.films;

import com.cinemar.phoneticket.external.APIClient;
import com.cinemar.phoneticket.external.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;


public class FilmsClientAPI {

	private RestClient client;

	public FilmsClientAPI() {
		client = new APIClient();
	}
	
	public void getFilms(JsonHttpResponseHandler responseHandler){			
		client.get("movies.json", responseHandler);		
	}

}
