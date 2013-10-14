package com.cinemar.phoneticket.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.content.pm.*;

public class AppCommunicator {
	
	private Context appContext;
	
	public AppCommunicator(Context context) {
		appContext = context;
	}

	public Intent getTwitterIntent() {
		final Set<String> twitterApps = new HashSet<String>();
		twitterApps.add("com.twitter.android");	 // official - 10 000
		twitterApps.add("com.twidroid"); //twidroyd - 5 000 
		twitterApps.add("com.handmark.tweetcaster"); //Tweecaster - 5 000
		twitterApps.add("com.thedeck.android");  // TweetDeck - 5 000		
		
		Intent tweetIntent = new Intent();
		tweetIntent.setType("text/plain");
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
	
	public Intent getFacebookIntent() {
		final Set<String> twitterApps = new HashSet<String>();
		twitterApps.add("com.facebook.katana");	
		
		Intent facebookIntent = new Intent();
		facebookIntent.setType("text/plain");
		PackageManager packageManager = appContext.getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(
				facebookIntent, PackageManager.MATCH_DEFAULT_ONLY);
		
		for (ResolveInfo resolveInfo : list) {
			String p = resolveInfo.activityInfo.packageName;
			if (p != null && twitterApps.contains(p)) {
				facebookIntent.setPackage(p);
				return facebookIntent;
			}
		}
		
		return null;
	}



}
