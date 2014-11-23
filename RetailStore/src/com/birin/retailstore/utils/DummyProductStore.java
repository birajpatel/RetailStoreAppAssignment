package com.birin.retailstore.utils;

import com.birin.retailstore.R;

/**
 * Dummy Data store thats used to insert the data in to DB.
 */
public class DummyProductStore {

	public static final int COUNT = 6;

	public static final int PRODUCT_ID_TABLE = 101;
	public static final int PRODUCT_ID_CHAIR = 102;
	public static final int PRODUCT_ID_ALMIRAH = 103;
	public static final int PRODUCT_ID_OVEN = 104;
	public static final int PRODUCT_ID_TV = 105;
	public static final int PRODUCT_ID_VACUUM = 106;

	public static final int CATEGORY_ID_FURNITURE = 1;
	public static final int CATEGORY_ID_ELECTRONICS = 2;

	public static final String DEFAULT_CURRENCY = "$";

	public static final String CATEGORY_NAME_FURNITURE = "Furniture";
	public static final String CATEGORY_NAME_ELECTRONICS = "Electronics";

	public static final int[] PRODUCT_IDS = { PRODUCT_ID_TABLE,
			PRODUCT_ID_CHAIR, PRODUCT_ID_ALMIRAH, PRODUCT_ID_OVEN,
			PRODUCT_ID_TV, PRODUCT_ID_VACUUM, };

	public static final String[] PRODUCT_NAMES = { "Table", "Chair", "Almirah",
			"Microwave Oven", "Television", "Vacuum Cleaner" };

	public static final int[] PRODUCT_PRICES = { 199, 249, 99, 500, 2000, 600 };

	public static final int[] PRODUCT_CATEGORIES = { CATEGORY_ID_FURNITURE,
			CATEGORY_ID_FURNITURE, CATEGORY_ID_FURNITURE,
			CATEGORY_ID_ELECTRONICS, CATEGORY_ID_ELECTRONICS,
			CATEGORY_ID_ELECTRONICS };

	public static final String getCategoryNameByCategoryId(int categoryId) {
		switch (categoryId) {
		case CATEGORY_ID_FURNITURE:
			return CATEGORY_NAME_FURNITURE;
		case CATEGORY_ID_ELECTRONICS:
			return CATEGORY_NAME_ELECTRONICS;
		}
		return null;
	}

	public static int getDetailImageByProductId(int productId) {
		switch (productId) {
		case PRODUCT_ID_TABLE:
			return R.drawable.table_detail;
		case PRODUCT_ID_CHAIR:
			return R.drawable.chair_detail;
		case PRODUCT_ID_ALMIRAH:
			return R.drawable.almirah_detail;
		case PRODUCT_ID_OVEN:
			return R.drawable.oven_detail;
		case PRODUCT_ID_TV:
			return R.drawable.tv_detail;
		case PRODUCT_ID_VACUUM:
			return R.drawable.vacuum_detail;
		}
		return 0;
	}

	public static int getCoverImageByProductId(int productId) {
		switch (productId) {
		case PRODUCT_ID_TABLE:
			return R.drawable.table_cover;
		case PRODUCT_ID_CHAIR:
			return R.drawable.chair_cover;
		case PRODUCT_ID_ALMIRAH:
			return R.drawable.almirah_cover;
		case PRODUCT_ID_OVEN:
			return R.drawable.oven_cover;
		case PRODUCT_ID_TV:
			return R.drawable.tv_cover;
		case PRODUCT_ID_VACUUM:
			return R.drawable.vacuum_cover;
		}
		return 0;
	}

}
