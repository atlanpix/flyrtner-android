package com.clv.vueling.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.clv.vueling.model.Flight;
import com.clv.vueling.model.OurUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AppData {
	
	private static final String KEY_USER = "user";
	private static final String KEY_FLIGHTS = "flights";

	private SharedPreferences sp;
	private Gson gson;
	
	public AppData(Context context) {
	     sp = context.getSharedPreferences("prefs", 0);
	     gson = new Gson();
	}
	
	public void saveUser(OurUser user) {
		Editor e = sp.edit();
		e.putString(KEY_USER, gson.toJson(user));
		e.commit();
	}

	public OurUser getUser() {
		if (sp.contains(KEY_USER)) {
			String s = sp.getString(KEY_USER, "{}");
			return gson.fromJson(s, OurUser.class);
		} else {
			return null;
		}
	}
	
	public void saveFlights(ArrayList<Flight> flights) {
		Editor e = sp.edit();
		e.putString(KEY_FLIGHTS, gson.toJson(flights));
		e.commit();
	}
	
	public ArrayList<Flight> getFlights() {
		if (sp.contains(KEY_FLIGHTS)) {
			String s = sp.getString(KEY_FLIGHTS, "{}");
			Type listOfTestObject = new TypeToken<List<Flight>>(){}.getType();
			return  gson.fromJson(s, listOfTestObject);
		} else {
			return null;
		}
	}
	
}
