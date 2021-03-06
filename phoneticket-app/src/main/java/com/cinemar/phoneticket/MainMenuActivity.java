package com.cinemar.phoneticket;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainMenuActivity extends AbstractApiConsumerActivity {
	//Referencias a UI
	private TextView welcomeView;
	private Button peliculasButton;
	private Button complejosButton;
	private Button miCuentaButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_menu);

		welcomeView = (TextView) findViewById(R.id.welcome_message);
		peliculasButton = (Button) findViewById(R.id.peliculasButton);
		peliculasButton.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
						goToPeliculasActivity();
					}
		
				});

		miCuentaButton = (Button) findViewById(R.id.miCuentaButton);
		miCuentaButton.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
						goToMyAccountActivity();
					}

				});
		complejosButton = (Button) findViewById(R.id.complejoButton);
		complejosButton.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						goToComplejosActivity();
					}
				});
	}
	


	@Override
	protected void onResume() {
		super.onResume();

		SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
		String userName = settings.getString("nombre", "");

		if (!userName.isEmpty()) {
			welcomeView.setText("Bienvenido " + userName);
		} else {
			welcomeView.setText("Bienvenido");
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	

}
