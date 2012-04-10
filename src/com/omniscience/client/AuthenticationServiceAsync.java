package com.omniscience.client;

import java.util.List;

import com.google.gwt.dev.util.Pair;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthenticationServiceAsync {

	void getLoginUrl(String callbackUrl, AsyncCallback<String> callback);

	void getLogoutUrl(String callbackUrl, AsyncCallback<List<String>> callback);

	void isUserLoggedIn(AsyncCallback<Boolean> callback);

}
