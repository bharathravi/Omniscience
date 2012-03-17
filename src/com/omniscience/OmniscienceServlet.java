package com.omniscience;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONException;
import org.xml.sax.SAXException;

import com.omniscience.Constants.*;
import com.omniscience.jdo.Calendar;
import com.omniscience.jdo.Location;
import com.sun.org.apache.xerces.internal.parsers.SAXParser;

@SuppressWarnings("serial")
public class OmniscienceServlet extends HttpServlet {
			
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		// LocationList = CalendarService.getLocationList()
		// KMLGenerator.generate(LocationList)
		// KML generator does the following: given a list of locations an events at that place, generates a kml
		// 
		/*PersistenceManager pm = PMF.get().getPersistenceManager();
		Calendar cal = new Calendar("My Public calendar", "33.78840",   
				"-84.40509", "0", "A Random public calendar with events and stuff",
				"https://www.google.com/calendar/feeds/bphu9dv3g9vb2aku7e7jlqesfg%40group.calendar.google.com/public/basic");
		try {
            pm.makePersistent(cal);
        } finally {
            pm.close();
        }*/
		CalendarService calServ = new CalendarService();
		List<Location> locations = calServ.getLocationCalendars();
		JSONGenerator jsonGen = new JSONGenerator();
		
		resp.setContentType("application/json");
		try {
			resp.getWriter().write(jsonGen.generateJSON(locations).toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//KMLGenerator kmlGen = new KMLGenerator();
		
		//String kml = kmlGen.generateKML(locations);
		
        //resp.setContentLength(kml.length());
        //resp.setHeader( "Content-Disposition", "attachment; filename=\"" + Constants.FILENAME + "\"" );
		//resp.getWriter().write(kml);
	}
	}
