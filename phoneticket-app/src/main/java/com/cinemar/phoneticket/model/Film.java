package com.cinemar.phoneticket.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;

public class Film {

	private String Id;
	private String title;
	private String synopsis;
	private String youTubeTrailerURL;
	private String coverURL;
	private List<Theatre> cinemas;

	private Film() {
		this.cinemas = new ArrayList<Theatre>();
	}

	public Film(String Id, String title, String synopsis,
			String youTubeTrailerURL, String coverURL) {
		super();
		this.Id = Id;
		this.title = title;
		this.synopsis = synopsis;
		this.youTubeTrailerURL = youTubeTrailerURL;
		this.coverURL = coverURL;
		this.cinemas = new ArrayList<Theatre>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getYouTubeTrailerURL() {
		return youTubeTrailerURL;
	}

	public void setYouTubeTrailerURL(String youTubeTrailerURL) {
		this.youTubeTrailerURL = youTubeTrailerURL;
	}

	public String getCoverURL() {
		return coverURL;
	}

	public void setCoverURL(String coverURL) {
		this.coverURL = coverURL;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public Film(JSONObject JSONfilm) {
		this();
		this.setId(JSONfilm.optString("id"));
		this.title = JSONfilm.optString("title");
		this.synopsis = JSONfilm.optString("synopsis");
		this.youTubeTrailerURL = JSONfilm.optString("youtube_trailer");
		this.coverURL = JSONfilm.optString("cover_url");

	}
	
	public void addFunciones(JSONObject filmShows) throws JSONException {
		String cinemasArrString = filmShows.optString("theatres");
		JSONArray cinemasArray = filmShows.optJSONArray("theatres");

		for (int i = 0; i < cinemasArray.length(); i++) {
			Theatre cinema = new Theatre(cinemasArray.getJSONObject(i));			
			this.cinemas.add(cinema);
		}

	}

	public void clearFunciones() {
		this.cinemas.clear();
	}
	

}
