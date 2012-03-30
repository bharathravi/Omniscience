<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.omniscience.CalendarServiceImpl" %>
<%@ page import="com.omniscience.jdo.Calendar" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory"%>

<html>

<body>

<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
       
%>
<p>Hello, <%= user.getNickname() %>! (You can
<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>

<%
      CalendarServiceImpl calService = new CalendarServiceImpl();
      String calendarID = request.getParameter("calID");
      List<Calendar> myCals = calService.getSpecificCalendarForUser(user, calendarID);
      if (myCals.size() == 1) {
           Calendar myCal = myCals.get(0);
%>


<form action="/edit" method="post">
Edit calendar: <br />
Name (from Google Calendar): <%= myCal.getName() %> <br />
Description  (from Google Calendar): <%= myCal.getDescription() %> <br />
Lat: <input type="text" name="lat" value="<%= myCal.getLatitude() %>" /><br />
Long: <input type="text" name="long" value="<%= myCal.getLongitude() %>" /><br />
Alt: <input type="text" name="alt" value="<%= myCal.getAltitude() %>" /><br />
Url: <input type="text" name="url" value="<%= myCal.getUrl() %>" /><br />
<input type="hidden" name="calID" value="<%= calendarID %>" />
<input type="submit" value="Submit" />
</form>

<%
      } else{
%>
     <p> No such calendar found! </p>
<%  
      }
    } else {
%>
<p>Hello!
<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
to include your name with greetings you post.</p>
<%
    }
%>

<body>
</html>
