package com.cinemar.phoneticket.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

public class ProcessBarUtil {
	
	private View processBar;
	private View form;
	private TextView messageStatus;
	private Activity activity;
	
	public ProcessBarUtil(View form, View processBar, TextView messageStatus, Activity activity) {
		
		this.processBar = processBar;
		this.form = form;
		this.messageStatus = messageStatus;
		this.activity = activity;
	}
	
	public void setMessage(String message) {
		
		messageStatus.setText(message);
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
	
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			
			int shortAnimTime = activity.getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			processBar.setVisibility(View.VISIBLE);
			processBar.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							processBar.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			form.setVisibility(View.VISIBLE);
			form.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							form.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			processBar.setVisibility(show ? View.VISIBLE : View.GONE);
			form.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
}
