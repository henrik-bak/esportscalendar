package com.ezzored.esports.model;

import java.io.Serializable;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Calendar implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@ElementList(inline=true) List<Month> month;

	public List<Month> getMonth() {
		return month;
	}

	public void setMonth(List<Month> month) {
		this.month = month;
	}

	@Override
	public String toString() {
		return "Calendar [month=" + month + "]";
	}
}
