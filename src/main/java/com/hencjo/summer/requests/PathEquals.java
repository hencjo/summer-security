package com.hencjo.summer.requests;

import javax.servlet.http.HttpServletRequest;
import com.hencjo.summer.api.RequestMatcher;
import com.hencjo.summer.utils.HttpServletRequests;

public class PathEquals implements RequestMatcher {
	private final String equals;

	public PathEquals(String beginsWith) {
		this.equals = beginsWith;
	}

	@Override
	public boolean matches(HttpServletRequest request) {
		return HttpServletRequests.path(request).equals(equals);
	}
	
	@Override
	public String describer() {
		return "PathEquals \"" + this.equals + "\"";
	}
}