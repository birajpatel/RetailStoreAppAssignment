package com.birin.retailstore.views;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.birin.retailstore.R;
import com.birin.retailstore.provider.ProductTableConstants;
import com.birin.retailstore.utils.DummyProductStore;
import com.birin.retailstore.utils.Utils;

public class ProductListAdapter extends SectionCursorAdapter {

	private final int DEVICE_WT;

	public ProductListAdapter(Context context, Cursor cursor) {
		super(context, cursor, 0);
		DEVICE_WT = Utils.getDeviceWidth(context);
	}

	@Override
	protected Object getSectionFromCursor(Cursor cursor) {
		return cursor.getInt(cursor
				.getColumnIndex(ProductTableConstants.PRODUCT_CATEGORY_ID));
	}

	@Override
	protected View newSectionView(Context context, Object item, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View sectionHeaderView = inflater.inflate(R.layout.list_section_header,
				null);
		int paddingTop = Utils.getSectionTopSpacing(context);
		int paddingLRB = Utils.getSectionLeftRightBottomSpacing(context);
		sectionHeaderView.setPadding(paddingLRB, paddingTop, paddingLRB,
				paddingLRB);
		return sectionHeaderView;
	}

	@Override
	protected void bindSectionView(View sectionHeader, Context context,
			int position, Object item) {
		if (null != item && item instanceof Integer) {
			int categoryId = (int) item;
			TextView section = (TextView) sectionHeader
					.findViewById(R.id.section_name);
			if (null != section) {
				section.setText(DummyProductStore
						.getCategoryNameByCategoryId(categoryId));
			}
		}
	}

	@Override
	protected View newItemView(Context context, Cursor cursor, ViewGroup parent) {
		ImageView imageView = new ImageView(context);
		imageView.setLayoutParams(new AbsListView.LayoutParams(DEVICE_WT,
				(int) (DEVICE_WT * (9 / 16.0))));
		imageView.setBackgroundResource(R.drawable.background);
		return imageView;
	}

	@Override
	protected void bindItemView(View row, Context context, Cursor cursor) {
		ImageView image = (ImageView) row;
		image.setImageResource(DummyProductStore
				.getCoverImageByProductId(cursor.getInt(cursor
						.getColumnIndex(ProductTableConstants.PRODUCT_ID))));

	}

}
