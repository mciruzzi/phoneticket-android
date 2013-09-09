package com.cinemar.phoneticket.authentication;

import com.cinemar.phoneticket.model.User;

public interface AuthenticationService {
	
	User loginSOAP (String user, String password);
	User login (String user, String password);
	User loginAsync(String user, String password);
			
	User signup (String user, String password);
	
	
		
	

}
