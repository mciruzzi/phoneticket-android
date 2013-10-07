package com.cinemar.phoneticket;

import com.cinemar.phoneticket.reserveandbuy.OperationConstants;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class BuyShowActivity extends Activity {

	private TextView mTitle;
	private TextView mCinema;
	private TextView mDate;
	private TextView mSeating;
	private TextView mTicketsType;
	private ImageView mCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_show);
		
		getUIElement();
		loadData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.buy_show, menu);
		return true;
	}

	private void loadData() {
		
		Intent intent = getIntent();
		
		mTitle.setText(intent.getStringExtra(OperationConstants.TITLE));
		mCinema.setText(intent.getStringExtra(OperationConstants.CINEMA));
		mDate.setText(intent.getStringExtra(OperationConstants.DATE));
		mSeating.setText(intent.getStringExtra(OperationConstants.SEATING));
		mTicketsType.setText(intent.getStringExtra(OperationConstants.TICKETS_TYPE));
//		mCode (intent.getStringExtra(OperationConstants.CODE));
	}
	
	private void getUIElement() {

		mTitle = (TextView) findViewById(R.id.accountBuyTitle);
		mCinema = (TextView) findViewById(R.id.accountBuyCinema);
		mDate = (TextView) findViewById(R.id.accountBuyDate);
		mSeating = (TextView) findViewById(R.id.accountBuySeating);
		mTicketsType = (TextView) findViewById(R.id.accountBuyTicketsType);
		mCode = (ImageView) findViewById(R.id.accountBuyCode);	
	}
}
