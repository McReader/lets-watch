package com.sickfuture.letswatch.images;

import android.graphics.Bitmap;

public interface Callback {

	String getUrl();

	void onSuccess(Bitmap bm);

	void onError(Exception e);

}
