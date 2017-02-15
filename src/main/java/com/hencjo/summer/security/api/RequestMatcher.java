package com.hencjo.summer.security.api;

import javax.servlet.http.HttpServletRequest;

public interface RequestMatcher {
	String describer();
	boolean matches(HttpServletRequest request) throws Exception;
}