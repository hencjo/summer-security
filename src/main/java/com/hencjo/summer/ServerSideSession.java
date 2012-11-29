package com.hencjo.summer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hencjo.summer.api.RequestMatcher;

public final class ServerSideSession {
	private final SummerAuthenticatedUser summerAuthenticatedUser = new SummerAuthenticatedUser();
	private final String sessionAttribute;

	public ServerSideSession(String sessionAttribute) {
		this.sessionAttribute = sessionAttribute;
	}
	
	private String hasSessionWithUsername(HttpServletRequest request) {
		if (request.getSession(false) == null) return null;
		Object attribute = request.getSession(false).getAttribute(sessionAttribute);
		if (attribute == null || !(attribute instanceof String)) return null;
		return (String) attribute;
	}

	public RequestMatcher exists() {
		return new RequestMatcher() {
			@Override
			public boolean matches(HttpServletRequest request) {
				String loggedInUsername = hasSessionWithUsername(request);
				if (loggedInUsername == null) return false;
				summerAuthenticatedUser.set(request, loggedInUsername);
				return true;
			}
			
			@Override
			public String describer() {
				return "ServerSideSession";
			}
		};
	}

	public SessionWriter sessionWriter() {
		return new SessionWriter() {
			@Override
			public void startSession(HttpServletRequest request, HttpServletResponse response, String username) {
				request.getSession(true).setAttribute(sessionAttribute, username);
			}
			
			@Override
			public void stopSession(HttpServletRequest request, HttpServletResponse response) {
				request.getSession(true).invalidate();
			}
		};
	}
}
