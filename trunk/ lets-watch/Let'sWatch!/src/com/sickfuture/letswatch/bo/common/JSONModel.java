package com.sickfuture.letswatch.bo.common;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONModel {
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

	public String getString(String key) {
		if (jsonObj.isNull(key)) {
			return null;
		}
		try {
			return jsonObj.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getPosters(int posterType) throws JSONException {
		switch (posterType) {
		case THUMBNAIL:
			return jsonObj.getJSONObject("posters").getString("thumbnail");
		case PROFILE:
			return jsonObj.getJSONObject("posters").getString("profile");
		case DETAILED:
			return jsonObj.getJSONObject("posters").getString("detailed");
		case ORIGINAL:
			return jsonObj.getJSONObject("posters").getString("original");
		default:
			throw new IllegalArgumentException(
					"Should use thumbnail, profile, detailed or original constants from base class");
		}
	}
}
