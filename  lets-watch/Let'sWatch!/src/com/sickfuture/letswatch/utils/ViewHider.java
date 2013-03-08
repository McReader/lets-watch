package com.sickfuture.letswatch.utils;

import android.view.View;
import android.widget.AbsListView.LayoutParams;

public class ViewHider {

	public static void hideListItem(View view, boolean hide){
		if(hide){
			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
			view.setVisibility(View.GONE);
		} else{
			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			view.setVisibility(View.VISIBLE);
		}
	}

}
