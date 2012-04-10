package com.omniscience.shared.jdo;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author bharath
 * A Physical Location with which many events are associated.
 */
public class Location {
	private List<Event> eventList = new ArrayList<Event>();
	private String name;
	private String description;
	private String lat;
	private String lng;
	private String alt;
	public List<Event> getEventList() {
		return eventList;
	}
	public void setEventList(List<Event> eventList) {
		this.eventList = eventList;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}	
	
	
}
