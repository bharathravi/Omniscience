package com.omniscience.server;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.servlet.http.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.UserService;
import com.omniscience.shared.Constants;
import com.omniscience.shared.jdo.Calendar;
import com.omniscience.shared.jdo.Location;
import com.sun.org.apache.xerces.internal.parsers.SAXParser;


@SuppressWarnings("serial")
public class EditCalendarServlet extends HttpServlet {
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    
	    if (user == null) {
	        resp.getWriter().write("Sign in first.");
	        return;
	    }
		
		resp.setContentType("text/html");
		resp.getWriter().write("<html><head><meta http-equiv=\"refresh\" content=\"5; url=" + Constants.MAIN_PAGE + "\"></head>");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		String calID = req.getParameter("calID");
        String query = String.format("select from " + Calendar.class.getName() + " where owner == '%s' && key == '%s'", 
        		user.getUserId(), calID);
		       
		List<Calendar> cals = (List<Calendar>) pm.newQuery(query).execute();
		if (cals.size() != 1) {
			resp.getWriter().write("You do not have the right permissions to modify this calendar. Redirecting in 5...</html>");
			return;
		}
		
		Transaction trans = pm.currentTransaction();
		Calendar thisCal = cals.get(0);
		try{ 
		  trans.begin();
		  thisCal.setLongitude(req.getParameter("long"));
		  thisCal.setLatitude(req.getParameter("lat"));
		  thisCal.setAltitude(req.getParameter("alt"));
		  thisCal.setUrl(req.getParameter("url"));		  
		  trans.commit();
		}finally{
		  if (trans.isActive()) {
		    trans.rollback();
		    System.out.println("----transaction rollback - submitLogin----");
		  }
		  pm.close();
		}
		
		
		resp.getWriter().write("Edited calendar " + thisCal.getName() + ". Redirecting in 5 ...</html>");
	}
	}
