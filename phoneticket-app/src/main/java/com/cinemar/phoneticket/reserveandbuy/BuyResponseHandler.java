package com.cinemar.phoneticket.reserveandbuy;

import org.json.JSONObject;

import android.content.Intent;
import android.util.Log;

import com.cinemar.phoneticket.BuyShowActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

public class BuyResponseHandler extends JsonHttpResponseHandler {

	private PerformBuyListener listener;

	public BuyResponseHandler(PerformBuyListener listener) {
		this.listener = listener;
	}

	@Override
	public void onSuccess(JSONObject result) {
		Log.i("BuyResponseHandler", "Compra Efectuada con Exito");

		listener.onBuyOk("OK", result);

	}

	@Override
	public void onFailure(Throwable arg0, String arg1) {
		Log.i("BuyResponseHandler", "Error Efectuando Compra");
		listener.onErrorWhenBuying("Error Chavon");
	};

	@Override
	public void onFailure(Throwable e, JSONObject errorResponse) {
		Log.i("BuyResponseHandler", "Error Efectuando Compra");
		listener.onErrorWhenBuying("Error Chavon");
	}

	public interface PerformBuyListener {
		public void onBuyOk(String msg, JSONObject result);

		public void onErrorWhenBuying(String msg);
	}

};
