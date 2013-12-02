package com.ezzored.esports.activities;

import android.app.Activity;
import android.os.Bundle;

public class PreferencesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new PrefsFragment()).commit();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

}