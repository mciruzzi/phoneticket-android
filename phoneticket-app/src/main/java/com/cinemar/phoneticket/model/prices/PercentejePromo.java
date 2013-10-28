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
		return ((priceInfo.getAdultPrice() * (100-discountPercentaje))/100) * howManyPromos;

	}

	@Override
	public int getSeatsNeeded() {
		return 1;

	}

}
