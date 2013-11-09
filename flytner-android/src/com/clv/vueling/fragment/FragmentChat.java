package com.clv.vueling.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.clv.vueling.FlightActivity;
import com.clv.vueling.R;

public class FragmentChat extends Fragment {

	private ViewGroup messagesContainer;
	private ScrollView scrollContainer;
	private EditText myEditText;
	private FlightActivity fa;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_chat, container, false);
		messagesContainer = (ViewGroup) rootView.findViewById(R.id.messagesContainer);
		scrollContainer = (ScrollView) rootView.findViewById(R.id.scrollView1);
		myEditText = (EditText) rootView.findViewById(R.id.editText1);
		TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				// mConnection.sendTextMessage("{\"text\":\"HOLAAA\"}");

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// mConnection.sendTextMessage("{\"text\":\"HOLAAA2\"}");
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (filterLongEnough()) {
					// Escribiendo...
					// TODO descomentar
					// mConnection.sendTextMessage("{\"type\":1}");
				}
			}

			private boolean filterLongEnough() {
				return myEditText.getText().toString().trim().length() > 2;
			}
		};
		myEditText.addTextChangedListener(fieldValidatorTextWatcher);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		fa = (FlightActivity) activity;
		fa.start();
	}

	public void showMessage(String username, String message, boolean leftSide) {

		if (isAdded()) {

			final TextView textView = new TextView(fa);
			textView.setTextColor(Color.BLACK);
			textView.setText(username + ": " + message);


			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			if (!leftSide) {
				params.gravity = Gravity.RIGHT;
			}
			params.leftMargin = 10;
			params.leftMargin = 10;
			params.topMargin = 10;

			textView.setLayoutParams(params);

			textView.setBackgroundResource(R.drawable.bg_card);

			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					messagesContainer.addView(textView);
				}
			});
			
			scrollContainer.post(new Runnable() {            
			    @Override
			    public void run() {
			    	scrollContainer.fullScroll(View.FOCUS_DOWN);              
			    }
			});
		}
	}

}