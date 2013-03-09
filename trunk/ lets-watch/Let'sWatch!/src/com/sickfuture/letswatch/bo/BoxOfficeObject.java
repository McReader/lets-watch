package com.sickfuture.letswatch.bo;

import org.json.JSONObject;

import com.sickfuture.letswatch.bo.common.JSONModel;

public class BoxOfficeObject extends JSONModel {

	public BoxOfficeObject(JSONObject jsonObject) {
		super(jsonObject);
	}

	@Override
	public String getId() {
		return super.getId();
	}

	@Override
	public String getTitle() {
		return super.getTitle();
	}

	@Override
	public String getYear() {
		return super.getYear();
	}

	@Override
	public String getMpaa() {
		return super.getMpaa();
	}

	@Override
	public String getRuntime() {
		return super.getRuntime();
	}

	@Override
	public String getReleaseDateTheater() {
		return super.getReleaseDateTheater();
	}

	@Override
	public String getRatingCritics() {
		return super.getRatingCritics();
	}

	@Override
	public String getRatingCriticsScore() {
		return super.getRatingCriticsScore();
	}

	@Override
	public String getRatingAudienceScore() {
		return super.getRatingAudienceScore();
	}

	@Override
	public String getSynopsis() {
		return super.getSynopsis();
	}

	@Override
	public String getActorsString() {
		return super.getActorsString();
	}

	@Override
	public String getAlternateIds() {
		return super.getAlternateIds();
	}

	@Override
	public String getLinkSelf() {
		return super.getLinkSelf();
	}

	@Override
	public String getLinkAlternate() {
		return super.getLinkAlternate();
	}

	@Override
	public String getLinkCast() {
		return super.getLinkCast();
	}

	@Override
	public String getLinkClips() {
		return super.getLinkClips();
	}

	@Override
	public String getLinkReviews() {
		return super.getLinkReviews();
	}

	@Override
	public String getLinkSimilar() {
		return super.getLinkSimilar();
	}

	@Override
	public String getPosters(int posterType) {
		return super.getPosters(posterType);
	}

	public String getRatingAudience(){
		return getStringFromObject(RATINGS, AUDIENCE_RATING);
	}
	
	public String getCriticConsensus() {
		return getString(CRITICS_CONSENSUS);
	}
	
}
