package com.cinemar.phoneticket;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

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