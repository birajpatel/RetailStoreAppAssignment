package com.birin.retailstore.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.Log;

public class Utils {

	public static void log(String tag, String log) {
		if (Constants.DEBUG) {
			Log.i(tag, log);
		}
	}

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static int getSectionTopSpacing(Context context) {
		final float screenPercent;
		if (isTablet(context)) {
			screenPercent = Constants.MARGIN_BETWEEN_CARD_TAB_SCREEN_PERCENT;
		} else {
			screenPercent = Constants.MARGIN_BETWEEN_CARD_PHONE_SCREEN_PERCENT;
		}
		final int DEVICE_WT = getDeviceWidth(context);
		int spacingbetweenCards = (int) (screenPercent * DEVICE_WT);
		return spacingbetweenCards * 3;
	}

	public static int getSectionLeftRightBottomSpacing(Context context) {
		return getSectionTopSpacing(context) / 3;
	}

	public static int getDeviceWidth(Context context) {
		DisplayMetrics displaymetrics = context.getResources()
				.getDisplayMetrics();
		return displaymetrics.widthPixels;
	}

}
