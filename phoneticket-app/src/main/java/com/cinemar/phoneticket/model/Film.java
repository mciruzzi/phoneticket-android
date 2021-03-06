package com.cinemar.phoneticket.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Film {

	private String Id;
	private String title;
	private String synopsis;
	private String youTubeTrailerURL;
	private String coverURL;
	private String director;
	private String audienceRating;
	private String cast;
	private String genre;
	private List<Theatre> cinemas;
	private String shareURL;

	private Film() {
		this.cinemas = new ArrayList<Theatre>();
	}

	public Film(String Id, String title, String synopsis,
			String youTubeTrailerURL, String coverURL, String director,
			String audienceRating, String cast, String genre, String shareURL) {
		super();
		this.Id = Id;
		this.title = title;
		this.synopsis = synopsis;
		this.youTubeTrailerURL = youTubeTrailerURL;
		this.coverURL = coverURL;
		this.director = director;
		this.cast = cast;
		this.genre = genre;
		this.audienceRating = audienceRating;
		this.shareURL = shareURL;
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
		this.director = JSONfilm.optString("director");		
		this.audienceRating = JSONfilm.optString("audience_rating");		
		this.cast = JSONfilm.optString("cast");		
		this.genre = JSONfilm.optString("genre");
		this.shareURL = JSONfilm.optString("share_url");
	}

	public String getShareURL() {
		return shareURL;
	}

	public void setShareURL(String shareURL) {
		this.shareURL = shareURL;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getAudienceRating() {
		return audienceRating;
	}

	public void setAudienceRating(String audienceRating) {
		this.audienceRating = audienceRating;
	}

	public String getCast() {
		return cast;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public void addFunciones(JSONObject filmShows) throws JSONException {
		JSONArray cinemasArray = filmShows.optJSONArray("theatres");

		for (int i = 0; i < cinemasArray.length(); i++) {
			Theatre cinema = new Theatre(cinemasArray.getJSONObject(i));
			this.cinemas.add(cinema);
		}

	}

	public void clearFunciones() {
		this.cinemas.clear();
	}

	public List<Theatre> getCinemas() {
		return cinemas;
	}

}
