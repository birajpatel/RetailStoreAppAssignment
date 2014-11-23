package com.birin.retailstore.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import com.birin.retailstore.tasks.AddToCartTask;
import com.birin.retailstore.tasks.RemoveFromCartTask;

/**
 * Quick Access utility functions.
 */
public class Utils {

	private static Handler sMainThreadHandler;

	public static void log(String tag, String log) {
		if (Constants.DEBUG) {
			Log.i(tag, log);
		}
	}

	/**
	 * A thread safe way to show a Toast. Can be called from any thread.
	 */
	public static void showToast(final Context context, final String message,
			final int duration) {
		getMainThreadHandler().post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, message, duration).show();
			}
		});
	}

	/**
	 * A thread safe way to show a Toast. Can be called from any thread.
	 */
	public static void showToast(Context context, final String message) {
		showToast(context, message, Toast.LENGTH_LONG);
	}

	public static void showToast(Context context, int stringId) {
		showToast(context, context.getResources().getString(stringId));
	}

	public static Handler getMainThreadHandler() {
		if (sMainThreadHandler == null) {
			// No need to synchronize -- it's okay to create an extra Handler,
			// which will be used only once and then thrown away.
			sMainThreadHandler = new Handler(Looper.getMainLooper());
		}
		return sMainThreadHandler;
	}

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static int getSpacingBetweenSections(Context context) {
		return getSpacing(context) * 3;
	}

	public static int getListingCartIconSize(Context context) {
		final float screenPercent;
		if (isTablet(context)) {
			screenPercent = Constants.ADD_CART_WT_HT_TAB_SCREEN_PERCENT;
		} else {
			screenPercent = Constants.ADD_CART_WT_HT_PHONE_SCREEN_PERCENT;
		}
		final int DEVICE_WT = getDeviceWidth(context);
		return (int) (screenPercent * DEVICE_WT);
	}

	public static int getSpacing(Context context) {
		final float screenPercent;
		if (isTablet(context)) {
			screenPercent = Constants.MARGIN_TAB_SCREEN_PERCENT;
		} else {
			screenPercent = Constants.MARGIN_PHONE_SCREEN_PERCENT;
		}
		final int DEVICE_WT = getDeviceWidth(context);
		int spacingbetweenCards = (int) (screenPercent * DEVICE_WT);
		return spacingbetweenCards;
	}

	public static int getDeviceWidth(Context context) {
		DisplayMetrics displaymetrics = context.getResources()
				.getDisplayMetrics();
		return displaymetrics.widthPixels;
	}

	public static int getDeviceHeight(Context context) {
		DisplayMetrics displaymetrics = context.getResources()
				.getDisplayMetrics();
		return displaymetrics.heightPixels;
	}

	public static void toggleCartState(int productId, int action,
			Context context) {
		switch (action) {
		case Constants.TAG_VALUE_ADD:
			new AddToCartTask(context).execute(productId);
			break;
		case Constants.TAG_VALUE_REMOVE:
			new RemoveFromCartTask(context).execute(productId);
			break;
		}
	}

	public static int calculateCoverImageHeight(int width) {
		return (int) (width * (Constants.PRODUCT_COVER_IMAGE_HT_RATIO) / Constants.PRODUCT_COVER_IMAGE_WT_RATIO);
	}

	public static int getActionBarHeight(Activity activity) {
		TypedValue tv = new TypedValue();
		if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize,
				tv, true)) {
			return TypedValue.complexToDimensionPixelSize(tv.data, activity
					.getResources().getDisplayMetrics());
		}
		return 64; // Default
	}

	public static Bitmap getBlurredBitmap(Bitmap inputBitmap, int radius) {
		Bitmap bitmap = inputBitmap.copy(inputBitmap.getConfig(), true);
		if (radius < 1) {
			return (null);
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		Log.e("pix", w + " " + h + " " + pix.length);
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
						| (dv[gsum] << 8) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += w;
			}
		}

		Log.e("pix", w + " " + h + " " + pix.length);
		bitmap.setPixels(pix, 0, w, 0, 0, w, h);
		return (bitmap);
	}

}
