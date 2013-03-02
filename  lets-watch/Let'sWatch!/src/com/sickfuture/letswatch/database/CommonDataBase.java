package com.sickfuture.letswatch.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CommonDataBase extends SQLiteOpenHelper {

	private static final String SELECT_DISTINCT_TBL_NAME_FROM_SQLITE_MASTER_WHERE_TBL_NAME = "select DISTINCT tbl_name from sqlite_master where tbl_name = '";

	private static final String VARCHAR = "VARCHAR";

	private static final String CREATE_TABLE = "CREATE TABLE";

	private static final String VARCHAR_PRIMARY_KEY = " VARCHAR PRIMARY KEY";

	private static final String DATABASE_NAME = "moviesinfo.store.db";

	protected static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

	private final static int DB_VERSION = 1;

	private CreateAndUpgrade createAndUpgrade;

	protected CommonDataBase(Context context, CursorFactory factory,
			final String tableName, final String[] coloumns) {
		super(context, DATABASE_NAME, factory, DB_VERSION);
		createAndUpgrade = new CreateAndUpgrade() {

			public void open(SQLiteDatabase db) {
				if (!isTableExists(db, tableName)) {
					onCreate(db);
				}
			}

			public void upgrade(SQLiteDatabase db, int oldVersion,
					int newVersion) {
				try {
					db.beginTransaction();
					db.execSQL(DROP_TABLE + tableName);
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
				onCreate(db);
			}

			public void create(SQLiteDatabase db) {
				try {
					db.beginTransaction();
					db.execSQL(creationTable(tableName, coloumns));
					Log.d("DB TABLE CREATION",
							"create" + creationTable(tableName, coloumns));
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
			}
		};
	}

	private String creationTable(String tableName, String[] coloumns) {
		StringBuilder sb = new StringBuilder();
		sb.append(CREATE_TABLE + " " + tableName + " (");
		sb.append(coloumns[0] + VARCHAR_PRIMARY_KEY + ", ");
		for (int i = 1; i < coloumns.length - 1; i++) {
			sb.append(coloumns[i] + " " + VARCHAR + ", ");
		}
		sb.append(coloumns[coloumns.length - 1] + " " + VARCHAR + ")");
		return sb.toString();
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		createAndUpgrade.open(db);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createAndUpgrade.create(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		createAndUpgrade.upgrade(db, oldVersion, newVersion);
	}

	protected void delete(String tableName, String where, String[] whereArgs) {
		if (isTableExists(getWritableDatabase(), tableName)) {
			getWritableDatabase().delete(tableName, where, whereArgs);
		}
	}

	private boolean isTableExists(SQLiteDatabase database, String tableName) {
		Cursor cursor = database.rawQuery(
				SELECT_DISTINCT_TBL_NAME_FROM_SQLITE_MASTER_WHERE_TBL_NAME
						+ tableName + "'", null);
		if (cursor.getCount() > 0) {
			cursor.close();
			return true;
		} else {
			cursor.close();
			return false;
		}
	}
}
