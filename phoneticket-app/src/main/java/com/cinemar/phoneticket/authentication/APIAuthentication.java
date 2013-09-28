package com.cinemar.phoneticket.authentication;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.cinemar.phoneticket.external.APIClient;
import com.cinemar.phoneticket.external.RestClient;
import com.cinemar.phoneticket.model.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class APIAuthentication implements AuthenticationService {

	private final RestClient client;

	public APIAuthentication() {
		this(new APIClient());
	}

	public APIAuthentication(RestClient restClient) {
		this.client = restClient;
	}

	public void login(String user, String password, JsonHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put("email", user);
		params.put("password", password);

		client.post("users/sessions.json", params, responseHandler);
	}

	public void signup(User user, JsonHttpResponseHandler responseHandler) {

		RequestParams params = new RequestParams();
		params.put("email", user.getEmail());
		params.put("password", user.getPassword());
		params.put("first_name", user.getNombre());
		params.put("last_name", user.getApellido());
		params.put("phone_number", user.getTelefono());
		params.put("document", user.getDni());
		params.put("address", user.getDireccion());
		params.put("date_of_birth", new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(user.getFechaNacimiento()));

		client.post("users.json", params, responseHandler);
	}

	public void getUser(String email, JsonHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put("email", email);

		client.get("users/me.json", params, responseHandler);
	}

}
