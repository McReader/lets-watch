package com.sickfuture.letswatch;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.custom.TouchImageView;
import com.sickfuture.letswatch.adapter.BoxOfficeCursorAdapter;
import com.sickfuture.letswatch.images.ImageLoader;
import com.sickfuture.letswatch.task.ParamCallback;

public class FullScreenImageActivity extends Activity {

	private TouchImageView mFullScreenImageView;

	private ProgressBar mProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_screen_image_view);
		mProgressBar = (ProgressBar) findViewById(R.id.full_screen_image_progress_bar);
		mFullScreenImageView = (TouchImageView) findViewById(R.id.full_screen_image_view);
		mProgressBar.setVisibility(View.VISIBLE);
		String source = getIntent().getStringExtra(
				BoxOfficeCursorAdapter.IMAGE_VIEW_SOURCE);
		Log.i("Full screen image intent", source);
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
	}
}
