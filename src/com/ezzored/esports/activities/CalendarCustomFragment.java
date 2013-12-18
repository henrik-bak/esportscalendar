package com.ezzored.esports.activities;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

public class CalendarCustomFragment extends CaldroidFragment {
	
	public CalendarCustomFragment() {}
	
	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		// TODO Auto-generated method stub
		return new CalendarCustomAdapter(getActivity(), month, year,
				getCaldroidData(), extraData);
	}

}
