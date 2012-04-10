package com.omniscience.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
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
import com.omniscience.shared.jdo.SerializableCalendar;
import com.omniscience.client.AddCalendarService;
import com.omniscience.client.GetCalendarsService;
import com.sun.org.apache.xerces.internal.parsers.SAXParser;


@SuppressWarnings("serial")
public class GetCalendarsServiceImpl extends RemoteServiceServlet implements
GetCalendarsService {
	
		@Override
	public List<SerializableCalendar> getCalendarsForCurrentUser() throws IllegalUserException {
			UserService userService = UserServiceFactory.getUserService();
		    User user = userService.getCurrentUser();
		    
		    if (user == null) {
		      throw new IllegalUserException("No user signed in");
		    }
		    
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Query query = pm.newQuery(Calendar.class);
			
			query.setFilter("owner == ownerParam");
			query.declareParameters("String ownerParam");
			List<Calendar> calendars = (List<Calendar>) query.execute(user.getUserId());
			
			
			List<SerializableCalendar> sCalendars = new ArrayList<SerializableCalendar>();
			for (Calendar c : calendars) {
				sCalendars.add(new SerializableCalendar(c));
			}
			
			return sCalendars;
	}
	}
