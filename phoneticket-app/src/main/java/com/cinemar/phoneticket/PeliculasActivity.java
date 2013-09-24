package com.cinemar.phoneticket;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cinemar.phoneticket.films.DownloadImageTask;
import com.cinemar.phoneticket.films.FilmOnClickListener;
import com.cinemar.phoneticket.films.FilmsClientAPI;
import com.cinemar.phoneticket.model.Film;
import com.loopj.android.http.JsonHttpResponseHandler;

public class PeliculasActivity extends AbstractApiConsumerActivity {
	
	public PeliculasActivity() {
		super();
	}

	List<Film> filmsList = new ArrayList<Film>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		setContentView(R.layout.activity_peliculas);
		
		//** Important to get in order to use the showProgress method**//
		mMainView = findViewById(R.id.peliculasHorizontalScrollView);
		mStatusView = findViewById(R.id.peliculas_status);
		mStatusMessageView = (TextView) findViewById(R.id.peliculas_status_message);		
		
		HorizontalScrollView mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.peliculasHorizontalScrollView);

		this.requestPeliculas();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.peliculas, menu);
		return true;
	}

	private void requestPeliculas() {
		mStatusMessageView.setText(R.string.peliculas_progress_getting);
		showProgress(true);
		

		FilmsClientAPI api = new FilmsClientAPI();
		api.getFilms(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject films) {
				Log.i("Peliculas Activity", "Peliculas Recibidas Como Object");
			}

			@Override
			public void onSuccess(JSONArray films) {
				Log.i("Peliculas Activity", "Peliculas Recibidas");
				try {					
					for (int i = 0; i < films.length(); i++) {
						filmsList.add(new Film(films.getJSONObject(i)));
						Log.i("Peliculas Activity","Pelicula" + films.getJSONObject(i)+ "recibida");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				Log.i("Peliculas Activity", "ERROR PAPA");
				if (errorResponse != null) {
					showSimpleAlert(errorResponse.optString("error"));
				} else {
					showSimpleAlert(e.getMessage());
				}
			}
			
			public void onFinish() {
				showProgress(false);
				displayFilms();
			}

		});

	}	
	

	private void goToFuncionActivity(String filmId) {
		Intent intent = new Intent(this, PeliculasFuncionActivity.class);
		intent.putExtra("filmId", filmId);
		startActivity(intent);
		
	}

	private void displayFilms() {
		LinearLayout imageContainer = (LinearLayout) findViewById (R.id.peliculasImageContainer);
		
		for (Film film : filmsList) {

			ImageView imageView = new ImageView(this);
			imageView.setOnClickListener(new FilmOnClickListener(film.getId()) {			
				public void onClick(View arg0) {					
					goToFuncionActivity(filmId); 
				}

			});
			
			imageView.setImageResource(R.drawable.film_cover_missing);			
			new DownloadImageTask(imageView).execute(film.getCoverURL());		
	
			//position parameters	
			imageView.setPadding(10, 10, 10, 10);		
			//TODO investigate a bit more how to resize images
						
			imageContainer.addView(imageView);
			
		}
		
	}

}
