package com.cinemar.phoneticket.model.prices;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Promotion implements Serializable {

	private static final long serialVersionUID = 6023972663947325236L;
	public static final String CODE="code";
	
	private String id,name,validationType, description;
	
	public Promotion(JSONObject jsonObject) throws JSONException {
		setId(jsonObject.getString("id"));
		name = jsonObject.getString("name");
		validationType = jsonObject.getString("validation_type");
		setDescription(jsonObject.getString("description"));
	}
	
		
	public Promotion(String name) {		
		this.name = name;
	}


	public abstract double getPrice (int howManyPromos, PriceInfo priceInfo);
	public abstract int getSeatsNeeded ();
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValidationType() {
		return validationType;
	}

	public void setValidationType(String validationType) {
		this.validationType = validationType;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public boolean isValidatedByCode() {
		 
		return validationType.equals(CODE);
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

}
