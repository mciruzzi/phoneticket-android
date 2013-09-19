package com.cinemar.phoneticket;

import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainMenuActivity extends Activity {
	//Referencias a UI
	private TextView welcomeView; 
	private ImageButton peliculasButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main_menu);
		
		String userName = getIntent().getStringExtra("userId");
				
		welcomeView = (TextView) findViewById(R.id.welcome_message);
		welcomeView.setVisibility(View.VISIBLE);
	    welcomeView.setText("Hola " + userName);		    
	    
	    peliculasButton = (ImageButton) findViewById(R.id.peliculasButton); 
	    peliculasButton.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
						goToPeliculasActivity();
					}
				});		
		
	}

	protected void goToPeliculasActivity() {
		Intent intent = new Intent(this, PeliculasActivity.class);
		//TODO put extra content to peliculas Intent
		//Podria ser la sala seleccionada, si es que ya fue seleccionada
		startActivity(intent);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

}
