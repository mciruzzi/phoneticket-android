package com.cinemar.phoneticket;

import java.text.ParseException;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.cinemar.phoneticket.authentication.APIAuthentication;
import com.cinemar.phoneticket.authentication.AuthenticationService;
import com.cinemar.phoneticket.model.User;
import com.cinemar.phoneticket.util.UserDataValidatorUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

public class LoginActivity extends AbstractApiConsumerActivity {
	public static final String PREFS_NAME = "LOGIN_INFORMATION";

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	public static final String SIGNIN_ACTION = "com.cinemar.phoneticket.SIGNIN";

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;
	private User sessionUser = null;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;

	private View mMessageConfirmationLayout;
	private TextView mMessageConfirmationEmail;

	private static final int REQUEST_REGISTER = 0;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_REGISTER) {
			if (resultCode == RESULT_OK) {
				String email = data.getStringExtra("email");
				mMessageConfirmationEmail.setText(email);
				mMessageConfirmationLayout.setVisibility(View.VISIBLE);
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			}
		}
	}

	public void hideMessageConfirmation(View view) {

		mMessageConfirmationLayout.setVisibility(View.GONE);
	}

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

		mMessageConfirmationLayout = findViewById(R.id.messageConfirmationLayout);
		mMessageConfirmationEmail = (TextView) findViewById (R.id.messageConfirmationEmail);
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
		startActivityForResult(intent, REQUEST_REGISTER);
	}




	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {

		resetErrors();
		storeValues();

		View focusView = null;

		if (!UserDataValidatorUtil.isValidPassword(mPassword, this)) {

			mPasswordView.setError((UserDataValidatorUtil.getError()));
			focusView = mPasswordView;
		}

		if (!UserDataValidatorUtil.isValidEmail(mEmail, this)) {

			mEmailView.setError((UserDataValidatorUtil.getError()));
			focusView = mEmailView;
		}

		if (focusView  != null) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);

			AuthenticationService api = new APIAuthentication();
			api.login(mEmail, mPassword, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject user) {
					try {
						sessionUser = new User(user);
						completeLogin(sessionUser);
					} catch (ParseException e) {
						showSimpleAlert("Error en la identificación. Intente más tarde.");
					}
				}

				@Override
				public void onFailure(Throwable e, JSONObject errorResponse) {
					if (errorResponse != null) {
						handleInvalidLoginResponse(errorResponse);
					} else {
						showSimpleAlert(e.getMessage());
					}
				}

				@Override
				public void onFailure(Throwable arg0, String arg1) {
					showSimpleAlert("Error de conexión. Intente más tarde.");
				}

				@Override
				public void onFinish() {
					showProgress(false);
				}
			});

		}
	}

	private void storeValues() {
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
	}

	private void resetErrors() {
		mEmailView.setError(null);
		mPasswordView.setError(null);
	}

	/**
	 * Si bien en todos los casos se muestra el alert, se podría querer tratar
	 * algún error de manera diferente
	 */
	private void handleInvalidLoginResponse(JSONObject errorResponse) {
		Log.i("LoginActivity", "JSON Error response: " + errorResponse.toString());
		showSimpleAlert(errorResponse.optString("error"));
	}

	private void completeLogin(User sessionUser) {
		Log.i("LoginActivity", "User Authenticated, email: " + sessionUser.getEmail());

		// Store preferences for future uses
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		Editor edit = settings.edit();
		edit.putString("email", sessionUser.getEmail());
		edit.putString("nombre", sessionUser.getNombre());
		edit.putString("apellido", sessionUser.getApellido());
		edit.commit();

		if (getIntent().getAction().equals(SIGNIN_ACTION)) {
			Intent data = new Intent();
			data.putExtra("userId", sessionUser.getEmail());

			setResult(RESULT_OK, data);
			finish();
		} else {
			goToMainActivity();
		}
	}

	public void goToMainActivity(){
		Intent intent = new Intent(this, MainMenuActivity.class);
		intent.putExtra("userId", sessionUser.getEmail());
		startActivity(intent);
	}

}
