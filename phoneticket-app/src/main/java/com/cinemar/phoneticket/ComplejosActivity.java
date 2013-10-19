package com.cinemar.phoneticket;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cinemar.phoneticket.films.DownloadImageTask;
import com.cinemar.phoneticket.model.Theatre;
import com.cinemar.phoneticket.theaters.TheatreOnClickListener;
import com.cinemar.phoneticket.theaters.TheatresClientAPI;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.net.Uri;
import android.os.Bundle;

import android.content.ActivityNotFoundException;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ComplejosActivity extends AbstractApiConsumerActivity {

	public ComplejosActivity() {
		super();
	}

	Map<String, Theatre> theatresMap = new HashMap<String, Theatre>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_complejos);

		// ** Important to get in order to use the showProgress method**//
		mMainView = findViewById(R.id.complejosContainer);
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
						Theatre complejo = new Theatre(theatres
								.getJSONObject(i));
						theatresMap.put(complejo.getId(), complejo);
						Log.i("Complejos Activity",
								"Complejo" + theatres.getJSONObject(i)
										+ "recibido");
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

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				showSimpleAlert(getString(R.string.error_connection));    
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
		LinearLayout theatresContainer = (LinearLayout) findViewById(R.id.complejosContainer);

		for (Theatre theatre : theatresMap.values()) {
		
			//complejoLayout --> Esto esta piola definir el xml en un archivito separado y
			//conseguir la vista aca en el programa, evita definir todo desde 0 ;
			LayoutInflater inflater = LayoutInflater.from(getBaseContext()); 
			ViewGroup theatreView = (ViewGroup)inflater.inflate(R.layout.complejo_layout, null);
						
			//THEATRE TEXT
			TextView titleText = (TextView) theatreView.findViewById(R.id.complejoName);
			titleText.setText(theatre.getName());
			
			//DIRECCION
			TextView dirText = (TextView) theatreView.findViewById(R.id.complejosDireccion);			
			dirText.setText(theatre.getAddress());
			
			//PHOTO
			ImageView theatrePhotoView = (ImageView) theatreView.findViewById(R.id.complejosPhoto);			
			theatrePhotoView.setMaxHeight(50);
			theatrePhotoView.setMaxWidth(50);
			theatrePhotoView.setImageResource(R.drawable.film_cover_missing);
				
			new DownloadImageTask(theatrePhotoView).execute(theatre.getPhotoUrl());
			
			//MAP BUTTON
			ImageButton MapButtonView = (ImageButton) theatreView.findViewById(R.id.LocationButton);
			MapButtonView
					.setOnClickListener(new TheatreOnClickListener(theatre) {
						public void onClick(View arg0) {
							double latitude = theatre.getLatitude();
							double longitude = theatre.getLongitude();
							String label = theatre.getName();
							String uriBegin = "geo:" + latitude + "," + longitude;
							String query = latitude + "," + longitude + "(" + label + ")";
							String encodedQuery = Uri.encode(query);
							String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
							Uri uri = Uri.parse(uriString);
							Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
							try{
								startActivity(intent);
							}
							catch(ActivityNotFoundException e){
								showSimpleAlert(getString(R.string.missingApplication));								
							}
							
						}

					});
			
			//THEATRE FILMS BUTTON		
			ImageButton FilmButtonView = (ImageButton) theatreView.findViewById(R.id.TheatreFilmsButton);			
			FilmButtonView
			.setOnClickListener(new TheatreOnClickListener(theatre) {
				public void onClick(View arg0) {
					goToCarteleraActivity(theatre);
				}
			});			
			
			//ADD THE VIEW
			theatresContainer.addView(theatreView);								
		}

	}

}
