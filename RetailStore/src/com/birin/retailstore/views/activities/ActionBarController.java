package com.birin.retailstore.views.activities;

import android.app.ActionBar;
import android.content.Context;
import android.database.Cursor;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.birin.retailstore.R;
import com.birin.retailstore.provider.ProductTableConstants;
import com.birin.retailstore.utils.Constants;

/**
 * This class is responsible for updating & setting up action bar.
 */
public class ActionBarController {

	private TextView cartSizeCount;
	private View cartActionView;
	private Context context;
	private ActionBar actionBar;
	private OnCartClickListener cartActionClickListner;

	/**
	 * Instantiates a new action bar controller.
	 * 
	 * @param context
	 *            the context
	 * @param cartActionClickListner
	 *            the cart action click listner
	 * @param actionBar
	 *            the action bar
	 */
	public ActionBarController(Context context,
			OnCartClickListener cartActionClickListner, ActionBar actionBar) {
		this.context = context;
		this.cartActionClickListner = cartActionClickListner;
		this.actionBar = actionBar;
	}

	/**
	 * Sets the up action bar.
	 * 
	 * @param ACTION_BAR_HT
	 *            the actual action bar height
	 */
	public void setupActionBar(final int ACTION_BAR_HT) {
		setCartActionView(ACTION_BAR_HT);
		setCartCountIndicator(ACTION_BAR_HT);
		setCartActionButton(ACTION_BAR_HT);
		setActionBarTitle(context.getString(R.string.app_name));
	}

	/**
	 * Sets the action bar title.
	 * 
	 * @param string
	 *            the new action bar title
	 */
	private void setActionBarTitle(String string) {
		actionBar.setTitle(string);
	}

	/**
	 * Sets the cart action button, cart icon etc.
	 * 
	 * @param ACTION_BAR_HT
	 *            the new cart action button
	 */
	private void setCartActionButton(int ACTION_BAR_HT) {
		final int ACTION_BAR_BUTTON_COUNT_SIZE = (int) (ACTION_BAR_HT * Constants.CART_ACTION_BAR_BUTTON_ICON_SIZE);
		final int ACTION_BAR_BUTTON_CART_ICON_SIZE = 2 * ACTION_BAR_BUTTON_COUNT_SIZE;
		ImageButton imageButton = (ImageButton) cartActionView
				.findViewById(R.id.cart_icon);
		imageButton.setBackgroundResource(R.drawable.title_cart);
		imageButton.getLayoutParams().width = imageButton.getLayoutParams().height = ACTION_BAR_BUTTON_CART_ICON_SIZE;
	}

	/**
	 * Sets the cart count balloon indicator to show the cart count.
	 * 
	 * @param ACTION_BAR_HT
	 *            the new cart count indicator
	 */
	private void setCartCountIndicator(int ACTION_BAR_HT) {
		final int ACTION_BAR_BUTTON_COUNT_SIZE = (int) (ACTION_BAR_HT * Constants.CART_ACTION_BAR_BUTTON_ICON_SIZE);
		cartSizeCount = (TextView) cartActionView.findViewById(R.id.cart_size);
		cartSizeCount.getLayoutParams().width = cartSizeCount.getLayoutParams().height = ACTION_BAR_BUTTON_COUNT_SIZE;
	}

	/**
	 * Adds up custom cart icon to the action bars & sets up on click listener.
	 * 
	 * @param ACTION_BAR_HT
	 *            the new cart action view
	 */
	private void setCartActionView(final int ACTION_BAR_HT) {
		final int ACTION_BAR_BUTTON_PADDING = (int) (ACTION_BAR_HT * Constants.CART_ACTION_BAR_BUTTON_PADDING);
		LayoutInflater inflater = LayoutInflater.from(context);
		cartActionView = inflater.inflate(R.layout.custom_actionbar, null);
		cartActionView.setPadding(ACTION_BAR_BUTTON_PADDING,
				ACTION_BAR_BUTTON_PADDING, ACTION_BAR_BUTTON_PADDING,
				ACTION_BAR_BUTTON_PADDING);
		ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ACTION_BAR_HT,
				ACTION_BAR_HT);
		lp.gravity = lp.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.RIGHT;
		actionBar.setCustomView(cartActionView, lp);
		cartActionView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null != cartActionClickListner) {
					cartActionClickListner.onCartClicked();
				}
			}
		});
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_TITLE);
	}

	/**
	 * Update cart count indicator balloon.
	 * 
	 * @param cursor
	 *            the cursor from which count need to be read.
	 */
	public void updateCartCountIndicator(Cursor cursor) {
		if (null != cartSizeCount) {
			if (null != cursor && cursor.moveToFirst()) {
				int count = cursor.getInt(cursor
						.getColumnIndex(ProductTableConstants.PRODUCT_COUNT));
				if (count == 0) {
					cartSizeCount.setVisibility(View.GONE);
				} else {
					cartSizeCount.setVisibility(View.VISIBLE);
					cartSizeCount.setText(Integer.toString(count));
				}
			} else {
				cartSizeCount.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * The listener interface for receiving onCartClick events. The class that
	 * is interested in processing a onCartClick event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addOnCartClickListener<code> method. When
	 * the onCartClick event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see OnCartClickEvent
	 */
	public interface OnCartClickListener {

		/**
		 * On cart clicked.
		 */
		public void onCartClicked();
	}

}
