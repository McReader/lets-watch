package com.sickfuture.letswatch.content.contract;

import android.net.Uri;

public class Contract {

	public static final String AUTHORITY = "com.sickfuture.letswatch.content.provider.";

	public static final class BoxOfficeColumns extends TableGetters {

		public BoxOfficeColumns() {
		}

		public static final String TABLE_NAME = "BOX_OFFICE_TABLE";

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/BOX_OFFICE_TABLE";
		/** path to table provider */
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "BoxOfficeProvider/" + TABLE_NAME);

		public static final String MOVIE_ID = _ID;

		public static final String MOVIE_TITLE = "MOVIE_TITLE";

		public static final String YEAR = "YEAR";

		public static final String MPAA = "MPAA";

		public static final String RUNTIME = "RUNTIME";

		public static final String RELEASE_DATE_THEATER = "RELEASE_DATE_THEATER";

		public static final String CRITICS_CONSENSUS = "CRITICS_CONSENSUS";

		public static final String SYNOPSIS = "SYNOPSIS";

		public static final String RATING_CRITICS = "RATING_CRITICS";

		public static final String RATING_CRITICS_SCORE = "RATING_CRITICS_SCORE";

		public static final String RATING_AUDIENCE = "RATING_AUDIENCE";

		public static final String RATING_AUDIENCE_SCORE = "RATING_AUDIENCE_SCORE";

		public static final String POSTERS_THUMBNAIL = "POSTERS_THUMBNAIL";

		public static final String POSTERS_PROFILE = "POSTERS_PROFILE";

		public static final String POSTERS_DETAILED = "POSTERS_DETAILED";

		public static final String POSTERS_ORIGINAL = "POSTERS_ORIGINAL";

		public static final String CAST_IDS = "CAST_IDS";

		public static final String ALTERNATE_IDS = "ALTERNATE_IDS";

		public static final String LINK_SELF = "LINK_SELF";

		public static final String LINK_ALTRENATE = "LINK_ALTRENATE";

		public static final String LINK_CAST = "LINK_CAST";

		public static final String LINK_CLIPS = "LINK_CLIPS";

		public static final String LINK_REVIEWS = "LINK_REVIEWS";

		public static final String LINK_SIMILAR = "LINK_SIMILAR";

		public final static String[] COLUMNS = {
				Contract.BoxOfficeColumns.MOVIE_ID,
				Contract.BoxOfficeColumns.MOVIE_TITLE,
				Contract.BoxOfficeColumns.YEAR, Contract.BoxOfficeColumns.MPAA,
				Contract.BoxOfficeColumns.RUNTIME,
				Contract.BoxOfficeColumns.RELEASE_DATE_THEATER,
				Contract.BoxOfficeColumns.CRITICS_CONSENSUS,
				Contract.BoxOfficeColumns.SYNOPSIS,
				Contract.BoxOfficeColumns.RATING_CRITICS,
				Contract.BoxOfficeColumns.RATING_CRITICS_SCORE,
				Contract.BoxOfficeColumns.RATING_AUDIENCE,
				Contract.BoxOfficeColumns.RATING_AUDIENCE_SCORE,
				Contract.BoxOfficeColumns.POSTERS_THUMBNAIL,
				Contract.BoxOfficeColumns.POSTERS_PROFILE,
				Contract.BoxOfficeColumns.POSTERS_DETAILED,
				Contract.BoxOfficeColumns.POSTERS_ORIGINAL,
				Contract.BoxOfficeColumns.CAST_IDS,
				Contract.BoxOfficeColumns.ALTERNATE_IDS,
				Contract.BoxOfficeColumns.LINK_SELF,
				Contract.BoxOfficeColumns.LINK_ALTRENATE,
				Contract.BoxOfficeColumns.LINK_CAST,
				Contract.BoxOfficeColumns.LINK_CLIPS,
				Contract.BoxOfficeColumns.LINK_REVIEWS,
				Contract.BoxOfficeColumns.LINK_SIMILAR };

		@Override
		String getTableName() {
			return TABLE_NAME;
		}

		@Override
		String[] getColumns() {
			return COLUMNS;
		}
	}

	public static final class UpcomingColumns extends TableGetters {

		public UpcomingColumns() {
		}

		public static final String TABLE_NAME = "UPCOMING_TABLE";

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/"
				+ TABLE_NAME;
		/** path to table provider */
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "UpcomingProvider/" + TABLE_NAME);

		public static final String MOVIE_ID = _ID;

		public static final String MOVIE_TITLE = "MOVIE_TITLE";

		public static final String YEAR = "YEAR";

		public static final String MPAA = "MPAA";

		public static final String RUNTIME = "RUNTIME";

		public static final String RELEASE_DATE_THEATER = "RELEASE_DATE_THEATER";

		public static final String SYNOPSIS = "SYNOPSIS";

		public static final String RATING_CRITICS = "RATING_CRITICS";

		public static final String RATING_CRITICS_SCORE = "RATING_CRITICS_SCORE";

		public static final String RATING_AUDIENCE_SCORE = "RATING_AUDIENCE_SCORE";

		public static final String POSTERS_THUMBNAIL = "POSTERS_THUMBNAIL";

		public static final String POSTERS_PROFILE = "POSTERS_PROFILE";

		public static final String POSTERS_DETAILED = "POSTERS_DETAILED";

		public static final String POSTERS_ORIGINAL = "POSTERS_ORIGINAL";

		public static final String CAST_IDS = "CAST_IDS";

		public static final String ALTERNATE_IDS = "ALTERNATE_IDS";

		public static final String LINK_SELF = "LINK_SELF";

		public static final String LINK_ALTRENATE = "LINK_ALTRENATE";

		public static final String LINK_CAST = "LINK_CAST";

		public static final String LINK_CLIPS = "LINK_CLIPS";

		public static final String LINK_REVIEWS = "LINK_REVIEWS";

		public static final String LINK_SIMILAR = "LINK_SIMILAR";

		public final static String[] COLUMNS = {
				Contract.UpcomingColumns.MOVIE_ID,
				Contract.UpcomingColumns.MOVIE_TITLE,
				Contract.UpcomingColumns.YEAR, Contract.UpcomingColumns.MPAA,
				Contract.UpcomingColumns.RUNTIME,
				Contract.UpcomingColumns.RELEASE_DATE_THEATER,
				Contract.UpcomingColumns.SYNOPSIS,
				Contract.UpcomingColumns.RATING_CRITICS,
				Contract.UpcomingColumns.RATING_CRITICS_SCORE,
				Contract.UpcomingColumns.RATING_AUDIENCE_SCORE,
				Contract.UpcomingColumns.POSTERS_THUMBNAIL,
				Contract.UpcomingColumns.POSTERS_PROFILE,
				Contract.UpcomingColumns.POSTERS_DETAILED,
				Contract.UpcomingColumns.POSTERS_ORIGINAL,
				Contract.UpcomingColumns.CAST_IDS,
				Contract.UpcomingColumns.ALTERNATE_IDS,
				Contract.UpcomingColumns.LINK_SELF,
				Contract.UpcomingColumns.LINK_ALTRENATE,
				Contract.UpcomingColumns.LINK_CAST,
				Contract.UpcomingColumns.LINK_CLIPS,
				Contract.UpcomingColumns.LINK_REVIEWS,
				Contract.UpcomingColumns.LINK_SIMILAR };

		@Override
		String getTableName() {
			return TABLE_NAME;
		}

		@Override
		String[] getColumns() {
			return COLUMNS;
		}

	}
}
