package com.hencjo.summer.requests;

import javax.servlet.http.HttpServletRequest;
import com.hencjo.summer.api.RequestMatcher;

public class PathBeginsWith implements RequestMatcher {
	private final String beginsWith;

	public PathBeginsWith(String beginsWith) {
		this.beginsWith = beginsWith;
	}

	@Override
	public boolean matches(HttpServletRequest request) {
		return request.getPathInfo().startsWith(beginsWith);
	}
	
	@Override
	public String describer() {
		return "PathBeginsWith \"" + this.beginsWith + "\"";
	}
}