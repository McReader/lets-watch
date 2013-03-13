package com.sickfuture.letswatch;

import android.content.Context;

public class ContextHolder {

	private static volatile ContextHolder instance;

	private ContextHolder() {
	}

	public static ContextHolder getInstance() {
		ContextHolder localInstance = instance;
		if (instance == null) {
			synchronized (ContextHolder.class) {
				localInstance = instance;
				if (localInstance == null) {
					instance = localInstance = new ContextHolder();
				}
			}
		}
		return localInstance;
	}

	private Context mContext;

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context pContext) {
		this.mContext = pContext;
	}

}
