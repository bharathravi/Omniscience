package com.omniscience.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.omniscience.shared.jdo.Calendar;
import com.omniscience.shared.jdo.SerializableCalendar;

public interface GetCalendarsServiceAsync {

	void getCalendarsForCurrentUser(
			AsyncCallback<List<SerializableCalendar>> asyncCallback);

}
