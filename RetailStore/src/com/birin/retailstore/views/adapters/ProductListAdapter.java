package com.birin.retailstore.views.adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.birin.retailstore.R;
import com.birin.retailstore.provider.ProductTableConstants;
import com.birin.retailstore.utils.DummyProductStore;
import com.birin.retailstore.utils.Utils;

/**
 * Adapter for product listing page.
 */
public class ProductListAdapter extends SectionCursorAdapter {

	private Context context;
	private Handler eventHandler;
	private final int DEVICE_WT;
	private final int SPACING;
	private final int SPACING_BETWEEN_SECTIONS;

	public ProductListAdapter(Context context, Cursor cursor,
			Handler eventHandler) {
		super(context, cursor, 0);
		this.context = context;
		this.eventHandler = eventHandler;
		this.DEVICE_WT = Utils.getDeviceWidth(context);
		this.SPACING = Utils.getSpacing(context);
		this.SPACING_BETWEEN_SECTIONS = Utils
				.getSpacingBetweenSections(context);
	}

	@Override
	protected Object getSectionFromCursor(Cursor cursor) {
		return cursor.getInt(cursor
				.getColumnIndex(ProductTableConstants.PRODUCT_CATEGORY_ID));
	}

	@Override
	protected View newSectionView(Context context, Object item, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		return inflater.inflate(R.layout.product_list_section_header, null);
	}

	@Override
	protected void bindSectionView(View sectionHeader, Context context,
			int position, Object item) {
		setSectionText(sectionHeader, position, item);
	}

	@Override
	protected View newItemView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View productListItem = inflater.inflate(R.layout.product_list_item,
				null);
		if (null != productListItem) {
			setProductListItem(productListItem);
			setProductImage(productListItem);
			setProductInfoContainer(productListItem);
			setCartContainer(productListItem);
			setCartIcon(productListItem);
		}
		return productListItem;
	}

	@Override
	protected void bindItemView(View row, Context context, Cursor cursor) {
		if (null != row && null != cursor) {
			new ListItemViewSetter(eventHandler).bindViews(row, cursor);
		}
	}

	private void setProductListItem(View productListItem) {
		productListItem.setPadding(SPACING, 0, SPACING, SPACING);
	}

	/**
	 * Sets the product image size of listing page.
	 */
	private void setProductImage(View productListItem) {
		ImageView productImage = (ImageView) productListItem
				.findViewById(R.id.product_image);
		if (null != productImage) {
			productImage.getLayoutParams().width = DEVICE_WT - (2 * SPACING);
			productImage.getLayoutParams().height = Utils
					.calculateCoverImageHeight(productImage.getLayoutParams().width);
		}
	}

	/**
	 * Sets the product info container padding & size.
	 * 
	 * @param productListItem
	 *            the new product info container
	 */
	private void setProductInfoContainer(View productListItem) {
		LinearLayout productInfoContainer = (LinearLayout) productListItem
				.findViewById(R.id.product_info);
		if (null != productInfoContainer) {
			int topBottomSpacing = (int) (SPACING / 2.0);
			productInfoContainer.setPadding(SPACING, topBottomSpacing, SPACING,
					topBottomSpacing);
			productInfoContainer.getLayoutParams().width = DEVICE_WT;
		}
	}

	/**
	 * Sets the cart container on each card.
	 */
	private void setCartContainer(View productListItem) {
		LinearLayout cartContainer = (LinearLayout) productListItem
				.findViewById(R.id.cart_container);
		if (null != cartContainer) {
			cartContainer.setPadding(0, SPACING, SPACING, 0);
		}
	}

	private void setCartIcon(View productListItem) {
		ImageView addKart = (ImageView) productListItem
				.findViewById(R.id.add_cart);
		if (null != addKart) {
			addKart.getLayoutParams().width = addKart.getLayoutParams().height = Utils
					.getListingCartIconSize(context);
		}
	}

	/**
	 * Sets the section text ie. Header category test.
	 * 
	 * @param sectionHeader
	 *            the section header which is section view to show categories
	 * @param position
	 *            the position of header
	 * @param item
	 *            the item data for this header.
	 */
	private void setSectionText(View sectionHeader, int position, Object item) {
		if (null != sectionHeader) {
			boolean isFirstSection = position == 0;
			int sectionTopSpacing = isFirstSection ? SPACING
					: SPACING_BETWEEN_SECTIONS;
			sectionHeader.setPadding(SPACING, sectionTopSpacing, SPACING,
					SPACING);
			if (null != item && item instanceof Integer) {
				int categoryId = (Integer) item;
				TextView section = (TextView) sectionHeader
						.findViewById(R.id.section_name);
				if (null != section) {
					section.setText(DummyProductStore
							.getCategoryNameByCategoryId(categoryId));
				}
			}
		}
	}

}
