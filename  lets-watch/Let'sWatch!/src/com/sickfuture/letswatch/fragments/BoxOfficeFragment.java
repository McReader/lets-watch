package com.sickfuture.letswatch.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragment;
import com.sickfuture.letswatch.R;
import com.sickfuture.letswatch.images.ImageLoader;
import com.sickfuture.letswatch.task.ParamCallback;

public class BoxOfficeFragment extends SherlockFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_box_office, null);
		final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.image_progress_bar);
		progressBar.setVisibility(View.VISIBLE);
		ImageView imView = (ImageView) rootView.findViewById(R.id.screen_image_view);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.bind(imView, "http://sr.gallerix.ru/1850657380/L/954944959/", new ParamCallback<Void>() {
			
			@Override
			public void onSuccess(Void c) {
				progressBar.setVisibility(View.GONE);
			}
			
			@Override
			public void onError(Throwable e) {
				progressBar.setVisibility(View.GONE);
			}
		});
		return rootView;
		
	}
	
	

}
