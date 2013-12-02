package com.ezzored.esports.model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@Root
@DatabaseTable(tableName = "events")
public class Event implements Serializable {
	
	@DatabaseField(generatedId = true)
    public int id;
	
	private static final long serialVersionUID = 1L;
	@Attribute(required=false)
	private int hour;
	@Attribute(required=false)
	private int minute;
	@Attribute(required=false)
	private int over;

	@DatabaseField
	@Element(required=false) String type;
	@DatabaseField
	@Element(required=false) String title;
	@Element(name="short-title", required=false) String shorttitle;
	@DatabaseField
	@Element(required=false) String description;
	@Element(name="event-id", required=false) int eventid;
	
	@DatabaseField
	DateTime eventDate;
	
	public Event() {}
	
	public DateTime getEventDate() {
		return eventDate;
	}
	public void setEventDate(DateTime eventDate) {
		this.eventDate = eventDate;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public int getOver() {
		return over;
	}
	public void setOver(int over) {
		this.over = over;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getShorttitle() {
		return shorttitle;
	}
	public void setShorttitle(String shorttitle) {
		this.shorttitle = shorttitle;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getEventid() {
		return eventid;
	}
	public void setEventid(int eventid) {
		this.eventid = eventid;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", hour=" + hour + ", minute=" + minute
				+ ", over=" + over + ", type=" + type + ", title=" + title
				+ ", shorttitle=" + shorttitle + ", description=" + description
				+ ", eventid=" + eventid + ", eventDate=" + eventDate.toString() + "]";
	}
}
