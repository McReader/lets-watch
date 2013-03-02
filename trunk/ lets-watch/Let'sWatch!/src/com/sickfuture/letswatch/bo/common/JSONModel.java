package com.sickfuture.letswatch.bo.common;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONModel {
	protected JSONObject jsonObj;

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
}
