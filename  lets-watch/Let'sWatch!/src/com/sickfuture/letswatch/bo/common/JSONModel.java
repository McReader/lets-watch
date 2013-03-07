package com.sickfuture.letswatch.bo.common;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONModel {
	
	private static final String POSTERS = "posters";
	
	protected static final String EMPTY = "";

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
			JSONObject obj = jsonObj.getJSONObject(object);
			if(!obj.isNull(key))
				s = obj.getString(key);
			obj = null;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return s;
	}
	public String getPosters(int posterType) {
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
}
