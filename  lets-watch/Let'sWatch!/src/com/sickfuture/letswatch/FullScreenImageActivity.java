package com.sickfuture.letswatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.custom.TouchImageView;
import com.sickfuture.letswatch.images.ImageLoader;
import com.sickfuture.letswatch.task.ParamCallback;

public class FullScreenImageActivity extends Activity {

	public static final String POSTERS_PROFILE = "posters_profile";
	
	public static final String POSTERS_ORIGINAL = "posters_original";

	private TouchImageView mFullScreenImageView;

	private ProgressBar mProgressBar;
	
	private Intent mIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_screen_image_view);
		mProgressBar = (ProgressBar) findViewById(R.id.full_screen_image_progress_bar);
		mFullScreenImageView = (TouchImageView) findViewById(R.id.full_screen_image_view);
		mProgressBar.setVisibility(View.VISIBLE);
		mIntent = getIntent();
		String source = mIntent.getStringExtra(POSTERS_PROFILE);
		Log.d("Full screen image intent", source);
		ImageLoader.getInstance().bind(mFullScreenImageView, source,
				new ParamCallback<Void>() {

					@Override
					public void onSuccess(Void c) {
						mProgressBar.setVisibility(View.GONE);
					}

					@Override
					public void onError(Throwable e) {
						mProgressBar.setVisibility(View.GONE);
					}
				});
		String original = mIntent.getStringExtra(POSTERS_ORIGINAL);
		Log.d("Full screen image intent", original);
		if(!TextUtils.isEmpty(original)){
			mProgressBar.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().bind(mFullScreenImageView, original,
					new ParamCallback<Void>() {
	
						@Override
						public void onSuccess(Void c) {
							mProgressBar.setVisibility(View.GONE);
						}
	
						@Override
						public void onError(Throwable e) {
							mProgressBar.setVisibility(View.GONE);
						}
					});
		}
	}
}
