package com.birin.retailstore.utils;

import android.util.Log;

public class Utils {

	public static void log(String tag, String log) {
		if (Constants.DEBUG) {
			Log.i(tag, log);
		}
	}

}
