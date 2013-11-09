package com.clv.vueling.adapters;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.clv.vueling.model.RowOpenConversation;


public class DatabaseAdapter {

	private static final String DATABASE_NAME = "vuqio.db";
	private static final int DATABASE_VERSION = 2;

	//Tablas
	private static final String TABLE_MESSAGES = "Messages";
	public static final String TABLE_CHATS = "Chats";

	//Campos tabla mensajes
	public static final String ID_MESSAGE = "id_message";	
	public static final String MESSAGE = "message";
	public static final String SEND = "send";
	public static final String TYPE = "type_msg";
	public static final String SIZE = "size";
	public static final String USER_FROM = "user_from";
	public static final String USERNAME = "username";

	//Campos tabla Chats
	public static final String ID_CREATOR = "id_creator";
	public static final String ID_CHAT = "id_chat"; //CAMPO INCLUIDO EN TABLA MENSAJES
	public static final String PHOTO = "photo";
	public static final String MEMBERS = "members";
	public static final String TITLE = "title";
	private static final String LAST = "last_message";
	
	private static final String MESSAGE_NOT_SEND = "msg_not_send";

	private final Context context;
	private DatabaseHelper helper;
	private SQLiteDatabase db;

	public DatabaseAdapter(Context context){
		this.context=context;
		helper = new DatabaseHelper(context);
		if(db == null)
			open();
	}

	private static class DatabaseHelper extends SQLiteOpenHelper{

