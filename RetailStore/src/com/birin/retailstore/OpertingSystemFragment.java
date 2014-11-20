package com.birin.retailstore;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class OpertingSystemFragment extends Fragment {
	public static final String ARG_OS = "OS";
	private String string;

	int count = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_layout, null);
		TextView textView = (TextView) view.findViewById(R.id.textView1);
		System.out.println("biraj String " + string);
		textView.setText(string);
		Button btn = (Button) view.findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				count++;
				TextView view = (TextView) (getActivity().getActionBar()
						.getCustomView().findViewById(R.id.title_text));
				view.setText("Count " + count);
			}
		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void setArguments(Bundle args) {
		string = Integer.toString(args.getInt(ARG_OS));
	}
}