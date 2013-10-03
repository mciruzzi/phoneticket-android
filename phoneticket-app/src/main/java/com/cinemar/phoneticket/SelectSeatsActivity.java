package com.cinemar.phoneticket;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.cinemar.phoneticket.films.SeatOnClickListener;

import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class SelectSeatsActivity extends Activity {

	String[][] seats = new String[10][10];
	TableLayout cinemaLayout;
	Map<String, ImageView> seatsImages;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_select_seats);
		cinemaLayout = (TableLayout) findViewById(R.id.cinemalayout);
		seatsImages = new HashMap<String, ImageView>();

		this.displaySeats();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_seats, menu);
		return true;
	}

	private void displaySeats() {
		ImageView seat;

		LayoutParams params = new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 15, 0, 15);

		for (Integer i = 0; i < seats.length; i++) {

			TableRow fila = new TableRow(this);
			fila.setPadding(0, 10, 3, 10);
			fila.setLayoutParams(params);
			cinemaLayout.addView(fila);

			for (Integer j = 0; j < seats[i].length; j++) {
				seats[i][j] = i.toString() + j.toString();
				// supose seats[i][j] is a seat id
				String id = seats[i][j];
				seat = new ImageView(this);
				seatsImages.put(seats[i][j], seat);

				Random rnd = new Random();
				if (rnd.nextBoolean()) {
					seat.setImageResource(R.drawable.seat_available);
					seat.setOnClickListener(new SeatOnClickListener(id,	seatsImages, true));
				} else {
					seat.setImageResource(R.drawable.seat_occupied);
					seat.setOnClickListener(new SeatOnClickListener(id,	seatsImages, false));
				}

				fila.addView(seat);
			}

		}

	}

}
