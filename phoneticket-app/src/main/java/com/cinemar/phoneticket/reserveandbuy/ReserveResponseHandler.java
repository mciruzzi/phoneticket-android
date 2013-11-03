package com.cinemar.phoneticket.reserveandbuy;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

public class ReserveResponseHandler extends JsonHttpResponseHandler{

	private final PerformReserveListener listener;

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
		if (errorResponse.has("errors")) {
			Log.i("BuyResponseHandler", "Error Efectuando Reserva: " + errorResponse.toString());
			try {
				decodeErrors(errorResponse.getJSONObject("errors"));
			} catch (JSONException e1) {
				listener.onErrorWhenReserving("Error en la respuesta del servidor. Intente más tarde");
			}
		} else {
			listener.onErrorWhenReserving("Error en la respuesta del servidor. Intente más tarde");
		}
	}

	private void decodeErrors(JSONObject errors) throws JSONException {
		boolean noneMessageHandled = true;
		@SuppressWarnings("unchecked")
		Iterator<String> keys = errors.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			Fields field = secureField(key);
			if (!field.equals(Fields.other)) {
				JSONArray fieldErrors = errors.getJSONArray(key);
				String firstErrorMessage = fieldErrors.getString(0);
				listener.onValidationError(field, firstErrorMessage);
				noneMessageHandled = false;
			}
		}

		if (noneMessageHandled) {
			listener.onErrorWhenReserving("Error en la respuesta del servidor. Intente más tarde");
		}
	}

	public interface PerformReserveListener{
		public void onReserveOk(String msg,JSONObject result);
		public void onErrorWhenReserving(String msg);
		public void onValidationError(Fields field, String msg);
	}

	private Fields secureField(String key) {
		try {
			return Fields.valueOf(key);
		} catch (IllegalArgumentException e) {
			return Fields.other;
		}
	}

	public enum Fields {
		seats, seats_count, show_id, other
	}
};


