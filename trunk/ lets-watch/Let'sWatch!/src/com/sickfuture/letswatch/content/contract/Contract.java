package com.sickfuture.letswatch.content.contract;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

	public static final String AUTHORITY = "com.sickfuture.letswatch.content.provider.";

	public Contract() {
	}

	public static final class BoxOfficeColumns implements BaseColumns {

		private BoxOfficeColumns() {
		}

		public final static String[] COLUMNS = {
				Contract.BoxOfficeColumns.MOVIE_ID,
				Contract.BoxOfficeColumns.MOVIE_TITLE,
				Contract.BoxOfficeColumns.CRITICS_CONSENSUS,
				Contract.BoxOfficeColumns.SYNOPSIS,
				Contract.BoxOfficeColumns.POSTERS };

		public static final String TABLE_NAME = "BOX_OFFICE_TABLE";

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/BOX_OFFICE_TABLE";
		/** path to table provider */
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "BoxOfficeProvider/" + TABLE_NAME);

		public static final String MOVIE_ID = _ID;

		public static final String MOVIE_TITLE = "MOVIE_TITLE";

		public static final String CRITICS_CONSENSUS = "CRITICS_CONSENSUS";

		public static final String SYNOPSIS = "SYNOPSIS";

		public static final String POSTERS = "POSTERS";
	}
}
