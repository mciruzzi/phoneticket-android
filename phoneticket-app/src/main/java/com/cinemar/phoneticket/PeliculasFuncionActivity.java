package com.cinemar.phoneticket;

import com.cinemar.phoneticket.films.DownloadImageTask;
import com.cinemar.phoneticket.films.FilmOnClickListener;
import com.cinemar.phoneticket.model.Film;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PeliculasFuncionActivity extends AbstractApiConsumerActivity {

	Film mFilm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		mFilm = new Film(getIntent().getStringExtra("filmId"), getIntent().getStringExtra("filmTitle"), getIntent().getStringExtra("filmSinopsis"), getIntent().getStringExtra("filmYouTubeTrailer"), getIntent().getStringExtra("filmCoverUrl"));
		
		setContentView(R.layout.activity_peliculas_funcion);
		
		//** Important to get in order to use the showProgress method**//
		mMainView = findViewById(R.id.funciones_main_view);
		mStatusView = findViewById(R.id.funciones_status);
		mStatusMessageView = (TextView) findViewById(R.id.funciones_status_message);
		
		TextView idPeliculaText = (TextView) findViewById(R.id.filmTitleText);
		idPeliculaText.setText(mFilm.getTitle());
		
		TextView idSinopsisText = (TextView) findViewById(R.id.sinopsisText);
		idSinopsisText.setText(mFilm.getSynopsis());
		
		ImageView coverView = (ImageView) findViewById(R.id.filmCoverImage);					
		new DownloadImageTask(coverView).execute(mFilm.getCoverURL());
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.peliculas, menu);
		return true;
	}

}
