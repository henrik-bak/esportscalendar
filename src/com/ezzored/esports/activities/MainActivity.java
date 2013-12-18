package com.ezzored.esports.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeZone;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ezzored.esports.dbutils.FeedEventsRepository;
import com.ezzored.esports.dbutils.UserEventsRepository;
import com.ezzored.esports.model.Event;
import com.ezzored.esports.reader.IFeedReader;
import com.ezzored.esports.reader.TLCalendarReader;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

@SuppressLint("SimpleDateFormat")
public class MainActivity extends FragmentActivity {

	private static final String TAG = "ESPORTS";

	private CaldroidFragment caldroidFragment;
	Map<String, Integer> gameMap = new HashMap<String, Integer>();
	IFeedReader reader;
	com.ezzored.esports.model.Calendar tl_calendar = null;

	private ArrayList<Event> eventList;

	private UserEventsRepository userEventRepo;
	private FeedEventsRepository feedEventRepo;

	private String timezone;
	private Set<String> selectedGames;

	private LinearLayout eventDetails;

	private View prevSelectedView;

	private void setCustomResourceForDates() {
		Calendar cal = Calendar.getInstance();

		// Min date is last 7 days
		cal.add(Calendar.DATE, -18);
		Date blueDate = cal.getTime();

		// Max date is next 7 days
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 16);
		Date greenDate = cal.getTime();

		if (caldroidFragment != null) {
			caldroidFragment.setBackgroundResourceForDate(R.color.blue,
					blueDate);
			caldroidFragment.setBackgroundResourceForDate(R.color.green,
					greenDate);
			caldroidFragment.setTextColorForDate(R.color.white, blueDate);
			caldroidFragment.setTextColorForDate(R.color.white, greenDate);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gameMap.put("StarCraft 2", R.drawable.sc2_icon);
		gameMap.put("League of Legends", R.drawable.lol_icon);
		gameMap.put("Dota 2", R.drawable.dota_2_icon);
		gameMap.put("Brood War", R.drawable.bw_icon);
		gameMap.put("Other", R.drawable.other_icon);

		loadCalendar();
		loadPref();

		userEventRepo = UserEventsRepository.getInstance(this);
		feedEventRepo = FeedEventsRepository.getInstance(tl_calendar, timezone);
		eventDetails = (LinearLayout) findViewById(R.id.scroll_layout);

		caldroidFragment = new CalendarCustomFragment();

		eventList = new ArrayList<Event>();

		// Setup arguments

		// If Activity is created after rotation
		if (savedInstanceState != null) {
			caldroidFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");
		}
		// If activity is created from fresh
		else {
			Bundle args = new Bundle();
			Calendar cal = Calendar.getInstance();
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

			eventList.addAll(userEventRepo.getAll());
			eventList.addAll(feedEventRepo.getAll());

			// Uncomment this to customize startDayOfWeek
			args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
					CaldroidFragment.MONDAY); // Tuesday
			caldroidFragment.setArguments(args);
		}

		setCustomResourceForDates();

