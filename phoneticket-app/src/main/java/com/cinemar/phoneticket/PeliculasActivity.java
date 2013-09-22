package com.cinemar.phoneticket;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cinemar.phoneticket.authentication.FilmsClientAPI;
import com.cinemar.phoneticket.model.Film;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;


public class PeliculasActivity extends Activity {
	
	List<Film> filmsList = new ArrayList<Film>(); 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_peliculas);
		
		this.requestPeliculas();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.peliculas, menu);
		return true;
	}	
	
	private void requestPeliculas() {		

		FilmsClientAPI api = new FilmsClientAPI();
		api.getFilms(new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(JSONObject films) {				
				Log.i("Peliculas Activity", "Peliculas Recibidas Como Object");
			}
			
			@Override
			public void onSuccess(JSONArray films) {				
				Log.i("Peliculas Activity", "Peliculas Recibidas");
				
				for (int i=0 ; i < films.length()-1 ; i++ ){
					try {
						filmsList.add(new Film(films.getJSONObject(i)));
						Log.i("Peliculas Activity", "Pelicula" + films.getJSONObject(i) +"recibida" );
					} catch (JSONException e) {					 
						e.printStackTrace();
					}					
				}			
			}

			public void onFailure(Throwable e, JSONObject errorResponse) {
				Log.i("Peliculas Activity", "ERROR PAPA");
				if (errorResponse != null) {					
					//handleInvalidLoginResponse(errorResponse);
				} else {
					//showSimpleAlert(e.getMessage());
				}
			}
		});

	}

}
