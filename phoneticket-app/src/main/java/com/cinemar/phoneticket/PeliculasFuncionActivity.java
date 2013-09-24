package com.cinemar.phoneticket;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class PeliculasFuncionActivity extends AbstractApiConsumerActivity {
	
	String filmId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String filmId = getIntent().getStringExtra("filmId");
		
		setContentView(R.layout.activity_peliculas_funcion);
		
		//** Important to get in order to use the showProgress method**//
		mMainView = findViewById(R.id.funciones_main_view);
		mStatusView = findViewById(R.id.funciones_status);
		mStatusMessageView = (TextView) findViewById(R.id.funciones_status_message);
		
		TextView idPeliculaText = (TextView) findViewById(R.id.funciones_pelicula_text);
		idPeliculaText.setText("Pelicula Id: "+filmId);
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.peliculas, menu);
		return true;
	}


}
