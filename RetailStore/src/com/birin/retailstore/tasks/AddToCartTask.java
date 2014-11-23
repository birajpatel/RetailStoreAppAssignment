package com.birin.retailstore.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.birin.retailstore.provider.ProductTableConstants;
import com.birin.retailstore.provider.RetailProvider;

/**
 * The Class AddToCartTask is responsible for updating cart size of given
 * productIds by 1.
 */
public class AddToCartTask extends BaseCartOperationTask {

	public AddToCartTask(Context context) {
		super(context);
	}

	/**
	 * Queries current count of DB & updates the count by 1.
	 */
	@Override
	protected void performCartOperation(int productId) {
		String where = ProductTableConstants.PRODUCT_ID + " = ? ";
		String[] whereArgs = new String[] { Integer.toString(productId) };
		Cursor currentCartData = resolver.query(
				RetailProvider.CONTENT_URI_PRODUCT,
				new String[] { ProductTableConstants.PRODUCT_CART_SIZE },
				where, whereArgs, null);
		if (currentCartData.moveToFirst()) {
			int currentCount = currentCartData.getInt(currentCartData
					.getColumnIndex(ProductTableConstants.PRODUCT_CART_SIZE));
			int newCount = currentCount + 1;
			ContentValues newCartData = new ContentValues();
			newCartData.put(ProductTableConstants.PRODUCT_CART_SIZE, newCount);
			resolver.update(RetailProvider.CONTENT_URI_PRODUCT, newCartData,
					where, whereArgs);
		}
	}
}
