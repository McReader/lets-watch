package com.sickfuture.letswatch.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sickfuture.letswatch.FullScreenImageActivity;
import com.sickfuture.letswatch.R;
import com.sickfuture.letswatch.content.contract.Contract;
import com.sickfuture.letswatch.images.ImageLoader;

public class UpcomingCursorAdapter extends CursorAdapter {

	private static final String LOG_TAG = "UpcomingCursorAdapter";

	public static final String POSTERS_PROFILE = "posters_profile";
	
	public static final String POSTERS_ORIGINAL = "posters_original";

	public UpcomingCursorAdapter(Context context, Cursor c) {
		super(context, c, true);
	}
	// Basic @getView method where implemented ViewHolder
	// It uses to avoid calling @findViewById all the time
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (!mDataValid) {
			throw new IllegalStateException("this should only be called when the cursor is valid");
		}
		if (!mCursor.moveToPosition(position)) {
			throw new IllegalStateException("couldn't move cursor to position "	+ position);
		}
		View view;
		if (convertView == null) {
			view = newView(mContext, mCursor, parent);
			ViewHolder holder = new ViewHolder();
			holder.mTitleTextView = (TextView) view.findViewById(R.id.upcoming_title_text_view);
			holder.mSynopsisTextView = (TextView) view.findViewById(R.id.upcoming_synopsis_text_view);
			holder.mReleaseDateTextView = (TextView) view.findViewById(R.id.upcoming_release_date_text_view);
			holder.mMPAATextView = (TextView) view.findViewById(R.id.upcoming_mpaa_text_view);
			holder.mCastTextView = (TextView) view.findViewById(R.id.upcoming_cast_text_view);
			holder.mPosterImageView = (ImageView) view.findViewById(R.id.upcoming_poster_image_view);
			view.setTag(R.string.view_holder, holder);
		} else {
			view = convertView;
		}
		bindView(view, mContext, mCursor);
		return view;
	}

	@Override
	public void bindView(View view, final Context context, final Cursor cursor) {
		ViewHolder holder = (ViewHolder) view.getTag(R.string.view_holder);
		final String poster = cursor.getString(cursor.getColumnIndex(Contract.UpcomingColumns.POSTERS_PROFILE));
		final String original = cursor.getString(cursor.getColumnIndex(Contract.UpcomingColumns.POSTERS_ORIGINAL));
		if (!TextUtils.isEmpty(poster)) {
			holder.mPosterImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, FullScreenImageActivity.class);
					intent.putExtra(POSTERS_PROFILE, poster);
					intent.putExtra(POSTERS_ORIGINAL, original);
					context.startActivity(intent);
				}
			});
			ImageLoader.getInstance().bind(this, holder.mPosterImageView, poster);
		}
		holder.mTitleTextView.setText(cursor.getString(cursor.getColumnIndex(Contract.UpcomingColumns.MOVIE_TITLE)));
		holder.mSynopsisTextView.setText(cursor.getString(cursor.getColumnIndex(Contract.UpcomingColumns.SYNOPSIS)));
		holder.mMPAATextView.setText(cursor.getString(cursor.getColumnIndex(Contract.UpcomingColumns.MPAA)));
		holder.mReleaseDateTextView.setText(cursor.getString(cursor.getColumnIndex(Contract.UpcomingColumns.RELEASE_DATE_THEATER)));
		String cast = cursor.getString(cursor.getColumnIndex(Contract.UpcomingColumns.CAST_IDS));
		Log.d(LOG_TAG, cast);
		holder.mCastTextView.setText(cast);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		View view = View.inflate(context, R.layout.adapter_upcoming, null);
		return view;
	}

	// ViewHolder pattern implementation class
	static class ViewHolder {
		TextView mTitleTextView, mSynopsisTextView, mMPAATextView, mReleaseDateTextView, mCastTextView;

		ImageView mPosterImageView;
	}

}
