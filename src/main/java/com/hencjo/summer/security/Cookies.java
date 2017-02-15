package com.hencjo.summer.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public final class Cookies {
	private static final DateTimeFormatter HTTP_DATE = DateTimeFormatter.ofPattern("EEE, dd-MMM-yyyy HH:mm:ss zzz")
		.withLocale(Locale.US)
		.withZone(ZoneId.of("GMT"));

	public static String cookie(Instant now, String name, String path, String contents, Duration expiration) {
		Instant expiry = now.plus(expiration);
		return new StringBuilder()
			.append(name).append("=")
			.append(contents)
			.append(";Expires=").append(HTTP_DATE.format(expiry))
			.append(";Path=").append(path)
			.toString();
	}

	public static List<Cookie> withName(HttpServletRequest request, String cookieName) {
		return cookies(request)
			.stream()
			.filter(x -> cookieName.equals(x.getName()))
			.collect(Collectors.toList());
	}

	public static String removeCookie(String name, String path) {
		return new StringBuilder()
			.append(name)
			.append("=deleted")
			.append(";Expires=").append(HTTP_DATE.format(Instant.EPOCH))
			.append(";Path=").append(path)
			.toString();
	}

	public static List<Cookie> withName(Cookie[] cookies, String cookieName) {
		List<Cookie> matches = new ArrayList<>();
		for (Cookie cookie : cookies) {
			if (cookieName.equals(cookie.getName())) matches.add(cookie);
		}
		return matches;
	}

	public static List<Cookie> cookies(HttpServletRequest request) {
		if (request.getCookies() == null) return Collections.emptyList();
		return Arrays.asList(request.getCookies());
	}

	public static void setCookie(HttpServletResponse response, String value) {
		response.setHeader("Set-Cookie", value);
	}
}