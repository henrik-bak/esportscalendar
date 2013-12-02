package com.ezzored.esports.activities;

import java.util.Arrays;
import java.util.Calendar;

import org.joda.time.MutableDateTime;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ezzored.esports.dbutils.UserEventsRepository;
import com.ezzored.esports.model.Event;

public class AddEventActivity extends FragmentActivity implements
		OnDateSetListener, OnTimeSetListener {

	EditText et_title;
	EditText et_description;
	String slectedEventType;
	Spinner spn_type;
	Button btn_time;
	Button btn_date;
	Button btn_add;
	MutableDateTime dt;
	FragmentManager fm;
	Boolean isShown = false;
	
	private UserEventsRepository repos;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addevent);
		fm = getFragmentManager();
		
		repos = UserEventsRepository.getInstance(this);

		et_title = (EditText) findViewById(R.id.et_title);
		et_description = (EditText) findViewById(R.id.et_desc);
		spn_type = (Spinner) findViewById(R.id.spn_type);
		btn_time = (Button) findViewById(R.id.btn_time);
		btn_add = (Button) findViewById(R.id.btn_addEvent);

		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_dropdown_item,
				Arrays.asList(getResources().getStringArray(R.array.games)));

		spn_type.setAdapter(spinnerArrayAdapter);

		btn_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerFragment newFragment = new DatePickerFragment(
						AddEventActivity.this);
				newFragment.show(fm, "date_picker");

			}
		});
		
		btn_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (dt == null) {
					Toast.makeText(getApplicationContext(), "Please select a time and date!", Toast.LENGTH_LONG).show();
				} else {
					if (et_title.getText().toString().isEmpty()) {
						Toast.makeText(getApplicationContext(), "Please add a title!", Toast.LENGTH_LONG).show();
					} else {
						Event event = new Event();
						event.setTitle(et_title.getText().toString());
						event.setDescription(et_description.getText().toString());
						event.setOver(0);
						event.setType(spn_type.getSelectedItem().toString());
						event.setEventDate(dt.toDateTime());
						
						repos.create(event);
						Log.d("ESPORTS", "event added: "+event.toString());
						
						finish();
						
					}
				}
				
			}
		});

	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		dt = new MutableDateTime();
		dt.setDate(year, monthOfYear, dayOfMonth);
		Log.d("ESPORTS", year + " " + monthOfYear + " " + dayOfMonth);
		
		if (!isShown) {
		TimePickerFragment timeFragment = new TimePickerFragment(
				AddEventActivity.this);
		timeFragment.show(fm, "time_picker");
		isShown = true;
		}
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		dt.setTime(hourOfDay, minute, 0, 0);
		
		Log.d("ESPORTS", dt.toString());
		isShown = false;
	}
	
	@SuppressLint("ValidFragment")
	public static class DatePickerFragment extends DialogFragment {

		private OnDateSetListener listener;

		public DatePickerFragment() {
		}

		public DatePickerFragment(OnDateSetListener listener) {
			this.listener = listener;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of TimePickerDialog and return it
			return new DatePickerDialog(getActivity(), listener, year, month,
					day);
		}

	}
	
	@SuppressLint("ValidFragment")
	public static class TimePickerFragment extends DialogFragment {

		private OnTimeSetListener listener;

		public TimePickerFragment() {
		}

		public TimePickerFragment(OnTimeSetListener listener) {
			this.listener = listener;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), listener, hour, minute, true);
		}

	}
}
