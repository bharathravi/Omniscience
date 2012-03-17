package com.omniscience;

import com.omniscience.jdo.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



/**
 * 
 * @author bharath
 * Accepts the URL of a calendar XML file and returns a list 
 * of events associated with it.
 */
public class XMLParser {

	private List<Event> events;
	private Location location;
	private DefaultHandler handler = new DefaultHandler() {		
		private boolean inEvent = false;
		private Event event;
		private String tempValue;	
		
		public void startElement(String uri, String localName,String qName,
                Attributes attributes) throws SAXException {
			tempValue = "";
             	if (qName.equalsIgnoreCase(Constants.ENTRY)) {
            		inEvent = true;
            		event = new Event();            		
            	}              	             
        }

        public void endElement(String uri, String localName,
                String qName) throws SAXException {
        	if (qName.equalsIgnoreCase(Constants.ENTRY)) {
        		inEvent = false;
        		events.add(event);
        	} else if (qName.equalsIgnoreCase(Constants.TITLE)) {
        		if (inEvent) {
        			event.setTitle(tempValue);
        		} else {
        			location.setName(tempValue);
        		}
        	} else if (qName.equalsIgnoreCase(Constants.SUMMARY)) {
        		if (inEvent) {
        			event.setSummary(tempValue);
        		}
        	} else if (qName.equalsIgnoreCase(Constants.SUBTITLE)) {
        		if (!inEvent) {
        			location.setDescription(tempValue);
        		}
        	}
        	
        }

        public void characters(char ch[], int start, int length) throws SAXException {
        	tempValue += new String(ch, start, length);
        }

	};
	
	public Location getEventsAtLocation(String link) {
		events = new ArrayList<Event>();
		location = new Location();
		
		try {				
			URL url = new URL(link);
			url.openConnection();
			InputStream reader = url.openStream();
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
            sp.parse(reader, handler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		      				
		location.setEventList(events);
		return location;
	}
}
