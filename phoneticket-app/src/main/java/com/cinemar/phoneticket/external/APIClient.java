package com.cinemar.phoneticket.external;

import org.apache.http.HttpEntity;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class APIClient implements RestClient {
//	private static final String BASE_URL = "http://10.0.2.2:5000/api/"; //url de nuestra API
	private static final String BASE_URL = "http://phoneticket-stg.herokuapp.com/api/"; //url de nuestra API
	private final AsyncHttpClient client;

	public APIClient() {
		client = new AsyncHttpClient();
	}

	public void post(String path, RequestParams params, JsonHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(path), params, responseHandler);
	}
	
	public void post(Context context,String path, HttpEntity httpEntity,JsonHttpResponseHandler responseHandler) {
		client.post(context,getAbsoluteUrl(path),httpEntity,"application/json",responseHandler);		
	}

	public void get(String path, JsonHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(path), responseHandler);
	}
	public void get(String path, RequestParams params,
			JsonHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(path), params, responseHandler);
	}

	public void put(String path, RequestParams params,
			JsonHttpResponseHandler responseHandler) {
		client.put(getAbsoluteUrl(path), params, responseHandler);
	}
	
	 public void delete(String path, JsonHttpResponseHandler responseHandler) {
         client.delete(getAbsoluteUrl(path), responseHandler);
	 }

	private String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}


}
