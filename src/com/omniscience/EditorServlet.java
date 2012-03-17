package com.omniscience;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.omniscience.Constants.*;
import com.omniscience.jdo.Calendar;
import com.omniscience.jdo.Location;
import com.sun.org.apache.xerces.internal.parsers.SAXParser;

@SuppressWarnings("serial")
public class EditorServlet extends HttpServlet {
			
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Calendar cal = new Calendar(req.getParameter("name"), req.getParameter("lat"),   
				req.getParameter("long"), req.getParameter("alt"), req.getParameter("desc"),
				req.getParameter("url"));
		try {
            pm.makePersistent(cal);
            resp.getWriter().write("Done!");
       
		 } catch(Exception e) {
	        	resp.getWriter().write("Not Done!");
	        }finally {        
	            pm.close();
	        }		
			
				
	}
	}
