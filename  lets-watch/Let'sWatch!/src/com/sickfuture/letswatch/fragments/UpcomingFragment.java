package com.sickfuture.letswatch.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sickfuture.letswatch.R;
import com.sickfuture.letswatch.adapter.UpcomingCursorAdapter;
import com.sickfuture.letswatch.content.contract.Contract;
import com.sickfuture.letswatch.service.UpcomingService;
import com.sickfuture.letswatch.service.common.CommonService;
import com.sickfuture.letswatch.utils.InetChecker;

public class UpcomingFragment extends SherlockFragment implements LoaderCallbacks<Cursor>, OnRefreshListener<ListView> {
	
	private static final String LOG_TAG = "UpcomingFragment";

	private PullToRefreshListView mListViewUpcoming;
	
	private UpcomingCursorAdapter mUpcomingCursorAdapter;
	
	private BroadcastReceiver mBroadcastReceiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_upcoming, null);
		mListViewUpcoming = (PullToRefreshListView) rootView.findViewById(R.id.upcoming_pull_refresh_list);
		mBroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action.equals(CommonService.ACTION_ON_ERROR)) {
					mListViewUpcoming.onRefreshComplete();
					Toast.makeText(
							getSherlockActivity(),
							intent.getStringExtra(CommonService.EXTRA_KEY_MESSAGE),
							Toast.LENGTH_SHORT).show();
				} else if (action.equals(CommonService.ACTION_ON_SUCCESS)) {
					mListViewUpcoming.onRefreshComplete();
				}
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(CommonService.ACTION_ON_ERROR);
		filter.addAction(CommonService.ACTION_ON_SUCCESS);
		getActivity().registerReceiver(mBroadcastReceiver, filter);
		mUpcomingCursorAdapter = new UpcomingCursorAdapter(getSherlockActivity(), null);
		mListViewUpcoming.setAdapter(mUpcomingCursorAdapter);
		mListViewUpcoming.setOnRefreshListener(this);
		getSherlockActivity().getSupportLoaderManager().initLoader(1, null, this);
		return rootView;
	}

	@Override
	public void onDestroy() {
		getSherlockActivity().unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}


	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		Log.d(LOG_TAG, "onRefresh");
		if (InetChecker.checkInetConnection(getSherlockActivity())) {
			Intent intent = new Intent(getSherlockActivity(), UpcomingService.class);
			getSherlockActivity().startService(intent);
			Log.d(LOG_TAG, "start service");
		} else {
			mListViewUpcoming.onRefreshComplete();
		}
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle bundle) {
		return new CursorLoader(getSherlockActivity(), Contract.UpcomingColumns.CONTENT_URI, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		/*if (cursor.getCount() == 0) {
			onRefresh(null);
		}*/
		mUpcomingCursorAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mUpcomingCursorAdapter.swapCursor(null);
		
	}

}
