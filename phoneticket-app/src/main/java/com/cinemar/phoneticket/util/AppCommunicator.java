package com.cinemar.phoneticket.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;

public class AppCommunicator {

	private final Context appContext;

	public AppCommunicator(Context context) {
		appContext = context;
	}

	public Intent getTwitterIntent(String msj, String url) {
		final Set<String> twitterApps = new HashSet<String>();
		twitterApps.add("com.twitter.android"); // official - 10 000
		twitterApps.add("com.twidroid"); // twidroyd - 5 000
		twitterApps.add("com.handmark.tweetcaster"); // Tweecaster - 5 000
		twitterApps.add("com.thedeck.android"); // TweetDeck - 5 000

		Intent tweetIntent = new Intent();
		tweetIntent.setType("text/plain");
		tweetIntent.putExtra(Intent.EXTRA_TEXT, msj + "  " + url);

		PackageManager packageManager = appContext.getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(
				tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

		for (ResolveInfo resolveInfo : list) {
			String p = resolveInfo.activityInfo.packageName;
			if (p != null && twitterApps.contains(p)) {
				tweetIntent.setPackage(p);
				return tweetIntent;
			}
		}

		return null;
	}

	public Intent getFacebookIntent(String url) {
		final Set<String> fbApps = new HashSet<String>();
		fbApps.add("com.facebook.katana");
		// Are more facebook apps if needed

		Intent facebookIntent = new Intent();
		facebookIntent.setType("text/plain");
		// aparentemente facebook tiene un bug con esto y solo soporta el pasaje
		// de urls
		facebookIntent.putExtra(Intent.EXTRA_TEXT, url);

		PackageManager packageManager = appContext.getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(
				facebookIntent, PackageManager.MATCH_DEFAULT_ONLY);

		for (ResolveInfo resolveInfo : list) {
			String p = resolveInfo.activityInfo.packageName;
			if (p != null && fbApps.contains(p)) {
				facebookIntent.setPackage(p);
				return facebookIntent;
			}
		}

		return null;
	}

	public boolean scheduleCalendar(String title, String desc, String location, Long time) {
		String[] projection = new String[] { "_id", "name" };
		Uri calendars = Uri.parse("content://com.android.calendar/calendars");

		Cursor managedCursor = appContext.getContentResolver().query(calendars,
				projection, null, null, null);

		if (managedCursor.moveToFirst()) {
			String calName;
			String calId;
			int nameColumn = managedCursor.getColumnIndex("name");
			int idColumn = managedCursor.getColumnIndex("_id");

			do {
				calName = managedCursor.getString(nameColumn);
				calId = managedCursor.getString(idColumn);
				if (calName.contains("calendar")) // Por defecto pongo este (android),pero podria ser el de gmail o cualquier otro
					break;
			} while (managedCursor.moveToNext());

			ContentValues event = new ContentValues();
			event.put("calendar_id", calId);
			event.put("title", title);
			event.put("description", desc);
			event.put("eventLocation", location);
			long startTime = time; //
			long endTime = startTime + 3600000; // TODO cambiar 2 horas x defecto de duracion
			event.put("dtstart", startTime);
			event.put("dtend", endTime);
			event.put("allDay", 0); // 0 false, 1 true
			event.put("eventStatus", 1); //  tentative (0), confirmed (1) or canceled (2):
			event.put("eventTimezone", "Argentina/Buenos Aires"); //
//			event.put("visibility", 0); //  x defecto(0) ,confidencial (1) publico(3) o privado(2)
//			event.put("transparency", 0); // opaque (0) or transparent (1).
			event.put("hasAlarm", 1); // 0 false, 1 true

			Uri eventsUri = Uri.parse("content://com.android.calendar/events");
			Uri eventUri = appContext.getContentResolver().insert(eventsUri, event);

			long id = Long.parseLong(eventUri.getLastPathSegment());

		    // reminder insert
	        Uri REMINDERS_URI = Uri.parse("content://com.android.calendar/reminders");
	        ContentValues values = new ContentValues();
	        values = new ContentValues();
	        values.put( "event_id", id);
	        values.put( "method", 1 );
	        values.put( "minutes", 0 );
	        appContext.getContentResolver().insert( REMINDERS_URI, values );

			return true;
		}
		return false; //No existen calendarios

	}

}
