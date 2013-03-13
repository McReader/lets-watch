package com.sickfuture.letswatch.content.provider;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.net.Uri;

import com.sickfuture.letswatch.bo.BoxOfficeObject;
import com.sickfuture.letswatch.bo.common.JSONModel;
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
	protected String[] getColumns() {
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
		values.put(Contract.BoxOfficeColumns.YEAR, object.getYear());
		values.put(Contract.BoxOfficeColumns.MPAA, object.getMpaa());
		values.put(Contract.BoxOfficeColumns.RUNTIME, object.getRuntime());
		values.put(Contract.BoxOfficeColumns.RELEASE_DATE_THEATER, object.getReleaseDateTheater());
		values.put(Contract.BoxOfficeColumns.SYNOPSIS, object.getSynopsis());
		values.put(Contract.BoxOfficeColumns.RATING_CRITICS, object.getRatingCritics());
		values.put(Contract.BoxOfficeColumns.RATING_CRITICS_SCORE, object.getRatingCriticsScore());
		values.put(Contract.BoxOfficeColumns.RATING_AUDIENCE, object.getRatingAudience());
		values.put(Contract.BoxOfficeColumns.RATING_AUDIENCE_SCORE, object.getRatingAudienceScore());
		values.put(Contract.BoxOfficeColumns.POSTERS_THUMBNAIL, object.getPosters(JSONModel.THUMBNAIL));
		values.put(Contract.BoxOfficeColumns.POSTERS_PROFILE, object.getPosters(JSONModel.PROFILE));
		values.put(Contract.BoxOfficeColumns.POSTERS_DETAILED, object.getPosters(JSONModel.DETAILED));
		values.put(Contract.BoxOfficeColumns.POSTERS_ORIGINAL, object.getPosters(JSONModel.ORIGINAL));
		values.put(Contract.BoxOfficeColumns.CAST_IDS, object.getActorsString());
		values.put(Contract.BoxOfficeColumns.ALTERNATE_IDS, object.getAlternateIds());
		values.put(Contract.BoxOfficeColumns.LINK_SELF, object.getLinkSelf());
		values.put(Contract.BoxOfficeColumns.LINK_ALTRENATE, object.getLinkSelf());
		values.put(Contract.BoxOfficeColumns.LINK_CAST, object.getLinkCast());
		values.put(Contract.BoxOfficeColumns.LINK_CLIPS, object.getLinkClips());
		values.put(Contract.BoxOfficeColumns.LINK_REVIEWS, object.getLinkReviews());
		values.put(Contract.BoxOfficeColumns.LINK_SIMILAR, object.getLinkSimilar());
		return values;
	}

}
