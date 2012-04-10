package com.omniscience.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EditCalendarServiceAsync {

	void editCalendar(String calendarUrl, String latitude, String longitude,
			String altitude, AsyncCallback<Boolean> callback);

}
