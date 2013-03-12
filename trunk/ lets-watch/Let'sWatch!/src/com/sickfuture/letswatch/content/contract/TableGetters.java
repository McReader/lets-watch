package com.sickfuture.letswatch.content.contract;

import android.provider.BaseColumns;

public abstract class TableGetters implements BaseColumns{

	abstract String getTableName();
	
	abstract String[] getColumns();
	
}
