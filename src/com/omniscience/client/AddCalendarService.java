package com.omniscience.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.omniscience.shared.IllegalUserException;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("addCalendar")
public interface AddCalendarService extends RemoteService {
	String addCalendar(String calendarUrl, String latitude, 
			String longitude, String altitude) throws IllegalArgumentException,
			IllegalUserException;
}

