package com.sickfuture.letswatch.service.common;

import java.util.List;

import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.sickfuture.letswatch.task.ParamCallback;

public abstract class CommonService<T> extends Service implements
		ParamCallback<T> {

	protected static final String PROVIDER_INSERT_ERROR_MESSAGE = "Can't insert items into provider with uri";

	public static String DATA = "data";

	public static String ACTION_ON_SUCCESS = "com.sickfuture.letswatch.service.common.ACTION_ON_SUCCESS";

	public static String ACTION_ON_ERROR = "com.sickfuture.letswatch.service.common.ACTION_ON_ERROR";

	public static final String EXTRA_KEY_MESSAGE = "error message";

	public static final String LOG_TAG = "CommonService";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		task(intent);
		return START_NOT_STICKY;
	}

	@Override
	public void onSuccess(final T t) {
		if(t!=null){
			new Thread (new Runnable() {
				
				@Override
				public void run() {
					callbackOnSuccess(t);
				}
				
			}).start();
		}
	}

	@Override
	public void onError(Throwable e) {
		callbackOnError(e);
	}

	protected abstract void task(Intent intent);

	protected abstract Uri getProviderUri();

	protected abstract void callbackOnSuccess(final T t);

	protected abstract void callbackOnError(Throwable e);

}
