package com.cinemar.phoneticket;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

public class MainMenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String userName = getIntent().getStringExtra("userId");
				
	    // Create the text view
	    TextView welcomeView = new TextView(this);
	    welcomeView.setTextSize(30);
	    welcomeView.setText("Hola " + userName);

		setContentView(welcomeView);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

}
