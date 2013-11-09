package com.clv.vueling.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.clv.vueling.AddActivity;
import com.clv.vueling.AddTaxiActivity;
import com.clv.vueling.FlightActivity;
import com.clv.vueling.R;
import com.clv.vueling.model.OurUser;
import com.clv.vueling.model.Preferences;
import com.clv.vueling.rest.LoginResponse;
import com.clv.vueling.rest.PassengerResponse;
import com.clv.vueling.rest.RestClient;
import com.clv.vueling.rest.TaxiCreate;
import com.clv.vueling.util.Constant;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FragmentTaxis extends ListFragment {

	private ProgressDialog mProgressDialog;
	private ArrayList<TaxiCreate> mTaxis;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_taxi, container, false);
		Button b = (Button) rootView.findViewById(R.id.buttonAdd);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), AddTaxiActivity.class);
				startActivityForResult(i, AddTaxiActivity.CODE);
			}
		});
		mProgressDialog = new ProgressDialog(getActivity());
		if (mTaxis == null) {
			mTaxis = new ArrayList<TaxiCreate>();
		}
		TaxisAdapter adapter = new TaxisAdapter();
		setListAdapter(adapter);
		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == AddTaxiActivity.CODE) {
				String s = data.getStringExtra("text");
				if (s != null) {
					int seats = data.getIntExtra("seats", 0);
					getTaxiInfo(s.toUpperCase(), seats);
				}
			}
		}
	}

	public void getTaxiInfo(String place, int seats) {
		mProgressDialog.setMessage(getText(R.string.adding).toString());
		mProgressDialog.show();
		try {
			TaxiCreate t = new TaxiCreate();
			t.setAddress(place);
			t.setFly_id(((FlightActivity) getActivity()).getFlight().getId());
			t.setNumberSit(seats);
			t.setUserCreate(((FlightActivity) getActivity()).getUserId());
			Gson gson = new Gson();
			StringEntity entity = new StringEntity(gson.toJson(t));
			RestClient.post(getActivity(), "/api/createTaxi", entity, "application/json",
					new AsyncHttpResponseHandler() {
						@Override
						public void onFailure(Throwable error, String content) {
							Log.e(Constant.TAG, "FAIL: " + content);
						}

						@Override
						public void onSuccess(String content) {
							Log.i(Constant.TAG, "SUCCESS: " + content);
							Gson gson = new Gson();
							TaxiCreate r = gson.fromJson(content, TaxiCreate.class);
							mTaxis.add(r);
							((BaseAdapter) getListAdapter()).notifyDataSetChanged();
						}

						@Override
						public void onFinish() {
							mProgressDialog.hide();
						}
					});
		} catch (UnsupportedEncodingException e) {
			mProgressDialog.hide();
		}

	}

	public void fillData(ArrayList<TaxiCreate> taxis) {
		mTaxis = taxis;
		((TaxisAdapter) getListAdapter()).notifyDataSetChanged();
	}

	class TaxisAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mTaxis.size();
		}

		@Override
		public Object getItem(int position) {
			return mTaxis.get(position);
		}

		@Override
		public long getItemId(int position) {
			return mTaxis.get(position).getAddress().hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				v = li.inflate(R.layout.taxi_item, null);
			}
			TaxiCreate p = (TaxiCreate) getItem(position);
			((TextView) v.findViewById(R.id.text1)).setText(p.getAddress().toUpperCase(Locale.getDefault()));
			((TextView) v.findViewById(R.id.text2)).setText(p.getNumberSit() + " free seats");
			return v;
		}

	}

}