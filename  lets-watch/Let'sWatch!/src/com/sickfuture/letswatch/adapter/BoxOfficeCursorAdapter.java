package com.sickfuture.letswatch.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sickfuture.letswatch.FullScreenImageActivity;
import com.sickfuture.letswatch.R;
import com.sickfuture.letswatch.content.contract.Contract;
import com.sickfuture.letswatch.images.ImageLoader;

public class BoxOfficeCursorAdapter extends CursorAdapter {

	public static final String POSTERS_PROFILE = "posters_profile";
	
	public static final String POSTERS_ORIGINAL = "posters_original";

	public BoxOfficeCursorAdapter(Context context, Cursor c) {
		super(context, c, true);
	}

	// Basic @getView method where implemented ViewHolder
	// It uses to avoid calling @findViewById every time
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (!mDataValid) {
			throw new IllegalStateException(
					"this should only be called when the cursor is valid");
		}
		if (!mCursor.moveToPosition(position)) {
			throw new IllegalStateException("couldn't move cursor to position "
					+ position);
		}
		View view;
		if (convertView == null) {
			view = newView(mContext, mCursor, parent);
			ViewHolder holder = new ViewHolder();
			holder.mTitleTextView = (TextView) view.findViewById(R.id.box_office_title_text_view);
			holder.mCriticsConsensusTextView = (TextView) view.findViewById(R.id.box_office_synopsis_text_view);
			holder.mMPAATextView = (TextView) view.findViewById(R.id.box_office_mpaa_text_view);
			holder.mCastTextView = (TextView) view.findViewById(R.id.box_office_cast_text_view);
			holder.mPosterImageView = (ImageView) view.findViewById(R.id.box_office_poster_image_view);
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
		final String posterUrl = cursor.getString(cursor.getColumnIndex(Contract.BoxOfficeColumns.POSTERS_PROFILE));
		final String orinalUrl = cursor.getString(cursor.getColumnIndex(Contract.BoxOfficeColumns.POSTERS_ORIGINAL));
		if (!TextUtils.isEmpty(posterUrl)) {
			holder.mPosterImageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, FullScreenImageActivity.class);
					intent.putExtra(POSTERS_PROFILE, posterUrl);
					intent.putExtra(POSTERS_ORIGINAL, orinalUrl);
					context.startActivity(intent);
					return;
				}
			});
			ImageLoader.getInstance().bind(this, holder.mPosterImageView, posterUrl);
		}
		holder.mCastTextView.setText(cursor.getString(cursor.getColumnIndex(Contract.BoxOfficeColumns.CAST_IDS)));
		holder.mTitleTextView.setText(cursor.getString(cursor.getColumnIndex(Contract.BoxOfficeColumns.MOVIE_TITLE)));
		String consensus = cursor.getString(cursor.getColumnIndex(Contract.BoxOfficeColumns.CRITICS_CONSENSUS));
		if (!TextUtils.isEmpty(consensus)) {
			holder.mCriticsConsensusTextView.setVisibility(View.VISIBLE);
			holder.mCriticsConsensusTextView.setText(consensus);
		} else {
			holder.mCriticsConsensusTextView
					.setText(cursor.getString(cursor.getColumnIndex(Contract.BoxOfficeColumns.SYNOPSIS)));
		}
		holder.mMPAATextView.setText(cursor.getString(cursor.getColumnIndex(Contract.BoxOfficeColumns.MPAA)));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		View view = View.inflate(context, R.layout.adapter_box_office, null);
		return view;
	}

	// ViewHolder pattern implementation class
	static class ViewHolder {
		
		TextView mTitleTextView, mCriticsConsensusTextView, mMPAATextView, mCastTextView;

		ImageView mPosterImageView;
	}
}
