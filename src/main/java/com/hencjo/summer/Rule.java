package com.hencjo.summer;

import javax.servlet.http.HttpServletRequest;

public interface Rule {
	String describer();
	boolean matches(HttpServletRequest request);
}