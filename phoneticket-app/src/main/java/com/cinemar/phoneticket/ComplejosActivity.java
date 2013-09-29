package com.cinemar.phoneticket;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cinemar.phoneticket.films.DownloadImageTask;
import com.cinemar.phoneticket.films.FilmOnClickListener;
import com.cinemar.phoneticket.model.Theatre;
import com.cinemar.phoneticket.theaters.TheatresClientAPI;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ComplejosActivity extends AbstractApiConsumerActivity {

	public ComplejosActivity() {
		super();
	}

	Map<String,Theatre> theatresMap = new HashMap<String,Theatre>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		setContentView(R.layout.activity_complejos);
		
		//** Important to get in order to use the showProgress method**//
		mMainView = findViewById(R.id.complejosScrollView);
		mStatusView = findViewById(R.id.complejos_status);
		mStatusMessageView = (TextView) findViewById(R.id.complejos_status_message);		
		
		this.requestComplejos();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.complejos, menu);
		return true;
	}

	private void requestComplejos() {
		mStatusMessageView.setText(R.string.complejos_progress_getting);
		showProgress(true);
		

		TheatresClientAPI api = new TheatresClientAPI();
		api.getTheatres(new JsonHttpResponseHandler() {	

			@Override
			public void onSuccess(JSONArray theatres) {
				Log.i("Complejos Activity", "Complejos Recibidos");
				try {					
					for (int i = 0; i < theatres.length(); i++) {
						Theatre complejo = new Theatre(theatres.getJSONObject(i));
						theatresMap.put(complejo.getId(), complejo);
						Log.i("Complejos Activity","Complejo" + theatres.getJSONObject(i)+ "recibido");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				Log.i("Complejos Activity", "Failure recibiendo complejos");
				if (errorResponse != null) {
					showSimpleAlert(errorResponse.optString("error"));
				} else {
					showSimpleAlert(e.getMessage());
				}
			}
			
			@Override public void onFailure(Throwable arg0, String arg1) {
				showSimpleAlert(arg1);			
			};
			
			public void onFinish() {
				showProgress(false);
				displayTheatres();
			}

		});

	}	
	

	private void goToCarteleraActivity(Theatre theatre) {
		Intent intent = new Intent(this, PeliculasActivity.class);
		intent.putExtra("theatreId", theatre.getId());
		intent.putExtra("theatreName", theatre.getName());
		startActivity(intent);
		
	}

	private void displayTheatres() {
		LinearLayout theatresContainer = (LinearLayout) findViewById (R.id.complejosContainer);
		
		for (Theatre theatre : theatresMap.values()) {
			//TODO: armar cada item					
		}
		
	}

}
