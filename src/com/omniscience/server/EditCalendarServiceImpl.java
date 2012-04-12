package com.omniscience.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.net.ssl.SSLHandshakeException;
import javax.servlet.http.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.datanucleus.jdo.exceptions.TransactionActiveException;
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
import com.omniscience.client.EditCalendarService;
import com.sun.org.apache.xerces.internal.parsers.SAXParser;


@SuppressWarnings("serial")
public class EditCalendarServiceImpl extends RemoteServiceServlet implements
EditCalendarService {
	
	@Override
	public boolean editCalendar(String calendarUrl, String latitude,
			String longitude, String altitude) throws IllegalArgumentException, 
			IllegalUserException {				
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    
	    if (!userService.isUserLoggedIn()) {	    	
	      throw new IllegalUserException("No user signed in");
	    }
	    
	    PersistenceManager pm = PMF.get().getPersistenceManager();				
		
	    
		// Verify that the updated url etc are fine.
		if (calendarUrl.length() < 26) {
			throw new IllegalArgumentException("Invalid Calendar URL");
		}
	    
		String fullUrl;
		if (calendarUrl.substring(calendarUrl.length() - 5).equalsIgnoreCase("basic")) {
			fullUrl = calendarUrl.substring(0, calendarUrl.length() - 5) + "full";
		} if (calendarUrl.substring(calendarUrl.length() - 4).equalsIgnoreCase("full")) {
			// Do nothing, this is fine
			fullUrl = calendarUrl;
		} else {		
			throw new IllegalArgumentException("Invalid Calendar URL: must be a 'basic' XML");			
		}
		
		// Retrieve the existing calendar
		String query = String.format("select from " + Calendar.class.getName() + " where owner == '%s' && url == '%s'", 
        		user.getUserId(), fullUrl);
		       
		List<Calendar> cals = (List<Calendar>) pm.newQuery(query).execute();
		if (cals.size() != 1) {
			throw new IllegalUserException("User does not have permissions on this calendar");
		}
						
		Calendar thisCal = cals.get(0);
		
		
		Transaction trans = pm.currentTransaction();
		try {
			XMLParser parser = new XMLParser();
			Location loc;			
			loc = parser.getEventsAtLocation(calendarUrl, 0);
						
			trans.begin();
			thisCal.setName(loc.getName());
			thisCal.setDescription(loc.getDescription());
			thisCal.setLongitude(longitude);
			thisCal.setLatitude(latitude);
			thisCal.setAltitude(altitude);
			thisCal.setUrl(fullUrl);		  
			trans.commit();
			
		} catch (ParserConfigurationException e1) {			
			throw new IllegalArgumentException("Invalid Calendar URL");
		} catch (SAXException e1) {				
			throw new IllegalArgumentException("Invalid Calendar URL");
		} catch (MalformedURLException e) {			
			throw new IllegalArgumentException("Invalid Calendar URL");
		} catch (Exception e) {			
			throw new IllegalArgumentException("Invalid Calendar URL");		
		} finally {
			if (trans.isActive()) {
			    trans.rollback();
			    throw new IllegalArgumentException("Could not save calendar");
			}		
			pm.close();
		}		
	
	  return true;
	}
}