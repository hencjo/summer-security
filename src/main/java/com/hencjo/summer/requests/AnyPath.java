package com.hencjo.summer.requests;

import javax.servlet.http.HttpServletRequest;
import com.hencjo.summer.api.RequestMatcher;

public final class AnyPath implements RequestMatcher {
	@Override
	public boolean matches(HttpServletRequest request) {
		return true;
	}
	
	@Override
	public String describer() {
		return "Any";
	}
}