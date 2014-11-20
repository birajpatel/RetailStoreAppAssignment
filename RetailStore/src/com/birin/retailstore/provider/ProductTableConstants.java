package com.birin.retailstore.provider;

import static com.birin.retailstore.provider.RetailProviderSqlHelper.INDEX;

public class ProductTableConstants {

	public static final String PRODUCTS_TABLE_NAME = "Products";
	public static final String PRODUCT_NAME = "name";
	public static final String PRODUCT_PRICE = "price";
	public static final String PRODUCT_ID = "productId";
	public static final String PRODUCT_CATEGORY_ID = "categoryId";
	public static final String PRODUCT_CART_SIZE = "cartSize";
	public static final int PRODUCT_TABLE_ID = 1;

	static final String CREATE_TABLE_QUERY = "CREATE TABLE "
			+ PRODUCTS_TABLE_NAME + " " + "(" /* Start */
			+ INDEX + " INTEGER PRIMARY KEY AUTOINCREMENT" /* Unique-Id */
			+ "," + PRODUCT_NAME + " TEXT" /* product name */
			+ "," + PRODUCT_PRICE + " TEXT " /* product price */
			+ "," + PRODUCT_ID + " INTEGER " /* product id */
			+ "," + PRODUCT_CATEGORY_ID + " INTEGER " /* product cat id */
			+ "," + PRODUCT_CART_SIZE + " INTEGER DEFAULT 0 " /* cart value */
			+ ");";

	static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS "
			+ PRODUCTS_TABLE_NAME;

}
