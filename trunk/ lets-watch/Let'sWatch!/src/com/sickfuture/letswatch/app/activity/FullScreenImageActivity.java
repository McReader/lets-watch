package com.sickfuture.letswatch.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.custom.TouchImageView;
import com.sickfuture.letswatch.R;
import com.sickfuture.letswatch.images.ImageLoader;
import com.sickfuture.letswatch.service.UpcomingService;
import com.sickfuture.letswatch.task.ParamCallback;

public class FullScreenImageActivity extends Activity {

	private static int PAGINATION = R.string.pagination;
	
	private SharedPreferences mPreferences;

	private TouchImageView mFullScreenImageView;

	private ProgressBar mProgressBar;
	
	private Intent mIntent;

	public static final String POSTERS_PROFILE = "posters_profile";
	
	public static final String POSTERS_ORIGINAL = "posters_original";

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
						loadHiRes();
					}

					@Override
					public void onError(Throwable e) {
						mProgressBar.setVisibility(View.GONE);
					}
				});
		
	}

	protected void loadHiRes() {
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
	
	@Override
	protected void onDestroy() {
		mPreferences = getSharedPreferences(getString(PAGINATION), Context.MODE_PRIVATE);
		Editor editor = mPreferences.edit();
		editor.remove(UpcomingService.NEXT_UPCOMING);
		editor.commit();
		super.onDestroy();
	}


}
