package com.cinemar.phoneticket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cinemar.phoneticket.films.SeatOnClickListener;
import com.cinemar.phoneticket.model.Room;
import com.cinemar.phoneticket.model.Room.Seat;
import com.cinemar.phoneticket.model.SeatStatus;
import com.cinemar.phoneticket.model.prices.PriceInfo;
import com.cinemar.phoneticket.reserveandbuy.ReserveBuyAPI;
import com.cinemar.phoneticket.reserveandbuy.ReserveRequest;
import com.cinemar.phoneticket.reserveandbuy.ReserveResponseHandler;
import com.cinemar.phoneticket.reserveandbuy.ReserveResponseHandler.PerformReserveListener;
import com.cinemar.phoneticket.theaters.TheatresClientAPI;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SelectSeatsActivity extends AbstractApiConsumerActivity implements
		PerformReserveListener {

	private String showId;
	private boolean isReserve;
	// String[][] seats = new String[20][20];
	private Room showRoom;
	private PriceInfo priceInfo;
	private TableLayout cinemaLayout;
	private Map<String, ImageView> seatsImages;
	private LinkedList<Seat> SelectedSeats = new LinkedList<Seat>();
	private int maxSeatsToTake = Integer.MAX_VALUE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_seats);

		setTitle(getIntent().getStringExtra("title"));

		// ** Important to get in order to use the showProgress method**//
		mMainView = findViewById(R.id.salaView);
		mStatusView = findViewById(R.id.sala_status);
		mStatusMessageView = (TextView) findViewById(R.id.sala_status_message);

		showId = getIntent().getStringExtra("showId");
		isReserve = getIntent().getBooleanExtra("isReserve", false);
		if (getIntent().getStringExtra("maxSelections") != null)
			// supongamos que viene dicho de afuera si hay una pantalla extra
			// donde se dice que cantidad de butacas se va a comprar
			maxSeatsToTake = Integer.parseInt(getIntent().getStringExtra(
					"maxSelections"));

		cinemaLayout = (TableLayout) findViewById(R.id.cinemalayout);
		seatsImages = new HashMap<String, ImageView>();

		this.requestRoomLayout();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_seats, menu);
		return true;
	}

	private void requestRoomLayout() {
		mStatusMessageView.setText(R.string.getting_sala);
		showProgress(true);

		TheatresClientAPI api = new TheatresClientAPI();
		api.getShowSeats(showId, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject roomInfo) {
				Log.i("SelectSeats Activity", "Complejos Recibidos");
				try {
					showRoom = new Room(roomInfo);
					priceInfo = new PriceInfo(roomInfo); // just to validate
															// parsing at this
															// moment
					Log.i("SelectSeats Activity", "Seats" + roomInfo
							+ "recibido");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				displaySeats();
			}

			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				Log.i("SelectSeats Activity", "Failure recibiendo Seats");
				if (errorResponse != null) {
					showSimpleAlert(errorResponse.optString("error"));
				} else {
					showSimpleAlert(e.getMessage());
				}
			}

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				showSimpleAlert(getString(R.string.error_connection));
			};

			public void onFinish() {
				showProgress(false);				
			}

		});

	}

	private void displaySeats() {
		ImageView seatView;

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 15, 10, 15);
		params.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
		int lastLeftColumn = showRoom.getLeftWidth();
		int lastMiddleColumn = showRoom.getMiddleWidth()
				+ showRoom.getLeftWidth();

		for (Integer i = 0; i < showRoom.getRowsLength(); i++) {

			TableRow fila = new TableRow(this);
			fila.setPadding(15, 10, 15, 10);
			fila.setLayoutParams(params);
			cinemaLayout.addView(fila);

			for (Integer j = 0; j < showRoom.getColumnsLength(); j++) {
				Seat seatModel = showRoom.getSeat(i, j);

				seatView = new ImageView(this);
				seatView.setPadding(2, 0, 2, 0);
				if ((j + 1 == lastLeftColumn) || (j + 1 == lastMiddleColumn)) {
					seatView.setPadding(2, 0, 40, 0);
				}

				seatsImages.put(seatModel.getId(), seatView);

				if (seatModel.getStatus().equals(SeatStatus.NON_EXISTENT)) {
					seatView.setImageResource(R.drawable.seat_available);// podria														// ser
																			// cualquier
																			// otro(es
																			// solo
																			// para
																			// que
																			// ocupe
																			// el
																			// lugar
																			// vacio)
					seatView.setVisibility(View.INVISIBLE); // la hago invisible
															// aunque quizas no
															// sea lo mejor
				}

				else if (seatModel.getStatus().equals(SeatStatus.AVAILABLE)) {
					seatView.setImageResource(R.drawable.seat_available);
					seatView.setOnClickListener(new SeatOnClickListener(
							seatModel, seatsImages, true, SelectedSeats,
							maxSeatsToTake));
				} else if (seatModel.getStatus().equals(SeatStatus.OCCUPIED)) {
					seatView.setImageResource(R.drawable.seat_occupied);
					seatView.setOnClickListener(new SeatOnClickListener(
							seatModel, seatsImages, false, SelectedSeats,
							maxSeatsToTake));
				}

				fila.addView(seatView);
			}

		}

	}

	public void readySelectingSeats(View view) {
		if (SelectedSeats.isEmpty()) {
			showSimpleAlert(getString(R.string.no_seats_selected));
			return;
		}

		ArrayList<String> seatsIds = new ArrayList<String>();
		for (Seat seat : SelectedSeats) {
			seatsIds.add(seat.getId());
		}

		// Hago la reserva de dichos asientos (no hay nada mas que cargar)
		if (isReserve) {
			ReserveRequest reserve = new ReserveRequest();
			reserve.setEmail("snipperme@gmail.com"); // TODO evitar hardcoding, // tomar el usuario logueado
			reserve.setShowId(showId);
			reserve.setSeats(seatsIds);

			ReserveBuyAPI api = new ReserveBuyAPI();
			ReserveResponseHandler reserveResponseHandler = new ReserveResponseHandler(
					this);
			api.performNumberedReserve(reserve, reserveResponseHandler);
		} else {
			// Caso de compra (faltan ingresar varios datos)
			Intent intent = new Intent(this, SelectTicketsActivity.class);
			intent.putExtra("showId", showId);
			intent.putExtra("priceInfo", priceInfo);
			intent.putExtra("isReserve", isReserve);

			intent.putStringArrayListExtra("selectedSeats", seatsIds);

			startActivity(intent);
		}
	}

	public void onReserveOk(String msg) {
		showSimpleAlert(msg);

	}

	public void onErrorWhenReserving(String msg) {
		showSimpleAlert(msg);

	}

}
