package com.sickfuture.letswatch;

import android.content.Context;

public class ContextHolder {

	private static ContextHolder instance;

	private ContextHolder() {
	}

	public static ContextHolder getInstance() {
		if (instance == null) {
			instance = new ContextHolder();
		}
		return instance;
	}

	private Context mContext;

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context pContext) {
		this.mContext = pContext;
	}

}
