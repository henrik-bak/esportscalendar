package com.ezzored.esports.dbutils;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeZone;

import com.ezzored.esports.model.Calendar;
import com.ezzored.esports.model.Day;
import com.ezzored.esports.model.Event;
import com.ezzored.esports.model.Month;

public class FeedEventsRepository {
	
	private static volatile FeedEventsRepository instance = null;
	private static List<Event> eventList = new ArrayList<Event>();
	
	// private constructor
	private FeedEventsRepository(Calendar cal, String timezone) {
		for (Month month : cal.getMonth()) {
			for (Day day : month.getDay()) {
				for (Event event : day.getEvent()) {
						Integer currentYear = month.getYear();
						Integer currentMonth = month.getNum();
						Integer currentDay = day.getNum();

						DateTime originalTime = new DateTime(currentYear,
								currentMonth, currentDay, event.getHour(),
								event.getMinute(),
								DateTimeZone.forID("Asia/Seoul"));
						DateTime convertedTime = new DateTime(originalTime,
								DateTimeZone.forID(timezone));

						event.setEventDate(convertedTime);
						eventList.add(event);
				}
			}
		}
	}

	public static FeedEventsRepository getInstance(Calendar cal, String timezone) {
		if (instance == null) {
			synchronized (FeedEventsRepository.class) {
				instance = new FeedEventsRepository(cal, timezone);
			}
		}
		return instance;
	}
	
	public static FeedEventsRepository getInstance() {
		if (instance == null) {
			synchronized (FeedEventsRepository.class) {
				instance = new FeedEventsRepository();
			}
		}
		return instance;
	}
	
	private FeedEventsRepository() {}
	
	public List<Event> getEventsForDate(DateTime date) {
		List<Event> returnList = new ArrayList<Event>();
		
		for (Event event : eventList) {
			if (DateTimeComparator.getDateOnlyInstance().compare(event.getEventDate(), date) == 0) {
				returnList.add(event);
			}
		}
		return returnList;
	}
	
	public List<Event> getAll() {
		return eventList;
	}

}
