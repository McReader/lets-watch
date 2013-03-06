package com.sickfuture.letswatch.content.provider;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.net.Uri;

import com.sickfuture.letswatch.bo.BoxOfficeObject;
import com.sickfuture.letswatch.content.contract.Contract;
import com.sickfuture.letswatch.content.provider.common.CommonProvider;

public class BoxOfficeProvider extends CommonProvider {

	private static final String SYNOPSIS = "synopsis";
	private static final String TITLE = "title";
	private static final String ID = "id";

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
		values.put(Contract.BoxOfficeColumns.MOVIE_ID, object.getString(ID));
		values.put(Contract.BoxOfficeColumns.MOVIE_TITLE,
				object.getString(TITLE));
		values.put(Contract.BoxOfficeColumns.CRITICS_CONSENSUS,
				object.getCriticConsensus());
		values.put(Contract.BoxOfficeColumns.SYNOPSIS,
				object.getString(SYNOPSIS));
		values.put(Contract.BoxOfficeColumns.POSTERS, object.getPosters(BoxOfficeObject.PROFILE));
		return values;
	}

}
