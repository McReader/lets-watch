package com.sickfuture.letswatch.utils;

import com.sickfuture.letswatch.R;
import com.sickfuture.letswatch.http.HttpManager;

import android.app.Activity;
import android.widget.Toast;

public class InetChecker {

	public static boolean checkInetConnection(Activity activity, boolean... showToast){
		if(!HttpManager.getInstance().isAvalibleInetConnection()){
			if(showToast.length!=0){
				if(showToast[0])
					Toast.makeText(activity, R.string.internet_connection_is_not_avalible, Toast.LENGTH_LONG).show();
			} else
				Toast.makeText(activity, R.string.internet_connection_is_not_avalible, Toast.LENGTH_LONG).show();
			return false;
		} else return true;
	}
}
