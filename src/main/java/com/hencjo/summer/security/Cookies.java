package com.hencjo.summer.security;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class Cookies {
	private static final ThreadLocal<SimpleDateFormat> HTTP_DATE = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss zzz", Locale.US);
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
			return simpleDateFormat;
		};
	};
	
	public static String cookie(long time, String name, String path, String contents, int expiresInSeconds) {
		Date date = new Date(time + (expiresInSeconds * 1000));
		return new StringBuilder()
			.append(name).append("=")
			.append(contents)
			.append(";Expires=").append(HTTP_DATE.get().format(date))
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
			.append(name).append("=deleted;Expires=Thu, 01-Jan-1970 00:00:00 GMT")
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
