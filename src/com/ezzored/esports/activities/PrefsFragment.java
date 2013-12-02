package com.ezzored.esports.activities;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

public class PrefsFragment extends PreferenceFragment {
	
	CharSequence[] entries = { "GMT", "UTC", "CET", "EET", "MSK", "PST", "MST", "CST", "EST", "HST"	, "AKST", "KST" };
	CharSequence[] entryValues = {"Etc/GMT", "Etc/UCT", "CET", "EET", "Etc/GMT-3", "PST8PDT", "MST7MDT", "CST6CDT", "EST5EDT", "HST", "America/Anchorage", "Asia/Seoul"	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        addPreferencesFromResource(R.xml.settings_activity);
        
        ListPreference lp = (ListPreference)findPreference("prefTimezone");
        lp.setEntries(entries);
        lp.setEntryValues(entryValues);
 
    }
}