		// Attach to the activity
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.calendar1, caldroidFragment);
		t.commit();

		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
				// TODO: fill scrollview with event data
				Log.d(TAG, feedEventRepo.getEventsForDate(new DateTime(date))
						.toString());

				if (prevSelectedView != null) {
					prevSelectedView.setBackgroundResource(R.drawable.cell_bg);
				}

				view.setBackgroundResource(R.drawable.red_border);

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());

				ScrollView scroll = new ScrollView(getApplicationContext());

				scroll.setBackgroundColor(getResources().getColor(
						android.R.color.transparent));
				scroll.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

				LinearLayout scrollBaseLayout = new LinearLayout(
						getApplicationContext());
				scrollBaseLayout.setOrientation(LinearLayout.VERTICAL);

				if (((LinearLayout) eventDetails).getChildCount() > 0) {
					((LinearLayout) eventDetails).removeAllViews();
				}

				scroll.addView(scrollBaseLayout);
				
				DateTime selectedDate = new DateTime(date);
				
				ArrayList<Event> eventsForDate = new ArrayList<Event>();
				eventsForDate.addAll(feedEventRepo.getEventsForDate(selectedDate));
				eventsForDate.addAll(userEventRepo.getAllForDate(selectedDate));
				
				ArrayList<Event> filteredDateEvents = new ArrayList<Event>();
				
				for (Event event : eventsForDate) {
					if (selectedGames.contains(event.getType())) {
						filteredDateEvents.add(event);
					}
				}
				
				for (Event event : filteredDateEvents) {

						RelativeLayout customRow = (RelativeLayout) inflater
								.inflate(R.layout.event_view, null, false);

						ImageView icon = (ImageView) customRow
								.findViewById(R.id.icon);
						TextView title = (TextView) customRow
								.findViewById(R.id.title);
						TextView desc = (TextView) customRow
								.findViewById(R.id.desc);
						TextView time = (TextView) customRow
								.findViewById(R.id.time);

						try {
							icon.setImageResource(gameMap.get(event.getType()));
						} catch (NullPointerException ex) {
							icon.setImageResource(gameMap.get("Other"));
						}

						title.setText(event.getTitle());
						if (event.getDescription() != null) {
							String noHTMLString = replaceLinks(
									event.getDescription()
											.replaceAll("\\<.*?>", "")
											.replaceAll("\\[.*?]", "")
											.replaceAll(".png", ".png "))
									.trim().replaceAll(" +", " ");

							Integer length = noHTMLString.length();
							if (length > 75) {
								desc.setText(noHTMLString.substring(0, 75)
										+ "...");
							} else {
								desc.setText(noHTMLString);
							}
						}
						String minute = (event.getEventDate().getMinuteOfHour() < 10) ? "0"
								+ event.getEventDate().getMinuteOfHour()
								: "" + event.getEventDate().getMinuteOfHour();
						String hour = (event.getEventDate().getHourOfDay() < 9) ? "0"
								+ event.getEventDate().getHourOfDay()
								: "" + event.getEventDate().getHourOfDay();

						time.setText(hour + ":" + minute);

						scrollBaseLayout.addView(customRow);

					}
				eventDetails.addView(scroll);

				prevSelectedView = view;

			}

			@Override
			public void onChangeMonth(int month, int year) {

			}

			@Override
			public void onLongClickDate(Date date, View view) {

			}

			@Override
			public void onCaldroidViewCreated() {
			}

		};

		caldroidFragment.setSelectedDates(new Date(), new Date());

		// Setup Caldroid
		caldroidFragment.setCaldroidListener(listener);
	}

	/**
	 * Save current states of the Caldroid here
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		if (caldroidFragment != null) {
			caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		loadPref();
	}

	private void loadPref() {
		SharedPreferences mySharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		timezone = mySharedPreferences.getString("prefTimezone", null);

		if (timezone == null || timezone.isEmpty()) {
			timezone = DateTimeZone.getDefault().getID();
		}

		selectedGames = mySharedPreferences.getStringSet("prefGames", null);

		if (selectedGames == null) {
			selectedGames = new HashSet<String>();
			for (String game : getResources().getStringArray(R.array.games)) {
				selectedGames.add(game);
			}
		}
	}

	private void loadCalendar() {
		reader = new TLCalendarReader();
		String xml = null;
		try {
			xml = new getTLFeed().execute().get();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}

		Serializer ser = new Persister();
		try {
			tl_calendar = ser.read(com.ezzored.esports.model.Calendar.class,
					xml);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String replaceLinks(String text) {

		String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		while (m.find()) {
			String urlStr = m.group();
			if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
				urlStr = urlStr.substring(1, urlStr.length() - 1);
			}
			if (urlStr.endsWith("png") || urlStr.endsWith("jpg"))
				text = text.replace(urlStr, "");
		}
		return text;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.calendar, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_search:
			openSearch();
			return true;
		case R.id.action_settings:
			openSettings();
			return true;
		case R.id.action_add:
			addEvent();
			return true;
		case R.id.action_hide:
			hideCalendar();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void hideCalendar() {
		if (caldroidFragment.getDateViewPager().getVisibility() == View.VISIBLE)
			caldroidFragment.getDateViewPager().setVisibility(View.GONE);
		else
			caldroidFragment.getDateViewPager().setVisibility(View.VISIBLE);
	}

	private void addEvent() {
		Intent i = new Intent(MainActivity.this, AddEventActivity.class);
		startActivity(i);
	}

	private void openSettings() {
		Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
		startActivityForResult(intent, 0);
	}

	private void openSearch() {
		Log.d(TAG, "search");

	}

	class getTLFeed extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... voids) {
			return reader.read();
		}

		@Override
		protected void onPostExecute(String result) {
		}

	}

}
