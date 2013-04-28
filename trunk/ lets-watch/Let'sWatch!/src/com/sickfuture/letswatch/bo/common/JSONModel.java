package com.sickfuture.letswatch.bo.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sickfuture.letswatch.content.contract.Contract;

public class JSONModel {

	private static final String LOG_TAG = "JSONModel";

	protected static final String EMPTY = "";
	private static final String ARRAY_SEPARATOR = "#";

	private static final String STUDIO = "studio";
	private static final String ABRIDGED_DIRECTORS = "abridged_directors";
	private static final String DVD = "dvd";
	private static final String GENRES = "genres";
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

	public JSONModel(String jsonString) throws JSONException {
		jsonObj = new JSONObject(jsonString);
	}

	public JSONModel() {
		jsonObj = new JSONObject();
	}

	protected String getString(String key) throws JSONException {
		if(!jsonObj.has(key))
			return EMPTY;
		if (!jsonObj.isNull(key)) {
			return jsonObj.getString(key);
		}
		return EMPTY;
	}

	protected int getInt(String key) throws JSONException {
		if(!jsonObj.has(key))
			return -1;
		if (!jsonObj.isNull(key)) {
			return jsonObj.getInt(key);
		}
		return -1;
	}

	private JSONArray getArray(String key) throws JSONException {
		if(!jsonObj.has(key))
			return new JSONArray();
		return jsonObj.getJSONArray(key);
	}

	protected String getStringFromObject(String object, String key)
			throws JSONException {
		String s = EMPTY;
		if (jsonObj.has(object)) {
			JSONObject obj = jsonObj.getJSONObject(object);
			if (!obj.isNull(key))
				s = obj.getString(key);
			obj = null;
		}
		return s;
	}

	protected String getId() throws JSONException {
		return getString(ID);
	}

	protected String getTitle() throws JSONException {
		return getString(TITLE);
	}

	protected String getYear() throws JSONException {
		return getString(YEAR);
	}

	protected String getMpaa() throws JSONException {
		return getString(MPAA_RATING);
	}

	protected String getRuntime() throws JSONException {
		return getString(RUNTIME);
	}

	protected String getReleaseDateTheater() throws JSONException {
		return getStringFromObject(RELEASE_DATES, THEATER);
	}

	protected String getReleaseDateDvd() throws JSONException {
		return getStringFromObject(RELEASE_DATES, DVD);
	}

	protected String getRatingCritics() throws JSONException {
		return getStringFromObject(RATINGS, CRITICS_RATING);
	}

	protected String getRatingCriticsScore() throws JSONException {
		return getStringFromObject(RATINGS, CRITICS_SCORE);
	}

	protected String getRatingAudienceScore() throws JSONException {
		return getStringFromObject(RATINGS, AUDIENCE_SCORE);
	}

	protected String getSynopsis() throws JSONException {
		return getString(SYNOPSIS);
	}

	protected String getActorsString() throws JSONException {
		JSONArray cast = getArray(ABRIDGED_CAST);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < cast.length() - 1; i++) {
			builder.append(cast.getJSONObject(i).get(NAME) + NAME_DIVIDER);
		}
		builder.append(cast.getJSONObject(cast.length() - 1).getString(NAME));
		return builder.toString();
	}

	protected String getAlternateIds() throws JSONException {
		return getStringFromObject(ALTERNATE_IDS, IMDB);
	}

	// this links needs to append "?apikey=[your_api_key]"
	protected String getLinkSelf() throws JSONException {
		return getStringFromObject(LINKS, SELF);
	}

	protected String getLinkAlternate() throws JSONException {
		return getStringFromObject(LINKS, ALTERNATE);
	}

	protected String getLinkCast() throws JSONException {
		return getStringFromObject(LINKS, CAST);
	}

	protected String getLinkClips() throws JSONException {
		return getStringFromObject(LINKS, CLIPS);
	}

	protected String getLinkReviews() throws JSONException {
		return getStringFromObject(LINKS, REVIEWS);
	}

	protected String getLinkSimilar() throws JSONException {
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

	public String getNextLink() throws JSONException {
		return getStringFromObject(LINKS, NEXT);
	}

	public String getRatingAudience() throws JSONException {
		return getStringFromObject(RATINGS, AUDIENCE_RATING);
	}

	public String getCriticConsensus() throws JSONException {
		return getString(CRITICS_CONSENSUS);
	}

	public String getGenres() throws JSONException {
		JSONArray array = getArray(GENRES);
		return array.join(ARRAY_SEPARATOR);
	}

	public String getDirectors() throws JSONException {
		JSONArray array = getArray(ABRIDGED_DIRECTORS);
		if(array.length()==0)
			return EMPTY;
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < array.length() - 1; i++) {
			builder.append(array.getJSONObject(i).get(NAME) + ARRAY_SEPARATOR);
		}
		builder.append(array.getJSONObject(array.length() - 1).getString(NAME));
		return builder.toString();
	}

	public String getStudio() throws JSONException {
		return getString(STUDIO);
	}
	
	public String getSection() throws JSONException {
		return getString(Contract.SECTION);
	}
}
