package com.cinemar.phoneticket;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MessageConfirmationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_confirmation);
		
		String email = getIntent().getStringExtra("email");
		TextView field = (TextView) findViewById(R.id.textViewEmailMessageConfirmation);
		
		field.setText(email);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message_confirmation, menu);
		return true;
	}
	
	public void goBack(View view){	
		finish();		
	}

}
