package com.cinemar.phoneticket;

import com.cinemar.phoneticket.reserveandbuy.OperationConstants;
import com.cinemar.phoneticket.util.AppCommunicator;
import com.cinemar.phoneticket.util.NotificationUtil;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BuyShowActivity extends AbstractApiConsumerActivity {

	private TextView mTitle;
	private TextView mCinema;
	private TextView mDate;
	private TextView mSeating;
	private ImageView mCode;
	private String mShareUrl;
	
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
		mSeating.setText("Asientos: " + intent.getStringExtra(OperationConstants.SEATING));
//		mCode (intent.getStringExtra(OperationConstants.CODE));
		mShareUrl = intent.getStringExtra(OperationConstants.SHARE_URL);
	}
	
	private void getUIElement() {

		mTitle = (TextView) findViewById(R.id.accountBuyTitle);
		mCinema = (TextView) findViewById(R.id.accountBuyCinema);
		mDate = (TextView) findViewById(R.id.accountBuyDate);
		mSeating = (TextView) findViewById(R.id.accountBuySeating);
		mCode = (ImageView) findViewById(R.id.accountBuyCode);
		
	}
	
	public void shareWithTwitter(View view) {
		AppCommunicator sharer = new AppCommunicator(this);
		Intent shareIntent= sharer.getTwitterIntent("Compre esta peli", mShareUrl);

		if (shareIntent == null ) {
			NotificationUtil.showSimpleAlert("No podra ser",getString(R.string.missingApplication),this);
			return;
		}		 			 
		startActivity(Intent.createChooser(shareIntent, "Share..."));
		

	}
	
	public void shareWithFacebook(View view) {
		AppCommunicator sharer = new AppCommunicator(this);
		Intent shareIntent= sharer.getFacebookIntent(mShareUrl);

		if (shareIntent == null ) {
			NotificationUtil.showSimpleAlert("No podra ser",getString(R.string.missingApplication),this);
			return;
		}		 			 
		startActivity(Intent.createChooser(shareIntent, "Share..."));

	}
	
	public void schedule(View view){
		NotificationUtil.showSimpleAlert("Agenda", "Agendar", this);

	}
}
