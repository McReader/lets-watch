package com.sickfuture.letswatch.database;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;

public interface Values {
	public abstract ContentValues getValues(JSONObject jsonObject)
			throws JSONException;
}
