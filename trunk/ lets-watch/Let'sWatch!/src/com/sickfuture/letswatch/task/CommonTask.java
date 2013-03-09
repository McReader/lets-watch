package com.sickfuture.letswatch.task;

import android.os.AsyncTask;
import android.os.Build;

public abstract class CommonTask<T> extends AsyncTask<String, Void, T> {

	private ParamCallback<T> mParamCallback;

	public CommonTask(ParamCallback<T> paramCallback) {
		super();
		this.mParamCallback = paramCallback;
	}

	public abstract Object load(String url);

	public abstract T convert(Object source) throws Exception;

	private Exception e;

	@Override
	protected T doInBackground(String... params) {
		try {
			Object source = load(params[0]);
			if (source != null) {
				return convert(source);
			}
		} catch (Exception e) {
			this.e = e;
		}
		return null;
	}

	@Override
	protected void onPostExecute(T result) {
		super.onPostExecute(result);
		if (e != null) {
			mParamCallback.onError(e);
		} else {
			mParamCallback.onSuccess(result);
		}
	}

	public void start(String... params) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
		} else {
			execute(params);
		}
	}

}
