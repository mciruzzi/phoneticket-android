package com.cinemar.phoneticket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cinemar.phoneticket.films.DownloadImageTask;
import com.cinemar.phoneticket.films.ExpandableListAdapter;
import com.cinemar.phoneticket.films.FilmOnClickListener;
import com.cinemar.phoneticket.films.FilmsClientAPI;
import com.cinemar.phoneticket.model.Film;
import com.cinemar.phoneticket.model.Show;
import com.cinemar.phoneticket.model.Theatre;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PeliculasFuncionActivity extends AbstractApiConsumerActivity {

	Film mFilm;
	String theatreId;
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader = new ArrayList<String>();
	HashMap<String, List<String>> listDataChild = new HashMap<String,List<String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_peliculas_funcion);

		mFilm = new Film(getIntent().getStringExtra("filmId"), getIntent()
				.getStringExtra("filmTitle"), getIntent().getStringExtra(
				"filmSinopsis"), getIntent().getStringExtra(
				"filmYouTubeTrailer"), getIntent().getStringExtra(
				"filmCoverUrl"));

		theatreId = getIntent().getStringExtra("theatreId");
		
		// ** Important to get in order to use the showProgress method**//
		mMainView = findViewById(R.id.funciones_main_view);
		mStatusView = findViewById(R.id.funciones_status);
		mStatusMessageView = (TextView) findViewById(R.id.funciones_status_message);

		TextView idPeliculaText = (TextView) findViewById(R.id.filmTitleText);
		idPeliculaText.setText(mFilm.getTitle());

		TextView idSinopsisText = (TextView) findViewById(R.id.sinopsisText);
		idSinopsisText.setText(mFilm.getSynopsis());

		ImageView coverView = (ImageView) findViewById(R.id.filmCoverImage);
		new DownloadImageTask(coverView).execute(mFilm.getCoverURL());
		
		this.getFunciones();		   
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.peliculas, menu);
		return true;
	}

	private void getFunciones() {
		mStatusMessageView.setText(R.string.funciones_progress_getting);
		showProgress(true);

		FilmsClientAPI api = new FilmsClientAPI();
		if(theatreId == null){ //Funciones sin filtrado
			api.getFunciones(mFilm, responseHandler);
		}
		else{
			api.getFuncionesEnComplejo(mFilm.getId(), theatreId, responseHandler);
		}
	}		
	
    private void prepareFuncionesList() {
        expListView = (ExpandableListView) findViewById(R.id.funcionesList);
    	
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
 
        for (Theatre cinema : mFilm.getCinemas()){
        	//TODO Formate funcion date properly
        	String key = cinema.getName()+ "\n" +cinema.getAddress(); 
        	listDataHeader.add(key);
        	
        	List<String> showsDate = new ArrayList<String>();
        	for (Show show : cinema.getShows()){
        		showsDate.add(show.getStartTimeString());        		
        	}	
        	
        	listDataChild.put(key,showsDate);        		
        }
        
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

	

	JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(JSONObject film) {
			Log.i("Funciones Activity", "Funciones Recibidas");
			Log.i("Funciones Activity",	"Funciones" + film + "recibida");
			
			mFilm.clearFunciones();
			try {
				mFilm.addFunciones(film);
			} catch (JSONException e) {
				this.onFailure(e, "Error while parsing funciones JSON object");
			}
		}

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			showSimpleAlert(arg1);
		};

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
			Log.i("Funciones Activity", "Failure pidiendo funciones");
			if (errorResponse != null) {
				showSimpleAlert(errorResponse.optString("error"));
			} else {
				showSimpleAlert(e.getMessage());
			}
		}

		public void onFinish() {
			showProgress(false);
			prepareFuncionesList();
		}

	};

}
