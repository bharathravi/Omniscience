package com.omniscience.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DeleteCalendarServiceAsync {

	void deleteCalendar(String calendarUrl, AsyncCallback<Void> callback);

}
