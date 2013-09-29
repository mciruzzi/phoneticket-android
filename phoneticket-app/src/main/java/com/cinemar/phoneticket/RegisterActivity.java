package com.cinemar.phoneticket;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cinemar.phoneticket.authentication.APIAuthentication;
import com.cinemar.phoneticket.authentication.AuthenticationService;
import com.cinemar.phoneticket.model.User;
import com.cinemar.phonoticket.util.UIDateUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class RegisterActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;
	private String mApellido;
	private String mDni;
	private String mNombre;
	private String mTelefono;
	private User sessionUser = null;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private EditText mNombreView;
	private EditText mApellidoView;
	private EditText mDNIView;
	private EditText mTelefonoView;
	private EditText mDireccionView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	private UIDateUtil utilDate;
	
	private void returnToLoginActivity() {
		
		Intent data = new Intent();
		data.putExtra("email", sessionUser.getEmail());
	
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register);
		setupActionBar();

		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptRegister();
							return true;
						}
						return false;
					}
				});

		mNombreView = (EditText) findViewById(R.id.nombre);
		mApellidoView = (EditText) findViewById(R.id.apellido);
		mDNIView = (EditText) findViewById(R.id.dni);
		mTelefonoView = (EditText) findViewById(R.id.tel);
		mDireccionView = (EditText) findViewById(R.id.direccion);
				
		utilDate = new UIDateUtil ((TextView) findViewById(R.id.fecha_nac),
				(ImageButton) findViewById(R.id.dateButton), this);
		
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.register_button).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
						attemptRegister();
					}
				});
	}

	@Override protected Dialog onCreateDialog(int id){
		return utilDate.createDialogWindow(id);
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
	 * Attempts to register the account specified by the form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual registration attempt is made.
	 */
	public void attemptRegister() {

		resetErrors();
		storeValues();

		//sessionUser = new User(mEmail, mPassword);
		Calendar mNacimiento = Calendar.getInstance();
		mNacimiento.set(utilDate.getYear(), utilDate.getMonth(), utilDate.getDay());
		//
		sessionUser = new User(mEmail,mPassword,mNombreView.getText().toString(),
				mApellidoView.getText().toString(),mDNIView.getText().toString(),mNacimiento.getTime(),
				mDireccionView.getText().toString(),mTelefonoView.getText().toString());

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

		//Check for a valid name
		if (TextUtils.isEmpty(mNombre)) {
			mNombreView.setError(getString(R.string.error_field_required));
			focusView = mNombreView;
			cancel = true;
		}
		if (TextUtils.isEmpty(mApellido)) {
			mApellidoView.setError(getString(R.string.error_field_required));
			focusView = mApellidoView;
			cancel = true;
		}

		//Check for a valid telephone
		if (TextUtils.isEmpty(mTelefono)) {
			mTelefonoView.setError(getString(R.string.error_field_required));
			focusView = mTelefonoView;
			cancel = true;
		}

		//Check for a valid DNI
		if (TextUtils.isEmpty(mDni)) {
			mDNIView.setError(getString(R.string.error_field_required));
			focusView = mDNIView;
			cancel = true;
		}


		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.

			mLoginStatusMessageView.setText(R.string.register_progress_registering);
			showProgress(true);
			AuthenticationService authenticationClient = new APIAuthentication();
			authenticationClient.signup(sessionUser, new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(JSONObject response) {
					returnToLoginActivity();
				}

				@Override
				public void onFailure(Throwable exception, JSONObject errors) {
					try {
						if (errors != null) {
								assignValidationErrors(errors);
						} else {
							showSimpleAlert(exception.getMessage());
						}
					} catch (JSONException e) {
						showSimpleAlert("Error al ingresar. Intente más tarde");
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
		mApellido = mApellidoView.getText().toString();
		mNombre = mNombreView.getText().toString();
		mDni = mDNIView.getText().toString();
		mTelefono = mTelefonoView.getText().toString();
	}

	private void resetErrors() {
		mEmailView.setError(null);
		mPasswordView.setError(null);
		mApellidoView.setError(null);
		mDireccionView.setError(null);
		mDNIView.setError(null);
		mNombreView.setError(null);
		mDireccionView.setError(null);
		mTelefonoView.setError(null);
	}

	private void assignValidationErrors(JSONObject errors) throws JSONException {

		if (errors.has("error")) {
			showSimpleAlert(errors.optString("error"));
			return;
		}

		errors = (JSONObject)errors.get("errors");
		if (errors.has("document")) {
			mDNIView.requestFocus();
			mDNIView.setError(errors.getJSONArray("document").get(0).toString());
		}
		if (errors.has("email")) {
			mEmailView.requestFocus();
			mEmailView.setError(errors.getJSONArray("email").get(0).toString());
		}

	}
	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	private void showSimpleAlert(String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg);
		builder.setTitle(getString(R.string.error));

		builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//Ver si vuelve directo a la pantalla anterior o hace falta hacer algun intent o algo
			}
		});

		builder.show();
	}

}
