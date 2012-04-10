package com.omniscience.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.net.ssl.SSLHandshakeException;
import javax.servlet.http.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.omniscience.shared.Constants;
import com.omniscience.shared.IllegalUserException;
import com.omniscience.shared.jdo.Calendar;
import com.omniscience.shared.jdo.Location;
import com.omniscience.client.AddCalendarService;
import com.sun.org.apache.xerces.internal.parsers.SAXParser;


@SuppressWarnings("serial")
public class AddCalendarServiceImpl extends RemoteServiceServlet implements
AddCalendarService {
	
	@Override
	public String addCalendar(String calendarUrl, String latitude,
			String longitude, String altitude) throws IllegalArgumentException, 
			IllegalUserException {
		System.out.println("Hello there!");
		
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    
	    if (user == null) {
	    	System.out.println("Bad");
	      throw new IllegalUserException("No user signed in");
	    }
	    
	    PersistenceManager pm = PMF.get().getPersistenceManager();				
		
		if (calendarUrl.length() < 26) {
			throw new IllegalArgumentException("Invalid Calendar URL");
		}
	    
		if (calendarUrl.substring(calendarUrl.length() - 5).equalsIgnoreCase("basic")) {
			calendarUrl = calendarUrl.substring(0, calendarUrl.length() - 5) + "full";
		} else if (calendarUrl.substring(calendarUrl.length() - 4).equalsIgnoreCase("full")) {
			// Do nothing, this is okay
		} else {		
			throw new IllegalArgumentException("Invalid Calendar URL: must be a 'basic' XML");			
		}
		
		// Check if a calendar already exists
		String query = String.format("select from " + Calendar.class.getName() + " where owner == '%s' && url == '%s'", 
		  		user.getUserId(), calendarUrl);
				       
		List<Calendar> cals = (List<Calendar>) pm.newQuery(query).execute();
		if (cals.size() != 0) {
			throw new IllegalArgumentException("A calendar with this URL already exists!");
		}
						
		
		try {
			XMLParser parser = new XMLParser();
			Location loc;			
			loc = parser.getEventsAtLocation(calendarUrl);
			// Check for existing cal with this url
			Calendar cal = new Calendar(user.getUserId(), loc.getName(), latitude,   
					longitude, altitude, loc.getDescription(),
					calendarUrl);
			System.out.println(loc.getName() + loc.getDescription());
			pm.makePersistent(cal);
		} catch (ParserConfigurationException e1) {			
			throw new IllegalArgumentException("Invalid Calendar URL");
		} catch (SAXException e1) {				
			throw new IllegalArgumentException("Invalid Calendar URL");
		} catch (MalformedURLException e) {			
			throw new IllegalArgumentException("Invalid Calendar URL");
		} catch (Exception e) {			
			throw new IllegalArgumentException("Invalid Calendar URL");		
		} finally {
			pm.close();
		}		
		return "Success!"; 	        									
	}
	}
