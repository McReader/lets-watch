package com.sickfuture.letswatch.bo;

import org.json.JSONException;
import org.json.JSONObject;

import com.sickfuture.letswatch.bo.common.JSONModel;

public class BoxOfficeObject extends JSONModel {

	public BoxOfficeObject(JSONObject jsonObject) {
		super(jsonObject);
	}

	public String getCriticConsensus() throws JSONException {
		if (jsonObj.isNull("critics_consensus")) {
			return "";
		}
		return jsonObj.getString("critics_consensus");
	}
}
