package com.sickfuture.letswatch.bo.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONModel {

	private static final String LOG_TAG = "JSONModel";
	
	protected static final String EMPTY = "";
	
	protected static final String NEXT = "next";
	protected static final String LINKS = "links";
	protected static final String POSTERS = "posters";
	protected static final String ALTERNATE = "alternate";
	protected static final String CAST = "cast";
	protected static final String CLIPS = "clips";
	protected static final String REVIEWS = "reviews";
	protected static final String SIMILAR = "similar";
	protected static final String SELF = "self";
	protected static final String IMDB = "imdb";
	protected static final String ALTERNATE_IDS = "alternate_ids";
	protected static final String NAME_DIVIDER = ", ";
	protected static final String NAME = "name";
	protected static final String ABRIDGED_CAST = "abridged_cast";
	protected static final String AUDIENCE_RATING = "audience_rating";
	protected static final String AUDIENCE_SCORE = "audience_score";
	protected static final String CRITICS_RATING = "critics_rating";
	protected static final String CRITICS_SCORE = "critics_score";
	protected static final String RATINGS = "ratings";
	protected static final String THEATER = "theater";
	protected static final String RELEASE_DATES = "release_dates";
	protected static final String RUNTIME = "runtime";
	protected static final String MPAA_RATING = "mpaa_rating";
	protected static final String YEAR = "year";
	protected static final String CRITICS_CONSENSUS = "critics_consensus";
	protected static final String SYNOPSIS = "synopsis";
	protected static final String TITLE = "title";
	protected static final String ID = "id";

	protected JSONObject jsonObj;

	public static final int THUMBNAIL = 0;

	public static final int PROFILE = 1;

	public static final int DETAILED = 2;

	public static final int ORIGINAL = 3;

	public JSONModel(JSONObject jsonObject) {
		jsonObj = jsonObject;
	}

	public JSONModel(String jsonString) {
		try {
			jsonObj = new JSONObject(jsonString);
		} catch (JSONException e) {
			throw new IllegalArgumentException("json is not valid");
		}
	}

	public JSONModel() {
		jsonObj = new JSONObject();
	}

	protected String getString(String key) {
		if (!jsonObj.isNull(key)) {
			try {
				return jsonObj.getString(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return EMPTY;
	}
	
	protected int getInt(String key){
		if (!jsonObj.isNull(key)) {
			try {
				return jsonObj.getInt(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	protected String getStringFromObject(String object, String key){
		String s = EMPTY;
		try {
			if(jsonObj.has(object)){
				JSONObject obj = jsonObj.getJSONObject(object);
				if(!obj.isNull(key))
					s = obj.getString(key);
				obj = null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return s;
	}

	protected String getId(){
		return getString(ID);
	}

	protected String getTitle(){
		return getString(TITLE);
	}
	
	protected String getYear(){
		return getString(YEAR);
	}
	
	protected String getMpaa(){
		return getString(MPAA_RATING);
	}
	
	protected String getRuntime(){
		return getString(RUNTIME);
	}
	
	protected String getReleaseDateTheater(){
		return getStringFromObject(RELEASE_DATES, THEATER);
	}
	
	protected String getRatingCritics(){
		return getStringFromObject(RATINGS, CRITICS_RATING);
	}
	
	protected String getRatingCriticsScore(){
		return getStringFromObject(RATINGS, CRITICS_SCORE);
	}
	
	protected String getRatingAudienceScore(){
		return getStringFromObject(RATINGS, AUDIENCE_SCORE);
	}
	
	protected String getSynopsis(){
		return getString(SYNOPSIS);
	}
	
	protected String getActorsString(){
		try {
			JSONArray cast = jsonObj.getJSONArray(ABRIDGED_CAST);
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i < cast.length()-1; i++){
				builder.append(cast.getJSONObject(i).get(NAME)+NAME_DIVIDER);
			}
			builder.append(cast.getJSONObject(cast.length()-1).getString(NAME));
			return builder.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return EMPTY;
	}
	
	protected String getAlternateIds(){
		return getStringFromObject(ALTERNATE_IDS, IMDB);
	}
	
	//this links needs to append "?apikey=[your_api_key]"
	protected String getLinkSelf(){
		return getStringFromObject(LINKS, SELF);
	}
	
	protected String getLinkAlternate(){
		return getStringFromObject(LINKS, ALTERNATE);
	}
	
	protected String getLinkCast(){
		return getStringFromObject(LINKS, CAST);
	}
	
	protected String getLinkClips(){
		return getStringFromObject(LINKS, CLIPS);
	}
	
	protected String getLinkReviews(){
		return getStringFromObject(LINKS, REVIEWS);
	}
	
	protected String getLinkSimilar(){
		return getStringFromObject(LINKS, SIMILAR);
	}
	
	protected String getPosters(int posterType) {
		switch (posterType) {
		case THUMBNAIL:
			try {
				return jsonObj.getJSONObject(POSTERS).getString("thumbnail");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		case PROFILE:
			try {
				return jsonObj.getJSONObject(POSTERS).getString("profile");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		case DETAILED:
			try {
				return jsonObj.getJSONObject(POSTERS).getString("detailed");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		case ORIGINAL:
			try {
				return jsonObj.getJSONObject(POSTERS).getString("original");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		default:
			throw new IllegalArgumentException(
					"Should use thumbnail, profile, detailed or original constants from base class");
		}
	}
	
	public String getNextLink(){
		return getStringFromObject(LINKS, NEXT);
	}
	
}
