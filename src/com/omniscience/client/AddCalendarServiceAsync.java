package com.omniscience.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface AddCalendarServiceAsync {
	void addCalendar(String calendarUrl, String latitude, String longitude,
			String altitude, AsyncCallback<String> callback);
}
