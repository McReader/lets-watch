package com.sickfuture.letswatch.database;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;

public class DBHelperFactory {

	private Map<String, CommonDataBaseHelper> helpers;

	private static DBHelperFactory instance;

	private DBHelperFactory() {
		helpers = Collections
				.synchronizedMap(new HashMap<String, CommonDataBaseHelper>());
	}

	public static DBHelperFactory getInstance() {
		if (instance == null) {
			instance = new DBHelperFactory();
		}
		return instance;
	}

	public void deleteTable(String tableName, String where, String[] whereArgs) {
		if (helpers.get(tableName) != null) {
			helpers.get(tableName).deleteTable(tableName, where, whereArgs);
		}
	}

	public CommonDataBaseHelper getHelper(Context context, String tableName,
			String[] coloumns, Values contentValues) {
		if (helpers.get(tableName) != null) {
			return helpers.get(tableName);
		}
		createHelper(context, tableName, coloumns, contentValues);
		return helpers.get(tableName);
	}

	private void createHelper(Context context, String tableName,
			String[] coloumns, final Values contentValues) {
		CommonDataBaseHelper helper = new CommonDataBaseHelper(context,
				tableName, coloumns) {
			@Override
			public ContentValues getContentValues(JSONObject jsonObject)
					throws JSONException {
				return contentValues.getValues(jsonObject);
			}
		};
		helpers.put(tableName, helper);
	}
}
