package com.birin.retailstore.views.adapters;

import android.content.Context;

import android.database.Cursor;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.birin.retailstore.R;
import com.birin.retailstore.utils.Utils;

/**
 * List Adapter for cart listing page, sets up all views & sizes & padding etc &
 * delegates view setting responsibility to {@link ListItemViewSetter} class.
 */
public class CartListAdapter extends CursorAdapter {

	private final float LIST_IMAGE_WT_SCREEN_PERCENT = 0.4f;
	private final float REMOVE_BUTTON_WT_SCREEN_PERCENT = 0.08f;
	private final int DEVICE_WT;
	private final int SPACING;
	private Handler eventHandler;

	public CartListAdapter(Context context, Cursor c, Handler handler) {
		super(context, c, false);
		this.SPACING = Utils.getSpacing(context);
		this.DEVICE_WT = Utils.getDeviceWidth(context);
		this.eventHandler = handler;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View cartListItem = inflater.inflate(R.layout.cart_list_item, null);
		if (null != cartListItem) {
			setCartListItem(cartListItem);
			setProductImage(cartListItem);
			setProductInfoContainer(cartListItem);
			setRemoveButton(cartListItem);
		}
		return cartListItem;
	}

	@Override
	public void bindView(View row, Context context, Cursor cursor) {
		if (null != row && null != cursor) {
			new ListItemViewSetter(eventHandler).bindViews(row, cursor);
		}
	}

	/**
	 * Sets the size of remove button on cart page.
	 * 
	 * @param cartListItem
	 *            the new removes the button
	 */
	private void setRemoveButton(View cartListItem) {
		Button removeButton = (Button) cartListItem
				.findViewById(R.id.remove_from_kart);
		if (null != removeButton) {
			removeButton.setPadding(3, 3, 3, 3);
			int buttonSize = (int) (REMOVE_BUTTON_WT_SCREEN_PERCENT * DEVICE_WT);
			ViewGroup.LayoutParams params = removeButton.getLayoutParams();
			params.width = params.height = buttonSize;
		}
	}

	/**
	 * Sets the cart list item's spacing.
	 * 
	 * @param cartListItem
	 *            the new cart list item
	 */
	private void setCartListItem(View cartListItem) {
		cartListItem.setPadding(SPACING, SPACING, SPACING, SPACING);
	}

	/**
	 * Assigns 16:9 ratio cover image in small image view.
	 */
	private void setProductImage(View cartListItem) {
		ImageView productImage = (ImageView) cartListItem
				.findViewById(R.id.product_image);
		if (null != productImage) {
			ViewGroup.LayoutParams params = productImage.getLayoutParams();
			params.width = (int) (LIST_IMAGE_WT_SCREEN_PERCENT * DEVICE_WT);
			params.height = Utils.calculateCoverImageHeight(productImage
					.getLayoutParams().width);
		}
	}

	/**
	 * Sets the product info container.
	 * 
	 * @param cartListItem
	 *            the new product info container
	 */
	private void setProductInfoContainer(View cartListItem) {
		LinearLayout infoContainer = (LinearLayout) cartListItem
				.findViewById(R.id.product_info);
		if (null != infoContainer) {
			infoContainer.setPadding(SPACING, 0, 0, 0);
		}
	}

}
