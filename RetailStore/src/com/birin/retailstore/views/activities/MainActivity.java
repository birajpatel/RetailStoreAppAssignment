package com.birin.retailstore.views.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import com.birin.retailstore.R;
import com.birin.retailstore.provider.ProductTableConstants;
import com.birin.retailstore.provider.RetailProvider;
import com.birin.retailstore.utils.Utils;
import com.birin.retailstore.views.fragments.CartFragment;
import com.birin.retailstore.views.fragments.DataListFragment;

public class MainActivity extends Activity implements LoaderCallbacks<Cursor>,
		ActionBarController.OnCartClickListener {

	private ActionBarController actionBarController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		actionBarController = new ActionBarController(getApplicationContext(),
				this, getActionBar());
		actionBarController.setupActionBar(Utils.getActionBarHeight(this));
		loadListingFragment();
		getLoaderManager().initLoader(0, null, this);
	}

	private void loadListingFragment() {
		FragmentManager fm = getFragmentManager();
		fm.beginTransaction().add(R.id.container, new DataListFragment())
				.commit();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getApplicationContext(),
				RetailProvider.CONTENT_URI_PRODUCT,
				ProductTableConstants.PRODUCT_COUNT_PROJECTION,
				ProductTableConstants.IN_CART_SELECTION, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		actionBarController.updateCartCountIndicator(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

	@Override
	public void onCartClicked() {
		Fragment currentFragment = getFragmentManager().findFragmentById(
				R.id.container);
		if (null != currentFragment
				&& currentFragment instanceof CartFragment == false) {
			// launch cart
			FragmentManager fm = getFragmentManager();
			fm.beginTransaction()
					.add(R.id.container, CartFragment.getInstance())
					.addToBackStack(null).commit();
		} else {
			// Cart is already is open.
			Utils.showToast(getApplicationContext(), R.string.already_in_cart);
		}
	}
}
