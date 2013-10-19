package com.cinemar.phoneticket;

import org.json.JSONObject;

import com.cinemar.phoneticket.reserveandbuy.OperationConstants;
import com.cinemar.phoneticket.reserveandbuy.ReserveBuyAPI;
import com.cinemar.phoneticket.util.AppCommunicator;
import com.cinemar.phoneticket.util.NotificationUtil;
import com.cinemar.phoneticket.util.ProcessBarUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class ReserveShowActivity extends Activity {

	private String idReserve;
	
	private TextView mTitle;
	private TextView mCinema;
	private TextView mDate;
	private TextView mSeating;
	private TextView mCode;
	private String mShareUrl;
	
	private ProcessBarUtil bar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reserve_show);
		
		getUIElement();
		loadData();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reserve_show, menu);
		return true;
	}

	public void cancelReserve(View view) {
		
	    AlertDialog alert = createWindowConfirmation();
	    alert.show();
	}
	
	private void doTheCancellation() {
		
		final ReserveShowActivity activity = this;
		
		bar.setMessage("Procesando...");
		bar.showProgress(true);

		ReserveBuyAPI api = new ReserveBuyAPI();
		
		Log.i("CANCELAR RESERVA", "Por cancelar " + idReserve );

		api.sendCancelation(idReserve, new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(JSONObject object) {
			NotificationUtil.showSimpleAlert("", "Cancelación exitosa de la reserva", activity, 
					new DialogInterface.OnClickListener() {			
						public void onClick(DialogInterface dialog, int which) {
							activity.finish();
						}
					});
		}
		
		@Override
		public void onFailure(Throwable exception, JSONObject errorResponse) {
			
			if (errorResponse != null) {
				NotificationUtil.showSimpleAlert(getString(R.string.error), 
						exception.getMessage(), activity);
			}
		}

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			NotificationUtil.showSimpleAlert(getString(R.string.error), 
					"No pudo realizarse la cancelación. Intente más tarde.", activity);
		}

		@Override
		public void onFinish() {
			bar.showProgress(false);
		}
	});

		Log.i("FIN DE CANCELAR RESERVA", "Por cancelar " + idReserve );

	}

	public void buyReserve(View view) {
		NotificationUtil.showSimpleAlert("Comprar", "Tenés q pagar!", this);
		
	}
	
	public void shareWithTwitter(View view) {		
		AppCommunicator sharer = new AppCommunicator(this);
		Intent shareIntent= sharer.getTwitterIntent("Reservé esta peli", mShareUrl);

		if (shareIntent == null ) {
			NotificationUtil.showSimpleAlert("No podrá ser", getString(R.string.missingApplication),this);
			return;
		}		 			 
		startActivity(Intent.createChooser(shareIntent, "Share..."));
		
	}
	
	public void shareWithFacebook(View view) {
		AppCommunicator sharer = new AppCommunicator(this);
		Intent shareIntent= sharer.getFacebookIntent(mShareUrl);

		if (shareIntent == null ) {
			NotificationUtil.showSimpleAlert("No podrá ser",getString(R.string.missingApplication),this);
			return;
		}		 			 
		startActivity(Intent.createChooser(shareIntent, "Share..."));
	}
	
	public void schedule(View view){
		NotificationUtil.showSimpleAlert("Agenda", "Agendar", this);
	}

	private AlertDialog createWindowConfirmation() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

	    builder.setTitle("Confirmar");
	    builder.setMessage("¿Está seguro que desea cancelar esta reserva?");

	    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {

	        public void onClick(DialogInterface dialog, int which) {
	           
	        	dialog.dismiss();
	        	doTheCancellation();
	        }

	    });

	    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

	        public void onClick(DialogInterface dialog, int which) {
	            dialog.dismiss();
	        }
	    });
	    
	    return builder.create();
	}
	
	private void loadData() {
		
		Intent intent = getIntent();
		
		idReserve = intent.getStringExtra(OperationConstants.CODE);
		mTitle.setText(intent.getStringExtra(OperationConstants.TITLE));
		mCinema.setText(intent.getStringExtra(OperationConstants.CINEMA));
		mDate.setText(intent.getStringExtra(OperationConstants.DATE));
		mSeating.setText("Asientos: " + intent.getStringExtra(OperationConstants.SEATING));
		mCode.setText("Cód.: " + idReserve );
		mShareUrl = intent.getStringExtra(OperationConstants.SHARE_URL);
		
	}
	
	private void getUIElement() {

		mTitle = (TextView) findViewById(R.id.accountReserveTitle);
		mCinema = (TextView) findViewById(R.id.accountReserveCinema);
		mDate = (TextView) findViewById(R.id.accountReserveDate);
		mSeating = (TextView) findViewById(R.id.accountReserveSeating);
		mCode = (TextView) findViewById(R.id.accountReserveCode);	
		
		bar = new ProcessBarUtil(findViewById(R.id.accountReserveForm),
				findViewById(R.id.accountReserveBar),
				(TextView) findViewById(R.id.accountReserveMessageStatus),
				this);
		
	}
}
