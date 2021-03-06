package com.cinemar.phoneticket;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cinemar.phoneticket.util.NotificationUtil;

public class AbstractApiConsumerActivity extends FragmentActivity {

	protected View mStatusView;
	protected TextView mStatusMessageView;
	protected View mMainView;

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	protected void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mStatusView.setVisibility(View.VISIBLE);
			mStatusView.animate().setDuration(shortAnimTime)
			.alpha(show ? 1 : 0)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mStatusView.setVisibility(show ? View.VISIBLE
							: View.GONE);
				}
			});

			mMainView.setVisibility(View.VISIBLE);
			mMainView.animate().setDuration(shortAnimTime)
			.alpha(show ? 0 : 1)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mMainView.setVisibility(show ? View.GONE
							: View.VISIBLE);
				}
			});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mMainView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	protected void showSimpleAlert(String msg) {
		NotificationUtil.showSimpleAlert(getString(R.string.error), msg, this);
	}

	protected void showSimpleAlert(String msg, DialogInterface.OnClickListener listener) {
		NotificationUtil.showSimpleAlert(getString(R.string.error), msg, this, listener);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Le da el menu de opciones a todas las actitivites que heredan de ella
		getMenuInflater().inflate(R.menu.common_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_cartelera:
	            goToPeliculasActivity();
	            return true;
	        case R.id.action_complejos:
	        	goToComplejosActivity();
	            return true;
	        case R.id.action_myaccount:
	            goToMyAccountActivity();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	protected void goToComplejosActivity() {
		Intent intent = new Intent(this, ComplejosActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	protected void goToPeliculasActivity() {
		Intent intent = new Intent(this, PeliculasActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	protected void goToMyAccountActivity() {
		Intent intent = new Intent(this, MainMyAccountActivity.class);	
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		
		startActivity(intent);
	}

}