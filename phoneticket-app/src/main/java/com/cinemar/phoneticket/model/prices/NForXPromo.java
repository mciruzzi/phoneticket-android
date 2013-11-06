package com.cinemar.phoneticket.model.prices;

import org.json.JSONException;
import org.json.JSONObject;


public class NForXPromo extends Promotion {
	
	private static final long serialVersionUID = -5578725509232278515L;
	int n,x;	

	public NForXPromo(JSONObject jsonObject) throws JSONException {
		super(jsonObject);
		n = jsonObject.getJSONObject("discount_calculation").getInt("n");
		x = jsonObject.getJSONObject("discount_calculation").getInt("x");
	}

	@Override
	public double getPrice(int howManyPromos, PriceInfo priceInfo) {
		return x * howManyPromos;
	}

	@Override
	public int getSeatsNeeded() {
		return n;
	}

}
