package com.hencjo.summer.security;

import com.hencjo.summer.security.api.RequestMatcher;
import com.hencjo.summer.security.api.Responder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
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
			) throws Exception {
				Optional<String> s = sessionData(request);
				if (!s.isPresent()) {
					System.err.println("Entered ClientSideSession.renewAndAllow() when there was no sessionData. That should've been checked before renewing.");
					response.sendError(401);
					return ContinueOrRespond.RESPOND;
				}

				setCookie(request, response, cookieName, expiresInSeconds, secureCookies.encode(s.get()));
				return ContinueOrRespond.CONTINUE;
			}

			@Override
			public String describer() {
				return "RenewAndAllow";
			}
		};
	}

	public Optional<String> sessionData(HttpServletRequest request) throws GeneralSecurityException, IOException {
		for (Cookie c : Cookies.withName(request, cookieName))
			return secureCookies.decodeAsString(c.getValue(), Duration.of(expiresInSeconds, ChronoUnit.SECONDS));
		return Optional.empty();
	}

	public RequestMatcher exists() {
		return new RequestMatcher() {
			@Override
			public boolean matches(HttpServletRequest request) throws Exception {
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
			public void startSession(HttpServletRequest request, HttpServletResponse response, String username) throws Exception {
				setCookie(request, response, cookieName, expiresInSeconds, secureCookies.encode(username));
			}
			
			@Override
			public void stopSession(HttpServletRequest request, HttpServletResponse response) {
				removeCookie(request, response, cookieName);
			}
		};
	}

	private static void setCookie(HttpServletRequest request, HttpServletResponse response, String name, int expiresInSeconds, String payload) {
		Cookies.setCookie(response,
			Cookies.cookie(
				System.currentTimeMillis(),
				name,
				request.getContextPath(),
				payload,
				expiresInSeconds
			)
		);
	}

	private static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookies.setCookie(response, Cookies.removeCookie(name, request.getContextPath()));
	}
}