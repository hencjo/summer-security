package com.hencjo.summer;

import javax.servlet.http.HttpServletRequest;

public class SummerAuthenticatedUser {
	private static final String REQUEST_ATTRIBUTE = "com.hencjo.summer.authenticatedUser";

	public void set(HttpServletRequest request, String user) {
		request.setAttribute(REQUEST_ATTRIBUTE, user);
	}
	
	public String get(HttpServletRequest request) {
		Object attribute = request.getAttribute(REQUEST_ATTRIBUTE);
		if (attribute == null) throw new IllegalArgumentException("Request doesn't contain an attribute named " + REQUEST_ATTRIBUTE + ". Has this request passed through a Rule actually authenticating the user?");
		if (!(attribute instanceof String)) throw new IllegalArgumentException("Request contained an attribute named " + REQUEST_ATTRIBUTE + " but it certainly isn't a String!");
		return (String) attribute;
	}
}
