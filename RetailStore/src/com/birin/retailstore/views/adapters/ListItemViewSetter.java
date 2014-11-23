package com.birin.retailstore.views.adapters;

import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.birin.retailstore.R;
import com.birin.retailstore.provider.ProductTableConstants;
import com.birin.retailstore.utils.Constants;
import com.birin.retailstore.utils.DummyProductStore;


/**
 * A common class which is used to set the views from both cart & listing page
 * adapters, This class makes adapter class much cleaner as all the view setting
 * responsibilities are delegated to this class.
 */
public class ListItemViewSetter implements OnClickListener {

	private Handler eventHandler;

	public ListItemViewSetter(Handler eventHandler) {
		this.eventHandler = eventHandler;
	}

	public void bindViews(View row, Cursor cursor) {
		final int productID = cursor.getInt(cursor
				.getColumnIndex(ProductTableConstants.PRODUCT_ID));
		setProductImage(row, productID);
		setProductName(row, cursor);
		setProductPrice(row, cursor);
		setAddRemoveCartButton(row, productID, cursor);
		setRemoveFromCartButton(row, productID, cursor);
	}

	private void setProductName(View row, Cursor cursor) {
		TextView name = (TextView) row.findViewById(R.id.product_name);
		if (null != name) {
			name.setText(cursor.getString(cursor
					.getColumnIndex(ProductTableConstants.PRODUCT_NAME)));
		}
	}

	private void setProductPrice(View row, Cursor cursor) {
		TextView price = (TextView) row.findViewById(R.id.product_price);
		if (null != price) {
			price.setText(DummyProductStore.DEFAULT_CURRENCY
					+ cursor.getInt(cursor
							.getColumnIndex(ProductTableConstants.PRODUCT_PRICE)));
		}
	}

	private void setProductImage(View row, int productID) {
		ImageView image = (ImageView) row.findViewById(R.id.product_image);
		if (null != image) {
			image.setImageResource(DummyProductStore
					.getCoverImageByProductId(productID));
		}
	}

	private void setAddRemoveCartButton(View row, int productID, Cursor cursor) {
		ImageView addKart = (ImageView) row.findViewById(R.id.add_cart);
		if (null != addKart) {
			addKart.setOnClickListener(this);
			addKart.setTag(productID);
			int cartSize = cursor.getInt(cursor
					.getColumnIndex(ProductTableConstants.PRODUCT_CART_SIZE));
			boolean isCartFilled = (cartSize > 0);
			addKart.setImageResource(isCartFilled ? R.drawable.item_cart_filled
					: R.drawable.item_cart_buy);
			addKart.setTag(Constants.TAG_CART_CLICK_ACTION,
					isCartFilled ? Constants.TAG_VALUE_REMOVE
							: Constants.TAG_VALUE_ADD);
		}
	}

	private void setRemoveFromCartButton(View row, int productID, Cursor cursor) {
		Button removeFromCart = (Button) row
				.findViewById(R.id.remove_from_kart);
		if (null != removeFromCart) {
			removeFromCart.setOnClickListener(this);
			removeFromCart.setTag(productID);
			removeFromCart.setTag(Constants.TAG_CART_CLICK_ACTION,
					Constants.TAG_VALUE_REMOVE);
		}

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.add_cart:
		case R.id.remove_from_kart:
			dispatchCartOperationEvent(view);
			break;
		}
	}

	private void dispatchCartOperationEvent(View view) {
		Message msg = eventHandler.obtainMessage();
		msg.what = Constants.TAG_CART_CLICK_ACTION;
		msg.arg1 = (Integer) view.getTag();
		msg.arg2 = (Integer) view.getTag(Constants.TAG_CART_CLICK_ACTION);
		eventHandler.sendMessage(msg);
	}

}
