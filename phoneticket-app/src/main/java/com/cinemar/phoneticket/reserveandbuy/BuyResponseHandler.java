package com.cinemar.phoneticket.reserveandbuy;

import org.json.JSONObject;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

public class BuyResponseHandler extends JsonHttpResponseHandler{
	
	private PerformBuyListener listener;
	
	public BuyResponseHandler(PerformBuyListener listener) {		
		this.listener = listener;
	}

	
	@Override
	public void onSuccess(JSONObject film) {
		Log.i("BuyResponseHandler", "Compra Efectuada con Exito");					
		
	}

	@Override
	public void onFailure(Throwable arg0, String arg1) {
		
	};

	@Override
	public void onFailure(Throwable e, JSONObject errorResponse) {
		
	}

	@Override
	public void onFinish() {
					
	}
	
	public interface PerformBuyListener{
		public void onBuyOk(String msg);
		public void onErrorWhenBuying(String msg);
	}


};



