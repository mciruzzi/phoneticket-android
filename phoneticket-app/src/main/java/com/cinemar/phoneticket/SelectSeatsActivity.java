package com.cinemar.phoneticket;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.cinemar.phoneticket.films.SeatOnClickListener;
import com.cinemar.phoneticket.model.Room;
import com.cinemar.phoneticket.model.Room.Seat;
import com.cinemar.phoneticket.model.SeatStatus;
import com.cinemar.phoneticket.theaters.TheatresClientAPI;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SelectSeatsActivity extends AbstractApiConsumerActivity {

	private String showId;
	//String[][] seats = new String[20][20];
	private Room showRoom;
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
		if (getIntent().getStringExtra("maxSelections") != null)
			//supongamos que viene dicho de afuera si hay una pantalla extra donde se dice que cantidad de butacas se va a comprar
			maxSeatsToTake = Integer.parseInt(getIntent().getStringExtra("maxSelections"));
		

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
			public void onSuccess(JSONObject room) {
				Log.i("SelectSeats Activity", "Complejos Recibidos");
				try {
					showRoom = new Room(room);
					Log.i("SelectSeats Activity", "Seats" + room + "recibido");
				} catch (JSONException e) {
					e.printStackTrace();
				}
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
				displaySeats();
			}

		});

	}

	private void displaySeats() {
		ImageView seatView;

		LayoutParams params = new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 15, 10, 15);

		for (Integer i = 0; i < showRoom.getRowsLength(); i++) {

			TableRow fila = new TableRow(this);
			fila.setPadding(15, 10, 15, 10);
			fila.setLayoutParams(params);
			cinemaLayout.addView(fila);

			for (Integer j = 0; j < showRoom.getColumnsLength() ; j++) {
				Seat seatModel = showRoom.getSeat(i, j);				
				
				seatView = new ImageView(this);
				seatView.setPadding(2, 0, 2, 0);
				seatsImages.put(seatModel.getId(), seatView);		
				
				if (seatModel.getStatus().equals(SeatStatus.NON_EXISTENT)){
					seatView.setImageResource(R.drawable.seat_available);// podria ser cualquier otro
					seatView.setVisibility(View.INVISIBLE); //la hago invisible aunque quizas no sea lo mejor 
				}
				
				else if (seatModel.getStatus().equals(SeatStatus.AVAILABLE)) {
					seatView.setImageResource(R.drawable.seat_available);
					seatView.setOnClickListener(new SeatOnClickListener(seatModel,
							seatsImages, true,SelectedSeats,maxSeatsToTake));
				} 
				else if (seatModel.getStatus().equals(SeatStatus.OCCUPIED)) {
					seatView.setImageResource(R.drawable.seat_occupied);
					seatView.setOnClickListener(new SeatOnClickListener(seatModel,
							seatsImages, false,SelectedSeats,maxSeatsToTake));
				}

				fila.addView(seatView);
			}

		}

	}

}