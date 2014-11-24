package com.birin.retailstore.views.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.birin.retailstore.R;
import com.birin.retailstore.utils.Constants;
import com.birin.retailstore.utils.Utils;

/**
 * Base class for all our fragments, this is useful to setup common
 * functionality at parent level, also handles common click events.
 */
abstract class BaseDataFragment extends Fragment implements
		LoaderCallbacks<Cursor> {

	protected Context context;
	protected EventHandler eventHandler;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity.getApplicationContext();
		eventHandler = new EventHandler(Looper.getMainLooper());
	}

	protected void launchProductDetailFragment(int productId) {
		ProductDetailFragment fragment = ProductDetailFragment
				.getInstance(productId);
		FragmentManager fm = getFragmentManager();
		fm.beginTransaction().add(R.id.container, fragment)
				.addToBackStack(null).commit();
	}

	class EventHandler extends Handler {

		public EventHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constants.TAG_CART_CLICK_ACTION:
				handleCartOperation(msg);
				break;
			}
		}

	}

	/**
	 * Handle cart operation, this will toggle our cart state.
	 * 
	 * @param msg
	 *            the msg from which details for cart operation needs to be
	 *            fetched.
	 */
	private void handleCartOperation(Message msg) {
		int productId = msg.arg1;
		int action = msg.arg2;
		Utils.toggleCartState(productId, action, context);
	}
}
