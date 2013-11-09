package com.clv.vueling;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddTaxiActivity extends FragmentActivity {

	public static final int CODE = 462;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_taxi);
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.add_taxi);
        final EditText et = (EditText) findViewById(R.id.editText1);
		final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> spinnerCountShoesArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.taxis));
		spinner.setAdapter(spinnerCountShoesArrayAdapter);
        Button b = (Button) findViewById(R.id.buttonOk);
        b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (et.getText() != null && !et.getText().toString().isEmpty()) {
					Intent data = new Intent();
					data.putExtra("text", et.getText().toString());
					data.putExtra("seats", spinner.getSelectedItemPosition() + 1);
					setResult(Activity.RESULT_OK, data);
					finish();
				}
			}
		});
        
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
    


}
