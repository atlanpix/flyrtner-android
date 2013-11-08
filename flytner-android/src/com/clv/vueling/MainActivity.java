package com.clv.vueling;

import java.util.ArrayList;
import java.util.Locale;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clv.vueling.model.Flight;

public class MainActivity extends ListActivity implements OnItemClickListener {

	private Context mContext;
	private ArrayList<Flight> mFlights;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		mFlights = new ArrayList<Flight>();
		mFlights.add(new Flight("vuelo prueba", "origen", "destino"));
		mFlights.add(new Flight("vuelo prueba2", "origen2", "destino2"));
		FlightAdapter adapter = new FlightAdapter();
		setListAdapter(adapter);
		getListView().setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
		Flight flight = (Flight) adapterView.getItemAtPosition(position);
		Intent i = new Intent(mContext, FlightActivity.class);
		i.putExtra(Flight.TAG, flight);
		startActivity(i);
	}

	class FlightAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mFlights.size();
		}

		@Override
		public Object getItem(int position) {
			return mFlights.get(position);
		}

		@Override
		public long getItemId(int position) {
			return mFlights.get(position).getFlightNumber().hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater li = (LayoutInflater) mContext
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				v = li.inflate(R.layout.flight_item, null);
			}
			Flight flight = (Flight) getItem(position);
			((TextView) v.findViewById(R.id.text1)).setText(flight
					.getFlightNumber().toUpperCase(Locale.getDefault()));
			((TextView) v.findViewById(R.id.text2)).setText(flight.getOrigin()
					+ " - " + flight.getDestination());
			return v;
		}

	}

}
