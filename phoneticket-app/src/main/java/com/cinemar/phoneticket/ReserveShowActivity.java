package com.cinemar.phoneticket;

import com.cinemar.phoneticket.reserveandbuy.OperationConstants;
import com.cinemar.phoneticket.util.NotificationUtil;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class ReserveShowActivity extends Activity {

	private TextView mTitle;
	private TextView mCinema;
	private TextView mDate;
	private TextView mSeating;
	private TextView mTicketsType;
	private TextView mCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reserve_show);
		
		getUIElement();
		loadData(getIntent());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reserve_show, menu);
		return true;
	}

//
//		//hacer algo
//		// if (todo bien)
//		Intent data = new Intent();
////		data.putExtra("email", sessionUser.getEmail());
//	
//		setResult(RESULT_OK, data);
//		finish();
//	}
	
	public void cancelReserve(View view) {
		
	    AlertDialog alert = createWindowConfirmation();
	    alert.show();
	}
	
	private void doTheCancellation() {
		
    	Intent data = new Intent();
//		data.putExtra("email", sessionUser.getEmail());
    	setResult(RESULT_OK, data);
	}

	private AlertDialog createWindowConfirmation() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

	    builder.setTitle("Confirmar");
	    builder.setMessage("¿Está seguro que desea cancelar esta reserva?");

	    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

	        public void onClick(DialogInterface dialog, int which) {
	           
	        	doTheCancellation();
	        	dialog.dismiss();
	        	finish();
	        }

	    });

	    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

	        public void onClick(DialogInterface dialog, int which) {
	            dialog.dismiss();
	        }
	    });
	    
	    return builder.create();
	}
	
	private void loadData(Intent intent) {
		
		mTitle.setText(intent.getStringExtra(OperationConstants.TITLE));
		mCinema.setText(intent.getStringExtra(OperationConstants.CINEMA));
		mDate.setText(intent.getStringExtra(OperationConstants.DATE));
		mSeating.setText(intent.getStringExtra(OperationConstants.SEATING));
		mTicketsType.setText(intent.getStringExtra(OperationConstants.TICKETS_TYPE));
		mCode.setText(intent.getStringExtra(OperationConstants.CODE));
	}
	
	private void getUIElement() {

		mTitle = (TextView) findViewById(R.id.accountReserveTitle);
		mCinema = (TextView) findViewById(R.id.accountReserveCinema);
		mDate = (TextView) findViewById(R.id.accountReserveDate);
		mSeating = (TextView) findViewById(R.id.accountReserveSeating);
		mTicketsType = (TextView) findViewById(R.id.accountReserveTicketsType);
		mCode = (TextView) findViewById(R.id.accountReserveCode);	
	}
}
