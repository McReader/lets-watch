package com.sickfuture.letswatch.content.provider;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.net.Uri;

import com.sickfuture.letswatch.bo.UpcomingObject;
import com.sickfuture.letswatch.bo.common.JSONModel;
import com.sickfuture.letswatch.content.contract.Contract;
import com.sickfuture.letswatch.content.provider.common.CommonProvider;

public class UpcomingProvider extends CommonProvider {

	@Override
	protected String getOrderBy() {
		return null;
	}

	@Override
	protected String getTableName() {
		return Contract.UpcomingColumns.TABLE_NAME;
	}

	@Override
	protected String getContentType() {
		return Contract.UpcomingColumns.CONTENT_TYPE;
	}

	@Override
	protected Uri getContentURI() {
		return Contract.UpcomingColumns.CONTENT_URI;
	}

	@Override
	protected String[] getColoumns() {
		return Contract.UpcomingColumns.COLUMNS;
	}

	@Override
	protected ContentValues getContentValues(JSONObject jsonObject)	throws JSONException {
		ContentValues values = new ContentValues();
		UpcomingObject object = new UpcomingObject(jsonObject);
		values.put(Contract.UpcomingColumns.MOVIE_ID, object.getId());
		values.put(Contract.UpcomingColumns.MOVIE_TITLE, object.getTitle());
		values.put(Contract.UpcomingColumns.YEAR, object.getYear());
		values.put(Contract.UpcomingColumns.MPAA, object.getMpaa());
		values.put(Contract.UpcomingColumns.RUNTIME, object.getRuntime());
		values.put(Contract.UpcomingColumns.RELEASE_DATE_THEATER, object.getReleaseDateTheater());
		values.put(Contract.UpcomingColumns.SYNOPSIS, object.getSynopsis());
		values.put(Contract.UpcomingColumns.RATING_CRITICS, object.getRatingCritics());
		values.put(Contract.UpcomingColumns.RATING_CRITICS_SCORE, object.getRatingCriticsScore());
		values.put(Contract.UpcomingColumns.RATING_AUDIENCE_SCORE, object.getRatingAudienceScore());
		values.put(Contract.UpcomingColumns.POSTERS_THUMBNAIL, object.getPosters(JSONModel.THUMBNAIL));
		values.put(Contract.UpcomingColumns.POSTERS_PROFILE, object.getPosters(JSONModel.PROFILE));
		values.put(Contract.UpcomingColumns.POSTERS_DETAILED, object.getPosters(JSONModel.DETAILED));
		values.put(Contract.UpcomingColumns.POSTERS_ORIGINAL, object.getPosters(JSONModel.ORIGINAL));
		values.put(Contract.UpcomingColumns.CAST_IDS, object.getActorsString());
		values.put(Contract.UpcomingColumns.ALTERNATE_IDS, object.getAlternateIds());
		values.put(Contract.UpcomingColumns.LINK_SELF, object.getLinkSelf());
		values.put(Contract.UpcomingColumns.LINK_ALTRENATE, object.getLinkSelf());
		values.put(Contract.UpcomingColumns.LINK_CAST, object.getLinkCast());
		values.put(Contract.UpcomingColumns.LINK_CLIPS, object.getLinkClips());
		values.put(Contract.UpcomingColumns.LINK_REVIEWS, object.getLinkReviews());
		values.put(Contract.UpcomingColumns.LINK_SIMILAR, object.getLinkSimilar());
		return values;
	}

}
