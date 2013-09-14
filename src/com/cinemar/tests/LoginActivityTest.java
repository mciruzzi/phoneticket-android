package com.cinemar.tests;

import android.test.ActivityInstrumentationTestCase2;

import com.cinemar.phoneticket.LoginActivity;

/* ASI ES UN EJEMPLO DE COMO SE PUEDE TESTEAR UNA ACTIVIDAD ENTERA INCLUSIVE ESTADO DE SU UI y etc*/

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
	
	private LoginActivity loginActivity;
	//private TextView testText;
	
	public LoginActivityTest(){
		super(LoginActivity.class);	
	}
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        loginActivity = getActivity();
        //testText = (TextView) loginActivity.findViewById(R.id.my_first_test_text_view);
    }
	
	public void testLoginWebService(){
		assertTrue(true);
	}


}
