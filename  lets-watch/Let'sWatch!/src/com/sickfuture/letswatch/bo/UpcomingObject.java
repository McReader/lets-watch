package com.sickfuture.letswatch.bo;

import org.json.JSONException;
import org.json.JSONObject;

import com.sickfuture.letswatch.bo.common.JSONModel;

public class UpcomingObject extends JSONModel {
	
	private static final String LOG_TAG = "UpcomingObject";
	

	public UpcomingObject(JSONObject jsonObject){
		super(jsonObject);
	}


	@Override
	public String getId() throws JSONException {
		return super.getId();
	}


	@Override
	public String getTitle() throws JSONException {
		return super.getTitle();
	}


	@Override
	public String getYear() throws JSONException {
		return super.getYear();
	}


	@Override
	public String getMpaa() throws JSONException {
		return super.getMpaa();
	}


	@Override
	public String getRuntime() throws JSONException {
		return super.getRuntime();
	}


	@Override
	public String getReleaseDateTheater() throws JSONException {
		return super.getReleaseDateTheater();
	}


	@Override
	public String getRatingCritics() throws JSONException {
		return super.getRatingCritics();
	}


	@Override
	public String getRatingCriticsScore() throws JSONException {
		return super.getRatingCriticsScore();
	}


	@Override
	public String getRatingAudienceScore() throws JSONException {
		return super.getRatingAudienceScore();
	}


	@Override
	public String getSynopsis() throws JSONException {
		return super.getSynopsis();
	}


	@Override
	public String getActorsString() throws JSONException {
		return super.getActorsString();
	}


	@Override
	public String getAlternateIds() throws JSONException {
		return super.getAlternateIds();
	}


	@Override
	public String getLinkSelf() throws JSONException {
		return super.getLinkSelf();
	}


	@Override
	public String getLinkAlternate() throws JSONException {
		return super.getLinkAlternate();
	}


	@Override
	public String getLinkCast() throws JSONException {
		return super.getLinkCast();
	}


	@Override
	public String getLinkClips() throws JSONException {
		return super.getLinkClips();
	}


	@Override
	public String getLinkReviews() throws JSONException {
		return super.getLinkReviews();
	}


	@Override
	public String getLinkSimilar() throws JSONException {
		return super.getLinkSimilar();
	}


	@Override
	public String getPosters(int posterType) {
		return super.getPosters(posterType);
	}
	

}
