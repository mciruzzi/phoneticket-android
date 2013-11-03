package com.cinemar.phoneticket.reserveandbuy;

import org.json.JSONObject;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

public class ReserveResponseHandler extends JsonHttpResponseHandler{
	
	private PerformReserveListener listener;
	
	public ReserveResponseHandler(PerformReserveListener listener) {		
		this.listener = listener;
	}

	@Override
	public void onSuccess(JSONObject jsonObject) {
		Log.i("ReserveResponseHandler", "Reserva Efectuada con Exito");	
		listener.onReserveOk("Reserva realizada con éxito",jsonObject);
		
	}

	@Override
	public void onFailure(Throwable arg0, String arg1) {
		Log.i("ReserveResponseHandler", "Reserva Fallida");
		listener.onErrorWhenReserving("errorChavon");
		
	};

	@Override
	public void onFailure(Throwable e, JSONObject errorResponse) {
		Log.i("ReserveResponseHandler", "Reserva Erronea");
		listener.onErrorWhenReserving("errorChavon");
		
	}
	
	public interface PerformReserveListener{
		public void onReserveOk(String msg,JSONObject result);
		public void onErrorWhenReserving(String msg);
	}
};


