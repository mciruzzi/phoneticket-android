package com.cinemar.phoneticket.external;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class APIClient implements RestClient {
//	private static final String BASE_URL = "http://192.168.1.5:5000/api/"; //url de nuestra API - Juan
//	private static final String BASE_URL = "http://10.0.2.2:5000/api/"; //url de nuestra API
	private static final String BASE_URL = "http://phoneticket-stg.herokuapp.com/api/"; //url de nuestra API
	private final AsyncHttpClient client;

	public APIClient() {
		client = new AsyncHttpClient();
	}

	public void post(String path, RequestParams params, JsonHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(path), params, responseHandler);
	}

	private String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

}
