package com.cinemar.phoneticket.authentication;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.util.Log;

import com.cinemar.phoneticket.exceptions.ServerSideException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class RestClient {
	  private static final String BASE_URL = "http://phoneticket-stg.herokuapp.com/api"; //url de nuestra API
	  private static AsyncHttpClient client = new AsyncHttpClient();
	  private static final int TIMEOUT = 60;	  
	  private static final String ERROR= "ERROR";

	  public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.get(getAbsoluteUrl(url), params, responseHandler);
	  }

	  public static String post(String url, RequestParams params) throws TimeoutException, ServerSideException {
			final CountDownLatch signal = new CountDownLatch(1);
			final StringBuilder strBuilder = new StringBuilder();			
			final String concatUrl= getAbsoluteUrl(url);
			final RequestParams postParams = params;
			final StringBuilder failure = new StringBuilder();
			
			
			Runnable thread = new Runnable() { // THIS IS THE KEY TO SUCCESS
				public void run() {					
					Log.i("RestClient", "URL: " + concatUrl.toString());
					client.post(concatUrl,postParams, new AsyncHttpResponseHandler() {
					//client.post("http://phoneticket-stg.herokuapp.com/api/users/sessions.json?email=snipperme@gmail.com&password=123456", new AsyncHttpResponseHandler() {
								@Override
								public void onStart() {
									Log.i("RestClient", "Starting comunication for post with params: " + postParams.toString());									
								}

								@Override
								public void onSuccess(String response) {
									Log.i("RestClient", "Response received: "+ response);									
									strBuilder.append(response);
								}

								@Override
								public void onFailure(Throwable e, String response) {
									Log.i("RestClient", "Failure received: "+ response);
									Log.i("RestClient", "Error:" + e.toString());
									strBuilder.append(response);									
									failure.append(ERROR);									
								}

								@Override
								public void onFinish() {
									Log.i("RestClient", "Comunication Finished");									
									signal.countDown();
								}
							});
				}
			};
			thread.run(); //magic			
			try {
				signal.await(TIMEOUT, TimeUnit.SECONDS); // wait for callback
			} catch (InterruptedException e) {
				Log.i("RestClient", "Error Waiting for response");
				e.printStackTrace();
			}
			if(strBuilder.toString().isEmpty()){
				throw new TimeoutException("TIMEOUT PAPA");
			}
			
			if(!failure.toString().isEmpty()){
				throw new ServerSideException(strBuilder.toString());					
			}					
						
			return strBuilder.toString();	      
	  }

	  private static String getAbsoluteUrl(String relativeUrl) {
	      return BASE_URL + relativeUrl;
	  }
	}

