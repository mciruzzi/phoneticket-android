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
			
		AlertDialog.Builder alertDialog = createDialog(title, message, activity);
		
		alertDialog.setPositiveButton("Aceptar", listener);
		alertDialog.show();
	}
	
	static public AlertDialog.Builder createDialog (String title, String message, Activity activity) {
		
		QustomDialogBuilder qustomDialogBuilder = new QustomDialogBuilder(activity).
				setTitle(title).
				setTitleColor("#400080").
				setDividerColor("#400080").
				setMessage(message);

		return qustomDialogBuilder;
	}

}
