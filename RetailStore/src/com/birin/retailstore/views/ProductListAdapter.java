package com.birin.retailstore.views;

import com.birin.retailstore.R;
import com.birin.retailstore.provider.ProductTableConstants;
import com.birin.retailstore.utils.DummyProductStore;

import android.content.Context;
import android.database.Cursor;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CursorAdapter;
import android.widget.ImageView;

public class ProductListAdapter extends CursorAdapter {

	private final int DEVICE_WT;

	public ProductListAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		DisplayMetrics displaymetrics = context.getResources()
				.getDisplayMetrics();
		DEVICE_WT = displaymetrics.widthPixels;
	}

	@Override
	public void bindView(View row, Context context, Cursor cursor) {
		ImageView image = (ImageView) row;
		image.setImageResource(DummyProductStore
				.getCoverImageByProductId(cursor.getInt(cursor
						.getColumnIndex(ProductTableConstants.PRODUCT_ID))));
	}

	@Override
	public View newView(Context context, Cursor arg1, ViewGroup arg2) {
		ImageView imageView = new ImageView(context);
		imageView.setLayoutParams(new AbsListView.LayoutParams(DEVICE_WT,
				(int) (DEVICE_WT * (9 / 16.0))));
		imageView.setBackgroundResource(R.drawable.background);
		return imageView;
	}

}
