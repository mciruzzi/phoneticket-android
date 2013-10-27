package com.cinemar.phoneticket.model.prices;

import org.json.JSONException;
import org.json.JSONObject;

public class PercentejePromo extends Promotion {

	private static final long serialVersionUID = -7510175863634145186L;
	double discountPercentaje;	
	
	public PercentejePromo(JSONObject jsonObject) throws JSONException {
		super(jsonObject);
		discountPercentaje = jsonObject.getJSONObject("discount_calculation").getInt("percentage");
		
	}

	@Override
	public double getPrice(int howManyPromos, PriceInfo priceInfo) {
		return (priceInfo.getAdultPrice() * (1-discountPercentaje))*howManyPromos;

	}

	@Override
	public int getSeatsNeeded(int howManyPromos) {
		return howManyPromos;

	}

}
