package com.clv.vueling.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.clv.vueling.model.OurUser;
import com.google.gson.Gson;

public class AppData {
	
	private static final String KEY_USER = "user";
	
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
}
