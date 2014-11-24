package com.birin.retailstore.views.fragments;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.birin.retailstore.provider.ProductTableConstants;
import com.birin.retailstore.provider.RetailProvider;
import com.birin.retailstore.views.adapters.ProductListAdapter;

/**
 * Main listing fragment which displays all the data from DB.
 */
public class DataListFragment extends BaseDataFragment implements
		OnItemClickListener {

	private ListView listView;
	private ProductListAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		listView = new ListView(getActivity());
		adapter = new ProductListAdapter(context, null, eventHandler);
		listView.setDivider(null);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		return listView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(context, RetailProvider.CONTENT_URI_PRODUCT,
				null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int correctPosition = adapter
				.getCursorPositionWithoutSections(position);
		Cursor cursor = adapter.getCursor();
		if (cursor.moveToPosition(correctPosition)) {
			int productId = cursor.getInt(cursor
					.getColumnIndex(ProductTableConstants.PRODUCT_ID));
			launchProductDetailFragment(productId);
		}
	}
}
