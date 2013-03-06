package com.sickfuture.letswatch;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.custom.TouchImageView;

public class FullScreenImageActivity extends Activity {

	private TouchImageView mFullScreenImageView;

	private ProgressBar mpProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_screen_image_view);
		mpProgressBar = (ProgressBar) findViewById(R.id.full_screen_image_progress_bar);
		mFullScreenImageView = (TouchImageView) findViewById(R.id.full_screen_image_view);
		//TODO Get image source from intent
	}

}
