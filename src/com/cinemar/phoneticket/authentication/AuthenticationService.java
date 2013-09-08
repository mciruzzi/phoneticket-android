package com.cinemar.phoneticket.authentication;

import com.cinemar.phoneticket.model.User;

public interface AuthenticationService {
	
	User login (String user, String Password);
			
	User signup (String user, String Password);
		
	

}
