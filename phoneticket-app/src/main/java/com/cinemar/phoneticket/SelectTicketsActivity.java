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
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SelectTicketsActivity extends AbstractApiConsumerActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_tickets);

		setTitle(getIntent().getStringExtra("title"));

		// ** Important to get in order to use the showProgress method**//
		mMainView = findViewById(R.id.salaView);
		mStatusView = findViewById(R.id.sala_status);
		mStatusMessageView = (TextView) findViewById(R.id.sala_status_message);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_tickets, menu);
		return true;
	}

	

}
