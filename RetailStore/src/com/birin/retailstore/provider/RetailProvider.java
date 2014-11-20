package com.birin.retailstore.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class RetailProvider extends ContentProvider {

	// Constant fields
	public static final String AUTHORITY = "com.birin.retailstore.provider.RetailProvider";
	public static final String CONTENT_TYPE = "vnd.android.cursor.item";
	public static final Uri CONTENT_URI_PRODUCT = Uri.parse("content://"
			+ AUTHORITY + "/" + ProductTableConstants.PRODUCTS_TABLE_NAME);

	private static UriMatcher sUriMatcher = null;
	private static HashMap<String, String> sProductProjectionMap = null;

	private RetailProviderSqlHelper dbHelper = null;

	static {
		// Product Table.
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY,
				ProductTableConstants.PRODUCTS_TABLE_NAME,
				ProductTableConstants.PRODUCT_TABLE_ID);
		sProductProjectionMap = new HashMap<String, String>();
		sProductProjectionMap.put(RetailProviderSqlHelper.INDEX,
				RetailProviderSqlHelper.INDEX);
		sProductProjectionMap.put(ProductTableConstants.PRODUCT_ID,
				ProductTableConstants.PRODUCT_ID);
		sProductProjectionMap.put(ProductTableConstants.PRODUCT_NAME,
				ProductTableConstants.PRODUCT_NAME);
		sProductProjectionMap.put(ProductTableConstants.PRODUCT_PRICE,
				ProductTableConstants.PRODUCT_PRICE);
		sProductProjectionMap.put(ProductTableConstants.PRODUCT_CATEGORY_ID,
				ProductTableConstants.PRODUCT_CATEGORY_ID);
		sProductProjectionMap.put(ProductTableConstants.PRODUCT_CART_SIZE,
				ProductTableConstants.PRODUCT_CART_SIZE);
	}

	@Override
	public boolean onCreate() {
		dbHelper = new RetailProviderSqlHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		if (null == uri || null == sUriMatcher) {
			throw new IllegalArgumentException("Empty URI " + uri);
		}
		switch (sUriMatcher.match(uri)) {
		case ProductTableConstants.PRODUCT_TABLE_ID:
			return CONTENT_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		int count = 0;
		if (null == uri || null == sUriMatcher) {
			return count;
		}
		String tableName = null;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (null != db && db.isOpen()) {
			try {
				tableName = getTableNameFromUri(uri);
				if (TextUtils.isEmpty(tableName) == false) {
					db.beginTransaction();
					count = db.delete(tableName, where, whereArgs);
					db.setTransactionSuccessful();
					db.endTransaction();
					if (count > 0) {
						notifyChangeToObeservers(uri, null);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return count;

	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (null == uri || null == sUriMatcher) {
			return null;
		}
		if (null == values) {
			values = new ContentValues();
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long rowId = 0;
		Uri newRowUri = null;
		String tableName = null;
		if (null != db && db.isOpen()) {
			try {
				tableName = getTableNameFromUri(uri);
				Uri contentUri = getContentURIFromUri(uri);
				if (TextUtils.isEmpty(tableName) == false && null != contentUri) {
					db.beginTransaction();
					rowId = db.insert(tableName, null, values);
					db.setTransactionSuccessful();
					db.endTransaction();
					if (rowId > 0) {
						newRowUri = ContentUris.withAppendedId(contentUri,
								rowId);
						notifyChangeToObeservers(newRowUri, null);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return newRowUri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		if (null == uri || null == sUriMatcher) {
			return null;
		}
		SQLiteQueryBuilder queryBuilder = getQueryBuilderForUri(uri);
		SQLiteDatabase sqliteDataBase = dbHelper.getReadableDatabase();
		Cursor cursor = null;
		if (null != sqliteDataBase && null != queryBuilder
				&& sqliteDataBase.isOpen()) {
			try {
				sqliteDataBase.beginTransaction();
				cursor = queryBuilder.query(sqliteDataBase, projection,
						selection, selectionArgs, null, null, sortOrder);
				sqliteDataBase.setTransactionSuccessful();
				sqliteDataBase.endTransaction();
				if (null != cursor) {
					cursor.setNotificationUri(
							getContext().getContentResolver(), uri);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// here we can not close the cursor because it will be used by
				// client application to retrieve data from .
			}
		}
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		int count = 0;
		if (null == uri || null == sUriMatcher) {
			return count;
		}
		String tableName = null;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (null != db && db.isOpen()) {
			try {
				tableName = getTableNameFromUri(uri);
				db.beginTransaction();
				count = db.update(tableName, values, where, whereArgs);
				db.setTransactionSuccessful();
				db.endTransaction();
				notifyChangeToObeservers(uri, null);
				if (count > 0) {
					notifyChangeToObeservers(uri, null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return count;
	}

	private void notifyChangeToObeservers(Uri uri, ContentObserver observer) {
		getContext().getContentResolver().notifyChange(uri, observer);
	}

	private String getTableNameFromUri(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case ProductTableConstants.PRODUCT_TABLE_ID:
			return ProductTableConstants.PRODUCTS_TABLE_NAME;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	private Uri getContentURIFromUri(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case ProductTableConstants.PRODUCT_TABLE_ID:
			return CONTENT_URI_PRODUCT;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	private SQLiteQueryBuilder getQueryBuilderForUri(Uri uri) {
		SQLiteQueryBuilder sQLiteQueryBuilderObj = null;
		switch (sUriMatcher.match(uri)) {
		case ProductTableConstants.PRODUCT_TABLE_ID:
			sQLiteQueryBuilderObj = new SQLiteQueryBuilder();
			sQLiteQueryBuilderObj
					.setTables(ProductTableConstants.PRODUCTS_TABLE_NAME);
			sQLiteQueryBuilderObj.setProjectionMap(sProductProjectionMap);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		return sQLiteQueryBuilderObj;
	}
}
