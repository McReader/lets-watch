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
import com.sickfuture.letswatch.adapter.BoxOfficeCursorAdapter;
import com.sickfuture.letswatch.content.contract.Contract;
import com.sickfuture.letswatch.http.HttpManager;
import com.sickfuture.letswatch.service.BoxOfficeService;
import com.sickfuture.letswatch.service.common.CommonService;

public class BoxOfficeFragment extends SherlockFragment implements	OnRefreshListener<ListView>, LoaderCallbacks<Cursor> {

	private PullToRefreshListView mListView;

	private BoxOfficeCursorAdapter mBoxOfficeCursorAdapter;

	private BroadcastReceiver mBroadcastReceiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_box_office, null);
		mListView = (PullToRefreshListView) rootView.findViewById(R.id.box_office_pull_refresh_list);
		mBroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action.equals(CommonService.ACTION_ON_ERROR)) {
					mListView.onRefreshComplete();
					Toast.makeText(
							getSherlockActivity(),
							intent.getStringExtra(CommonService.EXTRA_KEY_MESSAGE),
							Toast.LENGTH_SHORT).show();
				} else if (action.equals(CommonService.ACTION_ON_SUCCESS)) {
					mListView.onRefreshComplete();
				}
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(CommonService.ACTION_ON_ERROR);
		filter.addAction(CommonService.ACTION_ON_SUCCESS);
		getActivity().registerReceiver(mBroadcastReceiver, filter);
		mBoxOfficeCursorAdapter = new BoxOfficeCursorAdapter(getSherlockActivity(), null);
		mListView.setAdapter(mBoxOfficeCursorAdapter);
		mListView.setOnRefreshListener(this);
		getSherlockActivity().getSupportLoaderManager().initLoader(0, null, this);
		return rootView;
	}

	@Override
	public void onDestroy() {
		getSherlockActivity().unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		if (HttpManager.getInstance().isAvalibleInetConnection()) {
			Intent intent = new Intent(getSherlockActivity(),
					BoxOfficeService.class);
			getSherlockActivity().startService(intent);
		} else {
			mListView.onRefreshComplete();
			/*Intent intent=new Intent(CommonService.ACTION_ON_ERROR);
			intent.putExtra(CommonService.EXTRA_KEY_MESSAGE, getSherlockActivity().getString(R.string.internet_connection_is_not_avalible));
			getSherlockActivity().sendBroadcast(intent);*/
			/*Toast.makeText(
					getSherlockActivity(),
					getSherlockActivity().getString(
							R.string.internet_connection_is_not_avalible),
					Toast.LENGTH_LONG).show();*/
		}

	}

	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		return new CursorLoader(getSherlockActivity(), Contract.BoxOfficeColumns.CONTENT_URI, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (cursor.getCount() == 0) {
			onRefresh(null);
		}
		mBoxOfficeCursorAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mBoxOfficeCursorAdapter.swapCursor(null);
	}

}
