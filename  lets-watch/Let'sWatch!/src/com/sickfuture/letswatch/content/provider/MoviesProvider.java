package com.sickfuture.letswatch.content.provider;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.net.Uri;

import com.sickfuture.letswatch.bo.BoxOfficeObject;
import com.sickfuture.letswatch.bo.MovieObject;
import com.sickfuture.letswatch.bo.common.JSONModel;
import com.sickfuture.letswatch.content.contract.Contract;
import com.sickfuture.letswatch.content.provider.common.CommonProvider;

public class MoviesProvider extends CommonProvider {

	@Override
	protected String getOrderBy() {
		return null;
	}

	@Override
	protected String getTableName() {
		return Contract.MovieColumns.TABLE_NAME;
	}

	@Override
	protected String getContentType() {
		return Contract.MovieColumns.CONTENT_TYPE;
	}

	@Override
	protected Uri getContentURI() {
		return Contract.MovieColumns.CONTENT_URI;
	}

	@Override
	protected String[] getColumns() {
		return Contract.MovieColumns.COLUMNS;
	}

	@Override
	protected ContentValues getContentValues(JSONObject jsonObject)
			throws JSONException {
		ContentValues values = new ContentValues();
		MovieObject object = new MovieObject(jsonObject);
		values.put(Contract.MovieColumns.MOVIE_ID, object.getId());
		values.put(Contract.MovieColumns.MOVIE_TITLE, object.getTitle());
		values.put(Contract.MovieColumns.CRITICS_CONSENSUS,	object.getCriticConsensus());
		values.put(Contract.MovieColumns.YEAR, object.getYear());
		values.put(Contract.MovieColumns.MPAA, object.getMpaa());
		values.put(Contract.MovieColumns.RUNTIME, object.getRuntime());
		values.put(Contract.MovieColumns.RELEASE_DATE_THEATER, object.getReleaseDateTheater());
		values.put(Contract.MovieColumns.RELEASE_DATE_DVD, object.getReleaseDateDvd());
		values.put(Contract.MovieColumns.SYNOPSIS, object.getSynopsis());
		values.put(Contract.MovieColumns.RATING_CRITICS, object.getRatingCritics());
		values.put(Contract.MovieColumns.RATING_CRITICS_SCORE, object.getRatingCriticsScore());
		values.put(Contract.MovieColumns.RATING_AUDIENCE, object.getRatingAudience());
		values.put(Contract.MovieColumns.RATING_AUDIENCE_SCORE, object.getRatingAudienceScore());
		values.put(Contract.MovieColumns.POSTERS_THUMBNAIL, object.getPosters(JSONModel.THUMBNAIL));
		values.put(Contract.MovieColumns.POSTERS_PROFILE, object.getPosters(JSONModel.PROFILE));
		values.put(Contract.MovieColumns.POSTERS_DETAILED, object.getPosters(JSONModel.DETAILED));
		values.put(Contract.MovieColumns.POSTERS_ORIGINAL, object.getPosters(JSONModel.ORIGINAL));
		values.put(Contract.MovieColumns.CAST_IDS, object.getActorsString());
		values.put(Contract.MovieColumns.DIRECTORS, object.getDirectors());
		values.put(Contract.MovieColumns.GENRES, object.getGenres());
		values.put(Contract.MovieColumns.STUDIO, object.getStudio());
		values.put(Contract.MovieColumns.ALTERNATE_IDS, object.getAlternateIds());
		values.put(Contract.MovieColumns.LINK_SELF, object.getLinkSelf());
		values.put(Contract.MovieColumns.LINK_ALTRENATE, object.getLinkSelf());
		values.put(Contract.MovieColumns.LINK_CAST, object.getLinkCast());
		values.put(Contract.MovieColumns.LINK_CLIPS, object.getLinkClips());
		values.put(Contract.MovieColumns.LINK_REVIEWS, object.getLinkReviews());
		values.put(Contract.MovieColumns.LINK_SIMILAR, object.getLinkSimilar());
		values.put(Contract.MovieColumns.SECTION, object.getSection());
		return values;
	}

}
