package com.omniscience.jdo;

/**
 * 
 * @author bharath
 * A single event at some location.
 */
public class Event {
   private String title;
   private String summary;
   private String when;

public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getSummary() {
	return summary;
}
public void setSummary(String summary) {
	this.summary = summary;
}
public String getWhen() {
	return when;
}
public void setWhen(String when) {
	this.when = when;
}
 
}
