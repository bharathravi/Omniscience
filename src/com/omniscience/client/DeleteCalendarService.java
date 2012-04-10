package com.omniscience.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.omniscience.shared.IllegalUserException;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("deleteCalendar")
public interface DeleteCalendarService extends RemoteService {
	void deleteCalendar(String calendarUrl) throws IllegalArgumentException,
			IllegalUserException;
}

