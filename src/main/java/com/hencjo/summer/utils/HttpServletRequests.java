package com.hencjo.summer.utils;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequests {

	public static String fullUri(HttpServletRequest request) {
		return Strings.nullToEmpty(request.getServletPath()) + Strings.nullToEmpty(request.getPathInfo());
	}

}