		public DatabaseHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase database) {
			// TODO Auto-generated method stub
			createTable(database);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.w("DatabaseAdapter", "Upgrading database from version "
					+ oldVersion + " to version " + newVersion);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHATS);
			createTable(db);

		}
		
		

		private void createTable (SQLiteDatabase db){

			String table_chat = "CREATE TABLE " + TABLE_CHATS + " (" + ID_CHAT + " TEXT NOT NULL PRIMARY KEY, "+ ID_CREATOR +
					" TEXT NOT NULL, "+ PHOTO + " TEXT NOT NULL, "+ MEMBERS + " TEXT NOT NULL, "+ TITLE + " TEXT NOT NULL, "+
					LAST+" TEXT NOT NULL);";
			String table_message = "CREATE TABLE " + TABLE_MESSAGES + " (" + ID_MESSAGE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ ID_CHAT + " TEXT NOT NULL, "+ USER_FROM + " TEXT NOT NULL, "+ MESSAGE + " TEXT NOT NULL, "+ TYPE + " TEXT NOT NULL, "
					+ SIZE + " TEXT NOT NULL, "+ USERNAME + " TEXT NOT NULL, " + SEND + " BOOLEAN);";
			//			String table_message = "CREATE TABLE " + TABLE_MESSAGES + " (" + ID_MESSAGE + " INTEGER PRIMARY KEY, "
			//					+ ID_CHAT + " TEXT NOT NULL  PRIMARY KEY, "+ FROM + " TEXT NOT NULL, "+ MESSAGE + " TEXT NOT NULL, "+ TYPE + " TEXT NOT NULL, "
			//					+ SIZE + " TEXT NOT NULL, " + SEND + " BOOLEAN);";
			try {

				Log.d("bd",table_chat);
				db.execSQL(table_chat);
				Log.d("bd",table_message);
				db.execSQL(table_message);

			}
			catch (SQLException e) {
				Log.e("SQLException","DataBaseAdapter: "+e.getMessage());
				e.printStackTrace();
			}

		}
	}
	public DatabaseAdapter open() throws SQLException{
		if(db == null || !db.isOpen())
			db = helper.getWritableDatabase();
		return this;
	}
	public void close(){
		helper.close();
	}

	/**
	 * Insertar nuevo chat
	 * @param id_chat
	 * @param id_creator
	 * @param photo
	 * @param members
	 * @param title
	 * @return El id de la fila nueva o -1 en caso de error
	 */
	public long insertChat(String id_chat,String id_creator, String photo,String members, String title,String msg){

		ContentValues values = new ContentValues();
		values.put(ID_CHAT, id_chat);
		values.put(ID_CREATOR, id_creator);
		values.put(PHOTO, photo);
		values.put(MEMBERS, members);
		values.put(TITLE, title);
		values.put(LAST, msg);
		return db.insertWithOnConflict(TABLE_CHATS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		//return db.insert(TABLE_CHATS, null, values);

	}
	/**
	 * Insertar mensajes
	 * @param id_message
	 * @param id_chat
	 * @param from
	 * @param message
	 * @param size
	 * @param type
	 * @param status
	 * @return El id de la fila nueva o -1 en caso de error
	 */
	public long insertMessage(String id_chat, String from, String message, String size, String type, Boolean status, String username){
		ContentValues values = new ContentValues();

		
		if(getCountMessages(id_chat)> 50){
			//dame min id_message
			int id = getMinIdMessage(id_chat);
			if(id<0)
				return 0;
			else
				deleteMessage(id);//borra fla

		}

		values.put(ID_CHAT, id_chat);
		values.put(USER_FROM, from);
		values.put(MESSAGE, message);
		values.put(SIZE, size);
		values.put(TYPE, type);
		values.put(SEND, status);
		values.put(USERNAME, username);

		return db.insert(TABLE_MESSAGES, null, values);



	}
	/**
	 * Devuelve el numero de mensajes de una chat
	 * @param id_chat identificador del chat 
	 * @return numero de mensajes de un chat o -1 en caso de error
	 */
	public int getCountMessages(String id_chat){
		int result = -1;

		Cursor mCount= db.rawQuery("SELECT COUNT(*) FROM "+TABLE_MESSAGES+" WHERE "+ID_CHAT+" ='"+id_chat+"' GROUP BY "+ID_CHAT, null);
		//Cursor mCount= db.rawQuery("SELECT COUNT(*) FROM "+TABLE_MESSAGES+" WHERE "+ID_CHAT+" = ? GROUP BY ?", null);

		if(mCount.moveToFirst()){
			int count= mCount.getInt(0);
			result =  count;
		}
		mCount.close();
		return result;

	}

	public int getMinIdMessage(String id_chat){

		Cursor mCount= db.rawQuery("SELECT MIN("+ID_MESSAGE+") FROM "+TABLE_MESSAGES+" WHERE "+ID_CHAT+"='"+id_chat+"'", null);
		if(mCount.moveToFirst()){

			int min= mCount.getInt(0);
			Log.d("bd","MIN ID MESSAGE: "+min);
			return min;
		}
		mCount.close();
		return -1;
	}

	/**
	 * Delete message
	 * @param id_message identificador del mensaje a borrar
	 * @return true si ha sido borrado false en caso contrario
	 */
	public boolean deleteMessage(int id_message){
		return db.delete(TABLE_MESSAGES, ID_MESSAGE + "=" + id_message, null) > 0;
	}

	/**
	 * Comprueba si hay un chat con mensajes previos
	 * @param idChat 
	 * @return True si est? registrado o False en caso contrario
	 */
	public String existChat(String idChat) {
		String result = null;
		Cursor cursor = db.query(TABLE_MESSAGES, new String[] {ID_CHAT},
				ID_CHAT + " = '" + idChat+"'",
				null, null, null,null);
		if (cursor.moveToFirst()){
			result = idChat;
		}	
		if (!cursor.isClosed())
			cursor.close();
		return result;
	}
	public Cursor getMessages(String idChat){

		if(existChat(idChat) != null){
			return db.query(TABLE_MESSAGES,new String[] {
					ID_MESSAGE,
					ID_CHAT,
					USER_FROM,
					USERNAME,
					MESSAGE},
					ID_CHAT + "=?",
					new String[] {idChat},
					null, null, null,null);
		}else{
			return null;
		}
	}
	
	public void updateLastMessage(String idChat,String msg){
		Log.i("TW","updatelast: "+ msg);
			ContentValues values = new ContentValues();
			values.put(LAST, msg);
			db.update(TABLE_CHATS, values, ID_CHAT+"= '"+idChat+"'", null);

	}
	

	public List<RowOpenConversation> getConversations(){
		Log.d("TW","ENTRA EN GETCONVERSATION");
		List<RowOpenConversation> result = new LinkedList<RowOpenConversation>();
		if(db == null)
			open();
		
		Cursor c= db.rawQuery("SELECT * FROM "+TABLE_CHATS, null);

		if(c.moveToFirst()){
			Log.d("TW","ENTRA EN IF");

		do{
				String members =  c.getString(c.getColumnIndex(MEMBERS));
				List<String> users = new ArrayList<String>(); 
				users.add(c.getString(c.getColumnIndex(ID_CREATOR)));
				users.addAll(Arrays.asList(members.replace("[", "").replace("]", "").split(",")));
				
				
				Log.d("TW","users" + users.toString());
				

				RowOpenConversation r = new RowOpenConversation(users,c.getString(c.getColumnIndex(TITLE)),c.getString(c.getColumnIndex(LAST)),c.getString(c.getColumnIndex(PHOTO)));
				result.add(r);
			}while(c.moveToNext());

		}
		c.close();

		return result;
	}

}
