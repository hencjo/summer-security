package com.hencjo.summer.security;

import com.hencjo.summer.security.api.RequestMatcher;
import com.hencjo.summer.security.api.Responder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public final class ClientSideSession {
	private final String cookieName;
	private final int expiresInSeconds;
	private final SecureCookies secureCookies;

	public ClientSideSession(
		String cookieName,
		int expiresInSeconds,
		SecureCookies secureCookies
	) {
		this.cookieName = cookieName;
		this.expiresInSeconds = expiresInSeconds;
		this.secureCookies = secureCookies;
	}

	public Responder renewAndAllow() {
		return new Responder() {
			@Override
			public ContinueOrRespond respond(
				HttpServletRequest request,
				HttpServletResponse response
			) throws IOException {
				Optional<String> s = sessionData(request);
				if (!s.isPresent()) {
					System.err.println("Entered ClientSideSession.renewAndAllow() when there was no sessionData. That should've been checked before renewing.");
					response.sendError(401);
					return ContinueOrRespond.RESPOND;
				}

				secureCookies.setCookie(request, response, cookieName, s.get(), expiresInSeconds);
				return ContinueOrRespond.CONTINUE;
			}

			@Override
			public String describer() {
				return "RenewAndAllow";
			}
		};
	}

	public Optional<String> sessionData(HttpServletRequest request)  {
		for (Cookie c : Cookies.withName(request, cookieName))
			return secureCookies.cookieValue(c, expiresInSeconds);
		return Optional.empty();
	}

	public RequestMatcher exists() {
		return new RequestMatcher() {
			@Override
			public boolean matches(HttpServletRequest request) {
				return sessionData(request).isPresent();
			}
			
			@Override
			public String describer() {
				return "ClientSideSession";
			}
		};
	}

	public SessionWriter sessionWriter() {
		return new SessionWriter() {
			@Override
			public void startSession(HttpServletRequest request, HttpServletResponse response, String username) {
				secureCookies.setCookie(request, response, cookieName, username, expiresInSeconds);
			}
			
			@Override
			public void stopSession(HttpServletRequest request, HttpServletResponse response) {
				secureCookies.removeCookie(request, response, cookieName);
			}
		};
	}
}