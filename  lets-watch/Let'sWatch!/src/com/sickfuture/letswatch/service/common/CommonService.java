package com.sickfuture.letswatch.service.common;

import java.util.List;

import org.json.JSONObject;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.sickfuture.letswatch.ContextHolder;
import com.sickfuture.letswatch.R;
import com.sickfuture.letswatch.task.ParamCallback;

public abstract class CommonService extends Service implements
		ParamCallback<List<JSONObject>> {

	public static String ACTION_ON_SUCCESS = "com.sickfuture.letswatch.service.common.ACTION_ON_SUCCESS";

	public static String ACTION_ON_ERROR = "com.sickfuture.letswatch.service.common.ACTION_ON_ERROR";

	public static final String EXTRA_KEY_MESSAGE = "error message";

	private static final String LOG_TAG = "CommonService";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		task(intent);
		return START_NOT_STICKY;
	}

	@Override
	public void onSuccess(final List<JSONObject> c) {
		Intent intent = new Intent(ACTION_ON_SUCCESS);
		sendBroadcast(intent);
		if (c != null) {
			new Thread(new Runnable() {

				public void run() {
					for (JSONObject j : c) {
						Log.d(LOG_TAG, "JSON IS :" + j.toString());
						ContentValues contentValues = new ContentValues();
						contentValues.put(ContextHolder.getInstance()
								.getContext().getString(R.string.data),
								j.toString());
						getContentResolver().insert(getProviderUri(),
								contentValues);
					}
				}
			}).start();
		}
	}

	@Override
	public void onError(Throwable e) {
		Log.d(LOG_TAG, "ONERROR");
		Intent intent = new Intent(ACTION_ON_ERROR);
		intent.putExtra(EXTRA_KEY_MESSAGE, e.toString());
		sendBroadcast(intent);
	}

	protected abstract void task(Intent intent);

	protected abstract Uri getProviderUri();
}
