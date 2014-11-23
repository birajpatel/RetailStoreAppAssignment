package com.birin.retailstore.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.birin.retailstore.R;
import com.birin.retailstore.provider.ProductTableConstants;
import com.birin.retailstore.provider.RetailProvider;
import com.birin.retailstore.utils.Constants;
import com.birin.retailstore.utils.DummyProductStore;
import com.birin.retailstore.utils.Utils;

/**
 * Show product's detail page which is also loaded from same DB.
 */
public class ProductDetailFragment extends BaseDataFragment implements
		OnClickListener {

	private static final String KEY_PRODUCT_ID = "KEY_PRODUCT_ID";

	private int deviceWidth;
	private int deviceHeight;
	private int spacing;
	private int productId = -1;

	private Context context;

	private Button addCart;
	private TextView itemName;
	private TextView itemPrice;
	private ImageView detailImage;
	private ImageView blurredBackgroundImage;
	private ScrollView detailContainer;

	public static ProductDetailFragment getInstance(int id) {
		ProductDetailFragment fragment = new ProductDetailFragment();
		Bundle args = new Bundle();
		args.putInt(KEY_PRODUCT_ID, id);
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * Reads product-id from the bundle.
	 * 
	 * @return the product id
	 */
	private int getProductId() {
		Bundle args = getArguments();
		if (null != args && args.containsKey(KEY_PRODUCT_ID)) {
			return args.getInt(KEY_PRODUCT_ID);
		}
		return -1;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity.getApplicationContext();
		deviceWidth = Utils.getDeviceWidth(context);
		deviceHeight = Utils.getDeviceHeight(context);
		spacing = Utils.getSpacing(context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		productId = getProductId();
		View productDetailView = inflater.inflate(R.layout.product_detail_page,
				null);
		initViews(productDetailView);
		setProductBackgroundImage();
		setProductDetailImage();
		return productDetailView;
	}

	/**
	 * Sets up the views & event listeners
	 * 
	 * @param productDetailView
	 *            the product detail view
	 */
	private void initViews(View productDetailView) {
		blurredBackgroundImage = (ImageView) productDetailView
				.findViewById(R.id.blur_product_background);
		detailImage = (ImageView) productDetailView
				.findViewById(R.id.product_detail_pic);
		detailContainer = (ScrollView) productDetailView
				.findViewById(R.id.detail_container);
		detailContainer.getLayoutParams().width = deviceWidth;
		detailContainer.getLayoutParams().height = deviceHeight;
		int containerSpacing = 2 * spacing;
		detailContainer.setPadding(containerSpacing, containerSpacing,
				containerSpacing, containerSpacing);
		itemName = (TextView) productDetailView.findViewById(R.id.product_name);
		itemPrice = (TextView) productDetailView
				.findViewById(R.id.product_price);
		addCart = (Button) productDetailView.findViewById(R.id.add_cart);
		addCart.setOnClickListener(this);
	}

	/**
	 * Sets the product background image.
	 */
	private void setProductBackgroundImage() {
		new ProductBlurBackgroundImageLoader().execute();
	}

	/**
	 * Sets the product detail image half of device width
	 */
	private void setProductDetailImage() {
		int imageSize = (int) (deviceWidth / 2.0);
		detailImage.getLayoutParams().width = imageSize;
		detailImage.getLayoutParams().height = imageSize;
		detailImage.setBackgroundResource(DummyProductStore
				.getDetailImageByProductId(productId));
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String where = ProductTableConstants.PRODUCT_ID + " = ?";
		String[] whereArgs = { Integer.toString(getProductId()) };
		return new CursorLoader(context, RetailProvider.CONTENT_URI_PRODUCT,
				null, where, whereArgs, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		if (null != data && data.moveToFirst()) {
			updateViews(data);
		}

	}

	/**
	 * Update view values retrieved from DB.
	 * 
	 * @param data
	 *            the data to be used to render views.
	 */
	private void updateViews(Cursor data) {
		itemName.setText(data.getString(data
				.getColumnIndex(ProductTableConstants.PRODUCT_NAME)));
		String price = context.getString(R.string.price)
				+ DummyProductStore.DEFAULT_CURRENCY
				+ data.getInt(data
						.getColumnIndex(ProductTableConstants.PRODUCT_PRICE));
		itemPrice.setText(price);

		boolean isAddedToCart = data.getInt(data
				.getColumnIndex(ProductTableConstants.PRODUCT_CART_SIZE)) > 0;
		addCart.setTag(data.getInt(data
				.getColumnIndex(ProductTableConstants.PRODUCT_ID)));
		if (isAddedToCart == true) {
			addCart.setText(R.string.remove_from_cart);
			addCart.setTag(Constants.TAG_CART_CLICK_ACTION,
					Constants.TAG_VALUE_REMOVE);
		} else {
			addCart.setText(R.string.add_to_cart);
			addCart.setTag(Constants.TAG_CART_CLICK_ACTION,
					Constants.TAG_VALUE_ADD);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

	/**
	 * This task loads product's cover pic as Item detail page background &
	 * forms beautiful blurring bitmap.
	 * 
	 */
	private class ProductBlurBackgroundImageLoader extends
			AsyncTask<Void, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(Void... params) {
			final Bitmap productCover = BitmapFactory.decodeResource(
					context.getResources(),
					DummyProductStore.getCoverImageByProductId(productId));
			final Bitmap blurredProductCover = Utils.getBlurredBitmap(
					productCover, 5);
			return blurredProductCover;
		}

		@SuppressWarnings("deprecation")
		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (isAdded()) {
				blurredBackgroundImage.getLayoutParams().width = deviceWidth;
				blurredBackgroundImage.getLayoutParams().height = deviceHeight;
				blurredBackgroundImage.setImageBitmap(result);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					blurredBackgroundImage.setImageAlpha(100);
				} else {
					blurredBackgroundImage.setAlpha(100);

				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_cart:
			dispatchCartOperationEvent(v);
			break;
		default:
			break;
		}
	}

	/**
	 * Dispatch cart operation event
	 * 
	 * @param view
	 *            the view that was clicked for add/remove from cart.
	 */
	private void dispatchCartOperationEvent(View view) {
		Message msg = eventHandler.obtainMessage();
		msg.what = Constants.TAG_CART_CLICK_ACTION;
		msg.arg1 = (Integer) view.getTag();
		msg.arg2 = (Integer) view.getTag(Constants.TAG_CART_CLICK_ACTION);
		eventHandler.sendMessage(msg);
	}

}
