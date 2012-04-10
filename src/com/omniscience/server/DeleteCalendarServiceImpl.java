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
import com.omniscience.client.DeleteCalendarService;
import com.sun.org.apache.xerces.internal.parsers.SAXParser;


@SuppressWarnings("serial")
public class DeleteCalendarServiceImpl extends RemoteServiceServlet implements
DeleteCalendarService {

	@Override
	public void deleteCalendar(String calendarUrl)
			throws IllegalArgumentException, IllegalUserException {
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
		
	    if (!userService.isUserLoggedIn()) {
	    	throw new IllegalUserException("Invalid user");
	    }
	    
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    
	    String query = String.format("select from " + Calendar.class.getName() + " where owner == '%s' && url == '%s'", 
        		user.getUserId(), calendarUrl);
		       
		List<Calendar> cals = (List<Calendar>) pm.newQuery(query).execute();
		if (cals.size() != 1) {
			throw new IllegalUserException("No such calendar exists for this user");
		}
		
		pm.newQuery(query).deletePersistentAll();	    
	}
}
