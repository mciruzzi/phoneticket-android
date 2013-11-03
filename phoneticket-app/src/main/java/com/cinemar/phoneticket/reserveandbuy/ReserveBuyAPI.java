package com.cinemar.phoneticket.reserveandbuy;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.cinemar.phoneticket.external.APIClient;
import com.cinemar.phoneticket.external.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ReserveBuyAPI {

	private RestClient client;

	public ReserveBuyAPI() {
		client = new APIClient();
	}

	public void sendCancelation(String reservationId,
			JsonHttpResponseHandler responseHandler) {
		client.delete("reservations/" + reservationId + ".json",
				responseHandler);
	}

	public void performNumberedReserve(Context ctx,ReserveRequest reserve,
			JsonHttpResponseHandler responseHandler) {
		/*
		 * RequestParams params =new RequestParams(); params.put("email",
		 * reserve.getEmail()); params.put("show_id", reserve.getShowId());
		 * params.put("seats", reserve.getSeatsAsArray());
		 * 
		 * client.post("reservations.json", params, responseHandler);
		 */

		JSONObject obj = new JSONObject();
		HttpEntity entity = null;
		try {
			obj.put("email", reserve.getEmail());
			obj.put("show_id", reserve.getShowId());
			obj.put("seats", new JSONArray(reserve.getSeats()));

			entity = new StringEntity(obj.toString());
		} catch (JSONException e) {
			Log.i("Reserva","e");
		} catch (UnsupportedEncodingException e) {
			Log.i("Reserva","e");
		}

		client.post(ctx, "reservations.json", entity, responseHandler);

	}

	public void performUnNumberedReserve(ReserveRequest reserve,
			JsonHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put("email", reserve.getEmail());
		params.put("show_id", reserve.getShowId());
		params.put("seats_count", Integer.toString(reserve.getSeatsCount()));

		client.post("reservations.json", params, responseHandler);
	}
	


}
