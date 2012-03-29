package com.omniscience;


public class Constants {
  // KML Constants
  public static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<kml xmlns=\"http://www.opengis.net/kml/2.2\">";
  public static String PLACEMARK = "Placemark";
  public static String NAME = "name";
  public static String DESCRIPTION = "description";
  public static String POINT = "point";
  public static String COORDINATES = "coordinates";
  public static String FILENAME = "omniscience.kml";
  
  
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
  public static String URL_PARAMETERS = "?orderby=starttime&singleevents=true&sortorder=ascending&futureevents=true";
  public static String URL_RECURRENCE_EXPANSION_START = "recurrence-expansion-start";
  public static String URL_RECURRENCE_EXPANSION_END = "recurrence-expansion-end";
  
  // Static web pages
  public static String MAIN_PAGE = "/jsp/main.jsp";
  public static String EDIT_PAGE = "/jsp/edit.jsp";
}
