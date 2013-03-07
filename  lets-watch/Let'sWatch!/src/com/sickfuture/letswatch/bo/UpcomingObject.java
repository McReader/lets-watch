package com.sickfuture.letswatch.bo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sickfuture.letswatch.bo.common.JSONModel;

public class UpcomingObject extends JSONModel {
	
	private static final String ALTERNATE = "alternate";
	private static final String CAST = "cast";
	private static final String CLIPS = "clips";
	private static final String REVIEWS = "reviews";
	private static final String SIMILAR = "similar";
	private static final String SELF = "self";
	private static final String LINKS = "links";
	private static final String IMDB = "imdb";
	private static final String ALTERNATE_IDS = "alternate_ids";
	private static final String NAME_DIVIDER = ", ";
	private static final String NAME = "name";
	private static final String ABRIDGED_CAST = "abridged_cast";
	private static final String AUDIENCE_SCORE = "audience_score";
	private static final String CRITICS_RATING = "critics_rating";
	private static final String CRITICS_SCORE = "critics_score";
	private static final String RATINGS = "ratings";
	private static final String THEATER = "theater";
	private static final String RELEASE_DATES = "release_dates";
	private static final String RUNTIME = "runtime";
	private static final String MPAA_RATING = "mpaa_rating";
	private static final String YEAR = "year";

	public UpcomingObject(JSONObject jsonObject){
		super(jsonObject);
	}
	
	public String getId(){
		return getString(ID);
	}

	public String getTitle(){
		return getString(TITLE);
	}
	
	public String getYear(){
		return getString(YEAR);
	}
	
	public String getMpaa(){
		return getString(MPAA_RATING);
	}
	
	public String getRuntime(){
		return getString(RUNTIME);
	}
	
	public String getReleaseDateTheater(){
		return getStringFromObject(RELEASE_DATES, THEATER);
	}
	
	public String getRatingCritics(){
		return getStringFromObject(RATINGS, CRITICS_RATING);
	}
	
	public String getRatingCriticsScore(){
		return getStringFromObject(RATINGS, CRITICS_SCORE);
	}
	
	public String getRatingAudienceScore(){
		return getStringFromObject(RATINGS, AUDIENCE_SCORE);
	}
	
	public String getSynopsis(){
		return getString(SYNOPSIS);
	}
	
	public String getActorsString(){
		try {
			JSONArray cast = jsonObj.getJSONArray(ABRIDGED_CAST);
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i < cast.length()-1; i++){
				builder.append(cast.getJSONObject(i).get(NAME)+NAME_DIVIDER);
			}
			builder.append(cast.getJSONObject(cast.length()).getString(NAME));
			return builder.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return EMPTY;
	}
	
	public String getAlternateIds(){
		return getStringFromObject(ALTERNATE_IDS, IMDB);
	}
	
	//links needs to append "?apikey=[your_api_key]"
	public String getLinkSelf(){
		return getStringFromObject(LINKS, SELF);
	}
	
	public String getLinkAlternate(){
		return getStringFromObject(LINKS, ALTERNATE);
	}
	
	public String getLinkCast(){
		return getStringFromObject(LINKS, CAST);
	}
	
	public String getLinkClips(){
		return getStringFromObject(LINKS, CLIPS);
	}
	
	public String getLinkReviews(){
		return getStringFromObject(LINKS, REVIEWS);
	}
	
	public String getLinkSimilar(){
		return getStringFromObject(LINKS, SIMILAR);
	}
}
