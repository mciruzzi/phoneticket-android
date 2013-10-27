package com.cinemar.phoneticket.model.prices;

import org.json.JSONException;
import org.json.JSONObject;

public class TwoXOnePromo extends Promotion {
	

	public TwoXOnePromo(JSONObject jsonObject) throws JSONException {
		super(jsonObject);
	}

	@Override
	public double getPrice(int howManyPromos, PriceInfo priceInfo) {
		return priceInfo.getAdultPrice() * howManyPromos;
	}

	@Override
	public int getSeatsNeeded() {
		return 2;
		
	}

}
