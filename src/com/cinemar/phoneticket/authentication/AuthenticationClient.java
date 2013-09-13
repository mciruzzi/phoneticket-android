package com.cinemar.phoneticket.authentication;

import java.util.concurrent.TimeoutException;

import com.cinemar.phoneticket.exceptions.InvalidLoginInfoException;
import com.cinemar.phoneticket.exceptions.RepeatedUserException;
import com.cinemar.phoneticket.exceptions.ServerSideException;
import com.cinemar.phoneticket.exceptions.UnconfirmedException;
import com.cinemar.phoneticket.model.User;
import com.google.gson.Gson;
import com.loopj.android.http.*;


public class AuthenticationClient implements AuthenticationService {

	// Declaracion de variables para serealziar y deserealizar objetos y cadenas JSON
	Gson gson;	

	@Override
	public User login(String user, String password) throws InvalidLoginInfoException, UnconfirmedException {
		
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
		} catch (ServerSideException e) {			
			if (e.getMessage().contains("Invalid email or password")) throw new InvalidLoginInfoException();
			if (e.getMessage().contains("You have to confirm your account before continuing.")) throw new UnconfirmedException();
		}	
			
		return parsedUser;
		
	}
	
	@Override
	public void register(User user) throws RepeatedUserException,InvalidLoginInfoException{
		
		RequestParams params = new RequestParams();
		params.put("email", user.getEmail());
		params.put("password", user.getPassword());
		params.put("first_name", user.getNombre());
		params.put("last_name", user.getApellido());
		params.put("phone_number", user.getTelefono());
		params.put("document", user.getDni());
		params.put("address", user.getDireccion());
		params.put("date_of_birth", user.getFechaNacimiento().toString());
		
		String response = null;
		try {
			response = RestClient.post("/users.json", params);
		 	//Todavia no hay ningun codigo de OK			
			if(response.contains(user.getEmail())){
				 return;
			}			
			
		} catch (TimeoutException e) {	 
			e.printStackTrace();		
		} catch (ServerSideException e) {
			if (e.getMessage().contains("has already been taken")) throw new RepeatedUserException(user.getEmail());
			if (e.getMessage().contains("is invalid")) throw new InvalidLoginInfoException();			
		}	

	}

}
