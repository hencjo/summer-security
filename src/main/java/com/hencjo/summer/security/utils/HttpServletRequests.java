package com.hencjo.summer.security.utils;

import javax.servlet.http.HttpServletRequest;

public final class HttpServletRequests {
	public static String path(HttpServletRequest request) {
		return Strings.nullToEmpty(request.getServletPath()) + Strings.nullToEmpty(request.getPathInfo());
	}
}