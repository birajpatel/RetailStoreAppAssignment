package com.birin.retailstore;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.birin.retailstore.provider.CursorRetainingFragment;
import com.birin.retailstore.utils.Constants;
import com.birin.retailstore.views.DataListFragment;

public class MainActivity extends Activity {

	protected CursorRetainingFragment dataRetainingFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loadRetainedFragment();
		FragmentManager fm = getFragmentManager();
		fm.beginTransaction().add(R.id.container, new DataListFragment())
				.commit();
	}

	private void loadRetainedFragment() {
		FragmentManager fm = getFragmentManager();
		dataRetainingFragment = (CursorRetainingFragment) fm
				.findFragmentByTag(Constants.TAG_RETAINED_FRAGMENT);

		// If the Fragment is non-null, then it is currently being
		// retained across a configuration
		// changgenerateSomeDummyDataAndAddToList();e.
		if (dataRetainingFragment == null) {
			dataRetainingFragment = new CursorRetainingFragment();
			if (dataRetainingFragment != null) {
				fm.beginTransaction()
						.add(dataRetainingFragment,
								Constants.TAG_RETAINED_FRAGMENT).commit();
			}
		}
	}

	public void dummy() {

		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
		TextView mTitleTextView = (TextView) mCustomView
				.findViewById(R.id.title_text);
		mTitleTextView.setText("My Own Title");

		ImageButton imageButton = (ImageButton) mCustomView
				.findViewById(R.id.imageButton);
		imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Toast.makeText(getApplicationContext(), "Refresh Clicked!",
						Toast.LENGTH_LONG).show();
			}
		});

		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

		Fragment fragment = new OpertingSystemFragment();
		Bundle args = new Bundle();
		args.putInt(OpertingSystemFragment.ARG_OS, 188);
		fragment.setArguments(args);

		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container, fragment)
				.commit();
	}
}
