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
      CalendarServiceImpl calService = new CalendarServiceImpl();
      List<Calendar> myCals = calService.getCalendarsForUser(user);
%>
<p>Hello, <%= user.getNickname() %>! (You can
<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
Your calendars:<br>
<%      
      for (Calendar cal : myCals) {
%>

<%= cal.getName() %>: <%= cal.getDescription() %> <a href="/jsp/edit.jsp?calID=<%= cal.getKey()%>">Edit</a> <a href="/delete?calID=<%= cal.getKey()%>">Delete</a> <br>

<%      
      }    
%>

<form action="/add" method="post">
Add a new calendar: <br />
Lat: <input type="text" name="lat" /><br />
Long: <input type="text" name="long" /><br />
Alt: <input type="text" name="alt" /><br />
Url: <input type="text" name="url" /><br />
  <input type="submit" value="Submit" />
</form>

<%
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
