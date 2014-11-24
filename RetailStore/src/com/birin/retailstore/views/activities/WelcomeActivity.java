package com.birin.retailstore.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.birin.retailstore.R;
import com.birin.retailstore.tasks.DummyDataCopierTask;
import com.birin.retailstore.utils.Utils;

/**
 * The Class WelcomeActivity is a splash screen which is used to show
 * Thought-Works icon info etc.
 */
public class WelcomeActivity extends Activity {

	/** The time period for splash activity animation */
	private final int LAUNCH_DELAY = 2 * 1000;
	private final String TAG = WelcomeActivity.class.getSimpleName();

	/** The activity launcher thread. */
	private Thread mActivityLauncherThread = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		runAddDataTaskToEnsureMinimumData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		pauseAndLaunch();
	}

	private void runAddDataTaskToEnsureMinimumData() {
		new DummyDataCopierTask(getApplicationContext()).execute();
	}

	/**
	 * Pause the user for few second and launch the next UI screen.
	 */
	private void pauseAndLaunch() {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(LAUNCH_DELAY);
					finishSplashActivity();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		if (null == mActivityLauncherThread) {
			mActivityLauncherThread = new Thread(runnable);
			mActivityLauncherThread.start();
		}
	}

	/**
	 * this will finish splash activity and will launch up the Main activity.
	 */
	private void finishSplashActivity() {
		Utils.log(TAG, "finishSplashActivity");
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		WelcomeActivity.this.finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (null != mActivityLauncherThread
				&& mActivityLauncherThread.isAlive()) {
			// we will no longer launch next activity since user might have
			// pressed back or some other activity is already on top.
			mActivityLauncherThread.interrupt();
			mActivityLauncherThread = null;
		}
	}

}
