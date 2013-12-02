package com.ezzored.esports.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class TLCalendarReader implements IFeedReader {

	private static final String TL_CALENDAR_URL = "http://www.teamliquid.net/calendar/xml/calendar.xml";

	@Override
	public String read() {

		String resp = null;
		
		try {
		DefaultHttpClient httpclient = new DefaultHttpClient();

		HttpGet httppost = new HttpGet(TL_CALENDAR_URL);
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity ht = response.getEntity();

		BufferedHttpEntity buf = new BufferedHttpEntity(ht);

		InputStream is = buf.getContent();

		BufferedReader r = new BufferedReader(new InputStreamReader(is));
		
		StringBuilder total = new StringBuilder();
		String line;
		while ((line = r.readLine()) != null) {
			total.append(line + "\n");
		}
		resp = total.toString();
		} catch (IOException ex) {
			resp = "File read exception";
		}
		return resp;
	}

}
