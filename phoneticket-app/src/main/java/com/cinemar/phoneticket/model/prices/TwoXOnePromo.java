package com.cinemar.phoneticket.model.prices;

import org.json.JSONException;
import org.json.JSONObject;

public class TwoXOnePromo extends Promotion {
	
	private static final long serialVersionUID = 5483967907983463039L;

	public TwoXOnePromo(JSONObject jsonObject) throws JSONException {
		super(jsonObject);
	}

	@Override
	public double getPrice(int howManyPromos, PriceInfo priceInfo) {
		return (priceInfo.getAdultPrice() * howManyPromos) / 2;
	}

	@Override
	public int getSeatsNeeded() {
		return 2;
		
	}

}
