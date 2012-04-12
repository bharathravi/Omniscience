package com.omniscience.server;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONException;
import org.xml.sax.SAXException;


import com.omniscience.shared.Constants.*;
import com.omniscience.shared.jdo.Calendar;
import com.omniscience.shared.jdo.Location;
import com.sun.org.apache.xerces.internal.parsers.SAXParser;

@SuppressWarnings("serial")
public class KMLJsonGenerator extends HttpServlet {

        public void doGet(HttpServletRequest req, HttpServletResponse resp)
                        throws IOException {

                double myLat = Double.parseDouble(req.getParameter("lat"));
                double myLng = Double.parseDouble(req.getParameter("lng"));
                double myAlt = Double.parseDouble(req.getParameter("alt"));
                double dist = Double.parseDouble(req.getParameter("dist"));
                int days = Integer.parseInt(req.getParameter("days"));
                int timezoneOffset = Integer.parseInt(req.getParameter("tz")) * 60000;
                             
                CalendarServiceImpl calServ = new CalendarServiceImpl();
                List<Location> locations = calServ.getLocationCalendars(myLat, myLng, myAlt, dist, days, timezoneOffset);
                JSONGenerator jsonGen = new JSONGenerator();

                resp.setContentType("application/json");
                try {
                        resp.getWriter().write(jsonGen.generateJSON(locations).toString());
                } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

        }
}
