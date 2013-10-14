package com.cinemar.phoneticket.util;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.*;

public class Sharer {
	
	private Context appContext;
	
	public Sharer(Context context) {
		appContext = context;

	}

	public Intent getTwitterClient() {
		final String[] twitterApps = {// package // name - nb installs
										// (thousands)
		"com.twitter.android", // official - 10 000
		"com.twidroid", // twidroyd - 5 000
		"com.handmark.tweetcaster", // Tweecaster - 5 000
				"com.thedeck.android" }; // TweetDeck - 5 000

		Intent tweetIntent = new Intent();
		tweetIntent.setType("text/plain");
		PackageManager packageManager = appContext.getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(
				tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

		for (int i = 0; i < twitterApps.length; i++) {
			for (ResolveInfo resolveInfo : list) {
				String p = resolveInfo.activityInfo.packageName;
				if (p != null && p.startsWith(twitterApps[i])) {
					tweetIntent.setPackage(p);
					return tweetIntent;
				}
			}
		}
		return null;
	}

}
