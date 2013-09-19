package com.cinemar.tests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.cinemar.phoneticket.authentication.AuthenticationClient;
import com.cinemar.phoneticket.authentication.AuthenticationService;
import com.cinemar.phoneticket.model.User;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class LoginWebServiceTest extends TestCase {
	public void testTheTruth() {
		assertEquals(true, true);
	}
//
//	static AuthenticationService webService = new AuthenticationClient();
//
//	public LoginWebServiceTest() {
//		super();
//	}
//
//	public void testLoginWebService() throws Throwable {
//		final AsyncHttpClient client = new AsyncHttpClient();
//		final CountDownLatch signal = new CountDownLatch(1);
//		final StringBuilder strBuilder = new StringBuilder();
//		
//		runTestOnUiThread(new Runnable() { // THIS IS THE KEY TO SUCCESS
//			public void run() {
//				client.post(
//						"http://phoneticket-stg.herokuapp.com/api/users/sessions.json?email=snipperme@gmail.com&password=123456",
//						new AsyncHttpResponseHandler() {
//							@Override
//							public void onStart() {
//								Log.i("START", "start");
//								Log.i("START", "start");
//							}
//
//							@Override
//							public void onSuccess(String response) {
//								Log.i("OK", response);
//								Log.i("OK", response);
//								strBuilder.append(response);
//
//							}
//
//							@Override
//							public void onFailure(Throwable e, String response) {
//								Log.i("ERROR", response);
//								Log.i("ERROR", response);
//							}
//
//							@Override
//							public void onFinish() {
//								Log.i("FINISHA", "finish");
//								Log.i("FINISHA", "finish");
//								signal.countDown();
//							}
//						});
//			}
//		});//
//		Log.i("A", "Comenzado el test");
//		try {
//			signal.await(30, TimeUnit.SECONDS); // wait for callback
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		assertEquals(strBuilder.toString(),"{\"email\":\"snipperme@gmail.com\"}");
//		
//		//se crea el objeto que ayuda deserealizar la cadena JSON
//		Gson gson = new Gson();
//		
// 		//Deserealizamos la cadena JSON para que se convertida
//		User user = gson.fromJson(strBuilder.toString(), User.class);
//		assertEquals(user.getEmail(),"snipperme@gmail.com");
//	}
}
