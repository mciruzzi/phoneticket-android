package com.cinemar.phoneticket.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class NotificationUtil {
	
	static public void showSimpleAlert(String title, String message, Activity activity) {
				
		showSimpleAlert(title, message, activity, new DialogInterface.OnClickListener() {			
			public void onClick(DialogInterface dialog, int which) {				
			}
		});
	}
	
	static public void showSimpleAlert(String title, String message, Activity activity, DialogInterface.OnClickListener listener) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(message);
		builder.setTitle(title);
	
		builder.setPositiveButton("Aceptar",listener);
	
		builder.show();
	}

}
