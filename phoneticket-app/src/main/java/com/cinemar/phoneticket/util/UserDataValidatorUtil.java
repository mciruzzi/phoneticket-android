package com.cinemar.phoneticket.util;

import com.cinemar.phoneticket.R;

import android.text.TextUtils;
import android.app.Activity;

public class UserDataValidatorUtil {
	
	static private String error;

	static public boolean isValidEmail(String email, Activity activity) {
		
		if (TextUtils.isEmpty(email)) {
			
			setError(activity.getString(R.string.error_field_required));
			return false;
			
		} else if (!email.contains("@")) {
			
			setError(activity.getString(R.string.error_invalid_email));
			return false;
		}

		return true;
	}
	
	static public boolean isValidPassword(String password, Activity activity) {

		if (TextUtils.isEmpty(password)) {
			
			setError(activity.getString(R.string.error_field_required));
			return false;
			
		} else if (password.length() < 4) {
			
			setError(activity.getString(R.string.error_invalid_password));
			return false;
		}
		
		return true;
	}
	
	static public boolean isValidName(String name, Activity activity) {
		
		if (TextUtils.isEmpty(name)) {
			
			setError(activity.getString(R.string.error_field_required));
			return false;
		}
		
		return true;
	}
	
	static public boolean isValidLastName(String lastName, Activity activity) {
		
		
		if (TextUtils.isEmpty(lastName)) {
			
			setError(activity.getString(R.string.error_field_required));
			return false;
		}
		
		return true;
	}
	
	static public boolean isValidPhoneNumber(String phoneNumber, Activity activity) {
		
		if (TextUtils.isEmpty(phoneNumber)) {
			
			setError(activity.getString(R.string.error_field_required));
			return false;
		}
		
		return true;
	}
	
	static public boolean isValidDNI(String dni, Activity activity) {
		
		if (TextUtils.isEmpty(dni)) {
			
			setError(activity.getString(R.string.error_field_required));
			return false;
		}
		
		return true;
	}

	public static String getError() {
		return error;
	}

	private static void setError(String error) {
		UserDataValidatorUtil.error = error;
	}
}
