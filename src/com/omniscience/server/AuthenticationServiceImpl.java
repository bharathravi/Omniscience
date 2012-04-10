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

import com.google.gwt.rpc.server.Pair;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.omniscience.shared.Constants;
import com.omniscience.shared.IllegalUserException;
import com.omniscience.shared.jdo.Calendar;
import com.omniscience.shared.jdo.Location;
import com.omniscience.client.AddCalendarService;
import com.omniscience.client.AuthenticationService;
import com.sun.org.apache.xerces.internal.parsers.SAXParser;


@SuppressWarnings("serial")
public class AuthenticationServiceImpl extends RemoteServiceServlet implements
AuthenticationService {

	@Override
	public String getLoginUrl(String callbackUrl) {
		UserService userService = UserServiceFactory.getUserService();
		return userService.createLoginURL(callbackUrl);
	}

	@Override
	public List<String> getLogoutUrl(String callbackUrl) {
		UserService userService = UserServiceFactory.getUserService();
		List<String> ret = new ArrayList<String>();
		ret.add(userService.getCurrentUser().getNickname()); 
		ret.add(userService.createLogoutURL(callbackUrl));
		
		return ret;
	}

	@Override
	public boolean isUserLoggedIn() {
		UserService userService = UserServiceFactory.getUserService();	    	    
		return userService.isUserLoggedIn();
	}
}
