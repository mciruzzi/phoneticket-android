package com.cinemar.phoneticket;

import java.text.ParseException;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.cinemar.phoneticket.authentication.APIAuthentication;
import com.cinemar.phoneticket.authentication.AuthenticationService;
import com.cinemar.phoneticket.model.ItemOperation;
import com.cinemar.phoneticket.model.User;
import com.cinemar.phoneticket.reserveandbuy.GroupOperation;
import com.cinemar.phoneticket.reserveandbuy.OperationExpandableListAdapter;
import com.cinemar.phoneticket.reserveandbuy.OperationView;
import com.cinemar.phoneticket.util.NotificationUtil;
import com.cinemar.phoneticket.util.ProcessBarUtil;
import com.cinemar.phoneticket.util.UIDateUtil;
import com.cinemar.phoneticket.util.UserDataValidatorUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MainMyAccountActivity extends Activity {
	
	public final static int REQUEST_LOGIN = 0;
	public final static int REQUEST_SHOW_RESERVE = 1;
	public final static int REQUEST_SHOW_BUY = 2;

	private final static int ID_BUY = 0;
	private final static int ID_RESERVE = 1;
	
	private SparseArray<GroupOperation> groups = new SparseArray<GroupOperation>();

	private String email;
	private User user;

	private EditText mName;
	private EditText mLastName;
	private EditText mDNI;
	private EditText mPassword;
	private EditText mPhoneNumber;
	private EditText mAddress;
	private UIDateUtil utilDate;
	private ProcessBarUtil utilBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_my_account);

		SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
		email = settings.getString("email", "");

		if (email.isEmpty()) {
			requestLogin();
		} else {
			setupContent();
		}
	}

	private void setupContent() {
		setTitle(email);
		addTabs();
		getUIElement();
		getDataOfServer();
		
	    loadData();
	}

	private void requestLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.setAction(LoginActivity.SIGNIN_ACTION);
		startActivityForResult(intent, REQUEST_LOGIN);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_LOGIN) {
			if (resultCode == RESULT_OK) {
				email = data.getStringExtra("userId");
				setupContent();
			} else {
				// If the user didn't successfully login, finish account
				// activity
				finish();
			}
		} else if (requestCode == REQUEST_SHOW_RESERVE) {
			
			if (resultCode == RESULT_OK) {
				NotificationUtil.showSimpleAlert("", "La reserva ha sido cancelada", this);
			} else {
//				NotificationUtil.showSimpleAlert("Error", "La reserva no ha podido ser cancelada. Inténtelo más tarde", this);
			}			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_my_account, menu);
		return true;
	}

	private void getDataOfServer() {
		
		utilBar.setMessage(getString(R.string.load_progress));
		utilBar.showProgress(true);
		
		
		final MainMyAccountActivity activity = this;
		AuthenticationService api = new APIAuthentication();

		api.getUser(email, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject userData) {
				try {

					user = new User(userData);
					GroupOperation groupReserve = new GroupOperation("Reservas");
					
					Log.i("RESERVA", "Por tomar los datos de las reservas");

					if (userData.has("reservations")) {
						
						JSONArray reservations = userData.optJSONArray("reservations");
						
						for (int i = 0; i < reservations.length(); i++) {
							
							groupReserve.addItem(new OperationView( new ItemOperation(reservations.getJSONObject(i)),
									ReserveShowActivity.class, REQUEST_SHOW_RESERVE));
							Log.i("RESERVA", "Reserva " + reservations.getJSONObject(i) + " agregada");
						}
						
					}

					groups.append(ID_RESERVE, groupReserve);
							
				    viewConfiguration();

					completeDataRequest();
					
				} catch (ParseException e) {
					NotificationUtil.showSimpleAlert(getString(R.string.error),
							"Error al obtener los datos. Intente más tarde.",
							activity);
				}
				 catch (JSONException e) {
					Log.i("RESERVA", "Falla al parsear el JSON");
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				if (errorResponse != null) {
				//	handleInvalidLoginResponse(errorResponse);
				} else {
					NotificationUtil.showSimpleAlert(getString(R.string.error),
							"Error al guardar los datos. Intente más tarde.",
							activity);
				}
			}

			@Override
			public void onFailure(Throwable arg0, String arg1) {

				NotificationUtil.showSimpleAlert(getString(R.string.error),
						"Error de conexión. Intente más tarde.",
						activity);
			}

			@Override
			public void onFinish() {
				utilBar.showProgress(false);
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
		
		utilBar = new ProcessBarUtil( findViewById(R.id.accountForm),
			findViewById(R.id.accountBar),
			(TextView) findViewById(R.id.accountMessageStatus),
			this);
		

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


	private void saveChanges() {

		final MainMyAccountActivity activity = this;

		resetErrors();
		View focusView = validateData();

		if (focusView != null) {

			focusView.requestFocus();
		} else {

			utilBar.setMessage(getString(R.string.save_progress));
			utilBar.showProgress(true);

			updateData();

			AuthenticationService authenticationClient = new APIAuthentication();
			authenticationClient.update(user, new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(JSONObject response) {
					NotificationUtil.showSimpleAlert("",
							"Cambios guardados",
							activity);
				}

				@Override
				public void onFailure(Throwable exception, JSONObject errors) {
					try {
						if (errors != null) 
							assignValidationErrors(errors);
						else 
							NotificationUtil.showSimpleAlert(getString(R.string.error),
									exception.getMessage(),
									activity);
						
					} catch (JSONException e) {
						NotificationUtil.showSimpleAlert(getString(R.string.error),
								"Error al guardar los cambios. Intente más tarde.",
								activity);
					}				
				}

				@Override
				public void onFailure(Throwable arg0, String arg1) {
					NotificationUtil.showSimpleAlert(getString(R.string.error),
							"Error de conexión. Intente más tarde.",
							activity);
				}

				@Override
				public void onFinish() {
					utilBar.showProgress(false);
				}
			});

		}

	}

	private void assignValidationErrors(JSONObject errors) throws JSONException {

		if (errors.has("error")) {
			NotificationUtil.showSimpleAlert(getString(R.string.error),
					errors.optString("error"),
					this);
			return;
		}

		errors = (JSONObject)errors.get("errors");

		if (errors.has("document")) {
			mDNI.requestFocus();
			mDNI.setError(errors.getJSONArray("document").get(0).toString());
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
			// si está No vacía la pass entoces se fija que cumpla con las
			// condiciones
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
	
	private void viewConfiguration() {
		
		ExpandableListView listView = (ExpandableListView) findViewById(R.id.accountListViewReserveAndBuy);
	    OperationExpandableListAdapter adapter = new OperationExpandableListAdapter(this, groups);
	    listView.setAdapter(adapter);
	    listView.expandGroup(ID_BUY);
	    listView.expandGroup(ID_RESERVE);
	}

	public void loadData() {
		
		GroupOperation groupBuy = new GroupOperation("Compras");
//		GroupOperation groupReserve = new GroupOperation("Reservas");
		
		String[] seatingA = {"Q-22", "Q-5", "Q-34"};
		ItemOperation itemA = new ItemOperation("Título alguno", "PC");
		itemA.setTicketsType("Jubilado");
		itemA.setSeating(seatingA);
		
		groupBuy.addItem(new OperationView(itemA, BuyShowActivity.class, REQUEST_SHOW_BUY));

		String[] seatingB = {"R-10", "R-11", "R-23", "R-24", "R-25","R-26" };
		ItemOperation itemB = new ItemOperation("Título dos", "LH");
		itemB.setTicketsType("Miércoles");
		itemB.setSeating(seatingB);
		
		groupBuy.addItem(new OperationView(itemB, BuyShowActivity.class, REQUEST_SHOW_BUY));

		
		groups.append(ID_BUY , groupBuy);
		
//		String[] seating1 = {"A-2", "A-3", "A-4"};
//		ItemOperation item1 = new ItemOperation("Título 1", "Lavalle");
//		item1.setCode("cod-LSKJDAQWEQ2423423");
//		item1.setTicketsType("Jubilado");
//		item1.setSeating(seating1);
//		
//		groupReserve.addItem(new OperationView( item1, ReserveShowActivity.class, REQUEST_SHOW_RESERVE));
//
//		String[] seating2 = {"M-12", "M-13", "M-14"};
//		ItemOperation item2 = new ItemOperation("Título 5", "Puerto Madero");
//		item2.setCode("cod-KWJ4HK45HKWEH");
//		item2.setTicketsType("Miércoles");
//		item2.setSeating(seating2);
//		
//		groupReserve.addItem(new OperationView( item2, ReserveShowActivity.class, REQUEST_SHOW_RESERVE));
//
//		groups.append(ID_RESERVE, groupReserve);
	}

}
