package com.clv.vueling;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.ActionBar;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.clv.vueling.adapters.DatabaseAdapter;
import com.clv.vueling.model.Preferences;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class ChatActivity extends Activity {

	private static final String TAG = "INFO2";
	private ViewGroup messagesContainer;
	private ScrollView scrollContainer;
	private ActionBar myActionBar;
	private EditText myEditText;
	static Boolean escribiendo = false;
	private DatabaseAdapter db;
	private String idChat;
	private String idUser;
	private String nameUser;
	private final WebSocketConnection mConnection = new WebSocketConnection();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		idChat = (String) getIntent().getSerializableExtra("idChat");
		idUser = Preferences.getIdUser(getBaseContext());
		nameUser = Preferences.getUserName(getBaseContext());
		messagesContainer = (ViewGroup) findViewById(R.id.messagesContainer);
		scrollContainer = (ScrollView) findViewById(R.id.scrollView1);
		Bitmap image = (Bitmap) getIntent().getParcelableExtra("bimage");

		myEditText = (EditText) findViewById(R.id.editText1);
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
		db = new DatabaseAdapter(getBaseContext());
		start();
	}

	// Logica Chat

	// Ver si chat creado en la base de datos
	// Si existe-->cargar chat con mensajes de la base de datos
	// Si no existe -->JSON room/create
	// -->Insert en la base de datos

	private void start() {

		db.open(); // FIXME
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

						Log.d("INFO2", "KInd: " + kind);
						Log.d("INFO2", "User: " + user);
						Log.d("INFO2", "KInd: " + message);
						Log.d("INFO2", "username: " + username);

						// prueba recibir
						if (kind.equals("talk") && !user.equals(idUser)) {
							showMessage(message, true);
							db.insertMessage(idChat, user, message, Integer.toString(message.length()), "texto", true);
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
	}

	public void chatear(View v) {

		Log.d("INFO2", "BOTON CHATEAR");
		EditText edit = (EditText) findViewById(R.id.editText1);
		String texto = edit.getText().toString();
		if (texto != null && texto != "") {
			mConnection.sendTextMessage("{\"text\":\"" + texto + "\"}");
			// TODO introducir mensaje base de datos
			db.insertMessage(idChat, Preferences.getIdUser(getBaseContext()), texto, Integer.toString(texto.length()),
					"texto", true);
			db.updateLastMessage(idChat, texto);

		}
		edit.setText("");
		showMessage(texto, false);

	}

	@Override
	public void onBackPressed() {
		mConnection.disconnect();
		db.close();
		finish();
	};

	private void showMessage(String message, boolean leftSide) {
		final TextView textView = new TextView(ChatActivity.this);
		textView.setTextColor(Color.BLACK);
		textView.setText(message);

		int bgRes = R.drawable.left_message_bg;

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		if (!leftSide) {
			bgRes = R.drawable.right_message_bg;
			params.gravity = Gravity.RIGHT;
		}

		textView.setLayoutParams(params);

		textView.setBackgroundResource(bgRes);

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				messagesContainer.addView(textView);

				// Scroll to bottom
				if (scrollContainer.getChildAt(0) != null) {
					scrollContainer.scrollTo(scrollContainer.getScrollX(), scrollContainer.getChildAt(0).getHeight());
				}
				scrollContainer.fullScroll(View.FOCUS_DOWN);
			}
		});

		scrollContainer.post(new Runnable() {
			@Override
			public void run() {
				scrollContainer.fullScroll(View.FOCUS_DOWN);
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
					Log.i("DB", "ENTRO");
					if (c.getString(c.getColumnIndex("user_from")).equals(Preferences.getIdUser(getBaseContext())))
						showMessage(c.getString(c.getColumnIndex("message")), false);
					else
						showMessage(c.getString(c.getColumnIndex("message")), true);
				} while (c.moveToNext());
			}
			c.close();
		}

	}
}
