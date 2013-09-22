package com.cinemar.phoneticket;


import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.cinemar.phoneticket.authentication.UserClientAPI;
import com.cinemar.phoneticket.model.User;
import com.loopj.android.http.JsonHttpResponseHandler;



public class LoginActivity extends AbstractApiConsumerActivity {
	
	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;
	private User sessionUser = null;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		this.mMainView = findViewById(R.id.login_form);
		this.mStatusView = findViewById(R.id.login_status);
		this.mStatusMessageView = (TextView) findViewById(R.id.login_status_message);	
		setupActionBar();		
		

		// Set up the login form.		
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			public boolean onEditorAction(TextView textView, int id,
					KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		findViewById(R.id.register_button).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
						attemptLogin();
					}
				});

	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			// TODO: If Settings has multiple levels, Up should navigate up
			// that hierarchy.
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	/**
	 * Action on the Sign up button to redirect to Register Activity
	 */
	public void createAccountAction(View view){
		Intent intent = new Intent(this, RegisterActivity.class);
		EditText editText = (EditText) findViewById(R.id.email);
		String message = editText.getText().toString();
		intent.putExtra("mensaje para la register activity", message);
		startActivity(intent);
	}

	public void goToMainActivity(){
		Intent intent = new Intent(this, MainMenuActivity.class);
		intent.putExtra("userId", sessionUser.getEmail());
		startActivity(intent);
	}



	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);

			UserClientAPI api = new UserClientAPI();
			api.signin(mEmail, mPassword, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject user) {
					sessionUser = new User(user);
					Log.i("LoginActivity", "User Authenticated, email: " + sessionUser.getEmail());
					goToMainActivity();
				}

				public void onFailure(Throwable e, JSONObject errorResponse) {
					if (errorResponse != null) {
						handleInvalidLoginResponse(errorResponse);
					} else {
						showSimpleAlert(e.getMessage());
					}
				}
				public void onFinish() {
					showProgress(false);
				}
			});

		}
	}

	/**
	 * Si bien en todos los casos se muestra el alert, se podría querer
	 * tratar algún error de manera diferente 
	 */
	private void handleInvalidLoginResponse(JSONObject errorResponse) {
		Log.i("LoginActivity", "JSON Error response: " + errorResponse.toString());
		showSimpleAlert(errorResponse.optString("error"));
	}

}
