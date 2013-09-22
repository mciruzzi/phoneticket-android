package com.cinemar.phoneticket.model;

import org.json.JSONObject;

public class Film {
	
	private String title;
	private String synopsis;
	private String youTubeTrailerURL;
	private String coverURL;
	
	private Film(){		
	}
	
	public Film(String title, String synopsis, String youTubeTrailerURL,
			String coverURL) {
		super();
		this.title = title;
		this.synopsis = synopsis;
		this.youTubeTrailerURL = youTubeTrailerURL;
		this.coverURL = coverURL;
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

	public Film(JSONObject JSONfilm) {
		this();
		this.title = JSONfilm.optString("title");
		this.synopsis = JSONfilm.optString("syponsis");
		this.youTubeTrailerURL = JSONfilm.optString("youtube_trailer");
		this.coverURL = JSONfilm.optString("cover_url");	
		
	}
	

}
