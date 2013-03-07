package com.sickfuture.letswatch.content.provider;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.net.Uri;

import com.sickfuture.letswatch.bo.BoxOfficeObject;
import com.sickfuture.letswatch.content.contract.Contract;
import com.sickfuture.letswatch.content.provider.common.CommonProvider;

public class BoxOfficeProvider extends CommonProvider {

	@Override
	protected String getOrderBy() {
		return null;
	}

	@Override
	protected String getTableName() {
		return Contract.BoxOfficeColumns.TABLE_NAME;
	}

	@Override
	protected String getContentType() {
		return Contract.BoxOfficeColumns.CONTENT_TYPE;
	}

	@Override
	protected Uri getContentURI() {
		return Contract.BoxOfficeColumns.CONTENT_URI;
	}

	@Override
	protected String[] getColoumns() {
		return Contract.BoxOfficeColumns.COLUMNS;
	}

	@Override
	protected ContentValues getContentValues(JSONObject jsonObject)
			throws JSONException {
		ContentValues values = new ContentValues();
		BoxOfficeObject object = new BoxOfficeObject(jsonObject);
		values.put(Contract.BoxOfficeColumns.MOVIE_ID, object.getId());
		values.put(Contract.BoxOfficeColumns.MOVIE_TITLE, object.getTitle());
		values.put(Contract.BoxOfficeColumns.CRITICS_CONSENSUS,	object.getCriticConsensus());
		values.put(Contract.BoxOfficeColumns.SYNOPSIS, object.getSynopsis());
		values.put(Contract.BoxOfficeColumns.POSTERS, object.getPosters(BoxOfficeObject.PROFILE));
		return values;
	}

}
