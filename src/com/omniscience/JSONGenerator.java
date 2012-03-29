package com.omniscience;

import java.util.List;
import org.json.*;
import com.omniscience.jdo.Event;
import com.omniscience.jdo.Location;

public class JSONGenerator {

	public JSONObject generateJSON(List<Location> locations) throws JSONException {
	    JSONObject json = new JSONObject();
	    JSONArray results = new JSONArray();
	    json.put("results", results);
	    
	    for (Location loc: locations) {
	    	JSONObject location = new JSONObject();
	    	location.put("lat", loc.getLat());
	    	location.put("lng", loc.getLng());
	    	location.put("alt", loc.getAlt());
	    	location.put("name", loc.getName());
	    	
	    	JSONArray events = new JSONArray();
	    	
	    	for (Event event: loc.getEventList()) {
	    		JSONObject eventJson = new JSONObject();
	    		eventJson.put("title", event.getTitle());
	    		eventJson.put("summary", event.getSummary());
	    		eventJson.put("when", event.getWhen());
	    		
	    		events.put(eventJson);
	    	}
	    	
	    	location.put("events", events);
	    	results.put(location);
	    }
	  
	  return json;
	}
	
}
