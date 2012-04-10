package com.omniscience.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.omniscience.shared.IllegalUserException;
import com.omniscience.shared.jdo.Calendar;
import com.omniscience.shared.jdo.SerializableCalendar;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("getCalendars")
public interface GetCalendarsService extends RemoteService {
	List<SerializableCalendar> getCalendarsForCurrentUser() throws IllegalUserException;
}

