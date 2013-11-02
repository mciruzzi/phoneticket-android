package com.cinemar.phoneticket.model.prices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PriceInfo implements Serializable{
	
	private static final long serialVersionUID = -8182962223271434015L;
	private double adultPrice;
	private double childPrice;
	
	List<Promotion> promotions;


	public PriceInfo(JSONObject jsonObject) throws JSONException {
		JSONObject prices = jsonObject.getJSONObject("prices");
		
		adultPrice = prices.getDouble("adult");
		childPrice = prices.getDouble("kid");
		
		promotions = new ArrayList<Promotion>();
		
		JSONArray jsonPromotions = jsonObject.getJSONArray("promotions");
		for (int i = 0 ;i < jsonPromotions.length() ;i++){
			JSONObject promo = jsonPromotions.getJSONObject(i);
			
			if (promo.getString("discount_calculation_type").contains("percentage")){
				promotions.add(new PercentejePromo(jsonPromotions.getJSONObject(i)));				
			}
			else if (promo.getString("discount_calculation_type").contains("two_for_one")){
				promotions.add(new TwoXOnePromo(jsonPromotions.getJSONObject(i)));
			}
			else if (promo.getString("discount_calculation_type").contains("n_for_x")){
				promotions.add(new NForXPromo(jsonPromotions.getJSONObject(i)));
			}
			
		}
		
	}
	
	public double getChildPrice() {
		return childPrice;
	}

	public void setChildPrice(double childPrice) {
		this.childPrice = childPrice;
	}
	
	public double getAdultPrice() {
		return adultPrice;
	}

	public void setAdultPrice(double adultPrice) {
		this.adultPrice = adultPrice;
	}
	
	public List<Promotion> getPromotions() {
		return promotions;
	}

}
