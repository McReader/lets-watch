package com.sickfuture.letswatch.service;

import java.io.IOException;
import java.security.ProviderException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.sickfuture.letswatch.R;
import com.sickfuture.letswatch.bo.common.JSONModel;
import com.sickfuture.letswatch.content.contract.Contract;
import com.sickfuture.letswatch.http.HttpManager;
import com.sickfuture.letswatch.service.common.CommonService;
import com.sickfuture.letswatch.task.CommonTask;

public class UpcomingService extends CommonService<List<JSONObject>> {
	
	private static final String LOG_TAG = "UpcomingService";
	private List<JSONObject> mList;

	@Override
	protected void task(Intent intent) {
		Log.d(LOG_TAG, "start");
		new CommonTask<List<JSONObject>>(this) {

			@Override
			public Object load(String url) {
				try {
					return HttpManager.getInstance().loadAsJSONObject(url);
				} catch (ClientProtocolException e) {
					callbackOnError(e);
				} catch (JSONException e) {
					callbackOnError(e);
				} catch (IOException e) {
					callbackOnError(e);
				}
				return null;
			}

			@Override
			public List<JSONObject> convert(Object source) throws Exception {
				if(source==null) return null;
				JSONArray jsonArray = ((JSONObject) source).getJSONArray("movies");
				if (jsonArray.length() == 0) {
					return null;
				}
				mList = new ArrayList<JSONObject>(jsonArray.length());
				for (int i = 0; i < jsonArray.length(); i++) {
					mList.add(jsonArray.getJSONObject(i));
				}
				return mList;
			}
		}.start(getString(R.string.API_UPCOMING_REQUEST_URL));
	}

	@Override
	protected Uri getProviderUri() {
		return Contract.UpcomingColumns.CONTENT_URI;
	}

	@Override
	protected void callbackOnSuccess(List<JSONObject> t) {
		ContentValues[] contentValues = new ContentValues[t.size()];
		int insertResult = 0;
		for (int i = 0; i < t.size(); i++) {
			contentValues[i] = new ContentValues();
			contentValues[i].put(DATA, t.get(i).toString());
		}
		insertResult = getContentResolver().bulkInsert(
				getProviderUri(), contentValues);
		Log.d(LOG_TAG, "insertResult "+insertResult);
		if (insertResult == -1) {
			onError(new ProviderException(PROVIDER_INSERT_ERROR_MESSAGE + " = '"
							+ getProviderUri() + "'"));
			return;
		}
		Intent intent = new Intent(ACTION_ON_SUCCESS);
		sendBroadcast(intent);
	}

	@Override
	protected void callbackOnError(Throwable e) {
		Log.d(LOG_TAG, "ONERROR");
		Intent intent = new Intent(ACTION_ON_ERROR);
		intent.putExtra(EXTRA_KEY_MESSAGE, e.toString());
		sendBroadcast(intent);
	}

}
