package com.sickfuture.letswatch.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.sickfuture.letswatch.R;

public class UpcomingFragment extends SherlockFragment {
	
	private ListView mListViewUpcoming;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_upcoming, null);
		return rootView;
	}

}
