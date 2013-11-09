package com.clv.vueling.model;



import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

public class Preferences extends Preference {

	public Preferences(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	//FLAGS JSON
	public static final int TWITTER = 0;
	public static final int FACEBOOK = 1;
	public static final int CURRENT = 2;
	public static final int CHECKIN = 3;
	public static final int CREATE_CHAT = 4;
	//195.235.111.225:9000/
	//81.45.19.228:8000/

//	public static String URL = "http://vuqio.com:7070/";
//	public static String WS = "ws://vuqio.com:7070/";
	
	public static String URL = "http://54.194.16.103:9000/";
	public static String WS = "ws://54.194.16.103:9000/";
	
	public static String PREF_KEY_ID_USER = "userId";
	public static String PREF_KEY_NAME_USER = "nameUser";

	public static String PREF_KEY_IMAGE_USER = "imageUser";

	public static String PREF_KEY_BIOGRAPHY_USER = "bioUser";

	public static String PREF_KEY_ID_PROGRAM = "programId";



	public static final String PREF_KEY_OAUTH_TOKEN = "oauthTokenTW";
	//Applicacion publica por el usuario en TWITTER
	public static final String PREF_KEY_OAUTH_SECRET = "oauthSecreTW";
	public static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
	public static final String PREF_KEY_OAUTH_USER_ID = "idTwitter";
	public static final String PREF_KEY_OAUTH_USER_NAME_TW = "usernameTW";
	public static final String PREF_KEY_OAUTH_URL_IMAGE = "urlImage";
	public static final String PREF_KEY_OAUTH_NAME = "name";

	public static final String TWITTER_CALLBACK_URL = "oauth://tvuqio";

	public static final String URL_TWITTER_AUTH = "auth_url";
	public static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	public static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

	public static final String PREF_SERV_CONNECT_TWITTER = "isServerConnectT";
	//FACEBOOK
	public static final String TOKEN = "access_token";
	public static final String EXPIRES = "expires_in";
    public static final String KEY = "facebook-credentials";
    
	public static final String PREF_KEY_FACEBOOK_LOGIN = "isFaceBookLogedIn";
	public static final String PREF_KEY_EMAIL = "email";
	public static final String PREF_KEY_USERNAME_FB = "usernameFB";
	public static final String PREF_KEY_NAME_FB = "name";
	public static final String PREF_KEY_URL_IMAGE = "urlImage";
	public static final String PREF_KEY_OAUTH_TOKEN_FB = "oauthTokenFB";
	public static final String PREF_SERV_CONNECT_FACEBOOK = "isServerConnectFB";
	public static final String PREF_KEY_ID_FB = "idFacebook";
	public static final String APP_ID = "252843918162251";

	public static final String REG_ID = "regId";
	public static final String SYSTEM_PHONE = "systemPhone";

	//Para el perfil guardar en preferencias la url de la imagen del ultimo Programa visitado
	public static final String LAST_PROG = "lastProgram";

	public static String getStringNameUser (Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_KEY_NAME_USER, "");
	}
	public static void setStringNameUser (Context context,String name) {
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PREF_KEY_NAME_USER, name);
		editor.commit();
	} 

	public static String getImageUser (Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_KEY_IMAGE_USER, "");
	}
	public static String getBioUser (Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_KEY_BIOGRAPHY_USER, "");
	}

	public static void setBioUser (Context context,String bio) {
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PREF_KEY_BIOGRAPHY_USER, bio);
		editor.commit();
	}

	public static void setImageUser(Context context, String url){ 
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PREF_KEY_IMAGE_USER, url);
		editor.commit();
	}
	public static String getLastProgram (Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(LAST_PROG, "");
	}

	public static void setLastProgram(Context context, String url){ 
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(LAST_PROG, url);
		editor.commit();
	}
	public static String getIdUser(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_KEY_ID_USER, "");
	}
	
	public static String getUserName(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_KEY_NAME_USER, "");
	}
	public static void setIdUser (Context context,String id, String name) {
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PREF_KEY_ID_USER, id);
		editor.putString(PREF_KEY_NAME_USER, name);
		editor.commit();
	} 
	public static boolean isTwitterLoggedInAlready(Context context) {
		// return twitter login status from Shared Preferences
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
	}
	public static boolean isServerConnectTwitter(Context context) {
		// return twitter login status from Shared Preferences
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(PREF_SERV_CONNECT_TWITTER, false);
	}

	public static void setTwitterLogged (Context context,boolean logged) {
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(PREF_KEY_TWITTER_LOGIN, logged);
		editor.commit();
	}
	public static void setServerConnectTwitter (Context context,boolean connected) {
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(PREF_SERV_CONNECT_TWITTER, connected);
		editor.commit();
	}
	public static String getStringKeyOauthToken (Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_KEY_OAUTH_TOKEN, "");
	}
	public static String getStringKeyOauthSecret (Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_KEY_OAUTH_SECRET, "");
	}
	public static String getStringKeyOauthUserId (Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_KEY_OAUTH_USER_ID, "");
	}
	public static String getStringKeyOauthUserNameTW(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_KEY_OAUTH_USER_NAME_TW, "");
	}
	public static String getStringKeyOauthUrlImage(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_KEY_OAUTH_URL_IMAGE, "");
	}
	public static String getStringKeyOauthName(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_KEY_OAUTH_NAME, "");
	}
	/*FACEBOOK*/
	public static boolean isFacebookLoggedInAlready(Context context) {
		// return twitter login status from Shared Preferences
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(PREF_KEY_FACEBOOK_LOGIN, false);
	}
	public static void setFaceBookLogged (Context context,boolean logged) {
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(PREF_KEY_FACEBOOK_LOGIN, logged);
		editor.commit();
	}
	public static boolean isServerConnectFacebook(Context context) {
		// return twitter login status from Shared Preferences
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(PREF_SERV_CONNECT_FACEBOOK, false);
	}
	public static void setServerConnectFacebook (Context context,boolean connected) {
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(PREF_SERV_CONNECT_FACEBOOK, connected);
		editor.commit();
	}
	public static String getStringKeyEmail(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_KEY_EMAIL, "");
	}
	public static String getStringKeyUsernameFB(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_KEY_USERNAME_FB, "");
	}
	public static String getStringKeyNameFB(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_KEY_NAME_FB, "");
	}
	public static String getStringKeyUrlImage(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_KEY_URL_IMAGE, "");
	}
	public static String getStringKeyOauthTokenFb(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_KEY_OAUTH_TOKEN_FB, "");
	}

	public static String getStringKeyGetIdFB(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_KEY_ID_FB, "");
	}

	public static String getStringKeyRegId(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(REG_ID, "");
	}


	public static String getStringKeySystemPhone(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(SYSTEM_PHONE, "");
	}


}
