package com.hencjo.summer.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hencjo.summer.security.api.AttributeEncoding;
import com.hencjo.summer.security.api.RequestMatcher;

import java.util.Optional;

public final class ServerSideSession<T> {
	private final String sessionAttribute;
	private final AttributeEncoding<T> attributeEncoding;

	public ServerSideSession(String sessionAttribute, AttributeEncoding<T> attributeEncoding) {
		this.sessionAttribute = sessionAttribute;
		this.attributeEncoding = attributeEncoding;
	}

	public Optional<T> sessionData(HttpServletRequest request) {
		if (request.getSession(false) == null) return Optional.empty();
		Object attribute = request.getSession(false).getAttribute(sessionAttribute);
		if (attribute == null || !(attribute instanceof byte[])) return Optional.empty();
		return Optional.of(attributeEncoding.fromBytes((byte[]) attribute));
	}

	public RequestMatcher exists() {
		return new RequestMatcher() {
			@Override
			public boolean matches(HttpServletRequest request) {
				return sessionData(request).isPresent();
			}

			@Override
			public String describer() {
				return "ServerSideSession";
			}
		};
	}

	public SessionWriter<T> sessionWriter() {
		return new SessionWriter<T>() {
			@Override
			public void startSession(HttpServletRequest request, HttpServletResponse response, T t) {
				request.getSession(true).setAttribute(sessionAttribute, attributeEncoding.toBytes(t));
			}

			@Override
			public void stopSession(HttpServletRequest request, HttpServletResponse response) {
				request.getSession(true).invalidate();
				for (Cookie cookie : Cookies.withName(request.getCookies(), "JSESSIONID"))
					Cookies.setCookie(response, Cookies.removeCookie(cookie.getName(), cookie.getPath()));
			}
		};
	}
}