package com.sickfuture.letswatch.app.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sickfuture.letswatch.R;
import com.sickfuture.letswatch.adapter.UpcomingCursorAdapter;
import com.sickfuture.letswatch.content.contract.Contract;
import com.sickfuture.letswatch.database.CommonDataBase;
import com.sickfuture.letswatch.service.UpcomingService;
import com.sickfuture.letswatch.service.common.CommonService;
import com.sickfuture.letswatch.utils.InetChecker;
import com.sickfuture.letswatch.utils.ViewHider;

public class UpcomingFragment extends SherlockFragment implements
		LoaderCallbacks<Cursor>, OnRefreshListener<ListView>, OnScrollListener {

	private static int PAGINATION = R.string.pagination;

	private static final String URL = "url";

	private static final String LOG_TAG = "UpcomingFragment";

	private PullToRefreshListView mListViewUpcoming;

	private UpcomingCursorAdapter mUpcomingCursorAdapter;

	private BroadcastReceiver mBroadcastReceiver;

	private View mViewLoading;

	private boolean mViewLoadingHidden, mLoading = true;

	private SharedPreferences mPreferences;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_upcoming, null);
		mViewLoading = inflater.inflate(R.layout.view_loading, null);
		hideViewLoading(true);
		mListViewUpcoming = (PullToRefreshListView) rootView.findViewById(R.id.upcoming_pull_refresh_list);
		mListViewUpcoming.setOnScrollListener(this);
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
					mLoading = false;
				} else if (action.equals(CommonService.ACTION_ON_SUCCESS)) {
					mListViewUpcoming.onRefreshComplete();
					mLoading = false;
				}
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(CommonService.ACTION_ON_ERROR);
		filter.addAction(CommonService.ACTION_ON_SUCCESS);
		getActivity().registerReceiver(mBroadcastReceiver, filter);
		mUpcomingCursorAdapter = new UpcomingCursorAdapter(getSherlockActivity(), null);
		mListViewUpcoming.setAdapter(mUpcomingCursorAdapter);
		ListView actualListView = mListViewUpcoming.getRefreshableView();
		actualListView.addFooterView(mViewLoading);
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
			CommonDataBase.getInstance().deleteTable(Contract.UpcomingColumns.TABLE_NAME, null, null);
			Intent intent = new Intent(getSherlockActivity(), UpcomingService.class);
			intent.putExtra(URL, getString(R.string.API_UPCOMING_REQUEST_URL));
			load(intent);
		} else {
			Log.i(LOG_TAG, "onRefreshComplete");
			mListViewUpcoming.onRefreshComplete();
		}
	}

	private void load(Intent intent) {
		getSherlockActivity().startService(intent);
		mLoading = true;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle bundle) {
		return new CursorLoader(getSherlockActivity(),
				Contract.UpcomingColumns.CONTENT_URI, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (cursor.getCount() == 0) {
			onRefresh(null);
		}
		mUpcomingCursorAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mUpcomingCursorAdapter.swapCursor(null);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (visibleItemCount > 0
				&& firstVisibleItem + visibleItemCount + 3 >= totalItemCount 
				&& InetChecker.checkInetConnection(getSherlockActivity(), false)) {
			if (!mLoading) {
				Log.d(LOG_TAG, "onScroll load");
				mPreferences = getSherlockActivity().getSharedPreferences(getString(PAGINATION), Context.MODE_PRIVATE);
				String nextPage = mPreferences.getString(UpcomingService.NEXT_UPCOMING, null);
				if (!TextUtils.isEmpty(nextPage)) {
					if(mViewLoadingHidden) hideViewLoading(false);
					Log.d(LOG_TAG, "next = " + nextPage);
					Intent intent = new Intent(getSherlockActivity(),UpcomingService.class);
					intent.putExtra(URL, nextPage);
					load(intent);
				} else {
					if(!mViewLoadingHidden) hideViewLoading(true);
				}
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	private void hideViewLoading(boolean hide) {
		mViewLoadingHidden = hide;
		ViewHider.hideListItem(mViewLoading, hide);
	}

}
