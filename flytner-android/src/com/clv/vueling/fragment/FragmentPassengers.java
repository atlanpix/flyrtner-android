package com.clv.vueling.fragment;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.clv.vueling.R;
import com.clv.vueling.model.Flight;
import com.clv.vueling.rest.PassengerResponse;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FragmentPassengers extends ListFragment {
	
	private ArrayList<PassengerResponse> mPassengers;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_passengers, container, false);
		return rootView;
	}

	public void fillData(ArrayList<PassengerResponse> passengers) {
		mPassengers = passengers;
		PassengersAdapter adapter = new PassengersAdapter();
		setListAdapter(adapter);
		((BaseAdapter)getListAdapter()).notifyDataSetChanged();
	}
	

	class PassengersAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mPassengers.size();
		}

		@Override
		public Object getItem(int position) {
			return mPassengers.get(position);
		}

		@Override
		public long getItemId(int position) {
			return mPassengers.get(position).getId().hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				v = li.inflate(R.layout.passenger_item, null);
			}
			PassengerResponse p = (PassengerResponse) getItem(position);
			((TextView) v.findViewById(R.id.text1)).setText(p.getUsername().toUpperCase(Locale.getDefault()));
			ImageView iv = (ImageView) v.findViewById(R.id.imageView1);
			ImageLoader.getInstance().displayImage(p.getUrlImage(), iv);
//			((TextView) v.findViewById(R.id.text2)).setText(flight. + " - " + flight.getDestination());
			return v;
		}

	}

}