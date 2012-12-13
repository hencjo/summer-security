package com.hencjo.summer.security.requests;

import javax.servlet.http.HttpServletRequest;
import com.hencjo.summer.security.api.RequestMatcher;
import com.hencjo.summer.security.utils.HttpServletRequests;

public final class PathEquals implements RequestMatcher {
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