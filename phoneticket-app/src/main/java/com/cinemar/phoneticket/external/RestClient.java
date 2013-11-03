package com.cinemar.phoneticket.external;

import org.apache.http.HttpEntity;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public interface RestClient {

	void post(String path, RequestParams params, JsonHttpResponseHandler responseHandler);
	
	void post(Context context,String path, HttpEntity httpEntity,JsonHttpResponseHandler responseHandler);

	void get(String path, JsonHttpResponseHandler responseHandler);

	void get(String path, RequestParams params, JsonHttpResponseHandler responseHandler);

	void put(String path, RequestParams params, JsonHttpResponseHandler responseHandler);
	
	void delete(String path, JsonHttpResponseHandler responseHandler);

}
