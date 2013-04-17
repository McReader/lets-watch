package com.sickfuture.letswatch.app;

import android.app.Application;

public class LetsWatchApplication extends Application {
	@Override
	public void onCreate() {
		ContextHolder.getInstance().setContext(this);
		super.onCreate();
	}
}
