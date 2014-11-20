package com.birin.retailstore.provider;

import com.birin.retailstore.utils.DummyProductStore;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * Helper fragment class which will be used to hold data across configuration
 * changes of activity life-cycle.
 */
public class CursorRetainingFragment extends Fragment {

	private DummyDataGeneratorCallback mCallbacks;
	private DummyTask mTask;
	private boolean isTaskRunning;

	/**
	 * Hold a reference to the parent Activity so we can report the task's
	 * current progress and results. The Android framework will pass us a
	 * reference to the newly created Activity after each configuration change.
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof DummyDataGeneratorCallback) {
			mCallbacks = (DummyDataGeneratorCallback) activity;
		}
	}

	/**
	 * This method will only be called once when the retained Fragment is first
	 * created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Retain this fragment across configuration changes.
		setRetainInstance(true);
		runAddDataTaskToEnsureMinimumData();
	}

	/**
	 * Set the callback to null so we don't accidentally leak the Activity
	 * instance.
	 */
	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	public boolean isTaskRunning() {
		return isTaskRunning;
	}

	private void runAddDataTaskToEnsureMinimumData() {
		runAddDataTaskIfNotRunning();
	}

	private void runAddDataTaskIfNotRunning() {
		if (isTaskRunning() == false) {
			if (null != mTask) {
				mTask.cancel(true);
				mTask = null;
			}
			// Create and execute the background task.
			mTask = new DummyTask();
			mTask.execute();
		}
	}

	/**
	 * A dummy task that performs some (dumb) background work and proxies
	 * progress updates and results back to the Activity.
	 * 
	 */
	private class DummyTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {
			isTaskRunning = true;
			if (mCallbacks != null) {
				mCallbacks.onDataGeneratorTaskExecuting();
			}
		}

		@Override
		protected Void doInBackground(Void... ignore) {
			Activity activity = getActivity();
			if (null != activity) {
				ContentResolver resolver = activity.getContentResolver();
				Cursor existingData = resolver.query(
						RetailProvider.CONTENT_URI_PRODUCT, null, null, null,
						null);
				int currentSize = existingData == null ? 0 : existingData
						.getCount();
				if (currentSize <= 0) {
					generateNewDummyDataAndAddToDB(resolver);
				}
			}
			return null;
		}

		@Override
		protected void onCancelled() {
			isTaskRunning = false;
		}

		@Override
		protected void onPostExecute(Void result) {
			isTaskRunning = false;
		}

	}

	private void generateNewDummyDataAndAddToDB(ContentResolver resolver) {
		ContentValues valuesToInsert = new ContentValues();
		for (int i = 0; i < DummyProductStore.COUNT; i++) {
			valuesToInsert.clear();
			updateValuesToInsert(valuesToInsert, i);
			resolver.insert(RetailProvider.CONTENT_URI_PRODUCT, valuesToInsert);
		}
	}

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

	public interface DummyDataGeneratorCallback {

		public void onDataGeneratorTaskExecuting();
	}

}
