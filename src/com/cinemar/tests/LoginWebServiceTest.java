package com.cinemar.tests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.cinemar.phoneticket.authentication.AuthenticationClient;
import com.cinemar.phoneticket.authentication.AuthenticationService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class LoginWebServiceTest extends InstrumentationTestCase {

	static AuthenticationService webService = new AuthenticationClient();

	public LoginWebServiceTest() {
		super();
	}

	public void testLoginWebService() throws Throwable {
		final AsyncHttpClient client = new AsyncHttpClient();
		final CountDownLatch signal = new CountDownLatch(1);
		runTestOnUiThread(new Runnable() { // THIS IS THE KEY TO SUCCESS
			@Override
			public void run() {
				client.post(
						"http://phoneticket-stg.herokuapp.com/api/users/sessions.json?email=snipperme@gmail.com&password=123456",
						new AsyncHttpResponseHandler() {
							@Override
							public void onStart() {
								Log.i("START", "start");
								Log.i("START", "start");
							}

							@Override
							public void onSuccess(String response) {
								Log.i("OK", response);
								Log.i("OK", response);

							}

							@Override
							public void onFailure(Throwable e, String response) {
								Log.i("ERROR", response);
								Log.i("ERROR", response);
							}

							@Override
							public void onFinish() {
								Log.i("FINISHA", "finish");
								Log.i("FINISHA", "finish");
								signal.countDown();
							}
						});
			}
		});//
		Log.i("A", "Comenzado el test");
		try {
			signal.await(30, TimeUnit.SECONDS); // wait for callback
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
