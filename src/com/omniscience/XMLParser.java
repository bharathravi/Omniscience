package com.omniscience;

import com.omniscience.jdo.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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
        			parseSummary(tempValue, event);
        			event.setSummary(tempValue);
        		}
        	} else if (qName.equalsIgnoreCase(Constants.SUBTITLE)) {
        		if (!inEvent) {
        			location.setDescription(tempValue);
        		}
        	}
        	
        }

        private void parseSummary(String tempValue, Event event) {			
			if (isRecurringEvent(tempValue)) {
				int whenIndex = tempValue.indexOf("First start:");
				int endIndex = tempValue.indexOf("<br>", whenIndex);
				int duration = tempValue.indexOf("Duration:");
				int endDuration = tempValue.indexOf("\n", duration);
				
				String when = tempValue.substring(whenIndex + 13, endIndex);
				when += " for " + Integer.parseInt(tempValue.substring(duration+ 10, endDuration))/3600 + " hour(s)";
				event.setWhen(when);				
			} else {
				int whenIndex = tempValue.indexOf("When: ");
				int endIndex = tempValue.indexOf("&nbsp");
				
				String timezone = tempValue.substring(endIndex + 7, endIndex + 10);
				event.setWhen(tempValue.substring(whenIndex + 6, endIndex) + " " + timezone);
			}
		}

		private boolean isRecurringEvent(String tempValue) {
            return tempValue.contains(Constants.RECURRING_EVENT);
		}

		public void characters(char ch[], int start, int length) throws SAXException {
        	tempValue += new String(ch, start, length);
        }

	};
	
	public Location getEventsAtLocation(String link) 
			throws MalformedURLException, 
			ParserConfigurationException, SAXException {
		events = new ArrayList<Event>();
		location = new Location();
		
					
			URL url = new URL(link);
			try {
				url.openConnection();			
			    InputStream reader;
				reader = url.openStream();
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
	            sp.parse(reader, handler);
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		      				
		location.setEventList(events);
		return location;
	}
}
