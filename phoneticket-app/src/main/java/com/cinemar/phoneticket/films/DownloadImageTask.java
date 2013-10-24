package com.cinemar.phoneticket.films;

import java.io.InputStream;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Drawable> {

	private ImageView imageView;

	/*
	 * public static Drawable LoadImageFromWebOperations(String url) { try {
	 * InputStream is = (InputStream) new URL(url).getContent(); Drawable d =
	 * Drawable.createFromStream(is, "src name"); return d; } catch (Exception
	 * e) { return null; } }
	 */
	public DownloadImageTask(ImageView imageView) {
		this.imageView = imageView;
	}

	@Override
	protected Drawable doInBackground(String... params) {
		String url = params[0];
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(Drawable result) {
        //set image of your imageview
		imageView.setImageDrawable(result);  
    }

}
