package com.cinemar.phoneticket.authentication;

import com.cinemar.phoneticket.model.User;

public interface AuthenticationService {
	
	User login (String user, String password);			
	boolean register (User user);	

}
