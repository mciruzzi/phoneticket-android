package com.cinemar.phoneticket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainMenuActivity extends Activity {
	public static int REQUEST_LOGIN = 0;

	private TextView welcomeView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		welcomeView = new TextView(this);
		setContentView(welcomeView);

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
		welcomeView.setTextSize(30);
		welcomeView.setText("Hola " + userName);
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
