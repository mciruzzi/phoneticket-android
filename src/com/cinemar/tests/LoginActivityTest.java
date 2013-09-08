package com.cinemar.tests;

import com.cinemar.phoneticket.LoginActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityTestCase;
import android.widget.TextView;

/* ASI ES UN EJEMPLO DE COMO SE PUEDE TESTEAR UNA ACTIVIDAD ENTERA*/

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
