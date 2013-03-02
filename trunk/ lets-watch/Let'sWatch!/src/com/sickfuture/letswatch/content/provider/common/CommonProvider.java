package com.sickfuture.letswatch.content.provider.common;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.sickfuture.letswatch.R;
import com.sickfuture.letswatch.database.DBHelperFactory;
import com.sickfuture.letswatch.database.Values;

public abstract class CommonProvider extends ContentProvider implements Values {

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return getContentType();
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		JSONObject jsonObject;
		long itemID;
		try {
			jsonObject = new JSONObject(values.getAsString(getContext()
					.getString(R.string.data)));
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
