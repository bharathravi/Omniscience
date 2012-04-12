package com.omniscience.server;

import java.util.List;

import com.google.appengine.api.users.User;
import com.omniscience.shared.jdo.Calendar;
import com.omniscience.shared.jdo.Location;

public interface CalendarService {
	public List<Location> getLocationCalendars(double lat, double lng, double alt, 
			double dist, int days, int timezoneOffset);
	public List<Calendar> getCalendarsForUser(User user);
	public List<Calendar> getSpecificCalendarForUser(User user, String calendarID);
}
