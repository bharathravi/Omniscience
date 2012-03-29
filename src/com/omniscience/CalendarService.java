package com.omniscience;

import java.util.List;

import com.google.appengine.api.users.User;
import com.omniscience.jdo.Calendar;
import com.omniscience.jdo.Location;

public interface CalendarService {
	public List<Location> getLocationCalendars(double lat, double lng, double alt, 
			double dist, int days);
	public List<Calendar> getCalendarsForUser(User user);
	public List<Calendar> getSpecificCalendarForUser(User user, String calendarID);
}
