package com.cinemar.phoneticket.external;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public interface RestClient {

	void post(String path, RequestParams params, JsonHttpResponseHandler responseHandler);

}
