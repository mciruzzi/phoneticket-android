package com.cinemar.phoneticket.model;

public class User {
	
	private String email;
	private String password; //TODO reemplazar por token de sesion
	
	public User(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}


	


}
