package com.birin.retailstore.tasks;

import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Base class for all cart updating operations.
 * 
 */
abstract class BaseCartOperationTask extends AsyncTask<Integer, Void, Void> {

	protected final ContentResolver resolver;

	BaseCartOperationTask(Context context) {
		this.resolver = context.getContentResolver();
	}

	@Override
	protected Void doInBackground(Integer... productIds) {
		for (int productId : productIds) {
			performCartOperation(productId);
		}
		return null;
	}

	protected abstract void performCartOperation(int productId);

}
