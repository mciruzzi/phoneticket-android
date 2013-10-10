package com.cinemar.phoneticket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cinemar.phoneticket.films.DownloadImageTask;
import com.cinemar.phoneticket.films.ExpandableShowListAdapter;
import com.cinemar.phoneticket.films.FilmsClientAPI;
import com.cinemar.phoneticket.model.Film;
import com.cinemar.phoneticket.model.Show;
import com.cinemar.phoneticket.model.Theatre;
import com.loopj.android.http.JsonHttpResponseHandler;

public class PeliculasFuncionActivity extends AbstractApiConsumerActivity {

	Film mFilm;
	String theatreId;
	Show selectedShow = null;
	ExpandableShowListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader = new ArrayList<String>();
	HashMap<String, List<Show>> listDataChild = new HashMap<String,List<Show>>();
	private ImageView mYoutubeImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_peliculas_funcion);

		mFilm = new Film(getIntent().getStringExtra("filmId"), getIntent()
				.getStringExtra("filmTitle"), getIntent().getStringExtra(
				"filmSinopsis"), getIntent().getStringExtra(
				"filmYouTubeTrailer"), getIntent().getStringExtra(
				"filmCoverUrl"), getIntent().getStringExtra("filmDirector"),
				getIntent().getStringExtra("filmAudienceRating"),
				getIntent().getStringExtra("filmCast"),
				getIntent().getStringExtra("filmGenre"));

		theatreId = getIntent().getStringExtra("theatreId");		

		setTitle(mFilm.getTitle());

		// ** Important to get in order to use the showProgress method**//
		mMainView = findViewById(R.id.funciones_main_view);
		mStatusView = findViewById(R.id.funciones_status);
		mStatusMessageView = (TextView) findViewById(R.id.funciones_status_message);

		mYoutubeImage = (ImageView) findViewById(R.id.youtubeImage);
		if (mFilm.getYouTubeTrailerURL() != null && !mFilm.getYouTubeTrailerURL().isEmpty()) {
			mYoutubeImage.setClickable(true);
			mYoutubeImage.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(mFilm.getYouTubeTrailerURL()));
					startActivity(intent);
				}

			});
		} else {
			mYoutubeImage.setVisibility(View.GONE);
		}

		TextView idSinopsisText = (TextView) findViewById(R.id.sinopsisText);
		idSinopsisText.setText("Sinopsis: " +mFilm.getSynopsis());
		
		TextView genreText = (TextView) findViewById(R.id.genreText);
		genreText.setText("Genero: " +mFilm.getGenre());
		
		TextView castText = (TextView) findViewById(R.id.castText);
		castText.setText("Actores: " + mFilm.getCast());

		ImageView coverView = (ImageView) findViewById(R.id.filmCoverImage);
		new DownloadImageTask(coverView).execute(mFilm.getCoverURL());
		
		ImageButton comprarButton = (ImageButton) findViewById(R.id.comprarButton);
		comprarButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				goToSeatSelectionActivity();
			}			
		});		
				
		ImageButton reservarButton = (ImageButton) findViewById(R.id.reservarButton);
		reservarButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				goToSeatSelectionActivity();				
			}
		});
		


		this.getFunciones();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.peliculas, menu);
		return true;
	}
	
	private void goToSeatSelectionActivity() {
		if (selectedShow == null){
			showSimpleAlert(getResources().getString(R.string.no_selected_show));
			return;
		}
		Intent intent = new Intent(this, SelectSeatsActivity.class);
		intent.putExtra("showId", selectedShow.getShowId()); //hardcodeado para traer la funcion 1
		intent.putExtra("maxSelections","2"); //hardcodeado deberia seleccionarse en otra pag
		startActivity(intent);
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
        listDataChild = new HashMap<String, List<Show>>();
 
        for (Theatre cinema : mFilm.getCinemas()){
        	//TODO Formate funcion date properly
        	String key = cinema.getName()+ "\n" +cinema.getAddress();        	
        	listDataHeader.add(key);        	
        	listDataChild.put(key,cinema.getShows());        		
        }
        
        listAdapter = new ExpandableShowListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
        
        expListView.setOnChildClickListener(new OnChildClickListener() {        	 
            
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
            	
            	selectedShow = listDataChild.get(
                        listDataHeader.get(groupPosition)).get(
                        childPosition);
            	
                Toast.makeText( getApplicationContext(),listDataHeader.get(groupPosition)
                                + " : " + selectedShow.getShowId(), Toast.LENGTH_SHORT).show();            
                                 
                return false;
            }
        });
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

		@Override
		public void onFinish() {
			showProgress(false);
			prepareFuncionesList();
		}

	};

}
