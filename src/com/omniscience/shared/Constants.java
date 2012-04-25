package com.omniscience.shared;


public class Constants {
  // KML Constants
  public static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<kml xmlns=\"http://www.opengis.net/kml/2.2\">";
  public static String PLACEMARK = "Placemark";
  public static String NAME = "name";
  public static String DESCRIPTION = "description";
  public static String POINT = "point";
  public static String COORDINATES = "coordinates";
  public static String FILENAME = "omniscience.kml";
  
  //Client Constants
  public static final String SERVER_SUCCESS = "Calendar Saved!";
  public static final String SERVER_ERROR = "Unable to add calendar: ";
  
  // Other constants
  public static int DAYS_IN_FUTURE = 7;
  public static int KM_RADIUS = 5;
  public static String DATE_FORMAT = "h:m MM-dd-yyyy ";
  
  // Google Calendar constants
  public static String TITLE = "title";
  public static String ENTRY = "entry";
  public static String CONTENT = "content";
  public static String GD_WHEN = "gd:when";
  public static String GD_ORIGINAL_EVENT = "gd:originalEvent";
  public static String START_TIME = "startTime";
  public static String END_TIME = "endTime";
  public static String SUBTITLE = "subtitle";
  public static String RECURRING_EVENT = "Recurring Event";
  public static String URL_PARAMETERS = "?orderby=starttime&singleevents=true&sortorder=ascending";
  public static String URL_START_MIN = "start-min";
  public static String URL_START_END = "start-max";
  
  // Static web pages
  public static String MAIN_PAGE = "/jsp/main.jsp";
  public static String EDIT_PAGE = "/jsp/edit.jsp";
public static String ABOUT = "Omniscience allows you to discover what events are happening at a place" +
		"by simply looking in its direction! " +
		"Simply use your phone to look in the direction of a location to know what fun events " +
		"you can join in there.<br><br>" +
		"<i>In a new place?</i> Look around to check out what's happening!<br><br>" +
		"<i>In the middle of a busy mall?</i> Gaze through discounts and offers around you by just turning around!<br><br>" +
		"<i>Want to see if your friends are free for a visit?</i> Simply look in the direction of their home to see how busy their schedule is!<br><br>" +
		"<br>Use Omniscience to stay up to date to the world around you!";
}
