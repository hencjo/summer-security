package com.hencjo.summer.security;

import javax.servlet.http.HttpServletRequest;

public interface Rule {
	String describer();
	boolean matches(HttpServletRequest request);
}