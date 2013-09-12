package com.cinemar.phoneticket.authentication;

import java.util.concurrent.TimeoutException;

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
	public boolean register(User user){
		
		RequestParams params = new RequestParams();
		params.put("email", user.getEmail());
		params.put("password", user.getPassword());
		
		String response = null;
		try {
			response = RestClient.post("/users.json", params);
		 		//Deserealizamos la cadena JSON para que se convertida
			 if(response.contains(user.getEmail())){
				 return true;
			 }
			 else{
				 return false;
			 }
		} catch (TimeoutException e) {			 
			e.printStackTrace();
			return false;
		}	

	}

}
