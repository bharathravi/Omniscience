package com.omniscience;

import java.util.List;

import com.omniscience.jdo.Event;
import com.omniscience.jdo.Location;

public class KMLGenerator {

	public String generateKML(List<Location> locations) {
	  String kml = Constants.XML_HEADER;
	  
	  for (Location loc: locations) {
		  System.out.println(loc.getName());
		  String events = "";
		  for (Event event: loc.getEventList()) {
			  System.out.println(event.getTitle());
			  events += event.getTitle() + " * "; 
		  }
		  kml += makePlacemark(loc.getName(), events, loc.getLat() + "," + loc.getLng() + "," + loc.getAlt());
	  }
	  
	  kml += "</kml>";
	  return kml;
	}
	

	private String makePlacemark(String name, String description, String location) {		
		String nameElement = makeElement(Constants.NAME, name);
		String descElement = makeElement(Constants.DESCRIPTION, description);
		String locationElement = makeElement(Constants.POINT, makeElement(Constants.COORDINATES, location));
		return makeElement(Constants.PLACEMARK, nameElement + descElement + locationElement);			
	}
	
	private String makeElement(String name, String value) {
		String element = "";
		element += "<" + name + ">" + value + "</" + name + ">\n";
		return element;
	}	

}
