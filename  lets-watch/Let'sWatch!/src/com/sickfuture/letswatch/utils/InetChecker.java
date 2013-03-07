package com.sickfuture.letswatch.utils;

import com.sickfuture.letswatch.R;
import com.sickfuture.letswatch.http.HttpManager;

import android.app.Activity;
import android.widget.Toast;

public class InetChecker {

	public static boolean checkInetConnection(Activity activity){
		if(!HttpManager.getInstance().isAvalibleInetConnection()){
			Toast.makeText(activity, R.string.internet_connection_is_not_avalible, Toast.LENGTH_LONG).show();
			return false;
		} else return true;
	}
}
