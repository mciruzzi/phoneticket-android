package com.cinemar.tests;

import android.test.AndroidTestCase;

import com.cinemar.phoneticket.authentication.AuthenticationClient;
import com.cinemar.phoneticket.authentication.AuthenticationService;

public class LoginWebServiceTest extends AndroidTestCase {
	
	static AuthenticationService webService = new AuthenticationClient();
	
	public LoginWebServiceTest(){
		super();
	}
	
	public void testLoginWebService(){
		System.out.println("Comenzando el test");
		webService.login("mciruzzi", "pass");	
	}
}
