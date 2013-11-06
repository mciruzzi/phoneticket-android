package com.cinemar.phoneticket;

import com.cinemar.phoneticket.reserveandbuy.OperationConstants;
import com.cinemar.phoneticket.util.AppCommunicator;
import com.cinemar.phoneticket.util.NotificationUtil;
import com.cinemar.phoneticket.util.QRCodeEncoder;
import com.cinemar.phoneticket.util.QRContents;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
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
	private TextView mCongratulations;
	private String mShareUrl;
	private long mSchedulableDate;
	private boolean mNewOperation; //If coming from a recently purchasing or reserving
	
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
		mSchedulableDate = intent.getLongExtra(OperationConstants.SCHEDULABLE_DATE,0);
		mNewOperation = intent.getBooleanExtra(OperationConstants.NEW_OPERATION, false);
	}
	
	private void getUIElement() {

		Intent intent = getIntent();
		
		mTitle = (TextView) findViewById(R.id.accountBuyTitle);
		mCinema = (TextView) findViewById(R.id.accountBuyCinema);
		mDate = (TextView) findViewById(R.id.accountBuyDate);
		mSeating = (TextView) findViewById(R.id.accountBuySeating);
		mCode = (ImageView) findViewById(R.id.accountBuyCode);
		mCongratulations = (TextView)findViewById(R.id.congratulations);
		
		if(!mNewOperation)
			mCongratulations.setVisibility(View.GONE);
		
		//QR generation
		//Encode with a QR Code image		
		int smallerDimension = mCode.getWidth() < mCode.getHeight() ? mCode.getWidth() : mCode.getHeight() ;
		QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(intent.getStringExtra(OperationConstants.CODE),
		             null,
		             QRContents.Type.TEXT, 
		             BarcodeFormat.QR_CODE.toString(),
		             smallerDimension);
		try {
		    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();	   
		    mCode.setImageBitmap(bitmap);		    
		} catch (WriterException e) {
		    e.printStackTrace();
		}
	}
	
	public void shareWithTwitter(View view) {
		AppCommunicator sharer = new AppCommunicator(this);
		Intent shareIntent= sharer.getTwitterIntent("Compré esta peli", mShareUrl);

		if (shareIntent == null ) {
			NotificationUtil.showSimpleAlert("No podrá ser",getString(R.string.missingApplication),this);
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
		AppCommunicator sharer = new AppCommunicator(this);
		String title = mTitle.getText().toString();
		String description = title + " show";
		String location = mCinema.getText().toString();
		
		boolean success = sharer.scheduleCalendar(title,description,location,mSchedulableDate);
		if (success)	
			NotificationUtil.showSimpleAlert(getString(R.string.reminder_success_title), getString(R.string.reminder_success_desc), this);
		else
			NotificationUtil.showSimpleAlert(getString(R.string.no_way_title), getString(R.string.missingCalendar), this);
		

	}
}
