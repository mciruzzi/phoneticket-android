package com.cinemar.phoneticket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainMenuActivity extends Activity {
	//Referencias a UI
	private TextView welcomeView;
	private Button peliculasButton;
	private Button miCuentaButton;

	public static int REQUEST_LOGIN = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_menu);

		welcomeView = (TextView) findViewById(R.id.welcome_message);
		String userName = getIntent().getStringExtra("userId");

		if (userName != null) {
			displayUser(userName);
		} else {
			welcomeView.setText("Bienvenido");
		}

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
						goToLoginActivity();
					}

				});
	}

	protected void goToPeliculasActivity() {
		Intent intent = new Intent(this, PeliculasActivity.class);
		//TODO put extra content to peliculas Intent
		//Podria ser la sala seleccionada, si es que ya fue seleccionada
		startActivity(intent);
	}

	protected void goToLoginActivity() {
		String userName = getIntent().getStringExtra("userId");
		
		if (userName == null) {
			requestLogin();
		} else {
			displayUser(userName);
		}

	}

	private void requestLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.setAction(LoginActivity.SIGNIN_ACTION);
		startActivityForResult(intent, REQUEST_LOGIN);
	}

	private void displayUser(String userName) {
		welcomeView.setText("Hola aaaa" );
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_LOGIN) {
			if (resultCode == RESULT_OK) {
				String userName = data.getStringExtra("userId");
				displayUser(userName);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

}
