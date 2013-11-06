package com.cinemar.phoneticket.reserveandbuy;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

public class BuyResponseHandler extends JsonHttpResponseHandler {

	private final PerformBuyListener listener;

	public BuyResponseHandler(PerformBuyListener listener) {
		this.listener = listener;
	}

	@Override
	public void onSuccess(JSONObject result) {

		Log.i("BuyResponseHandler", "Compra Efectuada con Exito");
		listener.onBuyOk("Compra realizada con éxito", result);
	}

	@Override
	public void onFailure(Throwable arg0, String arg1) {

		Log.i("BuyResponseHandler", "Error Efectuando Compra:" + arg1);
		listener.onErrorWhenBuying(arg1);
	};

	@Override
	public void onFailure(Throwable e, JSONObject errorResponse) {
		if (errorResponse.has("errors")) {
			Log.i("BuyResponseHandler", "Error Efectuando Compra: " + errorResponse.toString());
			try {
				decodeErrors(errorResponse.getJSONObject("errors"));
			} catch (JSONException e1) {
				listener.onErrorWhenBuying("Error en la respuesta del servidor. Intente más tarde");
			}
		} else {
			listener.onErrorWhenBuying("Error en la respuesta del servidor. Intente más tarde");
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
			listener.onErrorWhenBuying("Error en la respuesta del servidor. Intente más tarde");
		}
	}


	private Fields secureField(String key) {
		try {
			return Fields.valueOf(key);
		} catch (IllegalArgumentException e) {
			return Fields.other;
		}
	}

	public interface PerformBuyListener {
		public void onBuyOk(String msg, JSONObject result);

		public void onValidationError(Fields field, String msg);

		public void onErrorWhenBuying(String msg);
	}

	public enum Fields {
		promotion_code, payment, card_number, seats, seats_count, show_id, other
	}
};
