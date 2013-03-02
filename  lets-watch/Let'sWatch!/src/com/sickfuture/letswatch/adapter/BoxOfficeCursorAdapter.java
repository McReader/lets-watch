package com.sickfuture.letswatch.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sickfuture.letswatch.R;
import com.sickfuture.letswatch.content.contract.Contract;
import com.sickfuture.letswatch.images.ImageLoader;

public class BoxOfficeCursorAdapter extends CursorAdapter {

	private TextView mTitleTextView, mCriticsConsensusTextView;

	private ImageView mPosterImageView;

	public BoxOfficeCursorAdapter(Context context, Cursor c) {
		super(context, c, true);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		mTitleTextView = (TextView) view
				.findViewById(R.id.box_office_title_text_view);
		mCriticsConsensusTextView = (TextView) view
				.findViewById(R.id.box_office_critics_consensus_text_view);
		mPosterImageView = (ImageView) view
				.findViewById(R.id.box_office_poster_image_view);
		if (!cursor.getString(
				cursor.getColumnIndex(Contract.BoxOfficeColumns.POSTERS))
				.equals("")) {
			ImageLoader
					.getInstance()
					.bind(this,
							mPosterImageView,
							cursor.getString(cursor
									.getColumnIndex(Contract.BoxOfficeColumns.POSTERS)));
		}
		mTitleTextView.setText(cursor.getString(cursor
				.getColumnIndex(Contract.BoxOfficeColumns.MOVIE_TITLE)));
		if (!cursor
				.getString(
						cursor.getColumnIndex(Contract.BoxOfficeColumns.CRITICS_CONSENSUS))
				.equals("")) {
			mCriticsConsensusTextView.setVisibility(View.VISIBLE);
			mCriticsConsensusTextView
					.setText(cursor.getString(cursor
							.getColumnIndex(Contract.BoxOfficeColumns.CRITICS_CONSENSUS)));
		} else {
			mCriticsConsensusTextView.setVisibility(View.GONE);
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		View view = View.inflate(context, R.layout.adapter_box_office, null);
		return view;
	}

}
