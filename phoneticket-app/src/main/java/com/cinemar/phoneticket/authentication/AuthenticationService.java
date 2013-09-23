package com.cinemar.phoneticket.authentication;

import com.cinemar.phoneticket.model.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public interface AuthenticationService {

	void login (String user, String password, JsonHttpResponseHandler responseHandler);
	void signup(User user, JsonHttpResponseHandler responseHandler);

}
