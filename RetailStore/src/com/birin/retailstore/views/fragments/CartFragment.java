package com.birin.retailstore.views.fragments;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.birin.retailstore.R;
import com.birin.retailstore.provider.ProductTableConstants;
import com.birin.retailstore.provider.RetailProvider;
import com.birin.retailstore.utils.DummyProductStore;
import com.birin.retailstore.utils.Utils;
import com.birin.retailstore.views.adapters.CartListAdapter;

/**
 * Cart Fragment, responsible for showing all the items present inside our cart
 * & also total value of cart items.
 * 
 */
public class CartFragment extends BaseDataFragment implements OnClickListener,
		OnItemClickListener {

	private Button checkoutButton;
	private ListView listView;
	private CartListAdapter adapter;

	public static CartFragment getInstance() {
		return new CartFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View cartPageViews = inflater.inflate(R.layout.cart_page, null);
		initViews(cartPageViews);
		return cartPageViews;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(0, null, this);
	}

	/**
	 * Setup views & on click listeners.
	 * 
	 * @param cartPageViews
	 *            the cart page views
	 */
	private void initViews(View cartPageViews) {
		checkoutButton = (Button) cartPageViews.findViewById(R.id.checkout);
		listView = (ListView) cartPageViews.findViewById(R.id.cart_list);
		adapter = new CartListAdapter(context, null, eventHandler);
		listView.setEmptyView(cartPageViews.findViewById(R.id.empty_list_item));
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		checkoutButton.setOnClickListener(this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new CursorLoader(context, RetailProvider.CONTENT_URI_PRODUCT,
				null, ProductTableConstants.IN_CART_SELECTION, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		updateCheckoutButton(data);
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}

	/**
	 * Updates checkout button with the total price in the cart.
	 * 
	 * @param data
	 *            the data which is used to check cart size
	 */
	private void updateCheckoutButton(Cursor data) {
		int finalPrice = 0;
		if (null != data) {
			while (data.moveToNext()) {
				int cartSize = data
						.getInt(data
								.getColumnIndex(ProductTableConstants.PRODUCT_CART_SIZE));
				int producePrice = data.getInt(data
						.getColumnIndex(ProductTableConstants.PRODUCT_PRICE));
				finalPrice += (cartSize * producePrice);
			}
		}
		if (finalPrice != 0) {
			checkoutButton.setVisibility(View.VISIBLE);
			checkoutButton.setText(context.getString(
					R.string.proceed_to_checkout,
					DummyProductStore.DEFAULT_CURRENCY, finalPrice));
		} else {
			checkoutButton.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.checkout:
			Utils.showToast(context, R.string.proceed_to_payment);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
		Cursor cursor = adapter.getCursor();
		if (cursor.moveToPosition(position)) {
			int productId = cursor.getInt(cursor
					.getColumnIndex(ProductTableConstants.PRODUCT_ID));
			launchProductDetailFragment(productId);
		}
	}

}
