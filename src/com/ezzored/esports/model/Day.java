package com.ezzored.esports.model;

import java.io.Serializable;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Day implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Attribute(required=false)
	private int num;
	
	@ElementList(inline=true)
	private List<Event> event;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public List<Event> getEvent() {
		return event;
	}

	public void setEvent(List<Event> event) {
		this.event = event;
	}

	@Override
	public String toString() {
		return "Day [num=" + num + ", event=" + event + "]";
	}

}
