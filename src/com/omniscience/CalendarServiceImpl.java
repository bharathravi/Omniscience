package com.omniscience;

import java.net.MalformedURLException;
import java.util.logging.Logger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.appengine.api.users.User;
import com.omniscience.jdo.Calendar;
import com.omniscience.jdo.Event;
import com.omniscience.jdo.Location;



public class CalendarServiceImpl implements CalendarService {
	private static final Logger log = Logger.getLogger(CalendarService.class.getName());
  
	public List<Location> getLocationCalendars(double lat, double lng, 
			double alt, double dist, int days) {		
		
		List<Location> locations = new ArrayList<Location>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Calendar.class);
		List<Calendar> calendars = (List<Calendar>) query.execute("");
		XMLParser parser = new XMLParser();
		
		System.out.println(calendars.size());
		for (Calendar cal : calendars) {	
			System.out.println(cal.getName());
			
			double calLng = Double.parseDouble(cal.getLongitude());
			double calLat = Double.parseDouble(cal.getLatitude());
			double calAlt = Double.parseDouble(cal.getAltitude());
			
			if (Util.distance(lat, lng, 
					calLat, calLng, 'K') > dist) {
				continue;
			}
			
			Location loc;
			try {				
				String url = cal.getUrl() + Constants.URL_PARAMETERS 
						+ '&' + Constants.URL_START_MIN + '=' + getDateFromNow(0)
						+ '&' + Constants.URL_START_END + '=' + getDateFromNow(days);
								
				url = url.replace("+", "-");
				
				System.out.println(url);
				log.info(url);
				loc = parser.getEventsAtLocation(url);
				System.out.println(loc.getName());
				loc.setLng(cal.getLongitude());
				loc.setLat(cal.getLatitude()); 
				loc.setAlt(cal.getAltitude());			
				locations.add(loc);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		return locations;
	}

	private String getDateFromNow(int days) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
		long backDateMS = System.currentTimeMillis() + ((long)days) *24*60*60*1000;  
		Date date = new Date();  
		date.setTime(backDateMS);  
		
		String url = dateFormat.format(date);  	
		url = url.substring(0, url.length() - 2) + ":" + url.substring(url.length() - 2);
		return url;
	}

	@Override
	public List<Calendar> getCalendarsForUser(User user) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Calendar.class);
		
		query.setFilter("owner == ownerParam");
		query.declareParameters("String ownerParam");
		List<Calendar> calendars = (List<Calendar>) query.execute(user.getUserId());
		System.out.println(calendars.size());
		for (Calendar c : calendars) {
			System.out.println(c.getName());
		}
		return calendars;
	}
	
	public List<Calendar> getSpecificCalendarForUser(User user, String calendarID) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = String.format("select from " + Calendar.class.getName() 
				+ " where owner == '%s' && key == '%s'", user.getUserId(), calendarID);		
		
		List<Calendar> calendars = (List<Calendar>) pm.newQuery(query).execute();
		System.out.println(calendars.size());
		for (Calendar c : calendars) {
			System.out.println(c.getName());
		}
		return calendars;
	}
}