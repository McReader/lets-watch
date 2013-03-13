package com.sickfuture.letswatch.database;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sickfuture.letswatch.ContextHolder;
import com.sickfuture.letswatch.content.contract.Contract;
import com.sickfuture.letswatch.content.contract.TableGetters;

public class CommonDataBase extends SQLiteOpenHelper {

	private static final String GET_COLUMNS = "getColumns";

	private static final String GET_TABLE_NAME = "getTableName";

	private static final String SELECT_DISTINCT_TBL = "select DISTINCT tbl_name from sqlite_master where tbl_name = '";

	private static final String VARCHAR = "VARCHAR";

	private static final String CREATE_TABLE = "CREATE TABLE";

	private static final String INTEGER_PRIMARY_KEY = " INTEGER PRIMARY KEY";

	private static final String DATABASE_NAME = "moviesinfo.store.db";

	private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

	private final static int DB_VERSION = 3;

	private SQLiteDatabase mDatabase;

	private static volatile CommonDataBase instance;

	private CommonDataBase() {
		super(ContextHolder.getInstance().getContext(), DATABASE_NAME, null,
				DB_VERSION);
	}

	public static CommonDataBase getInstance() {
		CommonDataBase localInstance = instance;
		if (instance == null) {
			synchronized (CommonDataBase.class) {
				localInstance = instance;
				if (localInstance == null) {
					instance = localInstance = new CommonDataBase();
				}
			}
		}
		return localInstance;
	}

	private String creationTable(String tableName, String[] coloumns) {
		StringBuilder sb = new StringBuilder();
		sb.append(CREATE_TABLE + " " + tableName + " (");
		sb.append(coloumns[0] + INTEGER_PRIMARY_KEY + ", ");
		for (int i = 1; i < coloumns.length - 1; i++) {
			sb.append(coloumns[i] + " " + VARCHAR + ", ");
		}
		sb.append(coloumns[coloumns.length - 1] + " " + VARCHAR + ")");
		return sb.toString();
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

	/** Note to self: Don't forget delete logs */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onCreate(SQLiteDatabase db) {
		Class<TableGetters>[] subClasses = (Class<TableGetters>[]) Contract.class
				.getClasses();
		if(subClasses == null){
			throw new ClassCastException("WARNING! Check your Contract class, please!");
		}
		if(subClasses.length < 1){
			throw new SQLException("WARNING! No tables in Contract!");
		}
		Log.i("REFLECTION TEST", "Class Array Size:  " + subClasses.length);
		String tableName=null;
		String[] columns=null; 
		for (Class<TableGetters> cls : subClasses) {
			try {
				Class cl = Class.forName(cls.getName());
				final Object obj = cl.newInstance();
				final Method getTableMethod = cl.getDeclaredMethod(GET_TABLE_NAME);
				final Method getColumnsMethod = cl.getDeclaredMethod(GET_COLUMNS);
				tableName = AccessController
						.doPrivileged(new PrivilegedExceptionAction() {
							public Object run() throws Exception {
								if (!getTableMethod.isAccessible()) {
									getTableMethod.setAccessible(true);
								}
								return getTableMethod.invoke(obj);
							}
						});
				columns = AccessController
						.doPrivileged(new PrivilegedExceptionAction() {
							public Object run() throws Exception {
								if (!getColumnsMethod.isAccessible()) {
									getColumnsMethod.setAccessible(true);
								}
								return getColumnsMethod.invoke(obj);
							}
						});
				Log.i("REFLECTION TEST", "class:  " + cls.getName());
				Log.i("REFLECTION TEST", "result:  " + tableName);
				Log.i("REFLECTION TEST", "result:  " + columns.length);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (PrivilegedActionException e) {
				e.printStackTrace();
			}
		
		try {
			db.beginTransaction();
			db.execSQL(creationTable(tableName, columns));
			Log.d("DB TABLE CREATION",
					"create" + creationTable(tableName, columns));
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	  }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//TODO Do onUpgrade like onCreate
		try {
			db.beginTransaction();
			//db.execSQL(DROP_TABLE + mTableName);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		onCreate(db);
	}

	public long addItem(String tableName, String[] columns,
			JSONObject jsonObject, ContentValues contentValues)
			throws JSONException {
		mDatabase = getWritableDatabase();
		long value;
		try {
			mDatabase.beginTransaction();
			value = mDatabase.insertWithOnConflict(tableName, null, contentValues,
					SQLiteDatabase.CONFLICT_REPLACE);
			if (value <= 0) {
				throw new SQLException("Failed to insert row into " + tableName);
			}
			mDatabase.setTransactionSuccessful();
		} finally {
			mDatabase.endTransaction();
		}
		return value;
	}

	public Cursor getItems(String tableName, String[] columns, String orderBy,
			String selection, String[] selectionArgs) {
		mDatabase = getReadableDatabase();
		Cursor cursor;
		try {
			mDatabase.beginTransaction();
			cursor = null;
			cursor = mDatabase.query(tableName, null, selection, selectionArgs,
					null, null, orderBy);
			if (cursor == null) {
				throw new SQLException("Failed to query row from " + tableName);
			}
			mDatabase.setTransactionSuccessful();
		} finally {
			mDatabase.endTransaction();
		}
		return cursor;
	}

	public void deleteTable(String tableName, String where,
			String[] whereArgs) {
		if (isTableExists(mDatabase, tableName)) {
			mDatabase.delete(tableName, where, whereArgs);
		}
	}

	private static boolean isTableExists(SQLiteDatabase database,
			String tableName) {
		database.beginTransaction();
		Cursor cursor;
		try {
			cursor = database.rawQuery(SELECT_DISTINCT_TBL + tableName + "'",
					null);
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
		if (cursor.getCount() > 0) {
			cursor.close();
			return true;
		} else {
			cursor.close();
			return false;
		}
	}
}