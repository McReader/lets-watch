package com.sickfuture.letswatch.database;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

abstract public class CommonDataBaseHelper extends CommonDataBase {

	private SQLiteDatabase database;

	private String mTableName;

	protected CommonDataBaseHelper(Context context, final String tableName,
			final String[] coloumns) {
		super(context, null, tableName, coloumns);
		mTableName = tableName;
	}

	public void deleteTable(String tableName, String where, String[] whereArgs) {
		delete(tableName, where, whereArgs);
	}

	public long addItem(JSONObject jsonObject) throws JSONException {
		database = getWritableDatabase();
		long value;
		ContentValues values = getContentValues(jsonObject);
		value = database.insertWithOnConflict(mTableName, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		return value;
	}

	public Cursor getItems(String orderBy, String selection,
			String[] selectionArgs) {
		database = getReadableDatabase();
		Cursor cursor = database.query(mTableName, null, selection,
				selectionArgs, null, null, orderBy);
		return cursor;
	}

	protected abstract ContentValues getContentValues(JSONObject jsonObject)
			throws JSONException;
}
