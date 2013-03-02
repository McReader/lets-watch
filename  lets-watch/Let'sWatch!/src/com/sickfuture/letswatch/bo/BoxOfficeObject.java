package com.sickfuture.letswatch.bo;

import org.json.JSONException;
import org.json.JSONObject;

import com.sickfuture.letswatch.bo.common.JSONModel;

public class BoxOfficeObject extends JSONModel {

	public BoxOfficeObject(JSONObject jsonObject) {
		super(jsonObject);
	}

	public String getPosters(String posterType) throws JSONException {
		// TODO make types of posters
		if (!jsonObj.getJSONObject("posters").isNull("profile")) {
			return jsonObj.getJSONObject("posters").getString("profile");
		}
		if (!jsonObj.getJSONObject("posters").isNull("detailed")) {
			return jsonObj.getJSONObject("posters").getString("detailed");
		}
		return null;
	}

	public String getCriticConsensus() throws JSONException {
		if (jsonObj.isNull("critics_consensus")) {
			return "";
		}
		return jsonObj.getString("critics_consensus");
	}
}
