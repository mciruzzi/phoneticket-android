package com.cinemar.phoneticket.authentication;

import com.cinemar.phoneticket.exceptions.InvalidLoginInfoException;
import com.cinemar.phoneticket.exceptions.RepeatedUserException;
import com.cinemar.phoneticket.exceptions.UnconfirmedException;
import com.cinemar.phoneticket.model.User;

public interface AuthenticationService {
	
	User login (String user, String password) throws InvalidLoginInfoException, UnconfirmedException;			
	void register (User user) throws RepeatedUserException,InvalidLoginInfoException;	

}
