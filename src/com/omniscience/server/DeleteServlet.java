package com.omniscience.server;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.omniscience.shared.jdo.Calendar;
import com.omniscience.shared.jdo.Location;
import com.sun.org.apache.xerces.internal.parsers.SAXParser;

@SuppressWarnings("serial")
public class DeleteServlet extends HttpServlet {
			
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Calendar.class);	
		query.deletePersistentAll();				
	}
	}
