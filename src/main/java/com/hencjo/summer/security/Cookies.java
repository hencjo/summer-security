package com.hencjo.summer.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

final class Cookies {
	private static final ZoneId GMT = ZoneId.of("GMT");
	private static final DateTimeFormatter HTTP_DATE = DateTimeFormatter.ofPattern("EEE, dd-MMM-yyyy HH:mm:ss zzz")
		.withLocale(Locale.US)
		.withZone(GMT);

	public static String cookie(Instant now, String name, String path, String contents, long expirationSeconds) {
		return name + "=" +
			contents +
			";Expires=" + HTTP_DATE.format(now.plusSeconds(expirationSeconds)) +
			";Path=" + path;
	}

	public static List<Cookie> withName(HttpServletRequest request, String cookieName) {
		return cookies(request)
			.stream()
			.filter(x -> cookieName.equals(x.getName()))
			.collect(Collectors.toList());
	}

	public static String removeCookie(String name, String path) {
		return name +
			"=deleted" +
			";Expires=" + HTTP_DATE.format(Instant.EPOCH) +
			";Path=" + path;
	}

	private static List<Cookie> cookies(HttpServletRequest request) {
		if (request.getCookies() == null) return Collections.emptyList();
		return Arrays.asList(request.getCookies());
	}

	public static void setCookie(HttpServletResponse response, String value) {
		response.setHeader("Set-Cookie", value);
	}
}