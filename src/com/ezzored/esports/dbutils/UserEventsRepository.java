package com.ezzored.esports.dbutils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import android.content.Context;

import com.ezzored.esports.model.Event;
import com.j256.ormlite.dao.Dao;

public class UserEventsRepository {
	
	private static volatile UserEventsRepository instance = null;
	 
    private DatabaseHelper db;
    Dao<Event, Integer> eventsDao;
 
 // private constructor
 	private UserEventsRepository(Context ctx) {
 		try {
            DatabaseManager dbManager = new DatabaseManager();
            db = dbManager.getHelper(ctx);
            eventsDao = db.getEventsDao();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
 	}

 	public static UserEventsRepository getInstance(Context ctx) {
 		if (instance == null) {
 			synchronized (UserEventsRepository.class) {
 				instance = new UserEventsRepository(ctx);
 			}
 		}
 		return instance;
 	}
 
    public int create(Event event)
    {
        try {
            return eventsDao.create(event);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }
    public int update(Event event)
    {
        try {
            return eventsDao.update(event);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }
    public int delete(Event event)
    {
        try {
            return eventsDao.delete(event);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }
 
    public List<Event> getAll()
    {
        try {
            return eventsDao.queryForAll();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Event> getAllForDate(DateTime date) {
    	List<Event> returnList = new ArrayList<Event>();
		
		for (Event event : getAll()) {
			if (DateTimeComparator.getDateOnlyInstance().compare(event.getEventDate(), date) == 0) {
				returnList.add(event);
			}
		}
		return returnList;
    }
}
