package com.cinemar.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.test.AndroidTestCase;
import android.util.Log;

import com.cinemar.phoneticket.authentication.AuthenticationClient;
import com.cinemar.phoneticket.authentication.AuthenticationService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class LoginWebServiceTest extends AndroidTestCase {
	
	static AuthenticationService webService = new AuthenticationClient();
	
	public LoginWebServiceTest(){
		super();
	}
	
	public void testLoginWebService(){
		/*System.out.println("Comenzando el test");
		webService.login("snipperme@gmail.com", "123456");*/
		
		HttpClient httpclient = new DefaultHttpClient();
		// specify the URL you want to post to
		HttpPost httppost = new HttpPost("http://phoneticket-stg.herokuapp.com/api/users/sessions");
		try {
			/*
			// create a list to store HTTP variables and their values
		    List nameValuePairs = new ArrayList();
		    // add an HTTP variable and value pair
		    nameValuePairs.add(new BasicNameValuePair("email", "snipperme@gmail.com"));
		    nameValuePairs.add(new BasicNameValuePair("password", "123456"));
		    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));*/
		    
		    //passes the results to a string builder/entity
		    //StringEntity se = new StringEntity("{\"email\": \"snipperme@gmail.com\", \"password\": \"123456\"}");
		    
		    JSONObject data = new JSONObject();
	        data.put("email", "snipperme@gmail.com");
	        data.put("password", "123456");	        
	        
	        StringEntity se = new StringEntity(data.toString());

		    //sets the post request as the resulting string
		    httppost.setEntity(se);
		    
		    httppost.setHeader("Accept", "application/json");
		    httppost.setHeader("Content-type", "application/json");			
		       		    
		    // send the variable and value, in other words post, to the URL
		    HttpResponse response = httpclient.execute(httppost);
		    Log.i("RESPONSE",response.toString());
	    } catch (ClientProtocolException e) {
	    	Log.i("PROTOCOL","A");
	    } catch (IOException e) {
		    Log.i("IO","S");
	    } catch (JSONException e) {
			Log.i("JSON","J");
		}

		/*
		Log.i("A","Comenzado el test");
		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.google.com", new AsyncHttpResponseHandler() {
		     @Override
		     public void onStart() {
		    	 Log.i("START","start");
		     }
			
		    @Override
		    public void onSuccess(String response) {
		    	Log.i("OK",response);
		    }
		    		 
		    @Override
		    public void onFailure(Throwable e, String response) {
		        Log.i("ERROR",response);
		    }

		     @Override
		     public void onFinish() {
		    	 Log.i("FINISHA","finish");
		     }
		});
		*/
	}
}
