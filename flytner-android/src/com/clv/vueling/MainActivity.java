package com.clv.vueling;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.clv.vueling.model.Flight;
import com.clv.vueling.model.OurUser;
import com.clv.vueling.model.Preferences;
import com.clv.vueling.rest.FlightResponse;
import com.clv.vueling.rest.LoginRequest;
import com.clv.vueling.rest.LoginResponse;
import com.clv.vueling.rest.RestClient;
import com.clv.vueling.util.AppData;
import com.clv.vueling.util.Constant;
import com.clv.vueling.util.FacebookUtils;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends ListActivity implements OnItemClickListener {

	private Context mContext;
	private Activity mActivity;
	private ArrayList<Flight> mFlights;
	private ProgressDialog mProgressDialog;
	private AppData mAppData;
	private OurUser mUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		mActivity = this;
		mAppData = new AppData(mContext);
		mUser = mAppData.getUser();
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setCancelable(false);
		mFlights = mAppData.getFlights();
		if (mFlights == null) {
			mFlights = new ArrayList<Flight>();
		}
		FlightAdapter adapter = new FlightAdapter();
		setListAdapter(adapter);
		getListView().setOnItemClickListener(this);

		((Button) findViewById(R.id.buttonAdd)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, AddActivity.class);
				startActivityForResult(i, AddActivity.CODE);

			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == AddActivity.CODE) {
				String s = data.getStringExtra("text");
				if (s != null) {
					getFlightInfo(s);
				}
			}
		}
		if (requestCode == LogoutActivity.CODE) {
			facebookLogin();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
	    final Session session = Session.getActiveSession();
	    if (session != null && session.isOpened()) {
			Flight flight = (Flight) adapterView.getItemAtPosition(position);
			Intent i = new Intent(mContext, FlightActivity.class);
			i.putExtra(Flight.TAG, flight);
			startActivity(i);
		} else {
			launchFacebookActivity();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case R.id.action_profile:
			launchFacebookActivity();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void launchFacebookActivity() {
		Intent i = new Intent(this, LogoutActivity.class);
		startActivityForResult(i, LogoutActivity.CODE);
	}

	private void facebookLogin() {
		// start Facebook Login
//		ArrayList<String> permissions = new ArrayList<String>();
//		permissions.add("email");
	    final Session session = Session.getActiveSession();
	    if (session != null && session.isOpened()) {
			mProgressDialog.setMessage(getText(R.string.logging).toString());
			mProgressDialog.show();

	    	// make request to the /me API
			Request.newMeRequest(session, new Request.GraphUserCallback() {

				// callback after Graph API response
				// with user
				// object
				@Override
				public void onCompleted(GraphUser user, Response response) {
					if (user != null) {
						Log.i(Constant.TAG, "LOG OK");
						Toast.makeText(mContext, getText(R.string.logged) + " " + user.getName(),
								Toast.LENGTH_LONG).show();
						ourLogin(user, session.getAccessToken());
					}
				}
			}).executeAsync();
	    } 

//		FacebookUtils.openActiveSession(mActivity, true, new Session.StatusCallback() {
//
//			// callback when session changes state
//			@Override
//			public void call(final Session session, SessionState state, Exception exception) {
//				if (session.isOpened()) {
//
//					// make request to the /me API
//					Request.newMeRequest(session, new Request.GraphUserCallback() {
//
//						// callback after Graph API response
//						// with user
//						// object
//						@Override
//						public void onCompleted(GraphUser user, Response response) {
//							if (user != null) {
//								Log.i(Constant.TAG, "LOG OK");
//								Toast.makeText(mContext, getText(R.string.logged) + " " + user.getName(),
//										Toast.LENGTH_LONG).show();
//								ourLogin(user, session.getAccessToken());
//							}
//						}
//					}).executeAsync();
//				}
//			}
//		}, permissions);

	}

	private void ourLogin(final GraphUser user, String token) {
		try {
			LoginRequest lr = new LoginRequest();
			lr.setEmail(user.asMap().get("email").toString());
			lr.setIdFacebook(user.getId());
			lr.setName(user.getName());
			lr.setOauthTokenFB(token);
			lr.setRegId("prueba");
			lr.setSystemPhone("prueba");
			lr.setUsernameFB(user.getName());
			lr.setUrlImage("http://graph.facebook.com/" + user.getId() + "/picture?type=small");
			Gson gson = new Gson();
			StringEntity entity = new StringEntity(gson.toJson(lr));
			RestClient.post(mContext, "/api/loginFacebook", entity, "application/json", new AsyncHttpResponseHandler() {
				@Override
				public void onFailure(Throwable error, String content) {
					Log.e(Constant.TAG, "FAIL: " + content);
				}

				@Override
				public void onSuccess(String content) {
					Log.i(Constant.TAG, "SUCCESS: " + content);
					Gson gson = new Gson();
					LoginResponse r = gson.fromJson(content, LoginResponse.class);
					mUser = new OurUser();
					mUser.setUserId(r.getUser_id());
					mAppData.saveUser(mUser);
					Preferences.setIdUser(mContext, mUser.getUserId(), user.getName());
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

	private void getFlightInfo(String flight) {
		mProgressDialog.setMessage(getText(R.string.adding).toString());
		mProgressDialog.show();
		RequestParams params = new RequestParams();
		params.put("flyNumber", flight);
		RestClient.get("/api/getFly", params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(Throwable error, String content) {
				Log.e(Constant.TAG, "FAIL: " + content);
			}

			@Override
			public void onSuccess(String content) {
				Log.i(Constant.TAG, "SUCCESS: " + content);
				Gson gson = new Gson();
				FlightResponse r = gson.fromJson(content, FlightResponse.class);
				mFlights.add(new Flight(r.getDestination(), r.getOrigin(), r.getFlyNumber(), r.getId()));
				((FlightAdapter)getListAdapter()).notifyDataSetChanged();
				mAppData.saveFlights(mFlights);
			}

			@Override
			public void onFinish() {
				mProgressDialog.hide();
			}
		});
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
				LayoutInflater li = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
				v = li.inflate(R.layout.flight_item, null);
			}
			Flight flight = (Flight) getItem(position);
			((TextView) v.findViewById(R.id.text1)).setText(flight.getFlightNumber().toUpperCase(Locale.getDefault()));
			((TextView) v.findViewById(R.id.text2)).setText(flight.getOrigin() + " - " + flight.getDestination());
			return v;
		}

	}

}
