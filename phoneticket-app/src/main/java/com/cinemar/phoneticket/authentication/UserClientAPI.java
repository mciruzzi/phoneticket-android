package com.cinemar.phoneticket.authentication;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UserClientAPI {

	private static final String BASE_URL = "http://phoneticket-stg.herokuapp.com/api/";

	private AsyncHttpClient client;

	public UserClientAPI() {
		client = new AsyncHttpClient();
	}

	public void signin(String user, String password, JsonHttpResponseHandler responseHandler) {

		RequestParams params = new RequestParams();
		params.put("email", user);
		params.put("password", password);

		client.post(getAbsoluteUrl("users/sessions.json"), params, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}
}
