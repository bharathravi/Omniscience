package com.omniscience;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import com.omniscience.jdo.Calendar;
import com.omniscience.jdo.Event;
import com.omniscience.jdo.Location;



public class CalendarService {
  
	public List<Location> getLocationCalendars() {		
		
		List<Location> locations = new ArrayList<Location>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Calendar.class);
		List<Calendar> calendars = (List<Calendar>) query.execute("");
		XMLParser parser = new XMLParser();
		
		System.out.println(calendars.size());
		for (Calendar cal : calendars) {	
			System.out.println(cal.getName());
			Location loc = parser.getEventsAtLocation(cal.getUrl());
			System.out.println(loc.getName());
			loc.setLng(cal.getLongitude());
			loc.setLat(cal.getLatitude()); 
			loc.setAlt(cal.getAltitude());			
			locations.add(loc);
		}
		
		return locations;
	}
}