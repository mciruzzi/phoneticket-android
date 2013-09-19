package com.cinemar.phoneticket.model;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	
	private String email;
	private String password; //TODO reemplazar por token de sesion
	private String nombre;
	private String apellido;
	private String dni;
	private Date fechaNacimiento;
	private String direccion;
	private String telefono;
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public User(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public User(String email, String password, String nombre, String apellido,
			String dni, Date fechaNacimiento, String direccion, String telefono) {
		this(email, password);
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.fechaNacimiento = fechaNacimiento;
		this.direccion = direccion;
		this.telefono = telefono;
	}

	public User(JSONObject user) {
		this(user.optString("email"), user.optString("password"));
		this.nombre = user.optString("first_name");
		this.apellido = user.optString("last_name");
		this.dni = user.optString("document");
//		this.fechaNacimiento = Calendaruser.getString("date_of_birth");
		this.direccion = user.optString("address");
		this.telefono = user.optString("phone_number");
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}


	


}
