package com.clv.vueling;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.clv.vueling.MainActivity.FlightAdapter;
import com.clv.vueling.adapters.DatabaseAdapter;
import com.clv.vueling.fragment.FragmentChat;
import com.clv.vueling.fragment.FragmentPassengers;
import com.clv.vueling.fragment.FragmentTaxis;
import com.clv.vueling.model.Flight;
import com.clv.vueling.model.Preferences;
import com.clv.vueling.rest.FlightResponse;
import com.clv.vueling.rest.PassengerResponse;
import com.clv.vueling.rest.RestClient;
import com.clv.vueling.rest.TaxiCreate;
import com.clv.vueling.util.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class FlightActivity extends FragmentActivity implements ActionBar.TabListener {

	private static final String TAG = Constant.TAG;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private Flight mFlight;
	private String idChat;
	private String idUser;
	private String nameUser;
	private DatabaseAdapter db;
	private final WebSocketConnection mConnection = new WebSocketConnection();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flight);

		mFlight = (Flight) getIntent().getSerializableExtra(Flight.TAG);
		if (mFlight == null) {
			return;
		}
		idChat = mFlight.getFlightNumber();
		idUser = Preferences.getIdUser(getBaseContext());
		nameUser = Preferences.getUserName(getBaseContext());
		db = new DatabaseAdapter(getBaseContext());

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(mFlight.getFlightNumber() + ": " + mFlight.getOrigin() + " - " + mFlight.getDestination());

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		mViewPager.setOffscreenPageLimit(2);

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}



	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		Fragment fragments[] = new Fragment[3];

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			fragments[0] = new FragmentChat();
			fragments[1] = new FragmentPassengers();
			fragments[2] = new FragmentTaxis();
		}

		@Override
		public Fragment getItem(int position) {
			return fragments[position];
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return fragments.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	public void start() {
		db.open();
		final String wsuri = Preferences.WS + "room/chat?username=" + URLEncoder.encode(nameUser) + "&amp;userId="
				+ idUser + "&amp;pid=" + idChat + "&amp;typeChat=2";
		Log.d("INFO2", "WS: " + wsuri);
		try {
			mConnection.connect(wsuri, new WebSocketHandler() {

				@Override
				public void onOpen() {
					Log.d(TAG, "Status: Connected to " + wsuri);
					// TODO añadir mensajes anteriores
					loadMessage();
				}

				@Override
				public void onTextMessage(String payload) {
					Log.d(TAG, "Got echo: " + payload);
					// control
					ObjectMapper mapper = new ObjectMapper();
					JsonNode aux;

					try {
						aux = mapper.readTree(payload);
						String kind = aux.findPath("kind").getTextValue();
						String user = aux.findPath("user").getTextValue();
						String message = aux.findPath("message").getTextValue();
						String username = aux.findPath("username").getTextValue();

						Log.d(TAG, "kind: " + kind);
						Log.d(TAG, "User: " + user);
						Log.d(TAG, "message: " + message);
						Log.d(TAG, "username: " + username);

						// prueba recibir
						if (kind != null && kind.equals("talk") && user != null && !user.equals(idUser)) {
							showMessage(username, message, true);
							db.insertMessage(idChat, user, message, Integer.toString(message.length()), "texto", true,
									username);
							db.updateLastMessage(idChat, message);

						}

					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				@Override
				public void onClose(int code, String reason) {
					Log.d(TAG, "Connection lost.");

				}
			});
		} catch (WebSocketException e) {

			Log.d(TAG, e.toString());
		}

		RequestParams params = new RequestParams();
		params.put("fly_id", mFlight.getId());
		RestClient.get("/api/getIdsUsersFly", params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(Throwable error, String content) {
				Log.e("passengers", "FAIL: " + content);
			}

			@Override
			public void onSuccess(String content) {
				Log.i("passengers", "SUCCESS: " + content);
				Gson gson = new Gson();
				Type listOfTestObject = new TypeToken<List<PassengerResponse>>() {
				}.getType();
				ArrayList<PassengerResponse> list = gson.fromJson(content, listOfTestObject);
				((FragmentPassengers) mSectionsPagerAdapter.getItem(1)).fillData(list);
			}
		});
		

		RequestParams params2 = new RequestParams();
		params2.put("flyId", mFlight.getId());
		RestClient.get("/api/getTaxis", params2, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(Throwable error, String content) {
				Log.e("taxis", "FAIL: " + content);
			}

			@Override
			public void onSuccess(String content) {
				Log.i("taxis", "SUCCESS: " + content);
				Gson gson = new Gson();
				Type listOfTestObject = new TypeToken<List<TaxiCreate>>() {
				}.getType();
				ArrayList<TaxiCreate> list = gson.fromJson(content, listOfTestObject);
				((FragmentTaxis) mSectionsPagerAdapter.getItem(2)).fillData(list);
			}
		});
	}

	public void loadMessage() {
		Cursor c;
		db.open();
		c = db.getMessages(idChat);
		if (c == null) {
			Log.d("bd", "NO HAY NADA BD");
		} else {

			if (c.moveToFirst()) {
				do {
					// Si el mensaje es mio
					// Log.i("DB", "ENTRO");
					if (c.getString(c.getColumnIndex("user_from")).equals(Preferences.getIdUser(getBaseContext())))
						showMessage(c.getString(c.getColumnIndex("username")),
								c.getString(c.getColumnIndex("message")), false);
					else
						showMessage(c.getString(c.getColumnIndex("username")),
								c.getString(c.getColumnIndex("message")), true);
				} while (c.moveToNext());
			}
			c.close();
		}

	}

	@Override
	public void onBackPressed() {
		mConnection.disconnect();
		db.close();
		finish();
	};

	private void showMessage(String username, String message, boolean leftSide) {
		((FragmentChat) mSectionsPagerAdapter.getItem(0)).showMessage(username, message, leftSide);
	}

	public void chatear(View v) {

		Log.d("INFO2", "BOTON CHATEAR");
		EditText edit = (EditText) findViewById(R.id.editText1);
		String texto = edit.getText().toString();
		if (texto != null && texto != "") {
			mConnection.sendTextMessage("{\"text\":\"" + texto + "\"}");
			// TODO introducir mensaje base de datos
			db.insertMessage(idChat, Preferences.getIdUser(getBaseContext()), texto, Integer.toString(texto.length()),
					"texto", true, nameUser);
			db.updateLastMessage(idChat, texto);

		}
		edit.setText("");
		showMessage(nameUser, texto, false);

	}
	
	public Flight getFlight() {
		return mFlight;
	}
	
	public String getUserId() {
		return idUser;
	}

}
