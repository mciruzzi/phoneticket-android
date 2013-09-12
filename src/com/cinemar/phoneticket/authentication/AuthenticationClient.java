package com.cinemar.phoneticket.authentication;

import java.io.IOException;
import java.net.ResponseCache;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.cinemar.phoneticket.model.User;
import com.google.gson.Gson;
import com.loopj.android.http.*;


public class AuthenticationClient implements AuthenticationService {

	// Declaracion de variables para serealziar y deserealizar objetos y cadenas JSON
	Gson gson;
	
	

	@Override
	public User login(String user, String password) {
		
		RequestParams params = new RequestParams();
		params.put("email", user);
		params.put("password", password);
		
		String response = null;
		User parsedUser = new User("xxxxx","yyyyy");
		try {
			response = RestClient.post("/users/sessions.json", params);
			//se crea el objeto que ayuda deserealizar la cadena JSON
			Gson gson = new Gson();
			
	 		//Deserealizamos la cadena JSON para que se convertida
			 parsedUser = gson.fromJson(response.toString(), User.class);	
		} catch (TimeoutException e) {			 
			e.printStackTrace();
		}	
			
		return parsedUser;
		
	}
	
	
	@Override
	public User signup(String user, String Password) {
		// TODO Auto-generated method stub
		return new User("A","B");
	}
	
	/**
     * Metodo que recibe una cadena JSON y la convierte en un @User
     * @param strJson (String) Cadena JSON
     */
    private User parseUser(String strJson){
		//se crea el objeto que ayuda deserealizar la cadena JSON
		gson = new Gson();
		
 		//Deserealizamos la cadena JSON para que se convertida
		return gson.fromJson(strJson, User.class);	
 
	}

}
