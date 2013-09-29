package com.cinemar.phoneticket;

import java.text.ParseException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.cinemar.phoneticket.authentication.APIAuthentication;
import com.cinemar.phoneticket.authentication.AuthenticationService;
import com.cinemar.phoneticket.model.User;
import com.cinemar.phoneticket.util.UIDateUtil;
import com.cinemar.phoneticket.util.UserDataValidatorUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class MainMyAccountActivity extends Activity {

	private String email;
	private User user;
	
	private EditText mName;
	private EditText mLastName;
	private EditText mDNI;
	private EditText mPassword;
	private EditText mPhoneNumber;
	private EditText mAddress;
	private UIDateUtil utilDate;
	
//	private View mLoginFormView;
//	private View mLoginStatusView;
//	private TextView mLoginStatusMessageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_my_account);
		
		Intent intent = getIntent();
		
		email = intent.getStringExtra("email");
		
		setTitle(email);
		addTabs();
		getUIElement();
		getDataOfServer();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_my_account, menu);
		return true;
	}
	
	private void getDataOfServer() {
		
		AuthenticationService api = new APIAuthentication();
		
		api.getUser(email, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject userData) {
				try {
					
					user = new User(userData);
					completeDataRequest();
				} catch (ParseException e) {
					showSimpleAlert("Error al obtener los datos. Intente más tarde.");
				}
			}

			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				if (errorResponse != null) {
				//	handleInvalidLoginResponse(errorResponse);
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
				//showProgress(false);
			}
		});
		
	}
	
	private void getUIElement() {

		mName = (EditText) findViewById(R.id.accountName);
		mLastName = (EditText) findViewById(R.id.accountLastName);
		mDNI = (EditText) findViewById(R.id.accountDNI);
		mPassword = (EditText) findViewById(R.id.accountPassword);
		mPhoneNumber = (EditText) findViewById(R.id.accountPhoneNumber);
		mAddress = (EditText) findViewById(R.id.accountAddress);

		utilDate = new UIDateUtil( (TextView) findViewById(R.id.accountBirthDay),
				(ImageButton) findViewById(R.id.accountBirthDayButton),
				this);

		findViewById(R.id.accountSave).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
			
				saveChanges();
			}
		});

	}
	
	@Override 
	protected Dialog onCreateDialog(int id){
		return utilDate.createDialogWindow(id);
	}
	
	private void addTabs() {
		
		TabHost tabHost = (TabHost) findViewById(R.id.tabsHost);
		tabHost.setup();
		
		TabSpec spec1 = tabHost.newTabSpec("tabFunctions");
        spec1.setIndicator("Funciones", null);
        spec1.setContent(R.id.myReservesBuys);
        tabHost.addTab(spec1);

		TabSpec spec2 = tabHost.newTabSpec("tabData");
        spec2.setIndicator("Datos", null);
        spec2.setContent(R.id.myAccountData);
        tabHost.addTab(spec2);
	}
	
	private void completeDataRequest() {
		
		mName.setText(user.getNombre());
		mLastName.setText(user.getApellido());
		mDNI.setText(user.getDni());
		mPassword.setText(user.getPassword());
		mPhoneNumber.setText(user.getTelefono());
		mAddress.setText(user.getDireccion());
		
		utilDate.setDate(user.getFechaNacimiento());
		utilDate.update();
	}

	protected void showSimpleAlert(String msg) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg);
		builder.setTitle(getString(R.string.error));
	
		builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {			
			public void onClick(DialogInterface dialog, int which) {				
			}
		});
	
		builder.show();
	}
	
	private void saveChanges() {

		resetErrors();
		View focusView = validateData();

		if (focusView != null) {
			
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.

//			mLoginStatusMessageView.setText(R.string.register_progress_registering);
//			showProgress(true);
			
			updateData();
			
			AuthenticationService authenticationClient = new APIAuthentication();
			authenticationClient.signup(user, new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(JSONObject response) {
					//no pasa nada, se queda en la misma ventana
					
					showSimpleAlert("Cambios guardados");
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
						showSimpleAlert("Error al guardar los cambios. Intente más tarde");
					}
					
					showSimpleAlert("Errorr!!!");

				}

				@Override
				public void onFailure(Throwable arg0, String arg1) {
					showSimpleAlert("Error de conexión. Intente más tarde.");
				}

				@Override
				public void onFinish() {
//					showProgress(false);
				}
			});

		}
		
	}
	
	private void assignValidationErrors(JSONObject errors) throws JSONException {

		if (errors.has("error")) {
			showSimpleAlert(errors.optString("error"));
			return;
		}

		errors = (JSONObject)errors.get("errors");
		if (errors.has("document")) {
			mDNI.requestFocus();
			mDNI.setError(errors.getJSONArray("document").get(0).toString());
		}
		if (errors.has("email")) {
			showSimpleAlert("email");
		}

	}

	private void updateData() {
		
		Calendar mBirthDate = Calendar.getInstance();
		mBirthDate.set(utilDate.getYear(), utilDate.getMonth(), utilDate.getDay());
		
		user.setNombre(mName.getText().toString());
		user.setApellido(mLastName.getText().toString());
		user.setDireccion(mAddress.getText().toString());
		user.setTelefono(mPhoneNumber.getText().toString());
		user.setDni(mDNI.getText().toString());
		user.setFechaNacimiento(mBirthDate.getTime());
		
		if ( isChangedPassword() )
			user.setPassword(mPassword.getText().toString());
	}
	
	private void resetErrors() {
		mName.setError(null);
		mLastName.setError(null);
		mDNI.setError(null);
		mPassword.setError(null);
		mPhoneNumber.setError(null);
	}
	
	private View validateData() {
		
		View focusView = null;

		if (!UserDataValidatorUtil.isValidPhoneNumber(mPhoneNumber.getText().toString(), this)) {
			mPhoneNumber.setError((UserDataValidatorUtil.getError()));
			focusView = mPhoneNumber;
		}

		if ( isChangedPassword() ) {
			//si está No vacía la pass entoces se fija que cumpla con las condiciones
			if ( !UserDataValidatorUtil.isValidPassword(mPassword.getText().toString(), this) )
			{
				mPassword.setError(UserDataValidatorUtil.getError());
				focusView = mPassword;
			}
		}
		
		if (!UserDataValidatorUtil.isValidDNI(mDNI.getText().toString(), this)) {
			mDNI.setError(UserDataValidatorUtil.getError());
			focusView = mDNI;
		}
			
		if (!UserDataValidatorUtil.isValidLastName(mLastName.getText().toString(), this)) {
			mLastName.setError(UserDataValidatorUtil.getError());
			focusView = mLastName;
		}

		if (!UserDataValidatorUtil.isValidName(mName.getText().toString(), this)) {
			
			mName.setError(UserDataValidatorUtil.getError());
			focusView = mName;
		}
		return focusView;
	}

	private boolean isChangedPassword() {
		
		return !TextUtils.isEmpty(mPassword.getText().toString());
	}
}
