package com.hencjo.summer.api;

import javax.servlet.http.HttpServletRequest;

public interface RequestMatcher {
	String describer();
	boolean matches(HttpServletRequest request);
}