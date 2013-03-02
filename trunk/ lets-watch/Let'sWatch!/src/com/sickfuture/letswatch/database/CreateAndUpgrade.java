package com.sickfuture.letswatch.database;

import android.database.sqlite.SQLiteDatabase;

public interface CreateAndUpgrade {
	public void create(SQLiteDatabase db);

	public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion);

	public void open(SQLiteDatabase db);
}
