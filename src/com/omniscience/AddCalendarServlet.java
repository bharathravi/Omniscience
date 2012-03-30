package com.omniscience;

import java.io.IOException;
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
import com.omniscience.Constants.*;
import com.omniscience.jdo.Calendar;
import com.omniscience.jdo.Location;
import com.sun.org.apache.xerces.internal.parsers.SAXParser;


@SuppressWarnings("serial")
public class AddCalendarServlet extends HttpServlet {
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    
	    if (user == null) {
	        resp.getWriter().write("Sign in first.");
	        return;
	    }
		
		resp.setContentType("text/html");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		String url = req.getParameter("url");
		
		System.out.println(url.substring(url.length() - 6));
		if (url.substring(url.length() - 5).equalsIgnoreCase("basic")) {
			url = url.substring(0, url.length() - 5) + "full";
		} else {
			resp.getWriter().write("Invalid URL");
			return;
		}
		
		XMLParser parser = new XMLParser();
		Location loc;
		try {
			loc = parser.getEventsAtLocation(url);
		} catch (ParserConfigurationException e1) {
			resp.getWriter().write("Invalid URL");
			return;
		} catch (SAXException e1) {			
			resp.getWriter().write("Invalid URL");
			return;
		}		
		
		Calendar cal = new Calendar(user.getUserId(), loc.getName(), req.getParameter("lat"),   
				req.getParameter("long"), req.getParameter("alt"), loc.getDescription(),
				url);
		
		System.out.println(cal.getKey());
		resp.getWriter().write("<html><head><meta http-equiv=\"refresh\" content=\"5; url=" + Constants.MAIN_PAGE + "\"></ head>");
		try {
            pm.makePersistent(cal);
            
            resp.getWriter().write("Done! Redirecting in 5...</html>");        	
		 } catch(Exception e) {
	        	resp.getWriter().write("Not Done! Redirecting in 5 </html>");
	        }finally {        
	            pm.close();
	        }									
	}
	}
