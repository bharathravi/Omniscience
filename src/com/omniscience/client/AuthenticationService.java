package com.omniscience.client;

import java.util.List;

import com.google.gwt.dev.util.Pair;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.omniscience.shared.IllegalUserException;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("auth")
public interface AuthenticationService extends RemoteService {
	String getLoginUrl(String callbackUrl);
	List<String> getLogoutUrl(String callbackUrl);
	boolean isUserLoggedIn();
}

