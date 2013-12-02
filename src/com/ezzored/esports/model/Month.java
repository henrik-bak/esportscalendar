package com.ezzored.esports.model;

import java.io.Serializable;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Month  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@ElementList(inline=true)
	private List<Day> day;

	@Attribute(required=false)
	private int year;

	@Attribute(required=false)
	private int num;

	public List<Day> getDay() {
		return day;
	}

	public void setDay(List<Day> day) {
		this.day = day;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	@Override
	public String toString() {
		return "Month [day=" + day + ", year=" + year + ", num=" + num + "]";
	}

}
