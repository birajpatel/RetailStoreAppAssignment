package com.birin.retailstore.tasks;

import android.content.ContentValues;
import android.content.Context;

import com.birin.retailstore.provider.ProductTableConstants;
import com.birin.retailstore.provider.RetailProvider;

/**
 * Removes a product from the cart.
 */
public class RemoveFromCartTask extends BaseCartOperationTask {

	public RemoveFromCartTask(Context context) {
		super(context);
	}

	@Override
	protected void performCartOperation(int productId) {
		String where = ProductTableConstants.PRODUCT_ID + " = ? ";
		String[] whereArgs = new String[] { Integer.toString(productId) };
		ContentValues newCartData = new ContentValues();
		newCartData.put(ProductTableConstants.PRODUCT_CART_SIZE, 0);
		resolver.update(RetailProvider.CONTENT_URI_PRODUCT, newCartData, where,
				whereArgs);
	}
}
