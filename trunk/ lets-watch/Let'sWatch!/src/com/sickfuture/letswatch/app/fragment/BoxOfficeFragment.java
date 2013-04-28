package com.sickfuture.letswatch.app.fragment;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sickfuture.letswatch.R;
import com.sickfuture.letswatch.adapter.BoxOfficeCursorAdapter;
import com.sickfuture.letswatch.app.activity.MainActivity;
import com.sickfuture.letswatch.app.callback.IListClickable;
import com.sickfuture.letswatch.content.contract.Contract;
import com.sickfuture.letswatch.database.CommonDataBase;
import com.sickfuture.letswatch.service.BoxOfficeService;
import com.sickfuture.letswatch.service.common.CommonService;
import com.sickfuture.letswatch.utils.InetChecker;

public class BoxOfficeFragment extends SherlockFragment implements
		OnRefreshListener<ListView>, LoaderCallbacks<Cursor>,
		OnItemClickListener {

	private PullToRefreshListView mListView;

	private BoxOfficeCursorAdapter mBoxOfficeCursorAdapter;

	private BroadcastReceiver mBroadcastReceiver;

	private IListClickable mCallback;

	@Override
	public void onAttach(Activity activity) {
		if (!(activity instanceof IListClickable)) {
			throw new IllegalArgumentException(
					"Activity must implements IListClickable");
		}
		mCallback = (IListClickable) activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_box_office, null);
		mListView = (PullToRefreshListView) rootView
				.findViewById(R.id.box_office_pull_refresh_list);
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
		mBoxOfficeCursorAdapter = new BoxOfficeCursorAdapter(
				getSherlockActivity(), null);
		mListView.setAdapter(mBoxOfficeCursorAdapter);
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(this);
		getSherlockActivity().getSupportLoaderManager().initLoader(0, null,
				this);
		return rootView;
	}

	@Override
	public void onDestroy() {
		getSherlockActivity().unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		if (InetChecker.checkInetConnection(getSherlockActivity())) {
			CommonDataBase.getInstance().deleteTable(
					Contract.BoxOfficeColumns.TABLE_NAME, null, null);
			Intent intent = new Intent(getSherlockActivity(),
					BoxOfficeService.class);
			intent.putExtra("url",
					getString(R.string.API_BOX_OFFICE_REQUEST_URL));
			getSherlockActivity().startService(intent);
		} else {
			refreshView.onRefreshComplete();
		}

	}

	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		return new CursorLoader(getSherlockActivity(),
				Contract.MovieColumns.CONTENT_URI, null,
				Contract.MovieColumns.SECTION + " = ?",
				new String[] { Contract.BOX_OFFICE_SECTION_MARK }, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		/*if (cursor.getCount() == 0) {
			onRefresh(mListView);
		}*/
		mBoxOfficeCursorAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mBoxOfficeCursorAdapter.swapCursor(null);
	}

	@Override
	public void onItemClick(AdapterView<?> list, View view, int position,
			long id) {
		Cursor cursor = (Cursor) list.getItemAtPosition(position);
		Bundle arguments = new Bundle();
		arguments.putInt(Contract.SECTION, Contract.BOX_OFFICE_SECTION);
		arguments.putInt(Contract.ID, cursor.getInt(cursor.getColumnIndex(Contract.MovieColumns.MOVIE_ID)));
		arguments
				.putInt(MainActivity.FRAGMENT, MainActivity.BOXOFFICE_FRAGMENT);
		mCallback.onItemListClick(arguments);

	}

}
