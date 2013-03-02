package com.sickfuture.letswatch.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;

import com.sickfuture.letswatch.R;
import com.sickfuture.letswatch.content.contract.Contract;
import com.sickfuture.letswatch.http.HttpManager;
import com.sickfuture.letswatch.service.common.CommonService;
import com.sickfuture.letswatch.task.CommonTask;

public class BoxOfficeService extends CommonService {

	private List<JSONObject> mList;

	@Override
	protected void task(Intent intent) {
		CommonTask<List<JSONObject>> commonTask = new CommonTask<List<JSONObject>>(
				this) {

			@Override
			public List<JSONObject> convert(String s) throws Exception {
				JSONArray jsonArray = HttpManager.getInstance()
						.loadAsJSONObject(s).getJSONArray("movies");
				mList = new ArrayList<JSONObject>(jsonArray.length());
				if (jsonArray.length() == 0) {
					return null;
				}
				for (int i = 0; i < jsonArray.length(); i++) {
					mList.add(jsonArray.getJSONObject(i));
				}
				return mList;
			}
		};
		commonTask.start(getString(R.string.API_BOX_OFFICE_REQUEST_URL));
	}

	@Override
	protected Uri getProviderUri() {
		return Contract.BoxOfficeColumns.CONTENT_URI;
	}

}
