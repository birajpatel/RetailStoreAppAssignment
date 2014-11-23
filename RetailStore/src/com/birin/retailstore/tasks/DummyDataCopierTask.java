package com.birin.retailstore.tasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.birin.retailstore.provider.ProductTableConstants;
import com.birin.retailstore.provider.RetailProvider;
import com.birin.retailstore.utils.DummyProductStore;

/**
 * A task that performs copying of our {@link DummyProductStore} values to DB.
 * 
 */
public class DummyDataCopierTask extends AsyncTask<Void, Integer, Void> {

	private ContentResolver resolver;

	public DummyDataCopierTask(Context context) {
		this.resolver = context.getContentResolver();
	}

	@Override
	protected Void doInBackground(Void... ignore) {
		Cursor existingData = resolver.query(
				RetailProvider.CONTENT_URI_PRODUCT, null, null, null, null);
		int currentSize = existingData == null ? 0 : existingData.getCount();
		if (currentSize <= 0) {
			generateNewDummyDataAndAddToDB(resolver);
		} else {
			// Data already present in DB ignore.
		}
		return null;
	}

	/**
	 * Generates new dummy data recursively and add to db.
	 * 
	 * @param resolver
	 *            the content resolver
	 */
	private void generateNewDummyDataAndAddToDB(ContentResolver resolver) {
		ContentValues valuesToInsert = new ContentValues();
		for (int dataIndex = 0; dataIndex < DummyProductStore.COUNT; dataIndex++) {
			valuesToInsert.clear();
			updateValuesToInsert(valuesToInsert, dataIndex);
			resolver.insert(RetailProvider.CONTENT_URI_PRODUCT, valuesToInsert);
		}
	}

	/**
	 * Updates Content-values to be insert & fills with new data.
	 * 
	 * @param valuesToInsert
	 *            the values to be updated
	 * @param i
	 *            the index of data to be inserted
	 */
	private void updateValuesToInsert(ContentValues valuesToInsert, int i) {
		valuesToInsert.put(ProductTableConstants.PRODUCT_NAME,
				DummyProductStore.PRODUCT_NAMES[i]);
		valuesToInsert.put(ProductTableConstants.PRODUCT_PRICE,
				DummyProductStore.PRODUCT_PRICES[i]);
		valuesToInsert.put(ProductTableConstants.PRODUCT_CATEGORY_ID,
				DummyProductStore.PRODUCT_CATEGORIES[i]);
		valuesToInsert.put(ProductTableConstants.PRODUCT_ID,
				DummyProductStore.PRODUCT_IDS[i]);
	}

}