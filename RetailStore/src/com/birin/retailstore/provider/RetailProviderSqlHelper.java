package com.birin.retailstore.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQL helper for content-provider
 */
public class RetailProviderSqlHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "RetailStore";
	public static final int DATABASE_VERSION = 1;

	public static final String INDEX = "_id";

	public RetailProviderSqlHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(ProductTableConstants.CREATE_TABLE_QUERY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(ProductTableConstants.DROP_TABLE_QUERY);
		onCreate(db);
	}

}
