package com.sickfuture.letswatch.content.provider.common;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

import com.sickfuture.letswatch.database.DBHelperFactory;
import com.sickfuture.letswatch.database.Values;
import com.sickfuture.letswatch.service.common.CommonService;

public abstract class CommonProvider extends ContentProvider implements Values {

	@Override
	public int delete(Uri uri, String string, String[] strings) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return getContentType();
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		JSONObject jsonObject;
		long itemID;
		int numInserted = 0;
		try {
			for (ContentValues contentValues : values) {
				jsonObject = new JSONObject(
						contentValues.getAsString(CommonService.DATA));
				itemID = DBHelperFactory
						.getInstance()
						.getHelper(getContext(), getTableName(), getColoumns(),
								this).addItem(jsonObject);
				if (itemID <= 0) {
					throw new SQLException("Failed to insert row into " + uri);
				}
				numInserted++;
			}
			getContext().getContentResolver().notifyChange(getContentURI(),
					null);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}
		return numInserted;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		JSONObject jsonObject;
		long itemID;
		try {
			jsonObject = new JSONObject(values.getAsString(CommonService.DATA));
			itemID = DBHelperFactory
					.getInstance()
					.getHelper(getContext(), getTableName(), getColoumns(),
							this).addItem(jsonObject);
			Uri itemUri = Uri.parse(getContentURI() + "/" + itemID);
			getContext().getContentResolver().notifyChange(itemUri, null);
			return itemUri;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor items = DBHelperFactory.getInstance()
				.getHelper(getContext(), getTableName(), getColoumns(), this)
				.getItems(getOrderBy(), selection, selectionArgs);
		items.setNotificationUri(getContext().getContentResolver(), uri);
		return items;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

	public ContentValues getValues(JSONObject jsonObject) throws JSONException {
		return getContentValues(jsonObject);
	}

	protected abstract String getOrderBy();

	protected abstract String getTableName();

	protected abstract String getContentType();

	protected abstract Uri getContentURI();

	protected abstract String[] getColoumns();

	protected abstract ContentValues getContentValues(JSONObject jsonObject)
			throws JSONException;
}
